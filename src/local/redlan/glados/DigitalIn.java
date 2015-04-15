package local.redlan.glados;

/**
 * This class is for digital / binary type inputs.
 * It uses InOrOutPut class for the input data and adds some higher level functionality 
 * like refreshing and or validating its value.
 * Also implements Watchable in order to automatically poll the input.
 * @author warpsr
 *
 */
public class DigitalIn  extends Input
{
   /**
    * high and low value may differ for IO hardware some return 1 and 0 other mat return true and false
    */
   private String highValue = "";
   private String lowValue = "";
   private boolean localbool = false;
   
   /**
    * DigitalIn constructor
    * takes in the name the InOrOutPut and the InOrOutPut object.
    * @param name String the unique name of the input
    * @param myIO InOrOutPut object used for the actual IO operations this hides the physical implementation
    */
   public DigitalIn(String name, InOrOutPut myIO)
   {
      super(name, myIO);
      this.highValue = "1";
      this.lowValue = "0";
      this.refresh();
   }
   
   /**
    * Method refresh
    * polls the physical input for its value compares it high and low value,
    * then sets the localValue to either true or false according to the match and returns localValue.
    * @return boolean localValue = physical input
    */
   @Override
   public Boolean refresh()
   {
      boolean equlas = super.refresh();
      if (highValue == null)
      {
         this.highValue = "1";
      }
      if (lowValue == null)
      {
         this.lowValue = "0";
      }
      if(highValue.equals(this.localValue))
      {
         localbool = true;
      }
      if(lowValue.equals(this.localValue))
      {
         localbool = false;
      }
      return equlas;
   } 
   
   /**
    * returns the local boolean value, Since this is a digital input it ought to be able to just produce true/false
    */
   public boolean getlocalbool()
   {
      return localbool;
   }
   /**
    * Method getLocalValue
    * returns member localValue 
    * 
    * @return String getLocalValue
    */
   @Override
   public String getLocalValue()
   {
      if (getlocalbool())
      {
         return "1";
      }
      else
      {
         return "0";
      }
   }
}
