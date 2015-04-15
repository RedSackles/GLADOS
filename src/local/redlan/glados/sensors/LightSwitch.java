package local.redlan.glados.sensors;

import local.redlan.glados.IO_Event;
import local.redlan.glados.systemfunctions.GenericFunction;
import local.redlan.glados.systemfunctions.Lights;

/**
 * 
 * @author Warpsr
 *
 */
public class LightSwitch extends Switch
{
   /**
    * the Function object of this switch
    */
   protected Lights myLights = null;
   /**
    * LightSwitch constructor, takes parameters and created a LightSwitch object
    * @param dInName Digital input name
    * @param sensorName the administrative name of this switch (for meatbags)
    * @param location so meatbags know where to find it
    * @param watchMe monitoring interval
    */
   public LightSwitch(String dInName, String sensorName, String location, int watchMe)
   {
      super(dInName, sensorName, location, watchMe);
      // TODO Auto-generated constructor stub
   }

   @Override
   public void processEvent(IO_Event event)
   {
      Lights function = (Lights) myFunction;
      if (event.getIntvalue() == 1)
      {
         function.turnON(this);
      }
      if (event.getIntvalue() == 0)
      {
         function.turnOFF(this);
      }
   }

   /**
    * @return Returns a string representation of the status of the LightSwitch
    */
   @Override
   public String getStatus()
   {
      if (mySensor.getLocalValue() == "1")
      {
         return "ON";
      }
      else
      {
         return "OFF";
      }
   }
   
   /**
    * 
    */
   @Override
   public void setMyFunction(GenericFunction myFunction)
   {
      this.myFunction = myFunction;
      this.myLights = (Lights) this.myFunction;
   }
   
   /**
    * get the current value from the sensor and return it by default take the raw value
    * @return the int value of the input
    */
   @Override
   public int getCurrentValue()
   {
      mySensor.refresh();
      return Integer.parseInt(mySensor.getLocalValue());
   }
}
