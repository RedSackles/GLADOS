package local.redlan.glados.snmp;

import java.util.ArrayList;
import local.redlan.glados.IO_Board;
import local.redlan.glados.InOrOutPut;

import org.snmp4j.smi.OID;
/**
 * this class extends InOrOutPut to create InOrOutPut object that communicate thru SNMP
 * @author WarpsR
 */
public class SNMP_InOrOutput extends InOrOutPut
{
   /**
    * OID are used by SNMP to identify a particular value
    */
   private OID oID = null;
   /**
    * an array list of snmp InOrOutPut objects all SNMP_InOrOutput add themselves to this array
    */
   public static ArrayList<SNMP_InOrOutput> SNMP_InOrOutputList = new ArrayList<>();
   /**
    * constructs an SNMP_InOrOutput object setting its OID and IO_Board
    * @param oID OID of the SNMP variable 
    * @param board thr IO_Board that this IOPut belongs with
    */
   public SNMP_InOrOutput(String oID, IO_Board board)
   {
      super(board);
      this.oID = new OID(oID);
      SNMP_InOrOutputList.add(this);
      board.getMyIOList().add(this);
   }
   /**
    * 
    */
   @Override
   public void setValue(String value)
   {
      super.setValue(value);
   }
   /**
    * 
    */
   public OID getoID()
   {
      return oID;
   }
}
