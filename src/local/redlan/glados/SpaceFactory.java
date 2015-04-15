package local.redlan.glados;

import java.util.ArrayList;

import local.redlan.glados.DB.DB_GladosDAO;
import local.redlan.glados.Spaces.GenericSpace;
import local.redlan.glados.Spaces.TestRoom;

/**
 * Is it a factory in space of a factory that makes space?
 * actually this class is there to take the space/room config from the database and create objects for them
 * @author Warpsr
 */
public class SpaceFactory
{
   /**
    * the list of all GenericSpace objects handy for keeping track of them
    */
   private static ArrayList<GenericSpace> spaceList = new ArrayList<>();
   /**
    * Database Access Object in order for us to access the Glados database
    */
   private static DB_GladosDAO dAO = Glados.getGlados().getGladosDAO();
   /**
    * space constructing function uses the DAO to get a list of all the spaces and types to be created then does the creating
    * @param gladosDAO
    */
   public static void createSpaces()
   {
      ArrayList<String[]> spaceConfig = dAO.getSpaces();
      for (String[] row : spaceConfig)
      {
         // TODO explain the row numbers / column names
         switch (row[1])
         {
            case "TestRoom":
               System.out.println("creating TestRoom: "+row[0]);
               spaceList.add(new TestRoom(row[0], Integer.parseInt(row[2])));
               break;
         }
         // TODO add initializing logging?
      }
   }
}
