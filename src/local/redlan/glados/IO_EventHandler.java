package local.redlan.glados;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import local.redlan.glados.Dispatchable;
import local.redlan.glados.Dispatcher;
import local.redlan.glados.IO_Event;

/**
 * Base EventHandler, Dispatchers can send events to this object and it will process them.
 * @author WarpsR
 */
public class IO_EventHandler implements Dispatchable
{
   /**
    * a Queue for the dispatchers to put the incoming events into. This queue is blocking so dispatcher and handler can be run on seperate threads.
    */
   private final BlockingQueue<IO_Event> eventQueue = new LinkedBlockingDeque<>();
   /**
    * a list of the subscriptions this event handler is interested in
    */
   private ArrayList<Integer> subscribtion = new ArrayList<>();
   /**
    * the dispatcher that supplies events to this handler
    */
   protected Dispatcher dispatcher;
   /**
    * constructs the handler creating a thread for it to run in
    * @param dispatcher the dispatcher that supplies events to this handler
    */
   public IO_EventHandler(Dispatcher dispatcher)
   {
      this.dispatcher = dispatcher;
      Runnable runner = new Runnable()
      {
         @Override
         public void run()
         {
            while(true)
            {
               try
               {
                  porcesIO(IO_EventHandler.this.eventQueue.take());
               }
               catch (InterruptedException e)
               {
                  // TODO Auto-generated catch block
                  e.printStackTrace();
               }
            }
            
         }
      };
      Thread thread = new Thread(runner);
      thread.start();
      
   }
   
   /**
    * put another event  in the dispatchqueue
    */
   public void eventToDispatchQueue(IO_Event event)
   {
      this.dispatcher.getEventDispatchQueue().add(event);
   }
   
   /**
    * Pass the event to the source sensor for it to process.
    * @param event
    */
   protected void porcesIO(IO_Event event)
   {
      event.getEventSource().getSourceSensor().processEvent(event);
   }
   /**
    * 
    */
   public BlockingQueue<IO_Event> getQueue()
   {
      return eventQueue;
   }
   /**
    * 
    */
   public ArrayList<Integer> getSubscribtion()
   {
      return subscribtion;
   }
   /**
    * this created a subscription and reports the handler to the dispatcher for it to start receiving events
    * @param iOPutIDNumber the ID of the IOPut that this handler is interested in
    */
   public void setSubscribtion(int iOPutIDNumber)
   {
      this.subscribtion.add(iOPutIDNumber);
      dispatcher.dispatchThis(this);
   }
   
}