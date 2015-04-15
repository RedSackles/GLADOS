/**
 * 
 */
package local.redlan.glados;

import java.io.*;

/**
 * @author warpsr
 *"D:\\logs\\snmptest.log"
 */
public class LogFileWriter implements AutoCloseable
{
   private BufferedWriter writer = null;
   
   /**
    * LogFileWriter constructor 
    * 
    * @param filepath : the path to the intended log file
    * @param append : whether to append messages to the end of the file or overwrite the file
    */
   LogFileWriter(String filepath, boolean append)
   {
      try
      {
         FileOutputStream fileHandle = new FileOutputStream(filepath, append);
         OutputStreamWriter outputStream = new OutputStreamWriter(fileHandle, "utf-8");
         this.writer = new BufferedWriter(outputStream);
      }
      catch (IOException ex)
      {
         // report
      }
   }
   
   /**
    * writeMessage  writes a supplied string into its writer
    * 
    * @param message : the string to be written into the file
    * @throws IOException
    */
   public void writeMessage(String message) throws IOException
   {
      writer.write(message);
   }
   
   /**
    * close close the writer recourse
    * 
    * @throws IOException
    */
   public void close() throws IOException
   {
      writer.close();
   }
}
