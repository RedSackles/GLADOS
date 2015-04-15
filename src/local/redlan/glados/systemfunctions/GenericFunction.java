package local.redlan.glados.systemfunctions;

import java.util.ArrayList;
import java.util.Hashtable;
import local.redlan.glados.Glados;
import local.redlan.glados.DB.DB_GladosDAO;
import local.redlan.glados.Spaces.GenericSpace;

/**
 * Base class for all functions 
 * @author WarpsR
 */
public abstract class GenericFunction
{
   /**
    * Database Access Object in order for us to access the Glados database
    */
   protected static DB_GladosDAO dAO = Glados.getGlados().getGladosDAO();
   /**
   * lets list all the GenericFunctions we create
   */
   protected static ArrayList<GenericFunction> gFunctionList = new ArrayList<>();
   /**
    * the name of this function
    */
   protected String functionName = null;
   /**
    * the space object this function belongs to
    */
   protected GenericSpace space = null;
   
   /**
    * takes function name and space to set their variables
    * @param functionName the name of this function
    * @param space the space object this function belongs to
    */
   public GenericFunction(String functionName, GenericSpace space)
   {
      this.functionName = functionName;
      this.space = space;
      gFunctionList.add(this);
   }
   
   /**
    * this function is called by the child to test whether there is a function of this kind configured. 
    * Upon calling it is not yet known whether this space actually has a Function of the supplied type.
    * @param Space space object that is requesting its thermostat
    * @param functionType the type of the function
    * @return String the function name of the particular function, Null if no function is found.
    */
   protected static String  getMyfunction(GenericSpace space, String functionType)
   {
      ArrayList<String[]> spaceConfig = dAO.getMyFunction(space.getName(), functionType);
      for (String[] row : spaceConfig)
      {
         // TODO explain the row numbers / column names
         return row[0];
         // TODO add initializing logging?
      }
      return null;
   }
   
   /**
    * Method getFromAnalogInList
    * Returns an instance of AnalogIn with the specified name or returns Null.
    * @param name String the unique name of the input
    * @return AnalogIn
    */
   public static ArrayList<GenericFunction> getgFunctionList()
   {
      return gFunctionList;
   }
   /**
    * 
    */
   public String getFunctionName()
   {
      return functionName;
   }
   /**
    * in order to produce a status overview of the system the subclasses return lists with name value combinations
    */
   @SuppressWarnings("rawtypes")
   public abstract Hashtable<String, Hashtable> getStatus();
}