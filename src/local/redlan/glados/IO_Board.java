package local.redlan.glados;

import java.util.ArrayList;

import local.redlan.glados.DB.DB_GladosDAO;

/**
 * IO_Board abstract base class
 * @author WarpsR
 */
public abstract class IO_Board
{
   /**
    * Database Access Object in order for us to save our measurements to the database
    */
   protected static DB_GladosDAO dAO = Glados.getGlados().getGladosDAO();
   /**
    * unique IO_Board name
    */
   public final String name;
   /**
    * IO_Board physical/administrative location
    */
   public final String location;
   /**
    * IO_Board IP address
    */
   protected String ip;
   /**
    * A list off all the InOrOutPut objects associated with this IO_Board
    */
   private ArrayList<InOrOutPut> myIOList = new ArrayList<>();
   /**
    * IO_Board constructor 
    * @param name unique IO_Board name
    * @param location IO_Board physical/administrative location
    */
   public IO_Board(String name, String location)
   {
      this.name = name;
      this.location = location;
   }
   /**
    * 
    */
   public ArrayList<InOrOutPut> getMyIOList()
   {
      return myIOList;
   }
   /**
    * 
    */
   public String getIp()
   {
      return ip;
   }
   /**
    * abstract function to return the proper communicator for this IO_Board
    */
   protected abstract Communicator getCommunicator();
   /**
    * abstract function to return the current value of an InOrOutPut
    */
   public abstract String getValue(InOrOutPut iOPut);
   /**
    * abstract function to set the value of an InOrOutPut
    */
   public abstract String setValue(InOrOutPut iOPut);
   /**
    * abstract function to return the current value of multiple InOrOutPuts
    */
   public abstract String[] getValues(InOrOutPut[] InOrOutputs);
   /**
    * abstract function to set the value of multiple InOrOutPuts
    */
   public abstract String[] setValues(InOrOutPut[] InOrOutputs);
}
