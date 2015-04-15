/**
 * 
 */
package local.redlan.glados;

/**
 * @author warpsr
 *
 */
public class DigitalOut extends OutPut
{
   /**
    * on and off values may differ for IO hardware some take 1 and 0 other mat return true and false
    */
   private String onValue = null;
   private String offValue = null;
   /**
    * a local boolean telling us whether the output in in the on or off state.
    */
   private Boolean on = false;
   
   /**
    * DigitalOut constructor
    * takes in the name the InOrOutPut and a watch interval value.
    * A watch interval value in ms (watched) of 0 means it is not watched(polled).
    * @param name String the unique name of the input
    * @param myIO InOrOutPut object used for the actual IO operations this hides the physical implementation
    * @param watched int A watch interval value in ms (watched) of 0 means it is not watched(polled).
    */
   public DigitalOut(String name, InOrOutPut myIO, String offValue, String onValue)
   {
      super(name, myIO);
      this.offValue = offValue;
      this.onValue = onValue;
   }
   
   /**
    * Method refresh
    * polls the physical output for its value compares it to the locally held value.
    * If unequal the new value replaces localValue and false is returned, if equal true is returned.
    * 
    * @return boolean true (no change in value) false (the input value changed from the last known value)
    */
   @Override
   public Boolean refresh()
   {
      String remoteValue = myIO.getBoard().getValue(myIO);
      Boolean equal = false;
      if(!getLocalValue().equals(remoteValue))
      {
         setLocalValue(remoteValue);
      }
      else
      {
         equal = true;
      }
      if(getLocalValue().equals(getOnValue()))
      {
         on = true;
      }
      else
      {
         on = false;
      }
      return equal;
   }
   
   /**
    * Method turnOn if in the off state it attempts to set the output in the on state and returns 
    * the result. If already in on state false is returned.
    * 
    */
   public Boolean turnOn()
   {
      if(!on)
      {
         myIO.setValue(onValue);
         Boolean succes = getOnValue().equals(myIO.getBoard().setValue(myIO));
         if(succes)
         {
            on = true;
            setLocalValue(onValue);
         }
         return succes;
      }
      return false;
   }
   
   /**
    * Method turnOff if in the on state it attempts to set the output in the off state and returns 
    * the result. If already in off state false is returned.
    * 
    */
   public Boolean turnOff()
   {
      if(on)
      {
         myIO.setValue(offValue);
         Boolean succes = getOffValue().equals(myIO.getBoard().setValue(myIO));
         if(succes)
         {
            on = false;
            setLocalValue(offValue);
         }
         return succes;
      }
      return false;
   }
   
   /**
    * Method turnOff if in the on state turnOff() is called and the result returned else turnOn() is called and the result returned.
    * 
    */
   public Boolean toggle()
   {
      if(on)
      {
         return turnOff();
      }
      else
      {
         return turnOn();
      }
   }
   
   /**
    * really you need documentation to figure this one out?
    * 
    */
   public String getOnValue()
   {
      return onValue;
   }

   /**
    * really you need documentation to figure this one out?
    * 
    */
   public String getLocalValue()
   {
      return localValue;
   }

   /**
    * really you need documentation to figure this one out?
    * 
    */
   public void setLocalValue(String value)
   {
      this.localValue = value;
   }
   
   /**
    * really you need documentation to figure this one out?
    * 
    */
   public void setOnValue(String onValue)
   {
      this.onValue = onValue;
   }
   
   /**
    * really you need documentation to figure this one out?
    * 
    */
   public String getOffValue()
   {
      return offValue;
   }

   /**
    * really you need documentation to figure this one out?
    * 
    */
   public void setOffValue(String offValue)
   {
      this.offValue = offValue;
   }
   
   /**
    * really you need documentation to figure this one out?
    * 
    */
   public String getName()
   {
      return name;
   }
}
