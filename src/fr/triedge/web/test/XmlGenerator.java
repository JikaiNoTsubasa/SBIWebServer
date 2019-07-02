package fr.triedge.web.test;

import java.io.File;

import javax.xml.bind.JAXBException;

import fr.triedge.web.model.ConfigurationFactory;
import fr.triedge.web.model.XmlServerConfig;
import fr.triedge.web.utils.XmlHelper;

public class XmlGenerator {

	public static void main(String[] args) {
		XmlServerConfig conf = ConfigurationFactory.createDefaultXmlServerConfig();
		try {
			XmlHelper.storeXml(conf, new File("conf/server.xml"));
		} catch (JAXBException e) {
			e.printStackTrace();
		}
	}

}
