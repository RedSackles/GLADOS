package local.redlan.glados.web;

/*
 Common Port Assignments and Corresponding RFC Numbers              
 Port Common Name RFC#  Purpose
 7     Echo        862   Echoes data back. Used mostly for testing.
 9     Discard     863   Discards all data sent to it. Used mostly for testing.
 13    Daytime     867   Gets the date and time.
 17    Quotd       865   Gets the quote of the day.
 19    Chargen     864   Generates characters. Used mostly for testing.
 20    ftp-data    959   Transfers files. FTP stands for File Transfer Protocol.
 21    ftp         959   Transfers files as well as commands.
 23    telnet      854   Logs on to remote systems.
 25    SMTP        821   Transfers Internet mail. Stands for Simple Mail Transfer Protocol.
 37    Time        868   Determines the system time on computers.
 43    whois       954   Determines a user's name on a remote system.
 70    gopher     1436   Looks up documents, but has been mostly replaced by HTTP.
 79    finger     1288   Determines information about users on other systems.
 80    http       1945   Transfer documents. Forms the foundation of the Web.
 110   pop3       1939   Accesses message stored on servers. Stands for Post Office Protocol, version 3.
 443   https      n/a    Allows HTTP communications to be secure. Stands for Hypertext Transfer Protocol over Secure Sockets Layer (SSL).
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import local.redlan.glados.Glados;

/*
 * Mini Web Server
 */
public class WebServer implements Runnable
{
   
   /**
    * WebServer constructor.
    */
   public WebServer()
   {
      
   }
   
   @SuppressWarnings("resource")
   @Override
   public void run()
   {
      ServerSocket s;
      
      System.out.println("Webserver starting up on port 8080");
      System.out.println("(press ctrl-c to exit)");
      try
      {
         // create the main server socket
         s = new ServerSocket(8080);
      }
      catch (Exception e)
      {
         System.out.println("Error: " + e);
         return;
      }
      
      System.out.println("Waiting for connection");
      for (;;)
      {
         try
         {
            // wait for a connection
            Socket remote = s.accept();
            // remote is now the connected socket
            System.out.println("Connection, sending data.");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                  remote.getInputStream()));
            PrintWriter out = new PrintWriter(remote.getOutputStream());
            
            // read the data sent. We basically ignore it,
            // stop reading once a blank line is hit. This
            // blank line signals the end of the client HTTP
            // headers.
            String str = ".";
            while (!str.equals(""))
               str = in.readLine();
            
            // Send the response
            // Send the headers
            out.println("HTTP/1.0 200 OK");
            out.println("Content-Type: text/xml");
            out.println("Server: Glados");
            // this blank line signals the end of the headers
            out.println("");
            // Send the HTML page
            Glados.getGladosState(out);
            //out.println(Glados.getGladosState());
            out.flush();
            remote.close();
         }
         catch (Exception e)
         {
            System.out.println("Error: " + e);
         }
      }
   }
}