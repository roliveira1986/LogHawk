/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package com.raf.loghawk.logMonitoring;

import com.raf.loghawk.ConfigLoader;
import static com.raf.loghawk.Main.prop;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author rafae
 */
public class LogFilteringTest
{
   private static String mockText = "Some Text To Test With NullPointer!";
   private static String mockText2 = "Some Text To Test With!";
   private static  LogFiltering lf = null;
   
   public LogFilteringTest()
   {
   }
   
   @BeforeAll
   public static void setUpClass()
   {
      ConfigLoader c = new ConfigLoader();
      prop = c.loadConfig(LogFilteringTest.class.getClassLoader().getResource("logHawkConfig.properties").getFile());
      lf = new LogFiltering(prop);
   }
   
   @Test
   public void testGetFilters()
   {
      System.out.println("getFilters");
      Map<String, String> result = lf.getFilters();
      assertNotNull(result);
   }
   
   @Test
   public void testEvaluateFilters()
   {
      System.out.println("evaluateFilters");
      boolean result1 = lf.evaluateFilters(mockText);
      assertEquals(true, result1);
     
      boolean result2 = lf.evaluateFilters(mockText2);
      assertEquals(false, result2);
   }

}
