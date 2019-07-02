package fr.triedge.web.model;

public class ConfigurationFactory {

	public static XmlServerConfig createDefaultXmlServerConfig() {
		XmlServerConfig conf = new XmlServerConfig();
		conf.setThreadPoolSize(20);
		return conf;
	}
}
