package fr.triedge.web.server;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import fr.triedge.web.model.ResponseFactory;
import fr.triedge.web.model.SBIHTTPResponse;
import fr.triedge.web.model.XmlServerConfig;

public class WorkerThread implements Runnable{

	private static Logger log = LogManager.getLogger(WorkerThread.class);
	private Socket socket;
	private XmlServerConfig config;
	private ArrayList<String> headers = new ArrayList<>();
	private String resultFile;
	
	public WorkerThread(Socket socket, XmlServerConfig config) {
		super();
		this.socket = socket;
		this.config = config;
		this.resultFile = getConfig().getDefaultFileNotFound();
	}

	@Override
	public void run() {
		log.debug("Worker started");
		try {
			// Read headers
			log.debug("Reading headers...");
			BufferedReader br = new BufferedReader(new InputStreamReader(getSocket().getInputStream()));
			String line = br.readLine();
			getHeaders().add(line);
			log.debug(line);
			/*
			for (String line = br.readLine(); line != null && line != ""; line = br.readLine()) {
			       getHeaders().add(line);
			       log.debug(line);
			}
			*/
			
			//br.close();
			
			// Get socket output
			log.debug("Creating socket outputs...");
			PrintWriter out = new PrintWriter(getSocket().getOutputStream());
			BufferedOutputStream dataOut = new BufferedOutputStream(getSocket().getOutputStream());
			
			// Get requested path
			log.debug("Getting request path...");
			String requestedPath = getRequestedPath(getHeaders().get(0));
			log.debug("Path is: "+ requestedPath);
			if (requestedPath != null) {
				if (requestedPath.equals("/")) {
					// If default path is used points to index
					File file = new File(getConfig().getWebApps() + File.separator + getConfig().getDefaultFileIndex());
					sendResponseFile(file, out, dataOut);
				}else {
					// Else get the path directly
					File file = new File(getConfig().getWebApps() + requestedPath);
					if (file.exists()) {
						sendResponseFile(file, out, dataOut);
					}else {
						send404(out, dataOut);
					}
				}
			}
			
			log.debug("HTTP response sent");
			
			
		} catch (IOException e) {
			log.error("Error during worker processing",e);
		}
	}
	
	private void sendResponseFile(File file, PrintWriter out, BufferedOutputStream dataOut) throws IOException {
		int fileLength = (int) file.length();
		byte[] data = readFileData(file, fileLength);
		
		SBIHTTPResponse response = ResponseFactory.createDefaultResponse(data);
		sendHTTPResponse(response, out, dataOut);
	}
	
	private void send404(PrintWriter out, BufferedOutputStream dataOut) throws IOException {
		log.debug("Sending 404");
		File file = new File(getConfig().getWebApps() + File.separator + getConfig().getDefaultFileNotFound());
		int fileLength = (int) file.length();
		byte[] data = readFileData(file, fileLength);
		SBIHTTPResponse response = ResponseFactory.create404Response(data);
		sendHTTPResponse(response, out, dataOut);
	}
	
	private void sendHTTPResponse(SBIHTTPResponse response, PrintWriter out, BufferedOutputStream dataOut) throws IOException {
		for (String header: response.getHeaders())
			out.println(header);
		out.flush();
		dataOut.write(response.getBody(), 0, response.getBody().length);
		dataOut.flush();
	}
	
	private byte[] readFileData(File file, int fileLength) throws IOException {
		byte[] data = new byte[fileLength];
		FileInputStream in = new FileInputStream(file);
		in.read(data);
		in.close();
		return data;
	}
	
	/**
	 * Method to get the requested path to files
	 * @param line - The header line
	 * @return The path to the disered location or NULL
	 */
	private String getRequestedPath(String line) {
		if (line !=null && line.startsWith("GET")) {
			String[] sline = line.split(" ");
			return sline[1];
		}
		return null;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public XmlServerConfig getConfig() {
		return config;
	}

	public void setConfig(XmlServerConfig config) {
		this.config = config;
	}

	public ArrayList<String> getHeaders() {
		return headers;
	}

	public void setHeaders(ArrayList<String> headers) {
		this.headers = headers;
	}

	public String getResultFile() {
		return resultFile;
	}

	public void setResultFile(String resultFile) {
		this.resultFile = resultFile;
	}

}
