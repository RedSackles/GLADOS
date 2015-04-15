/**
 * 
 */
package local.redlan.glados.snmp;

import java.io.IOException;

import local.redlan.glados.Glados;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.Target;
import org.snmp4j.TransportMapping;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.GenericAddress;
import org.snmp4j.smi.Integer32;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

/**
 * It's magic 
 * What are you doing in here any way get out, GET OUT!
 * @author WarpsR
 */
public class SNMP_Manager
{
   private static Snmp snmp = null;
   private String address = null;
   private String community;
   //private int pdu_requestID = 0;
   
   /**
    * Constructor
    * 
    * @param add
    *           : an address?
    * 
    */
   public SNMP_Manager(String add, String community)
   {
      address = add;
      this.community = community;
   }
   
   /**
    * Start the Snmp session. If you forget the listen() method you will not get
    * any answers because the communication is asynchronous and the listen()
    * method listens for answers.
    * 
    * @throws IOException
    */
   void start() throws IOException
   {
      TransportMapping transport = new DefaultUdpTransportMapping();
      snmp = new Snmp(transport);
      // Do not forget this line!
      transport.listen();
   }
   
   /**
    * Method which takes a single OID and returns the response from the agent as
    * a String.
    * 
    * @param oid
    * @return Response by agent
    * @throws IOException
    */
   public String getAsString(OID oid) throws IOException
   {
      ResponseEvent event = get(new OID[]{ oid });
      return event.getResponse().get(0).getVariable().toString();
   }
   
   /**
    * Method which takes a single OID and returns the response from the agent as
    * a String.
    * 
    * @param oids oid
    * @return Response by agent
    * @throws IOException
    */
   public String[] getAsStrings(OID[] oids) throws IOException
   {
      ResponseEvent event = get(oids); 
      int nrOfResponses = event.getResponse().size();
      String[] results = new String[nrOfResponses];
      for(int i = 0; i <= nrOfResponses; i++)
      {
         results[i] = event.getResponse().get(i).getVariable().toString();
      }
      return results;
   }
   
   /**
    * Method which takes a single OID and set the int value returning the
    * response from the agent as a String.
    * 
    * @param iOPut
    * @return Response by agent
    * @throws IOException
    */
   public String setAsInt(SNMP_InOrOutput iOPut) throws IOException
   {
      ResponseEvent[] events = set(new SNMP_InOrOutput[] { iOPut });
      return events[0].getResponse().get(0).getVariable().toString();
   }
   
   /**
    * Method which takes a single OID and set the int value returning the
    * response from the agent as a String.
    * 
    * @param oid
    * @return Response by agent
    * @throws IOException
    */
   public String[] setAsIntMultiple(SNMP_InOrOutput[] iOPuts) throws IOException
   {
      ResponseEvent[] events = set(iOPuts);
      String[] results = new String[events.length];
      int resultIndex = 0;
      for(ResponseEvent event:events)
      {
         results[resultIndex] = event.getResponse().get(0).getVariable().toString();
         resultIndex++;
      }
      return results;
   }
   
   private ResponseEvent[] set(SNMP_InOrOutput[] iOPuts)
   {
      ResponseEvent[] results = new ResponseEvent[iOPuts.length];
      int resultIndex = 0;
      for (SNMP_InOrOutput iOPut : iOPuts)
      {
         ResponseEvent response = null;
         PDU pdu = new PDU();
         if (Glados.isInt(iOPut.getValue()))
         {
            pdu.add(new VariableBinding(iOPut.getoID(), new Integer32(Integer
                  .parseInt(iOPut.getValue())))); // VariableBinding(OID oid,
                                                  // Variable variable)
         }
         pdu.setType(PDU.SET);
         //pdu.setRequestID(new Integer32(pdu_requestID));
         //pdu_requestID++;
         try
         {
            response = snmp.send(pdu, getTarget(), null);
         }
         catch (IOException e)
         {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         if (!(response != null))
         {
            throw new RuntimeException("SET timed out");
         }
         else
         {
            results[resultIndex] = response;
         }
         resultIndex++;
      }
      return results;
   }
   
   /**
    * This GET method is capable of handling multiple OIDs
    * 
    * @param oids
    * @return ResponseEvent
    * @throws IOException
    */
   public ResponseEvent get(OID oids[]) throws IOException
   {
      PDU pdu = new PDU();
      for (OID oid : oids)
      {
         pdu.add(new VariableBinding(oid));
      }
      pdu.setType(PDU.GET);
      //pdu.setRequestID(new Integer32(pdu_requestID));
      //pdu_requestID++;
      ResponseEvent event = snmp.send(pdu, getTarget(), null);
      if (event != null) { return event; }
      throw new RuntimeException("GET timed out");
   }
   
   /**
    * This SET method is capable of handling multiple OIDs
    * 
    * @param oids
    * @return ResponseEvent
    * @throws IOException
    */
   public ResponseEvent setAsInt(OID oids[], int value) throws IOException
   {
      PDU pdu = new PDU();
      for (OID oid : oids)
      {
         pdu.add(new VariableBinding(oid, new Integer32(value))); // VariableBinding(OID
                                                                  // oid,
                                                                  // Variable
                                                                  // variable)
      }
      //pdu.setRequestID(new Integer32(pdu_requestID));
      //pdu_requestID++;
      pdu.setType(PDU.SET);
      ResponseEvent event = snmp.send(pdu, getTarget(), null);
      if (event != null) { return event; }
      throw new RuntimeException("GET timed out");
   }
   
   /**
    * This method returns a Target, which contains information about where the
    * data should be fetched and how.
    * 
    * @return Target
    */
   private Target getTarget()
   {
      Address targetAddress = GenericAddress.parse(address);
      CommunityTarget target = new CommunityTarget();
      target.setCommunity(new OctetString(community));
      target.setAddress(targetAddress);
      target.setRetries(2);
      target.setTimeout(1500);
      target.setVersion(SnmpConstants.version2c);
      return target;
   }
   
}
