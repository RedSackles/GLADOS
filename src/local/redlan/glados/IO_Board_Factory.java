package local.redlan.glados;

import java.util.ArrayList;

import local.redlan.glados.DB.DB_GladosDAO;

/**
 * factory for making IO_Board objects (there will be more types of IOboard
 * @author warpsr
 */
public class IO_Board_Factory
{
   /**
    * the list of all IO_Board object handy for keeping track of them
    */
   private static ArrayList<IO_Board> iO_BoardList = new ArrayList<>();
   /**
    * Database Access Object in order for us to save our measurements to the database
    */
   private static DB_GladosDAO dAO = Glados.getGlados().getGladosDAO();
   /**
    * IO_Board constructing function uses the DAO to get a list of all the IO_Boards and types to be created then does the creating
    * @param gladosDAO
    */
   public static void createIOBoards()
   {
      ArrayList<String[]> iOBoardscConfig = dAO.getIO_Boards();
      for (String[] row : iOBoardscConfig)
      {
         // TODO explain the row numbers / column names
         switch (row[1])
         {
            case "DAEnetIP1":
               iO_BoardList.add(new DAEnetIP1(row[0], row[2], row[3], 161, row[5]));
               break;
         }
         // TODO add initializing logging?
      }
   }
}
