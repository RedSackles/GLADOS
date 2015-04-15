package local.redlan.glados;

import java.util.ArrayList;

/**
 * abstract base class for all inputs
 * @author Warpsr
 */
public abstract class Input
{
   /**
    * the value read last from the input used.
    * also there for obvious benefits of not needing IO every time we need its value.
    */
   protected String localValue = null;
   /**
    * a unique name for the input
    */
   protected String name = null;
   /**
    * object used for the actual IO operations this hides the physical implementation
    */
   protected InOrOutPut myIO = null;
   /**
    * a list of all Input objects this is used for easily searching an input by its name.
    * each Input adds itself to the list on creation.
    */
   public static ArrayList<Input> inList = new ArrayList<>();
   
   /**
    * Input constructor
    * takes in the name the InOrOutPut and the InOrOutPut object.
    * @param name String the unique name of the input
    * @param myIO InOrOutPut object used for the actual IO operations this hides the physical implementation
    */
   public Input(String name, InOrOutPut myIO)
   {
      this.myIO = myIO;
      this.name = name;
      this.localValue = "?";
      this.refresh();
      inList.add(this);
   }
   
  /**
   * In order to respond to traps the InOrOutPut object needs to be made aware of which Sensor objects are using it.
   * on an incoming trap only the physical IO opbject ca be determined but in order to process the event the Sensor needs to be determined.
   * @param userObj the GenericSensor object that is using this in or output object
   */
   public void setUserObj(InOrOutPut_User userObj)
   {
      this.myIO.setMyUser(userObj);
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
    * 
    * @return String getLocalValue
    */
   public String getLocalValue()
   {
      return localValue;
   }
   
   /**
    * Method getAnalogInList
    * Returns the analogInList that holds all the AnalogIn objects.
    * This is used for easily searching an input by its name. 
    * 
    * @return ArrayList<AnalogIn>
    */
   public static ArrayList<Input> getInList()
   {
      return inList;
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
    * Method getFromAnalogInList
    * Returns an instance of AnalogIn with the specified name or returns Null.
    * @param name String the unique name of the input
    * @return AnalogIn
    */
   public static Input getFromInList(String name)
   {
      for(Input input : inList)
      {
         if(input.getName().equals(name))
         {
            return input;
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
}
