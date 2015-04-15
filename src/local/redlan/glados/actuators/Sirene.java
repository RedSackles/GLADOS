/**
 * 
 */
package local.redlan.glados.actuators;

/**
 * @author Warpsr
 */
public class Sirene extends BinaryActuator
{
   public Sirene(String outputName,String actuatorName,String location, int watchMe)
   {
      super(outputName, actuatorName, location, 0);
   }
}
