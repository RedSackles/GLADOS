package local.redlan.glados;

/**
 * This class is for analog or rather "none binary" type inputs.
 * It uses InOrOutPut class for the input data and adds some higher level functionality 
 * like refreshing and or validating data.
 * Also implements Watchable in order to automatically poll the input.
 * @author warpsr
 *
 */
public class AnalogIn extends Input
{
   /**
    * AnalogIn constructor
    * takes in the name the InOrOutPut and the InOrOutPut object.
    * @param name String the unique name of the input
    * @param myIO InOrOutPut object used for the actual IO operations this hides the physical implementation
    */
   public AnalogIn(String name, InOrOutPut myIO)
   {
      super(name, myIO);
   }
}
