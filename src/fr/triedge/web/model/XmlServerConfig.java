package fr.triedge.web.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name ="ServerConfig")
public class XmlServerConfig {

	/**
	 * Number threads available in the pool
	 */
	private int threadPoolSize = 5;

	public int getThreadPoolSize() {
		return threadPoolSize;
	}

	@XmlElement(name = "ThreadPoolSize")
	public void setThreadPoolSize(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;
	}
}
