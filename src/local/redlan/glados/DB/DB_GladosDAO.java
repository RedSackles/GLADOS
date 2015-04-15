package local.redlan.glados.DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
/**
 * This is the Glados Database Access Object.
 * It does what a DAO does
 * @author WarpsR
 */
public class DB_GladosDAO
{
   /**
    * the url used to connect to the datase
    */
   private final String url;
   /**
    * database user
    */
   private final String user;
   /**
    * Database user password
    */
   private final String password;
   /**
    * Database connection object
    */
   private Connection connection = null;
   /**
    * PreparedStatements their names ought to be quite descriptive of what they do
    */
   private PreparedStatement getIO_Boards, getMyInOrOutPuts, setMyMeasurments, getSpaces, getMyFunction, getMySensors, getMyActuators;
   /**
    * Constructor for the Glados DAO  parameters supplied are used to configure the database connection and created prepared statements.
    * @param host Hostname of the database
    * @param portNr portnumber of the database
    * @param dbName database name
    * @param user database user
    * @param passwd database user password
    */
   public DB_GladosDAO(String host, int portNr, String dbName, String user,
         String passwd)
   {
      this.url = String.format("jdbc:mysql://%s:%d/%s", host, portNr, dbName);
      this.user = user;
      this.password = passwd;
      connect();
      try
      {
         getIO_Boards = connection.prepareStatement("SELECT Name, Model, Location, host, ComType, community FROM IO_Boards");
         getMyInOrOutPuts  = connection.prepareStatement("SELECT  Name, OID, type FROM IOputs WHERE IOboard = ? AND ComType = 'SNMP' ORDER BY oid");
         setMyMeasurments = connection.prepareStatement("INSERT INTO Measurements (`Sensor`, `Value`, `RawValue`) VALUES (?, ?, ?)");
         getSpaces = connection.prepareStatement("SELECT SpaceName, SpaceType, OccupationThreshold FROM Spaces");
         getMyFunction = connection.prepareStatement("SELECT FunctionName, FrostProtect, EnergySave, Comfort FROM Functions WHERE Spacename = ? AND FunctionType = ? ");
         getMySensors = connection.prepareStatement("SELECT se.SensorName, se.SensorType, se.InputName, se.Location, se.WatchMe, se.BottumTreshold, se.MidTreshold, se.HighTreshold, se.LogMe, se.AdjustMe FROM  Spaces sp, Functions fu, Sensors se WHERE sp.Spacename = ? AND fu.FunctionType = ? AND sp.Spacename = fu.Spacename AND fu.SensorName = se.Sensorname");
         getMyActuators = connection.prepareStatement("SELECT ac.ActuatorName, ac.ActuatorType, ac.OutputName,  ac.Location, ac.WatchMe FROM  Spaces sp, Functions fu, Actuators ac WHERE sp.Spacename = ? AND fu.FunctionType = ? AND sp.Spacename = fu.Spacename AND fu.ActuatorName = ac.ActuatorName");
      }
      catch (SQLException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
   /**
    * when called the getIO_Boards prepared statement is run against the database resulting is a list of IO_Board config being returned.
    * @return ArrayList<String[]> IO_Boards, A arraylist with the IO_Board configuration in string form.
    */
   public ArrayList<String[]> getIO_Boards()
   {
      return resultToArray(performQuery(getIO_Boards));
   }
   /**
    * when called the getMySensors prepared statement is run against the database 
    * resulting is a list of Sensors config being returned for the Space and function specified in the parameters.
    * @param spacename the name of the space that the function will belong to
    * @param functionType the class name of the function
    * @return ArrayList<String[]> A arraylist with the Sensors configuration in string form.
    */
   public ArrayList<String[]> getMySensors(String spacename, String functionType)
   {
      try
      {
         getMySensors.setString(1, spacename);
         getMySensors.setString(2, functionType);
      }
      catch (SQLException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return resultToArray(performQuery(getMySensors));
   }
   /**
    * when called the getMyActuators prepared statement is run against the database 
    * resulting is a list of Actuator config being returned for the Space and function specified in the parameters.
    * @param spacename the name of the space that the function will belong to
    * @param functionType the class name of the function
    * @return ArrayList<String[]> A arraylist with the Actuators configuration in string form.
    */
   public ArrayList<String[]> getMyActuators(String spacename, String functionType)
   {
      try
      {
         getMyActuators.setString(1, spacename);
         getMyActuators.setString(2, functionType);
      }
      catch (SQLException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return resultToArray(performQuery(getMyActuators));
   }
   /**
    * when called the getMyFunction prepared statement is run against the database 
    * resulting is a list of Function config being returned for the Space and function specified in the parameters.
    * @param spacename the name of the space that the function will belong to
    * @param functionType the class name of the function
    * @return ArrayList<String[]> IO_Boards, A arraylist with the function configuration in string form.
    */
   public ArrayList<String[]> getMyFunction(String spacename, String functionType)
   {
      try
      {
         getMyFunction.setString(1, spacename);
         getMyFunction.setString(2, functionType);
      }
      catch (SQLException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return resultToArray(performQuery(getMyFunction));
   }
   /**
    * when called the getMyInOrOutPuts prepared statement is run against the database 
    * resulting is a list of IO_Put config being returned for the IO_Board specified in the parameter IOboardName.
    * @param IOboardName the IOboard for which the IOPuts are to be retrieved
    * @return ArrayList<String[]> IO_Boards, A arraylist with the IO_Board configuration in string form.
    */
   public ArrayList<String[]> getMyInOrOutPuts(String IOboardName)
   {
      try
      {
         getMyInOrOutPuts.setString(1, IOboardName);
      }
      catch (SQLException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return resultToArray(performQuery(getMyInOrOutPuts));
   }
   /**
    * when called the setMyMeasurments prepared statement is run against the database saving measurment data to the measurements table.
    * @return true for a successful insert or false otherwise.
    */
   public boolean setMyMeasurments(String sensorName, int value, int rawValue)
   {
      boolean result = false;
      try
      {
         if (!connected())
         {
            connect();
         }
         setMyMeasurments.setString(1, sensorName);
         setMyMeasurments.setInt(2, value);
         setMyMeasurments.setInt(3, rawValue);
         int i = setMyMeasurments.executeUpdate();
         if(i>0)
         {
            result = true;
         }
      }
      catch (SQLException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return result;
   }
   /**
        * when called the getSpaces prepared statement is run against the database resulting is a list of spaces config being returned.
    * @return ArrayList<String[]> spaces, A arraylist with the spaces configuration in string form.
    */
   public ArrayList<String[]> getSpaces()
   {
      return resultToArray(performQuery(getSpaces));
   }
   /**
    * Takes an prepared statment and performs it
    * @param statment a PreparedStatement
    * @return ResultSet of the performed prepared statement
    */
   private ResultSet performQuery(PreparedStatement statment)
   {
      ResultSet result = null;
      try
      {
         if (!connected())
         {
            connect();
         }
         result = statment.executeQuery();
      }
      catch (SQLException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return result;
   }
   /**
    * takes a result set from a query or prepared statment and makes an string array out of it.
    * @param result
    * @return and array list of strings representing the result of the query.
    */
   private ArrayList<String[]> resultToArray(ResultSet result)
   {
      try
      {
         int nCol = result.getMetaData().getColumnCount();
         ArrayList<String[]> table = new ArrayList<>();
         while (result.next())
         {
            String[] row = new String[nCol];
            for (int iCol = 1; iCol <= nCol; iCol++)
            {
               row[iCol - 1] = result.getObject(iCol).toString();
            }
            table.add(row);
         }
         return table;
      }
      catch (SQLException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return null;
   }
   /**
    * test whether the DAO is connected to the database
    * @return true, if there is a connection false if not
    */
   private Boolean connected()
   {
      if (connection != null) { return true; }
      return false;
   }
   /**
    * connect to hte database
    * @return null if there was an exception when connecting. otherwise return a connection object.
    */
   private Connection connect()
   {
      if (connected())
      {
         disConnect();
      }
      try
      {
         connection = DriverManager.getConnection(url, user, password);
         return connection;
      }
      catch (SQLException ex)
      {
         System.out.println(ex.getMessage());
         return null;
      }
   }
   /**
    * if connection is not null then close it.
    * this might be a place to implement some more checks to do before disconnecting.
    */
   public void disConnect()
   {
      try
      {
         if (connection != null)
         {
            connection.close();
         }
      }
      catch (SQLException ex)
      {
         System.out.println(ex.getMessage());
      }
   }
   
   // private Connection getConnection()
   // {
   // return connection;
   // }
   
}
