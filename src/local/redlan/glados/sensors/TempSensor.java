package local.redlan.glados.sensors;

import local.redlan.glados.Glados;
import local.redlan.glados.IO_Event;
import local.redlan.glados.IO_EventHandler;
import local.redlan.glados.IO_Event_Measurement;
import local.redlan.glados.systemfunctions.GenericFunction;
import local.redlan.glados.systemfunctions.Thermostat;

/**
 * 
 * @author abstract tmeperature sensing class
 */
public abstract class TempSensor extends GenericSensor
{
   /**
    * a array of values received, this helps counter the flapping that can occur in the analog measuring of temperature.
    */
   private Integer[] previousValues = new Integer[]{0,0,0,0,0};
   /**
    * the time this sensors value will be logged next
    */
   private long nextLogging = System.currentTimeMillis();
   /**
    * the interval in ms that his sensor will be logged
    */
   private int LogInterval = 0;
   /**
    * A adjustment to the sensor measurements 
    */
   protected int adjustment = 0;
   /**
    * the Thermostat object of this tempsensor
    */
   private Thermostat myThermostat = null;
   /**
    * Reference to the IO_temperatureHandler in order to set subscription our sensor events
    */
   protected IO_EventHandler climateHandler = Glados.getClimateHandler();
   /**
    * Temperature sensor constructor takes parameters and grates a sensor
    * this class has temperature specific functionality like a temperature event handler
    * @param aInName Analog input name
    * @param sensorName the name of the sensor object
    * @param location physical/administrative location of the sensor
    * @param watchMe the watch/monitor interval for this sensor in ms, 0 means no watching (any thing below 100 and you need to get your head checked)
    */
   public TempSensor(String aInName,String sensorName,String location, int watchMe, int bottumTreshold, int midTreshold, int highTreshold, int logMe, int adjustMe)
   {
      super(aInName, sensorName, location, watchMe, bottumTreshold, midTreshold, highTreshold);
      climateHandler.setSubscribtion(this.getIOPutIDNumber());
      this.nextLogging = System.currentTimeMillis()+logMe;
      this.LogInterval = logMe;
      this.adjustment = adjustMe;
   }
   /**
    * get the current temperature from the sensor and return it by default take the raw value
    * @return the int value of the current temperature in °Celsius (only kelvin is an acceptable alternative)
    */
   public int getCurrentTemp()
   {
      return super.getCurrentValue();
   }
   /**
    * process events from the watcher
    */
   public void processEvent(IO_Event event)
   {
      if (System.currentTimeMillis() > nextLogging && LogInterval != 0)
      {
         IO_Event_Measurement temp = new IO_Event_Measurement();
         temp.setEventSource(event.getEventSource());
         temp.setIntvalue(getCurrentTemp());
         temp.setRawValue(event.getIntvalue());
         temp.setSubscribtion(event.getEventSource().getSourceSensor().getIOPutIDNumber());
         climateHandler.eventToDispatchQueue((IO_Event)temp);
         nextLogging = System.currentTimeMillis()+LogInterval;
      }
      this.myThermostat.TemperatureChange(newAverige(getCurrentTemp()));
   }
   /**
    * adds the parameter newValue to the list of previousValues shifting them along and in the proces removing one from the list.
    * then calculates the average and returns it.
    * @param newValue
    * @return a double that is the result of calculating the new everage of the last 5 temperature measurements.
    */
   private double newAverige(int newValue)
   {
      
      for(int i = 0; i < previousValues.length; i++)
      {
         if(i + 1 < previousValues.length)
         {
            previousValues[i + 1] = previousValues[i];
         }
      }
      previousValues[0] = newValue;
      int sum = 0;
      for(int value:previousValues)
      {
         sum += value;
      }
      return sum / previousValues.length;
   }
   /**
    * 
    */
   @Override
   public void setMyFunction(GenericFunction myFunction)
   {
      this.myFunction = myFunction;
      this.myThermostat = (Thermostat) this.myFunction;
   }
}
