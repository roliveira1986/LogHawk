package com.raf.loghawk.kafkaClient;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;

public class LogHawkKafkaProducer
{

   String topic = null;


   KafkaProducer kfkP = null;
   boolean KafkaProducerActive = true;

   public LogHawkKafkaProducer(String serverList, String topic) throws UnknownHostException
   {
      this.topic = topic;
      
      Properties config = new Properties();
      config.put("client.id", InetAddress.getLocalHost().getHostName());
      config.put("bootstrap.servers", serverList);
      config.put("acks", "all");
      config.put("retries", 0);
      config.put("batch.size", 16384);
      config.put("key.serializer", 
         "org.apache.kafka.common.serialization.StringSerializer");
      config.put("value.serializer", 
         "org.apache.kafka.common.serialization.StringSerializer");
      
      kfkP = new KafkaProducer<>(config);
   }
   

   public KafkaProducer getKfkP()
   {
      return kfkP;
   }

   public void setKfkP(KafkaProducer kfkP)
   {
      this.kfkP = kfkP;
   }
   
   public void sendRecord( ProducerRecord pr)
   {   
      kfkP.send(pr);
   }
   
   public String getTopic()
   {
      return topic;
   }
   
   public void sendLogMessage(String message){
      ProducerRecord pr = new ProducerRecord(topic, message);
      sendRecord(pr);
   }
}
