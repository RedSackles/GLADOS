/**
 * 
 */
package local.redlan.glados.actuators;

/**
 * @author Warpsr
 */
public class Light extends BinaryActuator
{
   public Light(String outputName,String actuatorName,String location, int watchMe)
   {
      super(outputName, actuatorName, location, 0);
   }
}
