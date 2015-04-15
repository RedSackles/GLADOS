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
public class IntrusionTamperAware extends IntrusionSensor
{
   public IntrusionTamperAware(String aInName,String sensorName,String location, int watchMe, int bottumTreshold, int midTreshold, int highTreshold)
   {
      super(aInName, sensorName, location, watchMe, bottumTreshold, midTreshold, highTreshold);
   }
   
   /**
    * process events from the watcher
    */
   public void processEvent(IO_Event event)
   {
      IntrusionDetection function = (IntrusionDetection) myFunction;
      if (event.getIntvalue() > this.midTreshold && event.getIntvalue() < this.highTreshold)
      {
         function.resetIntrusionAlarm(this);
         function.resetTamperAlarm(this);
      }
      if (event.getIntvalue() < this.midTreshold && event.getIntvalue() > this.bottumTreshold)
      {
         function.intrusionAlarm(this);
         function.resetTamperAlarm(this);
      }
      if (event.getIntvalue() < this.bottumTreshold || event.getIntvalue() > this.highTreshold)
      {
         function.tamperingAlarm(this);
         function.resetIntrusionAlarm(this);
      }
//      System.out.println("alarm?: "+Integer.toString(event.getIntvalue()));
   }
   
   /**
    * returns a string representation of the current Sensor status
    */
   @Override
   public String getStatus()
   {
      int sensorValue = Integer.parseInt(mySensor.getLocalValue());
      String status = "ERROR";
      if (sensorValue > this.midTreshold && sensorValue < this.highTreshold)
      {
         status = "ClosedLoop";
      }
      if (sensorValue < this.midTreshold && sensorValue > this.bottumTreshold)
      {
         status = "OpenLoop";
      }
      if (sensorValue < this.bottumTreshold || sensorValue > this.highTreshold)
      {
         status = "TamperdLoop";
      }
      return status;
   }
}
