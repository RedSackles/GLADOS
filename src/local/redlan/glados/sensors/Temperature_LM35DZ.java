/**
 * 
 */
package local.redlan.glados.sensors;

/**
 * object to represent the physical LM35DZ sensor, and adjustments or calculations to the raw data are performed by this class
 * Sensor type: temperature sensor
 * Kind of temperature sensor: analog, with voltage output
 * Calibrated directly in: °Celsius (Centigrade)
 * Temperature measuring range: 0...100°C
 * Scale factor: Linear 10.0 mV/°C
 * Case: TO92
 * Temperature measurement accuracy: 1%
 * @author WarpsR
 */
public class Temperature_LM35DZ extends TempSensor
{
   public Temperature_LM35DZ(String aInName,String sensorName,String location, int watchMe, int bottumTreshold, int midTreshold, int highTreshold, int logMe, int adjustMe)
   {
      super(aInName, sensorName, location, watchMe, bottumTreshold, midTreshold, highTreshold, logMe, adjustMe);
   }
   /**
    * get the current temperature from the sensor and return it
    * this particular model produces an input value that is really close to the actual temperate in Celsius
    * @return the int value of the current temperature in °Celsius (only kelvin is an acceptable alternative)
    */
   @Override
   public int getCurrentTemp()
   {
      mySensor.refresh();
      return Integer.parseInt(mySensor.getLocalValue())+this.adjustment;
   }
   /**
    * returns a string representation of the current Sensor status
    */
   @Override
   public String getStatus()
   {
      int state = Integer.parseInt(mySensor.getLocalValue())+this.adjustment;
      return Integer.toString(state);
   }
}
