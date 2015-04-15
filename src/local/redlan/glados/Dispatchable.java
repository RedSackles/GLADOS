/**
 * 
 */
package local.redlan.glados;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;

/**
 * Interface allowing the dispatching events toward event handlers
 * @author warpsr
 */
public interface Dispatchable
{
   /**
    * getQueue() returns the queue of the event handler so the dispatcher can put events in it
    */
   BlockingQueue<IO_Event> getQueue();
   /**
    * getSubscribtion() returns a arraylist of ID´s which the event handles is subscribed to
    */
   ArrayList<Integer> getSubscribtion();
   /**
    * setSubscribtion() call to the dispatches to have it add a subscribtion to a particular iOPutIDNumber for an eventhandler 
    * @param iOPutIDNumber int an ID number or an iOput object that the event handles wants to subscribe to.
    */
   void setSubscribtion(int iOPutIDNumber);
}
