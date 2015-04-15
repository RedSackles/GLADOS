package local.redlan.glados.systemfunctions;

import java.util.ArrayList;
import java.util.Hashtable;

import local.redlan.glados.Spaces.GenericSpace;
import local.redlan.glados.actuators.GenericActuator;
import local.redlan.glados.actuators.Sirene;
import local.redlan.glados.sensors.GenericSensor;
import local.redlan.glados.sensors.IntrusionNoneTamperAware;
import local.redlan.glados.sensors.IntrusionSensor;
import local.redlan.glados.sensors.IntrusionTamperAware;

/**
 * higher functionality (yes finally) this is a thermostat that thakes a temperature sensor and uses it to determine
 * whether or not to switch on the heating or cooling. This according to a schedule and presence data (still needs to be implemented)
 * @author WarpsR
 */
public class IntrusionDetection extends GenericFunction
{
   /**
    * the global intrusion detection object 
    */
   private GlobalIntrusionDetection intrusionSystem = GlobalIntrusionDetection.getInstance();
   /**
    * the sensor that this IntrusionDetection is using
    */
   private ArrayList<GenericSensor> myInputs = new ArrayList<>();
   /**
    * the actuator that this IntrusionDetection is using
    */
   private ArrayList<GenericActuator> myOutputs = new ArrayList<>();
   /**
    * is there an Alarm?
    */
   private boolean intrusionAlarm = false;
   private boolean tamperAlarm = false;
   /**
    * operational mode?
    */
   private boolean armed = false;
   private boolean maintenance = false;
   /**
    * the name of this function type used in the database
    */
   private static String functionType = "IntrusionDetection";
   
   /**
    * A private constructor as we don't want to create useless empty objects
    * @param intrusionDetectionName
    * @param Space
    */
   private IntrusionDetection(String intrusionDetectionName, GenericSpace Space)
   {
      super(intrusionDetectionName, Space);
      ArrayList<String[]> sensorList = dAO.getMySensors(Space.getName(), functionType);
      for (String[] row : sensorList)
      {
         System.out.println("Space: "+Space.getName());
         System.out.println("Sensor: "+row[0]);
         // TODO explain the row numbers / column names
         // this will couse things to go boom id the type in the database is not in the switch
         IntrusionSensor myInput = null;
         switch (row[1])
         {
            case "IntrusionTamperAware":
               myInput = new IntrusionTamperAware(row[2], row[0], row[3], Integer.parseInt(row[4]),Integer.parseInt(row[5]),Integer.parseInt(row[6]),Integer.parseInt(row[7]));
               System.out.println("creating IntrusionTamperAware: "+row[0]);
               break;
            case "IntrusionNoneTamperAware":
               myInput = new IntrusionNoneTamperAware(row[2], row[0], row[3], Integer.parseInt(row[4]),Integer.parseInt(row[5]),Integer.parseInt(row[6]),Integer.parseInt(row[7]));
               System.out.println("creating IntrusionNoneTamperAware: "+row[0]);
               break;
         }
         myInput.setMyFunction(this);
         myInputs.add(myInput);
         // TODO add initializing logging?
      }
      ArrayList<String[]> ActuatorList = dAO.getMyActuators(Space.getName(), functionType);
      for (String[] row : ActuatorList)
      {
         System.out.println("Space: "+Space.getName());
         System.out.println("Actuator: "+row[0]);
         GenericActuator myOutput = null;
         // TODO explain the row numbers / column names
         switch (row[1])
         {
            case "Sirene":
               myOutput = new Sirene(row[2], row[0], row[3], Integer.parseInt(row[4]));
               myOutput.setMyFunction(this);
               System.out.println("creating Sirene: "+row[0]);
               break;
         }
         myOutputs.add(myOutput);
         // TODO add initializing logging?
      }
   }
   
   /**
    * this function is to check whether an input recives is par of the myInputs array if not add it
    * sionce we just got a new input
    */
   private void checkInputs(GenericSensor sensor)
   {
      if (!myInputs.contains(sensor))
      {
         myInputs.add(sensor);
      }
   }
   
