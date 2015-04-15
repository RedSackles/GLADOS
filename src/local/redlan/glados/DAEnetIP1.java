/**
 * 
 */
package local.redlan.glados;

import java.util.ArrayList;

import local.redlan.glados.snmp.SNMP_IO_Board;
import local.redlan.glados.snmp.SNMP_InOrOutput;

/**
 * DAEnetIP1 is on model of SNMP IO boards more moddels may be added in the future
 * @author warpsr
 *
 */
public class DAEnetIP1 extends SNMP_IO_Board
{
   /**
    * DAEnetIP1 constructor
    * takes name location IP port number SNMP community and gladosDAO to create a instance of a DAEnetIP1 IOboard
    * Retrieves the required information from the DAO to configure its individual IO's
    * Right now there is no model specific functionality (but there could be)
    * 
    * @param name String the unique name of the IOboard
    * @param location String a string representation of the physical location of the IOboard
    * @param ip String an IP address xxx.xxx.xxx.xxx
    * @param portnr int the protnumber to use in combination with the IP (make a socket?)
    * @param community String the snmp community used for this IOboard
    * @param gladosDAO DB_GladosDAO the DAO object used to retrieve config data from the database
    */
   public DAEnetIP1(String name, String location, String ip, int portnr, String community)
   {
      super(name, location, ip, portnr, community);
      ArrayList<String[]> iOBoardscConfig = dAO.getMyInOrOutPuts(name);
      for (String[] row : iOBoardscConfig)
      {
         InOrOutPut temp = new SNMP_InOrOutput(row[1], this);
         getMyIOList().add(temp);
         switch(row[2])
         {
            case "DigitalOUT":
               new DigitalOut(row[0], temp, "1", "0");
               break;
            case "DigitalIN":
               new DigitalIn(row[0], temp);
               break;
            case "AnalogueIN":
               new AnalogIn(row[0], temp);
               break;
         }
      }
   }
}
