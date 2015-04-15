/**
 * 
 */
package local.redlan.glados;

import java.io.IOException;
import java.util.Calendar;

/**
 * @author warpsr
 *
 */
public class GladosLogger
{
   //private LogFileWriter writer = null;
   String logFilePath;
   Calendar calendar;
   int logLevel;
   
   /**
    * GladosLogger  constructor
    * 
    * @param logFilePath : the path to the intended log file
    * @param logLevel : the level to which logging is set (this determines what messages get written to the log)
    * @throws IOException
    */
   GladosLogger(String logFilePath, int logLevel) throws IOException
   {
      //try (LogFileWriter testwriter = new LogFileWriter(logFilePath, true);)
      //{
      //   testwriter.writeMessage("Something");
      //}
      this.logFilePath = logFilePath;
      this.calendar = Calendar.getInstance();
      if(logLevel >= 0 && logLevel <= 100 )
      {
         switch (logLevel)
         {
            case 0 :
            case 10 :
            case 20 :
            case 30 : break;
            default : throw new AssertionError("undefined logLevel value : " + logLevel);
         }
         this.logLevel = logLevel;
      }
      else
      {
         throw new AssertionError("logLevel needs to be a positive number below 100");
      }
      
   }
   
   /**
    * log  processes a supplied string and log level and passes the result to the log writer
    * 
    * @param message : the string to be written into the file
    * @param msgLevel : the log level of the message
    * @throws IOException
    */
   public void log(String message, int msgLevel) throws IOException
   {
      if(msgLevel <= logLevel)
      {
         try (LogFileWriter logWriter = new LogFileWriter(this.logFilePath, true);)
         {
            String messageLevel;
            switch (msgLevel)
            {
               case 0 : messageLevel = "SECURITY";
                  break;
               case 10 : messageLevel = "Error";
                  break;
               case 20 : messageLevel = "Info";
                  break;
               case 30 : messageLevel = "Debug";
                  break;
               default : throw new AssertionError("undefined logLevel value : " + msgLevel);
            }
            java.util.Date now = calendar.getTime();
            java.sql.Timestamp currentTimestamp = new java.sql.Timestamp(now.getTime());
            logWriter.writeMessage(currentTimestamp+" : "+messageLevel+" : "+message+"\r");
         }
         catch (Exception e) {
            // TODO: handle exception
         }
      }
   }
}
