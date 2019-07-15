package fr.triedge.web.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name ="ServerConfig")
public class XmlServerConfig {

	/**
	 * Number threads available in the pool
	 */
	private int threadPoolSize = 5;
	private String defaultFileNotFound = "404.html", defaultFileIndex = "index.html", webApps = "www";
	private int port = 8888;
	

	public int getThreadPoolSize() {
		return threadPoolSize;
	}

	@XmlElement(name = "ThreadPoolSize")
	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}

	public String getDefaultFileNotFound() {
		return defaultFileNotFound;
	}

	@XmlElement(name = "DefaultFileNotFound")
	public void setDefaultFileNotFound(String defaultFileNotFound) {
		this.defaultFileNotFound = defaultFileNotFound;
	}

	public int getPort() {
		return port;
	}

	@XmlElement(name = "Port")
	public void setPort(int port) {
		this.port = port;
	}

	public String getWebApps() {
		return webApps;
	}

	@XmlElement(name = "WebApps")
	public void setWebApps(String webApps) {
		this.webApps = webApps;
	}

	public String getDefaultFileIndex() {
		return defaultFileIndex;
	}

	@XmlElement(name = "DefaultFileIndex")
	public void setDefaultFileIndex(String defaultFileIndex) {
		this.defaultFileIndex = defaultFileIndex;
	}
}
