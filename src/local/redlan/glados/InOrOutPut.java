package local.redlan.glados;

/**
 * InOrOutPut is a base class which other protocol specific classes derive from.
 * 
 * @author WarpsR
 */
public abstract class InOrOutPut
{
   /**
    * used to track the total number of IO objects which is used to give each their own unique ID
    */
   private static int iOPutIDcounter;
   /**
    * this will be the IOPut unique ID
    */
   private int iOPutIDNumber = 0;
   /**
    * variable to store the IOPut value
    */
   private String value = null;
   /**
    * reference to the IO_Board object that this IOPut needs to use (the board that it is part of)
    */
   private IO_Board board = null;
   /**
    * reference to the IO object that is using this as is myIO
    */
   private InOrOutPut_User myUser = null;

   /**
    * constructor that sets the iOPutIDNumber and its board
    * @param IO_Board reference to the IO_Board object that this IOPut needs to use (the board that it is part of)
    */
   public InOrOutPut(IO_Board board)
   {
      iOPutIDNumber = ++iOPutIDcounter;
      this.board = board;
   }
   
   /**
    * 
    */
   public IO_Board getBoard()
   {
      return board;
   }

   /**
    * 
    */
   public int getiOPutIDNumber()
   {
      return iOPutIDNumber;
   }

   /**
    * 
    */
   public String getValue()
   {
      return value;
   }
   
   /**
    * 
    */
   public void setValue(String value)
   {
      this.value = value;
   }
   /**
    * 
    */
   public void setMyUser(InOrOutPut_User myUser)
   {
      this.myUser = myUser;
   }
   /**
    * 
    */
   public InOrOutPut_User getMyUser()
   {
      return myUser;
   }
   
}
