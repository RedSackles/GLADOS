package local.redlan.glados;

import local.redlan.glados.actuators.GenericActuator;
import local.redlan.glados.sensors.GenericSensor;

/**
 * inputs, outputs and event handlers can generate new IO_Events that can be dispatched. 
 * this interface provides a method to get back at the source of the event
 * @author WarpsR
 */
public interface IO_EventSourceAble
{
   public String getSourceName();
   public GenericSensor getSourceSensor();
   public GenericActuator getSourceActuator();
}
