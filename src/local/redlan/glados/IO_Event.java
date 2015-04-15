package local.redlan.glados;
/**
 * IO_Events are objects created as a result of an event and passed to the dispatchers and event handlers to be processed.
 * @author warpsr
 */
public class IO_Event
{
   /**
    * event source
    */
   private IO_EventSourceAble eventSource = null;
   /**
    * interger value of the event
    */
   private int intvalue = 0;
   /**
    * String value of the event
    */
   private String stringValue = "";
   /**
    * the subscription number of the event used by dispatchers to determine which handler gets the event
    */
   private int subscribtion = 0;
   /**
    * 
    */
   public int getIntvalue()
   {
      return intvalue;
   }
   /**
    * 
    */
   public void setIntvalue(int intvalue)
   {
      this.intvalue = intvalue;
   }
   /**
    * 
    */
   public String getStringValue()
   {
      return stringValue;
   }
   /**
    * 
    */
   public void setStringValue(String stringValue)
   {
      this.stringValue = stringValue;
   }
   /**
    * 
    */
   public int getSubscribtion()
   {
      return subscribtion;
   }
   /**
    * 
    */
   public void setSubscribtion(int subscribtion)
   {
      this.subscribtion = subscribtion;
   }
   /**
    * 
    */
   public IO_EventSourceAble getEventSource()
   {
      return eventSource;
   }
   /**
    * 
    */
   public void setEventSource(IO_EventSourceAble eventSource)
   {
      this.eventSource = eventSource;
   }
}
