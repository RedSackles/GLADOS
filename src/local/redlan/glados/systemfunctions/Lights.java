package local.redlan.glados.systemfunctions;

import java.util.ArrayList;
import java.util.Hashtable;

import local.redlan.glados.Spaces.GenericSpace;
import local.redlan.glados.actuators.Light;
import local.redlan.glados.sensors.LightSwitch;

/**
 * higher functionality (yes finally) this is a thermostat that thakes a temperature sensor and uses it to determine
 * whether or not to switch on the heating or cooling. This according to a schedule and presence data (still needs to be implemented)
 * @author WarpsR
 */
public class Lights extends GenericFunction
{
   /**
    * the LightSwitch sensor that this Lights is using
    */
   private LightSwitch myInput = null;
   /**
    * the Light actuator that this Lights is using
    */
   private Light myLight = null;
   /**
    * the name of this function type used in the database
    */
   private static String functionType = "Lights";
   /**
    * two boolean values that help make all the lights flash once the function sirens is called
    */
   private static boolean sirens = false;
   private static boolean sirensFlash = false;
   /**
    * A private constructor as we don't want to create useless empty objects
    * @param lightsName
    * @param space
    */
   private Lights(String lightsName, GenericSpace space)
   {
      super(lightsName, space);
      ArrayList<String[]> sensorList = dAO.getMySensors(space.getName(), functionType);
      for (String[] row : sensorList)
      {
         System.out.println("Space: "+space.getName());
         System.out.println("Sensor: "+row[0]);
         // TODO explain the row numbers / column names
         switch (row[1])
         {
            case "LightSwitch":
               myInput = new LightSwitch(row[2], row[0], row[3], Integer.parseInt(row[4]));
               System.out.println("creating LightSwitch: "+row[0]);
               break;
         }
         myInput.setMyFunction(this);
         // TODO add initializing logging?
      }
      ArrayList<String[]> ActuatorList = dAO.getMyActuators(space.getName(), functionType);
      for (String[] row : ActuatorList)
      {
         System.out.println("Space: "+space.getName());
         System.out.println("Actuator: "+row[0]);
         // TODO explain the row numbers / column names
         switch (row[1])
         {
            case "Light":
               myLight = new Light(row[2], row[0], row[3], Integer.parseInt(row[4]));
               myLight.setMyFunction(this);
               System.out.println("creating Light: "+row[0]);
               break;
         }
         // TODO add initializing logging?
      }
   }
   
   /**
    * this function is called by spaces to have their thermostat created. 
    * Upon calling it is not yet known whether this space actually has a thermostat.
    * @param Space space object that is requesting its thermostat
    * @return the thermostat object of the space or Null id there is no Thermostat for the space provided
    */
   @SuppressWarnings("unused")
   public static Lights getMyLights(GenericSpace space)
   {
      String functionName = GenericFunction.getMyfunction(space, "Lights");
      if (!(functionName == null))
      {
         ArrayList<String[]> functionConfig = dAO.getMyFunction(space.getName(), functionType);
         for (String[] row : functionConfig)
         {
            return new Lights(functionName, space);
         }
      }
      return null;
   }
   

   /**
    * 
    */
   public LightSwitch getMyInput()
   {
      return myInput;
   }
   /**
    * 
    */
   public Light getMyLight()
   {
      return myLight;
   }
   /**
    * in order to produce a status overview of the system the subclasses return lists with name value combinations
    */
   @SuppressWarnings("rawtypes")
   @Override
   public Hashtable<String, Hashtable> getStatus()
   {
      Hashtable<String, String> lightSwitchData = new Hashtable<String, String>();
      lightSwitchData.put("Name", myInput.getSensorName());
      lightSwitchData.put("Status", myInput.getStatus());
      Hashtable<String, String> LightData = new Hashtable<String, String>();
      LightData.put("Name", myLight.getSensorName());
      LightData.put("status", myLight.getStatus());
      Hashtable<String, String> functionData = new Hashtable<String, String>();
      String temp = "OFF";
      if(myLight.getStatus() == "ON")
      {
         temp = "ON";
      }
      functionData.put("Lights", temp);
      Hashtable<String, Hashtable> statusData = new Hashtable<String, Hashtable>();
      statusData.put(this.getFunctionName(), functionData);
      statusData.put("Sensor", lightSwitchData);
      statusData.put("Light", LightData);
      return statusData;
   }

   public void turnON(LightSwitch lightSwitch)
   {
      if(!sirens)
      {
         this.space.getIntrusionDetection().intrusionAlarm(lightSwitch);
         myLight.turnON();
         this.space.setOccupation(System.currentTimeMillis()+myInput.getWatchInterval()*3);
      }
   }

   public void turnOFF(LightSwitch lightSwitch)
   {
      if(!sirens)
      {
         this.space.getIntrusionDetection().resetIntrusionAlarm(lightSwitch);
         myLight.turnOFF();
      }
   }
   
   public static void sirens(boolean active)
   {
      sirens = active;
      if (sirens)
      {
         Runnable runner = new Runnable()
         {
            @Override
            public void run()
            {
               ArrayList<Lights> lightsFunctions = getLights();
               while(sirens)
               {
                  for(Lights lFunction : lightsFunctions)
                  {
                     if(sirensFlash)
                     {
                        lFunction.getMyLight().turnON();
                     }
                     else
                     {
                        lFunction.getMyLight().turnOFF();
                     }
                  }
                  if(sirensFlash)
                  {
                     sirensFlash = false;
                  }
                  else
                  {
                     sirensFlash = true;
                  }
                  try
                  {
                     Thread.sleep(1000);
                  }
                  catch (InterruptedException e)
                  {
                     // TODO Auto-generated catch block
                     e.printStackTrace();
                  }
               }
               
            }
         };
         Thread thread = new Thread(runner);
         thread.start();
      }
   }
   
   /**
    * check the FunctionList and return all the Lights functions
    * @return ArrayList of all Lights functions
    */
   private static ArrayList<Lights> getLights()
   {
      ArrayList<GenericFunction> gFunctionList = new ArrayList<>();
      gFunctionList = GenericFunction.getgFunctionList();
      ArrayList<Lights> lightsFunctions = new ArrayList<>();
      for(GenericFunction gFunction : gFunctionList)
      {
         if(gFunction instanceof Lights)
         {
            lightsFunctions.add((Lights)gFunction);
         }
      }
      return lightsFunctions;
   }
}
