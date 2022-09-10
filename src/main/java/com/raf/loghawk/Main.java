package com.raf.loghawk;

import com.raf.loghawk.kafkaClient.LogHawkKafkaProducer;
import com.raf.loghawk.logMonitoring.LogFiltering;
import com.raf.loghawk.logMonitoring.LogMonitorMain;
import java.io.File;
import java.util.Properties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 *
 * @author raf
 */
public class Main
{

   private final static Logger logger = LogManager.getLogger(Main.class);

   public static Properties prop = null;

   public static boolean testMode = true;

   /**
    * @param args the command line arguments
    */
   public static void main(String[] args)
   {

      ConfigLoader c = new ConfigLoader();
      LogHawkKafkaProducer lgKP = null;

      if (args.length > 0)
      {
         prop = c.loadConfig(args[0]);
      } else
      {
         logger.warn("No config file specified, please start program with 1st argument as the config file location.");
         //get the property value and print it out
         if (testMode)
         {
            prop = c.loadConfig("C:\\Users\\rafae\\OneDrive\\Documents\\NetBeansProjects\\LogHawk\\src\\main\\resources\\logHawkConfig.properties");
            for (String s : prop.stringPropertyNames())
            {
               logger.info(s + " : " + prop.getProperty(s));
            }
         }
      }

      //initialize the Filtering Rules
      LogFiltering lf = new LogFiltering(prop);

      //initialize KafkaProducer
      try
      {
         lgKP = new LogHawkKafkaProducer(prop.get("kafkaServers").toString(), prop.get("kafkaTopic").toString());
      } catch (Exception e)
      {
         logger.warn("Failed to load Kafka Producer. Check config file for kafkaServers and kafkaTopic params.");
         if (testMode)
         {
            e.printStackTrace();
         }
      }

      //initiate LogMonitorMain for each file declared, every one in a separate thread
      String[] logCSV = prop.get("logsToMonitor").toString().split(",");
      logger.info("Total logs being monitored -> " + logCSV.length);
      for (String log : logCSV)
      {
         File logFile = new File(log);
         if (prop.get("scanInterval") != null)
         {
            Long scanLongVal = Long.parseLong(prop.get("scanInterval").toString());
            LogMonitorMain lm = new LogMonitorMain(logFile, scanLongVal, true, lgKP, lf);
            lm.start();
         } else
         {
            LogMonitorMain lm = new LogMonitorMain(logFile, lgKP, lf);
            lm.start();
         }
      }

   }

}
