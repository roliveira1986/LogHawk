package com.raf.loghawk.kafkaClient;

import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


public class LogHawkKafkaProducerTest
{

   private static String serverListMock = "localhost:9092";
   private static String topicMock = "topic";
   private static String messageMock = "myMessage Test";
   private static LogHawkKafkaProducer instance = null;
   private  ProducerRecord pr = new ProducerRecord(topicMock, messageMock);
   
   public LogHawkKafkaProducerTest()
   {
   }

   @BeforeAll
   public static void setupTest(){
       
      try
      {
         instance = new LogHawkKafkaProducer(serverListMock, topicMock);
      } catch (UnknownHostException ex)
      {
         Logger.getLogger(LogHawkKafkaProducerTest.class.getName()).log(Level.SEVERE, null, ex);
      }
      
   }
   
      @Test
   public void testGetKfkP()
   {
      System.out.println("getKfkP");
      KafkaProducer result = instance.getKfkP();
      assertNotNull(result);
   
   }
   
   @Test
   public void testSetKfkP()
   {
      System.out.println("setKfkP");
      KafkaProducer localKfkP = instance.getKfkP();
      instance.setKfkP(localKfkP);
      assertNotNull(instance.getKfkP());
   }
}
