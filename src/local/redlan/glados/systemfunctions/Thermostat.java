package local.redlan.glados.systemfunctions;

import java.util.ArrayList;
import java.util.Hashtable;

import local.redlan.glados.Spaces.GenericSpace;
import local.redlan.glados.actuators.GenericActuator;
import local.redlan.glados.actuators.Heater;
import local.redlan.glados.sensors.TempSensor;
import local.redlan.glados.sensors.Temperature_LM35DZ;

/**
 * higher functionality (yes finally) this is a thermostat that thakes a temperature sensor and uses it to determine
 * whether or not to switch on the heating or cooling. This according to a schedule and presence data (still needs to be implemented)
 * @author WarpsR
 */
public class Thermostat extends GenericFunction
{
   /**
    * the temperature sensor that this thermostat is using
    */
   private TempSensor myInput = null;
   /**
    * the temperature actuator that this thermostat is using
    */
   private Heater myHeater = null;
   private GenericActuator myCooler = null;
   /**
    * the Temperature values for this thermostat 
    */
   private int frostProtect = 0;
   private int energySave = 0;
   private int Comfort = 0;
   /**
    * a boolean to indicate whether the space is in frost protect mode.
    */
   private Boolean frostProtectMode = false;
   /**
    * the name of this function type used in the database
    */
   private static String functionType = "Thermostat";
   /**
    * A private constructor as we dont want to create useless empty objects
    * @param thermostatName
    * @param space
    */
   private Thermostat(String thermostatName, GenericSpace space, int frostProtect, int energySave, int comfort)
   {
      super(thermostatName, space);
      this.frostProtect = frostProtect;
      this.energySave = energySave;
      this.Comfort = comfort;
      ArrayList<String[]> sensorList = dAO.getMySensors(space.getName(), functionType);
      for (String[] row : sensorList)
      {
         System.out.println("Space: "+space.getName());
         System.out.println("Sensor: "+row[0]);
         // TODO explain the row numbers / column names
         switch (row[1])
         {
            case "Temperature_LM35DZ":
               myInput = new Temperature_LM35DZ(row[2], row[0], row[3], Integer.parseInt(row[4]),Integer.parseInt(row[5]),Integer.parseInt(row[6]),Integer.parseInt(row[7]),Integer.parseInt(row[8]),Integer.parseInt(row[9]));
               System.out.println("creating Temperature_LM35DZ: "+row[0]);
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
            case "Heater":
               myHeater = new Heater(row[2], row[0], row[3], Integer.parseInt(row[4]));
               myHeater.setMyFunction(this);
               System.out.println("creating Heater: "+row[0]);
               break;
         }
         // TODO add initializing logging?
      }
      TemperatureChange((double) getCurrentTemp());
   }
   
   /**
    * this function is called by spaces to have their thermostat created. 
    * Upon calling it is not yet known whether this space actually has a thermostat.
    * @param Space space object that is requesting its thermostat
    * @return the thermostat object of the space or Null id there is no Thermostat for the space provided
    */
   public static Thermostat getMyThermostat(GenericSpace space)
   {
      String functionName = GenericFunction.getMyfunction(space, "Thermostat");
      if (!(functionName == null))
      {
         ArrayList<String[]> functionConfig = dAO.getMyFunction(space.getName(), functionType);
         for (String[] row : functionConfig)
         {
            return new Thermostat(functionName, space,Integer.parseInt(row[1]),Integer.parseInt(row[2]),Integer.parseInt(row[3]));
         }
      }
      return null;
   }

   /**
    * 
    */
   public int getCurrentTemp()
   {
      return myInput.getCurrentTemp();
   }
   /**
    * called by the temperature sensor when there is a change in temperature detected.
    */
   public void TemperatureChange(double newAverige)
   {
      if(newAverige < getTargetTemp() )
      {
         myHeater.turnON();
        // System.out.println("cooling off");
      }
      if(newAverige >= getTargetTemp() &&  newAverige <= Comfort+4)
      {
         myHeater.turnOFF();
         //System.out.println("cooling off");
      }
      if(newAverige > Comfort+4)
      {
         myHeater.turnOFF();
         //System.out.println("cooling on");
      }
   }
   
   /**
    * reruns the current target temperature
    */
   private int getTargetTemp()
   {
      if (space.getOccupation())
      {
         return Comfort;
      }
      else if (frostProtectMode)
      {
         return frostProtect;
      }
      else
      {
         return energySave;
      }
   }
   /**
    * 
    */
   public TempSensor getMyInput()
   {
      return myInput;
   }
   /**
    * 
    */
   public Heater getMyHeater()
   {
      return myHeater;
   }
   /**
    * 
    */
   public GenericActuator getMyCooler()
   {
      return myCooler;
   }
   /**
    * in order to produce a status overview of the system the subclasses return lists with name value combinations
    */
   @SuppressWarnings("rawtypes")
   @Override
   public Hashtable<String, Hashtable> getStatus()
   {
      Hashtable<String, String> sensorData = new Hashtable<String, String>();
      sensorData.put("name", myInput.getSensorName());
      sensorData.put("temperature", Integer.toString(myInput.getCurrentTemp()));
      Hashtable<String, String> heaterData = new Hashtable<String, String>();
      heaterData.put("name", myHeater.getSensorName());
      heaterData.put("status", myHeater.getStatus());
      Hashtable<String, String> functionData = new Hashtable<String, String>();
      String temp = "false";
      if(space.getOccupation())
      {
         temp = "true";
      }
      functionData.put("ComfortMode", temp);
      temp = "false";
      if(frostProtectMode)
      {
         temp = "true";
      }
      functionData.put("frostProtectMode", temp);
      functionData.put("TargetTemp", Integer.toString(getTargetTemp()));
      Hashtable<String, Hashtable> statusData = new Hashtable<String, Hashtable>();
      statusData.put(this.getFunctionName(), functionData);
      statusData.put("Sensor", sensorData);
      statusData.put("Heater", heaterData);
      return statusData;
   }
}
