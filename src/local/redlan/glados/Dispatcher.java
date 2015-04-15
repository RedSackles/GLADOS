/**
 * 
 */
package local.redlan.glados;

import java.util.HashSet;
import java.util.concurrent.BlockingQueue;

/**
 * a dispatcher creates its own thread and monitores a queue that watchers and trap listeners can put events into.
 * The dispatcher then determines if there is a eventhandler interested in these events and moves the event 
 * to the queue of the eventhandler.
 * unmatched events are sent to purgatory where the garbage collector may have them
 * @author warpsr
 */
public class Dispatcher
{
   /**
    * the incoming queue for the dispatcher
    */
   private BlockingQueue<IO_Event> EventDispatchQueue;
   /**
    * A set of event handlers that the dispatcher can move events to
    * these need to be unique in the set or events will be dispatched multiple times.
    */
   private HashSet<Dispatchable> dispatchSet = new HashSet<>();
   /**
    * this is an event handles that processes measurement data to be sent to the database
    * as there is only one of these and it has its own event subclass is made sense to have the dispatcher take care of its creation.
    * NOTE: this is initialized by calling createIO_EventMeasurementHandler() as Gloados needs to exit before it can be created and the dispatcher needs to exist for Glados te be created.
    */
   private IO_EventMeasurementHandler iO_EventMeasurementHandler = null;
   /**
    * a reference to the IO_EventMeasurementHandler queue 
    * NOTE: this is initialized by calling createIO_EventMeasurementHandler() as Gloados needs to exit before it can be created and the dispatcher needs to exist for Glados te be created.
    */
   private BlockingQueue<IO_Event> iO_EventMeasurementqueue = null;
   
   /**
    * Dispatcher constructor
    * takes the incoming queue for the dispatcher as an argument.
    * Creates a new thread that takes from the incoming queue and then matched the events to a subscription.
    * @param EventDispatchQueue BlockingQueue<IO_Event> the 
    */
   public Dispatcher(BlockingQueue<IO_Event> EventDispatchQueue)
   {
      this.EventDispatchQueue = EventDispatchQueue;
      Runnable dispatcher = new Runnable()
      {
         
         @Override
         public void run()
         {
            while(true)
            {
               try
               {
                  IO_Event event = Dispatcher.this.EventDispatchQueue.take();
                  if (event instanceof IO_Event_Measurement) 
                  {
                     iO_EventMeasurementqueue.offer(event);
                  }
                  else
                  {
                     for(Dispatchable destination:Dispatcher.this.dispatchSet)
                     {
                        for(int subscription:destination.getSubscribtion())
                        {
                           if(subscription == event.getSubscribtion())
                           {
                              destination.getQueue().offer(event);
                           }
                        }
                     }
                  }
               }
               catch (InterruptedException e)
               {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
               }
            }
            
         }
      };
      Thread dispatcher_thread = new Thread(dispatcher);
      dispatcher_thread.start();
   }
   
   /**
    * adds a destination supplied as an argument to the dispatchSet
    * @param destination Dispatchable the object that wants to receive events.
    */
   public void dispatchThis(Dispatchable destination)
   {
      dispatchSet.add(destination);
   }
   
   /**
    * removes a destination supplied as an argument to the dispatchSet
    * @param destination Dispatchable the object that no longer wants to receive events.
    */
   public void stopDispatchingThis(Dispatchable destination)
   {
      dispatchSet.remove(destination);
   }
   
   /**
    * this is for the event handlers that may produce another event and need to queue it
    * @return the event dispatch queue of this dispatcher
    */
   public BlockingQueue<IO_Event> getEventDispatchQueue()
   {
      return EventDispatchQueue;
   }
   /**
    * 
    */
   public void createIO_EventMeasurementHandler()
   {
      if (this.iO_EventMeasurementHandler == null)
      {
         this.iO_EventMeasurementHandler = new IO_EventMeasurementHandler(this);
         iO_EventMeasurementqueue = iO_EventMeasurementHandler.getQueue();
      }
   }
}
