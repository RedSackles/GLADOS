package local.redlan.glados;

import java.util.ArrayList;

/**
 * abstract base class for all output
 * @author Warpsr
 */
public abstract class OutPut
{
   /**
    * the value read last from the output.
    * also there for obvious benefits of not needing IO every time we need its value.
    */
   protected String localValue = null;
   /**
    * a unique name for the output
    */
   protected String name = null;
   /**
    * object used for the actual IO operations this hides the physical implementation
    */
   protected InOrOutPut myIO = null;
   /**
    * a list of all Output objects this is used for easily searching an input by its name.
    * each Output adds itself to the list on creation.
    */
   public static ArrayList<OutPut> outList = new ArrayList<>();
   
   /**
    * Output constructor
    * takes in the name the InOrOutPut and the InOrOutPut object.
    * @param name String the unique name of the input
    * @param myIO InOrOutPut object used for the actual IO operations this hides the physical implementation
    */
   public OutPut(String name, InOrOutPut myIO)
   {
      this.myIO = myIO;
      this.name = name;
      this.localValue = "?";
      this.refresh();
      outList.add(this);
   }
   
   /**
    * Method refresh
    * polls the physical input for its value compares it to the locally held value.
    * If unequal the new value replaces localValue and false is returned, if equal true is returned.
    * 
    * @return boolean true (no change in value) false (the input value changed from the last known value)
    */
   public Boolean refresh()
   {
      String remoteValue = myIO.getBoard().getValue(myIO);
      Boolean equal = false;
      if(!getLocalValue().equals(remoteValue))
      {
         localValue = remoteValue;
      }
      else
      {
         equal = true;
      }
      return equal;
   }
   
   /**
    * Method getLocalValue
    * returns member localValue 
    * @return String getLocalValue
    */
   public String getLocalValue()
   {
      return localValue;
   }
   
   /**
    * Method getOutList
    * Returns the OutList that holds all the Output objects.
    * This is used for easily searching an OutPut by its name. 
    * 
    * @return ArrayList<AnalogIn>
    */
   public static ArrayList<OutPut> getOutList()
   {
      return outList;
   }
   
   /**
    * Method getName
    * Returns the unique name of the Input instance.
    * 
    * @return String
    */   
   public String getName()
   {
      return name;
   }
   
   /**
    * Method getFromOutList
    * Returns an instance of Output with the specified name or returns Null.
    * @param name String the unique name of the output
    * @return Output
    */
   public static OutPut getFromOutList(String name)
   {
      for(OutPut outPut : outList)
      {
         if(outPut.getName().equals(name))
         {
            return outPut;
         }
      }
      return null;
   }
   /**
    * Method getIOPutIDNumber
    * returns the In or Output ID, these are used in generating events and having event handlers subscribe to them. 
    * @return int
    */
   public int getIOPutIDNumber()
   {
      return myIO.getiOPutIDNumber();
   }
   /**
    * In order to respond to events the InOrOutPut object needs to be made aware of which Actuator objects are using it.
    * @param userObj the GenericActuator object that is using this output object
    */
    public void setUserObj(InOrOutPut_User userObj)
    {
       this.myIO.setMyUser(userObj);
    }
}
