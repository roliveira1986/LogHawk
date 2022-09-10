package com.raf.loghawk.logMonitoring;

import com.raf.loghawk.Main;
import com.raf.loghawk.kafkaClient.LogHawkKafkaProducer;
import java.io.*;
import java.util.*;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

public class LogMonitorMain extends Thread 
{

  private final static Logger logger = LogManager.getLogger(Main.class);
  private long sampleInterval = 5000;
  private File logfile;
  private boolean startAtBeginning = false;
  private boolean tailing = false;
  private Set listeners = new HashSet();
  private LogHawkKafkaProducer lgKP = null;
  private LogFiltering lf = null;
  
  /**
   * Creates a new log file tailer that tails an existing file and checks the file for
   * updates every 5000ms
   */
  public LogMonitorMain( File file , LogHawkKafkaProducer lgKP, LogFiltering lf)
  {
    this.logfile = file;
    this.lgKP = lgKP;
    this.lf = lf;
  }


  public LogMonitorMain( File file, long sampleInterval, boolean startAtBeginning , LogHawkKafkaProducer lgKP, LogFiltering lf)
  {
    this.logfile = file;
    this.sampleInterval = sampleInterval;
    this.startAtBeginning = startAtBeginning;
    this.lgKP = lgKP;
    this.lf = lf;
  }

  public void stopTailing()
  {
    this.tailing = false;
  }

  public void run()
  {
    
     
    // The file pointer keeps track of where we are in the file
    long filePointer = 0;

    // Determine start point
    if( this.startAtBeginning )
    {
      filePointer = 0;
    }
    else
    {
      filePointer = this.logfile.length();
    }
    
    try
    {
      logger.info("Adding Monitoring for log file -> "+logfile); 
      // Start tailing
      this.tailing = true;
      RandomAccessFile file = new RandomAccessFile( logfile, "r" );     
      
      boolean filterActive = (lf.getFilters().size() > 0) ? true : false ;
      
      logger.info("filterActive set to "+ filterActive);
      
      while( this.tailing )
      {
        try
        {  
          // Compare the length of the file to the file pointer
          long fileLength = this.logfile.length();
          if( fileLength < filePointer ) 
          {
            //System.out.println("1 - fileLength < filePointer - " + fileLength +" <> "+filePointer) ;
            // Log file must have been rotated or deleted; 
            // reopen the file and reset the file pointer
            file = new RandomAccessFile( logfile, "r" );
            filePointer = 0;
          }

          if( fileLength > filePointer ) 
          {
            //System.out.println("2 - fileLength > filePointer - " + fileLength +" <> "+filePointer) ;
             
            // There is data to read
            file.seek( filePointer );
            String line = file.readLine();
            
            while( line != null )
            {
              line = file.readLine();
              //filter - send if filter = true
              
              
              
              if (filterActive && lf.evaluateFilters(line)) {
                 lgKP.sendLogMessage(line);
                 logger.debug("From file "+logfile+ " Sending -> "+ line);
              }else {
                 lgKP.sendLogMessage(line);
                 logger.debug("From file "+logfile+ " Sending -> "+ line);
              }
              
             filePointer = file.getFilePointer();
            }
          }
          // Sleep for the specified interval
          sleep( this.sampleInterval );
        }
        catch( Exception e )
        {
           
           e.printStackTrace();
        }
      }

      // Close the file that we are tailing
      file.close();
    }
    catch( Exception e )
    {
      e.printStackTrace();
    }
  }
}