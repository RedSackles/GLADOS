package local.redlan.glados.snmp;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import local.redlan.glados.Glados;
import local.redlan.glados.IO_Event;
import local.redlan.glados.IO_EventSourceAble;

import org.snmp4j.*;
import org.snmp4j.mp.*;
import org.snmp4j.smi.*;
import org.snmp4j.transport.*;
import org.snmp4j.util.*;
/**
 * It's magic 
 * What are you doing in here any way get out, GET OUT!
 * @author WarpsR
 */
public class SNMP_TrapListener implements CommandResponder, Runnable
{
   private UdpAddress listenerAddress;
   private BlockingQueue<IO_Event> EventDispatchQueue;
   
   public SNMP_TrapListener()
   {
      this.EventDispatchQueue = Glados.EventDispatchQueue;
   }
   
   public UdpAddress getListenerAddress()
   {
      return listenerAddress;
   }

   public void setListenerAddress(UdpAddress listenerAddress)
   {
      this.listenerAddress = listenerAddress;
   }
   
   @Override
   public void run()
   {
      try
      {
         this.listen(getListenerAddress());
      }
      catch (IOException e)
      {
         System.err.println("Error in Listening for Trap");
         System.err.println("Exception Message = " + e.getMessage());
      }
   }
   
   /**
    * This method will listen for traps and response pdu's from SNMP agent.
    */
   public synchronized void listen(TransportIpAddress address) throws IOException
   {
      AbstractTransportMapping transport;
      if (address instanceof TcpAddress)
      {
         transport = new DefaultTcpTransportMapping((TcpAddress) address);
      }
      else
      {
         transport = new DefaultUdpTransportMapping((UdpAddress) address);
      }
      
      ThreadPool threadPool = ThreadPool.create("DispatcherPool", 10);
      MessageDispatcher mtDispatcher = new MultiThreadedMessageDispatcher(
            threadPool, new MessageDispatcherImpl());
      
      // add message processing models
      mtDispatcher.addMessageProcessingModel(new MPv1());
      mtDispatcher.addMessageProcessingModel(new MPv2c());
      
      // Create Target
      CommunityTarget target = new CommunityTarget();
      target.setCommunity(new OctetString("public"));
      
      Snmp snmp = new Snmp(mtDispatcher, transport);
      snmp.addCommandResponder(this);
      
      transport.listen();
      System.out.println("Listening on " + address);
      
      try
      {
         this.wait();
      }
      catch (InterruptedException ex)
      {
         Thread.currentThread().interrupt();
      }
   }
   
   /**
    * This method will be called whenever a pdu is received on the given port
    * specified in the listen() method
    */
   public synchronized void processPdu(CommandResponderEvent cmdRespEvent)
   {
      PDU pdu = cmdRespEvent.getPDU();
      if (pdu != null && pdu.getType() == PDU.TRAP)
      {
         VariableBinding trap = (VariableBinding)pdu.getVariableBindings().lastElement();
         String trapValue = trap.getVariable().toString();
         String trapIP = cmdRespEvent.getPeerAddress().toString().split("/")[0];
         SNMP_InOrOutput trapSourse = null;
         for(SNMP_InOrOutput InOrOutput : SNMP_InOrOutput.SNMP_InOrOutputList)
         {
            if(trap.getOid().equals(InOrOutput.getoID()) && trapIP.equals(InOrOutput.getBoard().getIp()))
            {
               trapSourse = InOrOutput;
               break;
            }
         }
         if(trapSourse != null)
         {
            IO_Event temp = new IO_Event();
            temp.setEventSource((IO_EventSourceAble)trapSourse.getMyUser());
            temp.setIntvalue(Integer.parseInt(trapValue));     
            temp.setSubscribtion(trapSourse.getiOPutIDNumber());
            EventDispatchQueue.add(temp);
            //System.out.println("Trap received from : " + trapSourse.getoID()); //we no longer should need this
         }
         else
         {
//            System.out.println("Trap received from unknown source!");
//            System.out.println(trapIP);
//            System.out.println(trap.getOid());
         }
      }
   }
}
