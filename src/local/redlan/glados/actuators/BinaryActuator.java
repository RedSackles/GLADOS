/**
 * 
 */
package local.redlan.glados.actuators;

import local.redlan.glados.DigitalOut;
import local.redlan.glados.IO_Event;
import local.redlan.glados.sensors.GenericSensor;

/**
 * @author Warpsr
 */
public class BinaryActuator extends GenericActuator
{
   /**
    * 
    */
   protected DigitalOut MyDigitalOut = null;
   /**
    * 
    * @param outputName
    * @param actuatorName
    * @param location
    * @param watchMe
    */
   public BinaryActuator(String outputName,String actuatorName,String location, int watchMe)
   {
      super(outputName, actuatorName, location, 0);
      this.MyDigitalOut = (DigitalOut) this.myOutput;
   }
   
   /**
    * enable the output
    */
   public void turnON()
   {
      MyDigitalOut.turnOn();
   }
   
   /**
    * disable the output
    */
   public void turnOFF()
   {
      MyDigitalOut.turnOff();
   }
   
   /**
    * alternate the output between its two states
    */
   public void toggle()
   {
      MyDigitalOut.toggle();
   }

   /**
    * 
    */
   @Override
   public GenericSensor getSourceSensor()
   {
      return null;
   }

   /**
    * 
    */
   @Override
   public void processEvent(IO_Event event)
   {
      // TODO Auto-generated method stub
      
   }

   /**
    * 
    */
   @Override
   public GenericActuator getSourceActuator()
   {
      // TODO Auto-generated method stub
      return (GenericActuator)this;
   }

   /**
    * return the current value of our output
    */
   @Override
   public String getStatus()
   {
      if (MyDigitalOut.getLocalValue() == MyDigitalOut.getOnValue())
      {
         return "ON";
      }
      else
      {
         return "OFF";
      }
   }
}
