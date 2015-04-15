package local.redlan.glados.scrolbar;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

import javax.swing.JFrame;
import javax.swing.JScrollBar;

import local.redlan.glados.DigitalOut;
import local.redlan.glados.Dispatchable;
import local.redlan.glados.Dispatcher;
import local.redlan.glados.IO_Event;
import local.redlan.glados.OutPut;

public class MyGui extends JFrame implements Dispatchable
{

   /**
    * 
    */
   private static final long serialVersionUID = 6621492351736597846L;
   private final BlockingQueue<IO_Event> eventQueue  = new LinkedBlockingDeque<>();
   private volatile boolean stop = false;
   private ArrayList<Integer> subscribtion = new ArrayList<>();
   private Dispatcher dispatcher;
   
   public MyGui(Dispatcher dispatcher)
   {
      final JScrollBar mybar = new JScrollBar(JScrollBar.HORIZONTAL, 0, 1, 0, 1023);
      getContentPane().add(mybar);
      setSize(400, 100);
      setVisible(true);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      this.dispatcher = dispatcher;
      Runnable runner = new Runnable()
      {
         
         @Override
         public void run()
         {
            while(!stop)
            {
               try
               {
                  int i = MyGui.this.eventQueue.take().getIntvalue();
                  //System.out.println(i);
                  //System.out.println(Math.log1p(new Double(i)));
                  mybar.setValue(i);
                  testje(i);
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
   
   @Override
   public void dispose()
   {
      super.dispose();
      stop = true;
   }

   @Override
   public BlockingQueue<IO_Event> getQueue()
   {
      return eventQueue;
   }

   @Override
   public ArrayList<Integer> getSubscribtion()
   {
      return subscribtion;
   }

   @Override
   public void setSubscribtion(int iOPutIDNumber)
   {
      this.subscribtion.add(iOPutIDNumber);
      dispatcher.dispatchThis(this);
   }
   
   private void testje(int value)
   {
      ArrayList<OutPut> OutList= DigitalOut.getOutList();
      int limit = 1023 / 24;
      int current = limit;
      for(OutPut out:OutList)
      {
         DigitalOut thisout = (DigitalOut) out;
         if(value >= current)
         {
            thisout.turnOn();
         }
         else
         {
            thisout.turnOff();
         }
         current += limit;
      }
   }
   
}
