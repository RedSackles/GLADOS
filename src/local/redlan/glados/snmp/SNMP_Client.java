package local.redlan.glados.snmp;

import java.io.IOException;

import local.redlan.glados.Communicator;

import org.snmp4j.smi.OID;
/**
 * this class extends of communicator as to provide a SNMP client to other objects.
 * @author Warpsr
 */
public class SNMP_Client extends Communicator
{
   /**
    * SNMP_manager is the object controlling the actual SNMP communication.
    */
   private SNMP_Manager client = null;
   /**
    * constructs an SNMP_Client using its parameters to configure its SNMP_Manager
    * @param iPAddress the IP address of the SNMP host the client has to connect to
    * @param portNR the port number of the SNMP host the client has to connect to
    * @param community the SNMP community of the SNMP host the client has to connect to
    */
   public SNMP_Client(String iPAddress, int portNR, String community)
   {
      client = new SNMP_Manager("udp:" + iPAddress + "/" + portNR, community);
      try
      {
         client.start();
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
   }
   
   /**
    * requests the SNMP_Manager to get the value of an OID,
    * Returns null in case of a exception.
    * @param iOPut the SNMP_InOrOutput who's value we are interested in
    * @return a string representation of the OID value
    */
   public String getOIDvalue(SNMP_InOrOutput iOPut)
   {
      String result = null;
      try
      {
         result = client.getAsString(iOPut.getoID());
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return result;
   }
   
   /**
    * requests the SNMP_Manager to get the value of multiple OID's,
    * @param iOPut SNMP_InOrOutput's who's values we are interested in
    * @return a string arrat representation of the OID's values
    */
   public String[] getOIDvalues(SNMP_InOrOutput[] iOPuts)
   {
      String results[] = null;
      OID[] oIDs = new OID[iOPuts.length];
      int oIDIndex = 0;
      for(SNMP_InOrOutput iOPut:iOPuts)
      {
         oIDs[oIDIndex] = iOPut.getoID();
         oIDIndex++;
      }
      try
      {
         results = client.getAsStrings(oIDs);
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return results;
   }
   /**
    * requests the SNMP_Manager to set the value of an OID, it then returns the result 
    * Returns null in case of a exception, or the value of the OID after setting it.
    * @param iOPut the SNMP_InOrOutput who's value we are interested in
    * @return a string representation of the OID value
    */
   public String setOIDvalue(SNMP_InOrOutput iOPut)
   {
      String result[] = setAsIntMultiple(new SNMP_InOrOutput[] {iOPut});
      return result[0];
   }
   /**
    * requests the SNMP_Manager to set the value of multiple OID's.
    * Returns null in case of a exception, or the value of the OID after setting it.
    * @param iOPut SNMP_InOrOutput's who's values we want to set
    * @return a string arrat representation of the OID's values
    */
   private String[] setAsIntMultiple(SNMP_InOrOutput[] iOPuts)
   {
      String result[] = null;
      try
      {
         result = client.setAsIntMultiple(iOPuts);
      }
      catch (IOException e)
      {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }
      return result;
   }

   /**
    * Overrides getvalue to use getOIDvalue(iOPut)
    * @param iOPut the SNMP_InOrOutput who's value we want
    * @return a string representation of the OID value
    */
   @Override
   public String getValue(SNMP_InOrOutput iOPut)
   {
      return getOIDvalue(iOPut);
   }
   /**
    * Overrides setValue to use setOIDvalue(iOPut)
    * @param iOPut the SNMP_InOrOutput who's value we want to set
    * @return a string representation of the OID value after setting it
    */
   @Override
   public String setValue(SNMP_InOrOutput iOPut)
   {
      return setOIDvalue(iOPut);
   }
   /**
    * Overrides getValues to use getOIDvalues(InOrOutputs)
    * @param SNMP_InOrOutput[] and array of the SNMP_InOrOutput's who's value we want
    * @return a string representation of the OID value
    */
   @Override
   public String[] getValues(SNMP_InOrOutput[] InOrOutputs)
   {
      return getOIDvalues(InOrOutputs);
   }
   /**
    * Overrides setValues to use setOIDvalue(iOPut)
    * @param iOPut the SNMP_InOrOutput who's value we want to set
    * @return a string array representation of the OIDs ant their values
    */
   @Override
   public String[] setValues(SNMP_InOrOutput[] InOrOutputs)
   {
      return setAsIntMultiple(InOrOutputs);
   }
}
