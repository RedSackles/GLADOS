/**
 * This package holds the main components of GLADOS
 */
package local.redlan.glados;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.concurrent.LinkedBlockingDeque;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import local.redlan.glados.DB.DB_GladosDAO;
import local.redlan.glados.Spaces.GenericSpace;
import local.redlan.glados.snmp.SNMP_TrapListener;
import local.redlan.glados.systemfunctions.GlobalIntrusionDetection;
import local.redlan.glados.web.Hashtree2Node;
import local.redlan.glados.web.WebServer;

import org.snmp4j.smi.UdpAddress;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

/**
 * This is the main class of the GLADOS program also known as where doomsday
 * started. This class initiates several watchers a trap listener and Dispatcher
 * produce and handle events. It connects to the database in order to create the
 * IOboards and IOputs to interact with the world (God help us)
 * 
 * @author warpsr
 *
 */
public class Glados
{
   /**
    * object used for writing messages to a log file
    */
   private static GladosLogger logger;
   /**
    * HostName or IP for database connection
    */
   private String dbHost = "redspi2.automationandsecurity.redlan.local";
   // private String dbHost = "172.16.0.75"; // static IP of database
   /**
    * PortNumber for database connection
    */
   private int dbPort = 3306;
   /**
    * DatabaseName for database connection
    */
   private String dbName = "Glados";
   /**
    * UserName for database connection
    */
   private String dbUser = "Glados";
   /**
    * Password for database connection
    */
   private String dbPassword = "gL4D05herSQL";
   /**
    * DataBaseAccesObject for database GLADOS
    */
   private DB_GladosDAO gladosDAO;
   /**
    * Queue into which events are put by the watchers and trap listeners to be
    * dispatched to the event handlers
    */
   public static LinkedBlockingDeque<IO_Event> EventDispatchQueue = new LinkedBlockingDeque<>();
   /**
    * the one and only GLADOS
    */
   private static Glados instance;
   /**
    * A watcher for watch interval of 0.1 seconds
    */
   public static InOrOutput_Watcher watcher_0_1;
   /**
    * A watcher for watch interval of 1 seconds
    */
   public static InOrOutput_Watcher watcher_1;
   /**
    * A watcher for watch interval of 10 seconds
    */
   public static InOrOutput_Watcher watcher_10;
   /**
    * A watcher for watch interval of 60 seconds
    */
   public static InOrOutput_Watcher watcher_60;
   /**
    * a mini webserver that handles the user interface
    */
   public static WebServer webServer;
   /**
    * SNMP trap listener
    */
   public static SNMP_TrapListener sNMP_TrapListener;
   /**
    * the Dispatcher This object handles the routing of events
    */
   public static Dispatcher dispatcher;
   /**
    * The event handles for the different functional areas this split is mainly
    * for timing/performance concerns
    */
   private static IO_EventHandler climateHandler;
   private static IO_EventHandler securityHandler;
   private static IO_EventHandler generalHandler;
   
   static
   {
      watcher_0_1 = new InOrOutput_Watcher(EventDispatchQueue, 100);
      watcher_1 = new InOrOutput_Watcher(EventDispatchQueue, 1000);
      watcher_10 = new InOrOutput_Watcher(EventDispatchQueue, 10 * 1000);
      watcher_60 = new InOrOutput_Watcher(EventDispatchQueue, 60 * 1000);
      webServer = new WebServer();
      sNMP_TrapListener = new SNMP_TrapListener();
      sNMP_TrapListener.setListenerAddress(new UdpAddress("172.16.0.174/1062"));
      dispatcher = new Dispatcher(EventDispatchQueue);
      try
      {
         instance = new Glados();
      }
      catch (IOException e)
      {
         throw new AssertionError("Whoops WTF happened here", e);
      }
      // we can only create this once there is an instance of Glados or all
      // Nullpointer hell breaks loose -_-
      dispatcher.createIO_EventMeasurementHandler();
      climateHandler = new IO_EventHandler(dispatcher);
      securityHandler = new IO_EventHandler(dispatcher);
      generalHandler = new IO_EventHandler(dispatcher);
   }
   
   /**
    * Method which returns the one and only instance of GLADOS
    * 
    * @return Glados
    */
   public static Glados getGlados()
   {
      return instance;
   }
   
   /**
    * GLADOS constructor this one is private as there can only be one GLADOS
    * 
    * @throws IOException
    */
   private Glados() throws IOException
   {
      String logPath = "D:\\logs\\snmptest.log";
      logger = new GladosLogger(logPath, 30);
      gladosDAO = new DB_GladosDAO(dbHost, dbPort, dbName, dbUser, dbPassword);
   }
   
