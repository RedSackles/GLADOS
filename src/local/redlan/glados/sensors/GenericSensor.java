package local.redlan.glados.sensors;

import java.util.ArrayList;

import local.redlan.glados.Glados;
import local.redlan.glados.IO_Event;
import local.redlan.glados.Input;
import local.redlan.glados.IO_EventSourceAble;
import local.redlan.glados.InOrOutPut_User;
import local.redlan.glados.Watchable;
import local.redlan.glados.actuators.GenericActuator;
import local.redlan.glados.systemfunctions.GenericFunction;

/**
 * base class for all sensors
 * @author WarpsR
 */
public abstract class GenericSensor implements Watchable, IO_EventSourceAble, InOrOutPut_User
{
   /**
    * this represents the input that the physical sensor is attached to
    */
   protected Input mySensor = null;
   /**
    * this represents the space function that uses this sensor. 
    * this is particularly usefull when the input of the sensor has to triger something in the function.
    */
   protected GenericFunction myFunction = null;
   /**
    * this represents the sensor in a way that meatbags get it
    */
   protected String sensorName = null;
   /**
    * this represents the physical location sensor is at
    */
   protected String location = null;
   /**
    * for analog sensors thresholds can be set to trigger actions
    * there as 3 levels f thresholds
    */
   protected int bottumTreshold = 0;
   protected int midTreshold = 0;
   protected int highTreshold = 0;
   /**
    * watch interval in ms
    */
   protected int watchInterval = 0;
   /**
    * a list of all GenericSensor objects this is used for easily searching an Sensor by its name.
    * each GenericSensor adds itself to the list on creation.
    */
   public static ArrayList<GenericSensor> genericSensorList = new ArrayList<>();
   
   public GenericSensor(String inputName,String sensorName,String location, int watchMe, int bottumTreshold, int midTreshold, int highTreshold)
   {
      this.mySensor = Input.getFromInList(inputName);
      this.mySensor.setUserObj(this);
      this.sensorName = sensorName;
      this.location = location;
      this.bottumTreshold = bottumTreshold;
      this.midTreshold = midTreshold;
      this.highTreshold = highTreshold;
      genericSensorList.add(this);
      if (watchMe > 0 )
      {
         Glados.getMyWatcher(watchMe).watchThis(this);
         this.watchInterval = watchMe;
      }
   }
   /**
    * 
    */
   public int getWatchInterval()
   {
      return watchInterval;
   }
   /**
    * get the current value from the sensor and return it by default take the raw value
    * @return the int value of the input
    */
   public int getCurrentValue()
   {
      mySensor.refresh();
      return Integer.parseInt(mySensor.getLocalValue());
   }
   /**
    * Takes an input and if there is no object for mySensor sets it as mySensor
    * @param mySensor the Analog input that the physical sensor is connected to
    */
   public void setMySensor(Input mySensor)
   {
      if (mySensor == null)
      {
         this.mySensor = mySensor;
      }
   }
   /**
    * 
    */
   public String getSensorName()
   {
      return sensorName;
   }
   /**
    * 
    */
   public String getLocation()
   {
      return location;
   }
   /**
    * Method watch
    * returns the result of the refresh method. 
    * This is needed for polling.
    * 
    * @return Boolean
    */
   @Override
   public Boolean watch()
   {
      return mySensor.refresh();
   }

   /**
    * Method getNewValue
    * returns getLocalValue. 
    * This is needed for polling, as watch() does not return a string representation of the value
    * 
    * @return String
    */
   @Override
   public String getNewValue()
   {
      return mySensor.getLocalValue().toString();
   }

   /**
    * Method getIOPutIDNumber
    * returns the In or Output ID, these are used in generating events and having event handlers subscribe to them. 
    * 
    * @return int
    */
   @Override
   public int getIOPutIDNumber()
   {
      return mySensor.getIOPutIDNumber();
   }
   /**
    * returns the name variable
    */
   @Override
   public String getSourceName()
   {
      return sensorName;
   }
   /**
    * return the object itself, this allowed the watcher to pass along the object associated with an event.
    */
   @Override
   public IO_EventSourceAble getEventSource()
   {
      return this;
   }
   /**
    * return the InOrOutPut object used by this event source object.
    */
   @Override
   public GenericSensor getSourceSensor()
   {
      return this;
   }
   
   /**
    * 
    */
   @Override
   public GenericActuator getSourceActuator()
   {
      // TODO Auto-generated method stub
      return null;
   }
   /**
    * getFromdigitalInList is called by other classes to have the digitalInList list checked for the name of a particular input.
    * If found the DigitalIn is returned if not null is returned (Have fun with the nullpointer errors!)
    * 
    * @param name String the name of the particular output that is being requested
    * 
    * @return DigitalOut 
    */
   public static GenericSensor getFromSensorList(String name)
   {
      for(GenericSensor sensor : genericSensorList)
      {
         if(sensor.getSourceName().equals(name))
         {
            return sensor;
         }
      }
      return null;
   }
   /**
    * process events from the watcher
    */
   public abstract void processEvent(IO_Event event);
   /**
    * 
    */
   public void setMyFunction(GenericFunction myFunction)
   {
      this.myFunction = myFunction;
   }
   /**
    * returns a string representation of the current Sensor status
    */
   public abstract String getStatus();
}
