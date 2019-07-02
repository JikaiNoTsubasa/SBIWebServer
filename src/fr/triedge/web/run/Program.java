package fr.triedge.web.run;

import java.io.IOException;

import fr.triedge.web.server.SBIWebServer;

public class Program {

	public static void main(String[] args) {
		SBIWebServer server = new SBIWebServer();
		try {
			server.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
