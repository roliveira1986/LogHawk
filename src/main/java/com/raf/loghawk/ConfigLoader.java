package com.raf.loghawk;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @author rafae
 */
public class ConfigLoader
{
   
   public Properties loadConfig(String configFile){
      Properties prop = new Properties();
      try {
          //load a properties file from class path, inside static method
          FileInputStream input = new FileInputStream(configFile);
          prop.load(input);
      } 
      catch (IOException ex) {
         System.out.println(prop.getProperty("Failed to load config file."));
      }
      return prop;
   }
   
   
}
