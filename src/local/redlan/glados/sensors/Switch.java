package local.redlan.glados.sensors;

import local.redlan.glados.Glados;
import local.redlan.glados.IO_Event;
import local.redlan.glados.IO_EventHandler;
import local.redlan.glados.systemfunctions.GenericFunction;

/**
 * 
 * @author Warpsr
 *
 */
public abstract class Switch extends GenericSensor
{
   /**
    * the Function object of this switch
    */
   protected GenericFunction myFunction = null;
   /**
    * Reference to the IO_temperatureHandler in order to set subscription our sensor events
    */
   protected IO_EventHandler GeneralHandler = Glados.getGeneralHandler();
   /**
    * Switch sensor constructor takes parameters and creates a Switch sensor object
    * this class has temperature specific functionality like a temperature event handler
    * @param dInName digital input name
    * @param sensorName the name of the sensor object
    * @param location physical/administrative location of the sensor
    * @param watchMe the watch/monitor interval for this sensor in ms, 0 means no watching (any thing below 100 and you need to get your head checked)
    */
   public Switch(String dInName,String sensorName,String location, int watchMe)
   {
      super(dInName, sensorName, location, watchMe, 0, 0, 0);
      GeneralHandler.setSubscribtion(this.getIOPutIDNumber());
   }
   
   /**
    * process events from the watcher
    */
   public abstract void processEvent(IO_Event event);
}
