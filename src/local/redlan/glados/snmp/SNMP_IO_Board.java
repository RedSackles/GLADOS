package local.redlan.glados.snmp;

import java.util.ArrayList;

import local.redlan.glados.Communicator;
import local.redlan.glados.IO_Board;
import local.redlan.glados.InOrOutPut;
/**
 * extends IO_Board adding SNMP functions which are implemented by the actual "model" class
 * @author WarpsR
 */
public abstract class SNMP_IO_Board extends IO_Board
{
   /**
    * array of all SNMP IO_Boards
    */
   public static ArrayList<SNMP_IO_Board> SNMP_IO_BoardList = new ArrayList<>();
   /**
    * Array of all IOPuts that belong with this IO_Board
    */
   protected ArrayList<SNMP_InOrOutput> myIOList = new ArrayList<>();
   /**
    * SNMP client that the IO_Boards can use to communicate to its corresponding hardware
    */
   private SNMP_Client io_BoardClient;
   /**
    * constructor that adds the SNMP_IO_Board to the SNMP_IO_BoardList sets its variables 
    * and creates a SNMP client for it 
    * @param name unique IO_Board name 
    * @param location physical/administative location
    * @param ip IP address of the board
    * @param portnr portnumber of the board
    * @param community SNMP comunity used by the board
    */
   public SNMP_IO_Board(String name, String location, String ip, int portnr, String community)
   {
      super(name, location);
      SNMP_IO_BoardList.add(this);
      io_BoardClient = new SNMP_Client(ip, portnr, community);
      super.ip = ip;
   }
   /**
    * 
    * @return
    */
   public SNMP_Client getIO_BoardClient()
   {
      return io_BoardClient;
   }
   /**
    * returns the SNMP client as a communicator 
    */
   @Override
   protected Communicator getCommunicator()
   {
      return (Communicator) io_BoardClient;
   }
   /**
    * retrieve the value of a particular InOrOutPut from the board
    * @param iOPut InOrOutPut object of which we want the value, Naturally this will need to be an SNMP_InOrOutput class
    * @return a string representation of the OID value
    */
   public String getValue(InOrOutPut iOPut)
   {
      return getIO_BoardClient().getValue((SNMP_InOrOutput)iOPut);
   }
   /**
    * set the value of a particular InOrOutPut from the board
    * @param iOPut InOrOutPut object of which we want the value set, Naturally this will need to be an SNMP_InOrOutput class
    * @return a string representation of the OID value after setting it
    */
   public String setValue(InOrOutPut iOPut)
   {
      return getIO_BoardClient().setValue((SNMP_InOrOutput)iOPut);
   }
   /**
    * retrieve the values of multiple InOrOutPuts from the board
    * @param InOrOutputs array of InOrOutPut objects of which we want the value, Naturally this will need to be an SNMP_InOrOutput class
    * @return a array of string representations of the OID values
    */
   public String[] getValues(InOrOutPut[] InOrOutputs)
   {
      return getIO_BoardClient().getValues((SNMP_InOrOutput[])InOrOutputs);
   }
   /**
    * set the values of multiple InOrOutPuts from the board
    * @param InOrOutputs array of InOrOutPut objects of which we want the value set, Naturally this will need to be an SNMP_InOrOutput class
    * @return a array of string representations of the OID values after setting them
    */
   public String[] setValues(InOrOutPut[] InOrOutputs)
   {
      return getIO_BoardClient().setValues((SNMP_InOrOutput[])InOrOutputs);
   }
   
}
