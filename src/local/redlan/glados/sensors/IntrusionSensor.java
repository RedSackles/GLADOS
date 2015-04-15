package local.redlan.glados.sensors;

import local.redlan.glados.Glados;
import local.redlan.glados.IO_EventHandler;

/**
 * 
 * @author abstract tmeperature sensing class
 */
public abstract class IntrusionSensor extends GenericSensor
{
   /**
    * Reference to the IO_temperatureHandler in order to set subscription our sensor events
    */
   protected IO_EventHandler securityHandler = Glados.getSecurityHandler();
   /**
    * Temperature sensor constructor takes parameters and grates a sensor
    * this class has temperature specific functionality like a temperature event handler
    * @param aInName Analog input name
    * @param sensorName the name of the sensor object
    * @param location physical/administrative location of the sensor
    * @param watchMe the watch/monitor interval for this sensor in ms, 0 means no watching (any thing below 100 and you need to get your head checked)
    */
   public IntrusionSensor(String aInName,String sensorName,String location, int watchMe, int bottumTreshold, int midTreshold, int highTreshold)
   {
      super(aInName, sensorName, location, watchMe, bottumTreshold, midTreshold, highTreshold);
      securityHandler.setSubscribtion(this.getIOPutIDNumber());
   }
}
