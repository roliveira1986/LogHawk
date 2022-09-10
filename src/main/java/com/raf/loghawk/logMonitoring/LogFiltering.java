package com.raf.loghawk.logMonitoring;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class LogFiltering
{

   private final static Logger logger = LogManager.getLogger(LogFiltering.class);

   private Map<String, String> filters = new HashMap();

   public LogFiltering(Properties prop)
   {
      loadFilters(prop);
   }
   // Filter rules will be a string with TYPE,RULE

   // ALLOWED types -> regex,contains,startsWith
   // 
   // regex filtering - slower
   private boolean evaluateRegex(String line, String regex)
   {
       return (line == null || regex ==null) ? false : line.matches(regex);
   }

   // contains / match filtering / quicker 
   private boolean evaluateContains(String line, String value)
   {
      return (line == null || value ==null) ? false :  line.contains(value);
   }

   // startsWith filter
   private boolean evaluateStartsWith(String line, String value)
   {
      return (line == null || value ==null) ? false :  line.startsWith(value);
   }

   private void loadFilters(Properties prop)
   {
      for (String s : prop.stringPropertyNames())
      {
         if (s.startsWith("filter."))
         {
            String[] filter = prop.get(s).toString().split(",");
            String type = filter[0];
            String filterString = filter[1];
            filters.put(type, filterString);
         }
      }

      logger.info("Loded Filters are:");
      for (String key : filters.keySet())
      {
         logger.info(key + " -> "+ filters.get(key));
      }
   }

   public boolean evaluateFilters(String line)
   {
      boolean result = false;
      for (String key : filters.keySet())
      {
         if(result) break;
         if("regex".equals(key)){
            result = evaluateRegex(line, filters.get(key));
         }else if("contains".equals(key)){
            result = evaluateContains(line, filters.get(key));
         }else if("startsWith".equals(key)){
            result = evaluateStartsWith(line, filters.get(key));
         }
      }
      return result;
   }
   
   public Map<String, String> getFilters(){
      return filters;
   }
}