   /**
    * this function is called by spaces to have their IntrusionDetection created. 
    * Upon calling it is not yet known whether this space actually has IntrusionDetection.
    * @param Space space object that is requesting its thermostat
    * @return the IntrusionDetection object of the space or Null if there is no IntrusionDetection for the space provided
    */
   public static IntrusionDetection getMyIntrusionDetection(GenericSpace space)
   {
      String functionName = GenericFunction.getMyfunction(space, "IntrusionDetection");
      if (!(functionName == null))
      {
         return new IntrusionDetection(functionName, space);
      }
      return null;
   }
   /**
    * intrusion sensors will use this function to pas along an Alarm to the function of the space
    * in turn the function will pas it to the global system 
    * @param sensor the sensor responsible for the alarm
    */
   public void intrusionAlarm(GenericSensor sensor)
   {
      checkInputs(sensor);
      if (!intrusionAlarm && isArmed())
      {
         intrusionAlarm = true;
         System.out.println("Intrusion Alarm: "+sensor.getSensorName()+" value:"+Integer.toString(sensor.getCurrentValue()));
         if (isArmed())
         {
            intrusionSystem.soundTheAlarm(this);
         }
      }
      this.space.setOccupation(sensor.getWatchInterval()*3);
   }
   /**
    * intrusion sensors will use this function to pas along an TamperAlarms to the function of the space
    * in turn the function will pas it to the global system 
    */
   public void tamperingAlarm(GenericSensor sensor)
   {
      checkInputs(sensor);
      if (!tamperAlarm)
      {
         tamperAlarm = true;
         System.out.println("Tampering Alarm: "+sensor.getSensorName()+" value:"+Integer.toString(sensor.getCurrentValue()));
         if (!isMaintenance())
         {
            intrusionSystem.soundTheTamperAlarm(this);
         }
      }
   }
   /**
    * intrusion sensors will use this function to pas along restores of Alarms to the function of the space
    * in turn the function will pas it to the global system 
    * @param sensor the sensor responsible for the alarm
    */
   public void resetIntrusionAlarm(GenericSensor sensor)
   {
      checkInputs(sensor);
      if (intrusionAlarm && !otherIntrusionAlarms())
      {
         intrusionAlarm = false;
         intrusionSystem.resetIntrusionAlarm(this);
         System.out.println("Restored: "+sensor.getSensorName()+" value:"+Integer.toString(sensor.getCurrentValue()));
      }
   }
   /**
    * intrusion sensors will use this function to pas along restores of TamperAlarms to the function of the space
    * in turn the function will pas it to the global system 
    * @param sensor the sensor responsible for the alarm
    */
   public void resetTamperAlarm(GenericSensor sensor)
   {
      checkInputs(sensor);
      if (tamperAlarm && !otherTamperAlarms())
      {
         tamperAlarm = false;
         intrusionSystem.resetTamperAlarm(this);
         System.out.println("Restored: "+sensor.getSensorName()+" value:"+Integer.toString(sensor.getCurrentValue()));
      }
   }
   /**
    * 
    */
   public boolean isIntrusionAlarm()
   {
      return intrusionAlarm;
   }
   /**
    * 
    */
   public boolean isTamperAlarm()
   {
      return tamperAlarm;
   }
   /**
    * turn on and sirens that this function controls
    */
   public void activateSirens()
   {
      for (GenericActuator actuator : myOutputs)
      {
         if (actuator instanceof Sirene)
         {
            ((Sirene) actuator).turnON();
         }
      }
   }
   /**
    * turn off and sirens that this function controls
    */
   public void deactivateSirens()
   {
      for (GenericActuator actuator : myOutputs)
      {
         if (actuator instanceof Sirene)
         {
            ((Sirene) actuator).turnOFF();
         }
      }
   }
   /**
    *
    */
   public ArrayList<GenericSensor> getMyInputs()
   {
      return myInputs;
   }
   /**
   *
   */
   public ArrayList<GenericActuator> getMyOutputs()
   {
      return myOutputs;
   }
   /**
    * in order to produce a status overview of the system the subclasses return lists with name value combinations
    */
   @SuppressWarnings("rawtypes")
   @Override
   public Hashtable<String, Hashtable> getStatus()
   {
      Hashtable<String, Hashtable> sensorsData = new Hashtable<String, Hashtable>();
      for (GenericSensor sensor : myInputs)
      {
         Hashtable<String, String> sensorData = new Hashtable<String, String>();
         sensorData.put("name", sensor.getSensorName());
         sensorData.put("State", sensor.getStatus());
         sensorsData.put(sensor.getSensorName(), sensorData);
      }
      
      Hashtable<String, Hashtable> actuatorsData = new Hashtable<String, Hashtable>();
      for (GenericActuator actuator : myOutputs)
      {
         Hashtable<String, String> actuatorData = new Hashtable<String, String>();
         actuatorData.put("name", actuator.getSourceName());
         actuatorData.put("State", actuator.getStatus());
         actuatorsData.put(actuator.getSourceName(), actuatorData);
      }
      
      Hashtable<String, String> statusDataIntrusionDetection = new Hashtable<String, String>();
      String temp = "false";
      if(isArmed())
      {
         temp = "true";
      }
      statusDataIntrusionDetection.put("Armed", temp);
      temp = "false";
      if(isMaintenance())
      {
         temp = "true";
      }
      statusDataIntrusionDetection.put("Maintenance", temp);
      Hashtable<String, Hashtable> statusData = new Hashtable<String, Hashtable>();
      statusData.put(this.getFunctionName(), statusDataIntrusionDetection);
      statusData.put("Sensors", sensorsData);
      statusData.put("Actuators", actuatorsData);
      return statusData;
   }
   /**
    * 
    */
   public boolean isArmed()
   {
      return armed;
   }
   /**
    * 
    */
   public void disarm()
   {
      this.armed = false;
   }
   public void arm()
   {
      this.armed = true;
   }
   /**
    * 
    */
   public boolean isMaintenance()
   {
      return maintenance;
   }
   /**
    * 
    */
   public void setMaintenance()
   {
      this.maintenance = true;
   }
   public void unsetMaintenance()
   {
      this.maintenance = false;
   }
   /**
    * test whetehr there are other intrusion function that have a intrusion alarm state
    * @return true if there is at least one other intrusion alarm active, false otherwise
    */
   private boolean otherIntrusionAlarms()
   {
      for(GenericSensor input : myInputs)
      {
         if (input.getStatus() == "OpenLoop" || input.getStatus() == "ON" )
            {
               return true;
            }
      }
      return false;
   }
   /**
    * test whetehr there are other intrusion function that have a tamper alarm state
    * @return true if there is at least one other tamper alarm active, false otherwise
    */
   private boolean otherTamperAlarms()
   {
      for(GenericSensor input : myInputs)
      {
         if (input.getStatus() == "TamperdLoop")
            {
               return true;
            }
      }
      return false;
   }
   
}