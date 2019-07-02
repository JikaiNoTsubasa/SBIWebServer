package fr.triedge.web.server;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.ConfigurationSource;
import org.apache.logging.log4j.core.config.Configurator;

/**
 * Usage of
 *  - Log4j 2.12
 *  https://www.journaldev.com/1069/threadpoolexecutor-java-thread-pool-example-executorservice
 *  Main class holding the server's data
 *  
 * @author sbi
 *
 */
public class SBIWebServer {
	
	private static Logger log;
	private static String CONFIG_LOG_LOCATION				= "conf/log4j2.xml";
	private static String DEFAULT_LOG_LOCATION				= "logs";

	/**
	 * Server initialization
	 * @throws IOException 
	 * @throws FileNotFoundException 
	 */
	public void init() throws IOException {
		// Create files and folders if missing
		createFileHierarchie();
		
		// Setup logging configuration
		configureLogging();
		
		log.debug("This is a debug line");
		log.warn("This is a warn line");
		log.error("This is a error line");
	}
	
	/**
	 * Creates directories if needed
	 * 
	 * @throws IOException
	 */
	private void createFileHierarchie() throws IOException {
		// Create logs directory if not exist
		Path logPath = Paths.get(DEFAULT_LOG_LOCATION);
		if (!Files.exists(logPath))
			Files.createDirectory(Paths.get(DEFAULT_LOG_LOCATION));
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
}
