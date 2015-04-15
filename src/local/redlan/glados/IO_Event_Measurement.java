/**
 * 
 */
package local.redlan.glados;

/**
 * @author warpsr
 *
 */
public class IO_Event_Measurement extends IO_Event
{
   /**
    * raw int value of the event
    */
   private int rawValue = 0;
   /**
    * name of the sensor that triggered the measurement event
    */
   private String sensorName = "";
   /**
    * 
    */
   public int getRawValue()
   {
      return rawValue;
   }
   /**
    * 
    */
   public void setRawValue(int rawValue)
   {
      this.rawValue = rawValue;
   }
   /**
    * 
    */
   public String getSensorName()
   {
      return sensorName;
   }
   /**
    * 
    */
   public void setSensorName(String sensorName)
   {
      this.sensorName = sensorName;
   }
   
}
