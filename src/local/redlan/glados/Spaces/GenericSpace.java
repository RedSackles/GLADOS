package local.redlan.glados.Spaces;

import java.util.ArrayList;
import java.util.Hashtable;

import local.redlan.glados.Glados;
import local.redlan.glados.DB.DB_GladosDAO;
import local.redlan.glados.systemfunctions.IntrusionDetection;
import local.redlan.glados.systemfunctions.Lights;
import local.redlan.glados.systemfunctions.Thermostat;

/**
 * base class for all spaces/rooms in the system
 * @author WarpsR
 */
public abstract class GenericSpace
{
   /**
    * the name of the room
    */
   protected String name = "";
   /**
    * Database Access Object in order for us to save our measurements to the database
    */
   protected static DB_GladosDAO dAO = Glados.getGlados().getGladosDAO();
   /**
    * system time in milliseconds until the occupation of this space times out
    */
   private long occupationTimeOut = System.currentTimeMillis();
   /**
    * to prevent unnecessarily switching on the heating because the room was only occupied briefly
    * there is a threshold time that needs to pass before the room is considered occupied
    */
   private int occupationThreshold = 10000;
   private boolean waitingForThresholdToPass = false;
   private long occupationTime = System.currentTimeMillis();
   /**
    * list of all the spaces in the system.
    */
   private static ArrayList<GenericSpace> spaceList = new ArrayList<>();
   /**
    * this room has a thermostat (how novel)
    */
   private Thermostat thermostat = null;
   /**
    * this room has IntrusionDetection (how novel)
    */
   private IntrusionDetection intrusionDetection = null;
   /**
    * this room has Lights (how novel)
    */
   private Lights lights = null;
   
   public GenericSpace (String name, int occupationThreshold)
   {
      this.name = name;
      this.occupationThreshold = occupationThreshold;
      spaceList.add(this);
      this.thermostat = Thermostat.getMyThermostat(this);
      this.intrusionDetection = IntrusionDetection.getMyIntrusionDetection(this);
      this.lights = Lights.getMyLights(this);
   }
   /**
    * 
    */
   public String getName()
   {
      return name;
   }
   /**
    * 
    */
   public static ArrayList<GenericSpace> getSpaceList()
   {
      return spaceList;
   }
   /**
    * is the timeout greater then the current time then the room is occupied
    */
   public boolean getOccupation()
   {
      return occupationTimeOut > System.currentTimeMillis();
   }
   /**
    * set a new timeout 
    */
   public void setOccupation(long occupationTimeOut)
   {
      if (!getOccupation() && !waitingForThresholdToPass)
      {
         setOccupationTimeOut(System.currentTimeMillis());
         occupationTime = System.currentTimeMillis()+occupationThreshold;
         waitingForThresholdToPass = true;
      }
      else if (waitingForThresholdToPass && occupationTime < System.currentTimeMillis() && this.occupationTimeOut > occupationTime-occupationThreshold )
      {
         waitingForThresholdToPass = false;
      }
      else
      {
         setOccupationTimeOut(System.currentTimeMillis());
      }
      if(!waitingForThresholdToPass)
      {
         if(occupationTimeOut < System.currentTimeMillis()+occupationThreshold)
         {
            setOccupationTimeOut(System.currentTimeMillis()+occupationThreshold);
         }
         else
         {
            setOccupationTimeOut(occupationTimeOut);
         }
      }
   }
   /**
    * is the timeout greater then the current time then the room is occupied
    */
   private void setOccupationTimeOut(long occupationTimeOut)
   {
      if (this.occupationTimeOut < occupationTimeOut)
      {
         this.occupationTimeOut = occupationTimeOut;
      }
      
   }
   /**
    * 
    */
   public void setThermostat(Thermostat thermostat)
   {
      this.thermostat = thermostat;
   }
   
   /**
    * 
    */
   public void setIntrusionDetection(IntrusionDetection intrusionDetection)
   {
      this.intrusionDetection = intrusionDetection;
   }
   
   /**
    * 
    */
   public Thermostat getThermostat()
   {
      return thermostat;
   }
   
   /**
    * 
    */
   public IntrusionDetection getIntrusionDetection()
   {
      return intrusionDetection;
   }
   
   /**
    * in order to produce a status overview of the system the subclasses return
    * lists with name value combinations
    */
   @SuppressWarnings("rawtypes")
   public Hashtable<String, Hashtable> getStatus()
   {
      Hashtable<String, Hashtable> statusData = new Hashtable<String, Hashtable>();
      if (intrusionDetection != null)
      {
         statusData.put(intrusionDetection.getFunctionName(), intrusionDetection.getStatus());
      }
      if (thermostat != null)
      {
         statusData.put(thermostat.getFunctionName(), thermostat.getStatus());
      }
      if (lights != null)
      {
         statusData.put(lights.getFunctionName(), lights.getStatus());
      }
      return statusData;
   }
}
