package fr.triedge.web.server;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.xml.bind.JAXBException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

import fr.triedge.web.model.ConfigurationFactory;
import fr.triedge.web.model.XmlServerConfig;
import fr.triedge.web.utils.XmlHelper;

/**
 * Usage of
 *  - Log4j 2.12
 *  https://www.journaldev.com/1069/threadpoolexecutor-java-thread-pool-example-executorservice
 *  https://medium.com/@ssaurel/create-a-simple-http-web-server-in-java-3fc12b29d5fd
 *  
 *  https://stackoverflow.com/questions/38679686/setting-content-type-in-java-httpserver-response
 *  
 *  Main class holding the server's data
 *  
 * @author sbi
 *
 */
public class SBIWebServer {
	
	private static Logger log;
	private static String CONFIG_LOCATION					= "conf";
	private static String CONFIG_LOG_LOCATION				= CONFIG_LOCATION+File.separator+"log4j2.xml";
	private static String CONFIG_SERVER_LOCATION			= CONFIG_LOCATION+File.separator+"server.xml";
	private static String DEFAULT_LOG_LOCATION				= "logs";
	private XmlServerConfig config;
	private ExecutorService threadPool;
	private boolean running									= true;

	/**
	 * Server initialization
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public void init() throws IOException {
		// Setup logging configuration
		configureLogging();
		
		// Create files and folders if missing
		createFileHierarchie();
		
		// Load server configuration
		reloadServerConfig();
		
		log.info("Server Initialized");
	}
	
	public void start() {
		log.debug("START: SBIWebServer.start");
		log.info("Starting server...");
		int port = getConfig().getPort();
		int threadPoolSize = getConfig().getThreadPoolSize();
		log.info("Server listening to port: "+port);
		threadPool = Executors.newFixedThreadPool(threadPoolSize);
		log.info("Server thread pool size: "+threadPoolSize);
		
		try {
			ServerSocket listener = new ServerSocket(port);
			while (isRunning()) {
				log.info("Waiting for incoming connections...");
				Socket clientSocket = listener.accept();
				log.debug("Connection received from: "+clientSocket.getRemoteSocketAddress());
				log.debug("Creating worker to server the request...");
				WorkerThread worker = new WorkerThread(clientSocket, getConfig());
				threadPool.execute(worker);
			}
			log.info("Shutting down server");
			listener.close();
			threadPool.shutdown();
		} catch (IOException e) {
			log.error("Cannot listen to port "+port,e);
		}
		
		log.info("Server stopped");
		log.debug("END: SBIWebServer.start");
	}
	
	private void reloadServerConfig() {
		log.debug("START: SBIWebServer.reloadServerConfig");
		try {
			XmlServerConfig xml = XmlHelper.loadXml(XmlServerConfig.class, new File(CONFIG_SERVER_LOCATION));
			setConfig(xml);
			log.info("Server configuration loaded");
		} catch (JAXBException e) {
			log.error("Cannot load server configuration from: "+CONFIG_SERVER_LOCATION,e);
		}
		log.debug("END: SBIWebServer.reloadServerConfig");
	}

	/**
	 * Creates directories if needed
	 * 
	 * @throws IOException
	 */
	private void createFileHierarchie() throws IOException {
		log.debug("START: SBIWebServer.createFileHierarchie");
		// Create logs directory if not exist
		Path logPath = Paths.get(DEFAULT_LOG_LOCATION);
		if (!Files.exists(logPath)) {
			Files.createDirectory(Paths.get(DEFAULT_LOG_LOCATION));
			log.debug("Directory "+DEFAULT_LOG_LOCATION+" created");
		}else {
			log.debug("Directory "+DEFAULT_LOG_LOCATION+" already exists");
		}
		
		// Create default server.xml
		Path serverConfigPath = Paths.get(CONFIG_SERVER_LOCATION);
		if (!Files.exists(serverConfigPath)) {
			XmlServerConfig xml = ConfigurationFactory.createDefaultXmlServerConfig();
			try {
				XmlHelper.storeXml(xml, new File(CONFIG_SERVER_LOCATION));
				log.debug("File "+CONFIG_SERVER_LOCATION+" created");
			} catch (JAXBException e) {
				log.error("Cannot store "+CONFIG_SERVER_LOCATION,e);
			}
		}else {
			log.debug("File "+CONFIG_SERVER_LOCATION+" already exists");
		}
		log.debug("END: SBIWebServer.createFileHierarchie");
	}
	
	/**
	 * Sets the path to the log4j configuration file
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	private void configureLogging() throws FileNotFoundException, IOException {
		// Set configuration file for log4j2
		ConfigurationSource source = new ConfigurationSource(new FileInputStream(CONFIG_LOG_LOCATION));
		Configurator.initialize(null, source);
		log = LogManager.getLogger(SBIWebServer.class);
	}

	public XmlServerConfig getConfig() {
		return config;
	}

	public void setConfig(XmlServerConfig config) {
		this.config = config;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public ExecutorService getThreadPool() {
		return threadPool;
	}

	public void setThreadPool(ExecutorService threadPool) {
		this.threadPool = threadPool;
	}
}