   public static void main(String args[]) throws IOException
   {
      // create some threads for trap listener and watchers
      Thread sNMP_TrapListener_Thread = new Thread(Glados.sNMP_TrapListener);
      sNMP_TrapListener_Thread.start();
      Thread watcher_0_1_Thread = new Thread(Glados.watcher_0_1);
      watcher_0_1_Thread.start();
      Thread watcher_1_Thread = new Thread(Glados.watcher_1);
      watcher_1_Thread.start();
      Thread watcher_10_Thread = new Thread(Glados.watcher_10);
      watcher_10_Thread.start();
      Thread watcher_60_Thread = new Thread(Glados.watcher_60);
      watcher_60_Thread.start();
      Thread webServerThread = new Thread(Glados.webServer);
      webServerThread.start();
      
      // lets create the controlling objects
      IO_Board_Factory.createIOBoards();
      SpaceFactory.createSpaces();
      
      
      // here starts the icky test stuff
      
      //GlobalIntrusionDetection.arm("");
      GlobalIntrusionDetection.arm("Lab2 intrusion test");
      GlobalIntrusionDetection.setMaintenance("Lab Security");
      
      // Glados.logger.log("a test debug message", 30);
      // Glados.logger.log("a test info message", 20);
      // Glados.logger.log("a test error message", 10);
      // Glados.logger.log("a test security message", 0);
      
      // DigitalOut.getFromDigitalOutList("Relay1").toggle();
      // DigitalOut.getFromDigitalOutList("Relay6").toggle();
      // DigitalOut.getFromDigitalOutList("Relay12").toggle();
      // DigitalOut.getFromDigitalOutList("Relay1_2").toggle();
      // DigitalOut.getFromDigitalOutList("Relay6_2").toggle();
      // DigitalOut.getFromDigitalOutList("Relay12_2").toggle();
      
      // some none test stuff again
      
   }
   
   /**
    * generate Glados state in HTML
    */
   public static void getGladosState(PrintWriter ouput)
   {
      
      try
      {
         Document document;
         DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
         DocumentBuilder builder;
         builder = factory.newDocumentBuilder();
         document = builder.newDocument();
         Node root = document.createElement("root");
         Hashtree2Node.appendHashToNode(GlobalIntrusionDetection.getInstance().getStatus(), "GlobalIntrusionDetection", root, document);
         ArrayList<GenericSpace> spaceList = GenericSpace.getSpaceList();
         for (GenericSpace space : spaceList)
         {
            Hashtree2Node.appendHashToNode(space.getStatus(), space.getName(), root, document);
         }
         // response.append(document.);
         // create DOMSource for source XML document
         Source xmlSource = new DOMSource(root);
         // create StreamResult for transformation result
         Result result = new StreamResult(ouput);
         // create TransformerFactory
         TransformerFactory transformerFactory = TransformerFactory
               .newInstance();
         // create Transformer for transformation
         Transformer transformer = transformerFactory.newTransformer();
         transformer.setOutputProperty("indent", "yes");
         // transform and deliver content to client
         transformer.transform(xmlSource, result);
      }
      catch (ParserConfigurationException | TransformerException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
   
   /**
    * iterate recursively over a hashtable turning out a stringbuilder object.
    * 
    * @param Hashtable
    *           <String, Hashtable> hashtable
    * @return StringBuilder containing a html representation of the Hashtable
    *         contents
    * no longer used as output is now delivered as XML
   @SuppressWarnings(
   { "unchecked", "rawtypes" })
   private static StringBuilder hashTableToStringBuilder(
         Hashtable<String, Hashtable> hashtable)
   {
      StringBuilder response = new StringBuilder();
      Enumeration<String> entries = hashtable.keys();
      while (entries.hasMoreElements())
      {
         String key = entries.nextElement();
         Object entry = hashtable.get(key);
         if (entry instanceof Hashtable)
         {
            response.append(key + ":<BR>");
            response.append(hashTableToStringBuilder(
                  (Hashtable<String, Hashtable>) entry).toString());
         }
         else
         {
            response.append(key + ":" + entry + "<BR>");
         }
      }
      return response;
   }
   */
   
   /**
    * 
    * @return the glados DAO
    */
   public DB_GladosDAO getGladosDAO()
   {
      return gladosDAO;
   }
   
   /**
    * Method which takes a string and tests whether it is an int Returns true if
    * the string can be parched to an int
    * 
    * @param str
    *           String
    * @return boolean
    */
   public static boolean isInt(String str)
   {
      try
      {
         @SuppressWarnings("unused")
         double d = Integer.parseInt(str);
      }
      catch (NumberFormatException nfe)
      {
         return false;
      }
      return true;
   }
   
   /**
    * Method which takes a string and tests whether it equals "true" and returns
    * the result
    * 
    * @param value
    *           String
    * @return boolean
    */
   public static boolean getBool(String value)
   {
      return value.equals("true");
   }
   
   /**
    * Method which takes an integer interval value and returns the most
    * appropriate InOrOutput_Watcher
    * 
    * @param interval
    *           integer
    * @return InOrOutput_Watcher most appropriate InOrOutput_Watcher
    */
   public static InOrOutput_Watcher getMyWatcher(int interval)
   {
      assert interval < 100 : "Interval should not be smaller then 100 ms";
      
      if (interval < 1000 - 1) { return Glados.watcher_0_1; }
      
      if (interval < 10 * 1000 - 1) { return Glados.watcher_1; }
      
      if (interval < 60 * 1000 - 1) { return Glados.watcher_10; }
      
      if (interval > 60 * 1000) { return Glados.watcher_60; }
      
      return Glados.watcher_1;
   }
   
   /**
    * 
    */
   public static Dispatcher getDispatcher()
   {
      return dispatcher;
   }
   
   /**
    * 
    */
   public static IO_EventHandler getClimateHandler()
   {
      return climateHandler;
   }
   
   /**
    * 
    */
   public static IO_EventHandler getSecurityHandler()
   {
      return securityHandler;
   }
   
   /**
    * 
    */
   public static IO_EventHandler getGeneralHandler()
   {
      return generalHandler;
   }
}
