package local.redlan.glados.actuators;

import java.util.ArrayList;
import local.redlan.glados.Glados;
import local.redlan.glados.IO_Event;
import local.redlan.glados.IO_EventSourceAble;
import local.redlan.glados.InOrOutPut_User;
import local.redlan.glados.OutPut;
import local.redlan.glados.Watchable;
import local.redlan.glados.systemfunctions.GenericFunction;

/**
 * base class for all sensors
 * @author WarpsR
 */
public abstract class GenericActuator implements Watchable, IO_EventSourceAble, InOrOutPut_User
{
   /**
    * this represents the output that the physical actuator is attached to
    */
   protected OutPut myOutput = null;
   /**
    * this represents the space function that uses this sensor. 
    * this is particularly useful when the output of the actuator has to trigger something in the function.
    */
   protected GenericFunction myFunction = null;
   /**
    * this represents the sensor in a way that meatbags get it
    */
   protected String actuatorName = null;
   /**
    * this represents the physical location actuator is at
    */
   protected String location = null;
   /**
    * a list of all GenericActuator objects this is used for easily searching an Actuarot by its name.
    * each GenericActuator adds itself to the list on creation.
    */
   public static ArrayList<GenericActuator> genericActuatorList = new ArrayList<>();
   
   public GenericActuator(String inputName,String actuatorName,String location, int watchMe)
   {
      this.myOutput = OutPut.getFromOutList(inputName);
      this.myOutput.setUserObj(this);
      this.actuatorName = actuatorName;
      this.location = location;
      genericActuatorList.add(this);
      if (watchMe > 0 )
      {
         Glados.getMyWatcher(watchMe).watchThis(this);
      }
   }
   /**
    * get the current value from the sensor and return it by default take the raw value
    * @return the int value of the input
    */
   public int getCurrentValue()
   {
      myOutput.refresh();
      return Integer.parseInt(myOutput.getLocalValue());
   }
   /**
    * Takes an OutPut and if there is no object for myOutput sets it as myOutput
    * @param myActuator the OutPut that the physical Actuator is connected to
    */
   public void setMyActuator(OutPut myActuator)
   {
      if (myActuator == null)
      {
         this.myOutput = myActuator;
      }
   }
   /**
    * 
    */
   public String getSensorName()
   {
      return actuatorName;
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
      return myOutput.refresh();
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
      return myOutput.getLocalValue().toString();
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
      return myOutput.getIOPutIDNumber();
   }
   /**
    * returns the name variable
    */
   @Override
   public String getSourceName()
   {
      return actuatorName;
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
    * getFromdigitalInList is called by other classes to have the digitalInList list checked for the name of a particular input.
    * If found the DigitalIn is returned if not null is returned (Have fun with the nullpointer errors!)
    * 
    * @param name String the name of the particular output that is being requested
    * 
    * @return DigitalOut 
    */
   public static GenericActuator getFromSensorList(String name)
   {
      for(GenericActuator sensor : genericActuatorList)
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
    * returns a string representation of the current actuator status
    */
   public abstract String getStatus();
   
}
