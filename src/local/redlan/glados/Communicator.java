package local.redlan.glados;

import local.redlan.glados.snmp.SNMP_InOrOutput;

/**
 * This abstract class is to help with the decoupling of SNMP classes and program logic
 * once there is a second communication protocol this class may be altered to allow the program logic to 
 * communicate in a uniform manor
 * @author warpsr
 *
 */
public abstract class Communicator
{
   /**
    * Retrieve the value of an in or output (OID)
    * 
    * @param iOPut SNMP_InOrOutput this will need to be changes when protocols other then SNMP are added
    * @return a string representation of the OID value.
    */
   public abstract String getValue(SNMP_InOrOutput iOPut);
   /**
    * Set the value of an in or output (OID)
    * 
    * @param iOPut SNMP_InOrOutput this will need to be changes when protocols other then SNMP are added
    * @return a string representation of the OID value.
    */
   public abstract String setValue(SNMP_InOrOutput iOPut);
   /**
    * Retrieve the values of an list of in or output's (OID's)
    * 
    * @param InOrOutputs []SNMP_InOrOutput this will need to be changes when protocols other then SNMP are added
    * @return a arraylist of string representations of the OID value's.
    */
   public abstract String[] getValues(SNMP_InOrOutput[] InOrOutputs);
   /**
    * Set the values of an list of in or output's (OID's)
    * 
    * @param InOrOutputs []SNMP_InOrOutput this will need to be changes when protocols other then SNMP are added
    * @return a arraylist of string representations of the OID value's after their setting.
    */
   public abstract String[] setValues(SNMP_InOrOutput[] InOrOutputs);
}
