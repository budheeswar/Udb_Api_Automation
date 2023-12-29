package com.unisys.udb.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.testng.Reporter;

public class ApplicationPropertyLoader {

	static Properties properties = null;
	static String resourcePath = null;

	/**
	 * Prevent anyone to create an instance of this class. All constant variables
	 * should have accessed through static way.
	 */
	public ApplicationPropertyLoader() {
		// Nothing
	}

	static {
		try {
			properties = new Properties();
			resourcePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main"
					+ File.separator + "resources" + File.separator + "application.properties";
			//Reporter.log("Executing  environment - " + resourcePath);
			FileInputStream ins = new FileInputStream(resourcePath);
			properties.load(ins);
		} catch (Exception ex) {
			//Reporter.log("Exception occured while reading the property file due to " + ex.getMessage(), true);
		}
	}

	/**
	 * Get Property By Name
	 * 
	 * @param give key name to get value
	 * @return return the value
	 */
	public static String getPropertyByName(String key) {
		String value = null;
		value = properties.getProperty(key);
		return value;
	}

	public static void main(String[] args) {
		getPropertyByName("key");
		//System.out.println(getPropertyByName("key"));

	}

}
