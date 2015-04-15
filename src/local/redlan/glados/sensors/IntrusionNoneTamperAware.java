/**
 * 
 */
package local.redlan.glados.sensors;

import local.redlan.glados.IO_Event;
import local.redlan.glados.systemfunctions.IntrusionDetection;

/**
 * this is a class for tamper aware intrusion sensors
 * @author WarpsR
 */
public class IntrusionNoneTamperAware extends IntrusionSensor
{
   public IntrusionNoneTamperAware(String aInName,String sensorName,String location, int watchMe, int bottumTreshold, int midTreshold, int highTreshold)
   {
      super(aInName, sensorName, location, watchMe, bottumTreshold, midTreshold, highTreshold);
   }
   
   /**
    * process events from the watcher
    */
   public void processEvent(IO_Event event)
   {
      IntrusionDetection function = (IntrusionDetection) myFunction;
      if (event.getIntvalue() == 1)
      {
         function.resetIntrusionAlarm(this);
      }
      if (event.getIntvalue() == 0)
      {
         function.intrusionAlarm(this);
      }
   }
   /**
    * returns a string representation of the current Sensor status
    */
   public String getStatus()
   {
      if (mySensor.getLocalValue() == "1")
      {
         return "ClosedLoop";
      }
      else
      {
         return "OpenLoop";
      }
   }
}
