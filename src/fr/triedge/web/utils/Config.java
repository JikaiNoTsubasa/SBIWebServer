package fr.triedge.web.utils;

import java.util.Properties;

/**
 * Singleton to hold configuration of the server
 * @author sbi
 *
 */
public class Config {

	private Config() {
	}
	
    private static Config INSTANCE = new Config();
    
    public Properties parameters = new Properties();

    /**
     * Lazy loading + thread sync
     * @return INSTANCE of the singleton
     */
    public static synchronized Config getInstance()
    {           
        if (INSTANCE == null)
        {   INSTANCE = new Config(); 
        }
        return INSTANCE;
    }
}
