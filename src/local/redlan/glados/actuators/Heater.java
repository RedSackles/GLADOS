/**
 * 
 */
package local.redlan.glados.actuators;

/**
 * @author Warpsr
 */
public class Heater extends BinaryActuator
{
   public Heater(String outputName,String actuatorName,String location, int watchMe)
   {
      super(outputName, actuatorName, location, 0);
   }
}
