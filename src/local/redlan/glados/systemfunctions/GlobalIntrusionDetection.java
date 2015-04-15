package local.redlan.glados.systemfunctions;

import java.util.ArrayList;
import java.util.Hashtable;

/**
 * higher functionality (yes finally) this is a global intrusion function that will take input from the IntrusionDetection 
 * objects in all the spaces and take action accordingly
 * @author WarpsR
 */
public class GlobalIntrusionDetection
{
   /**
    * is there an Alarm?
    */
   private boolean globalIntrusionAlarm = false;
   private boolean globalTamperAlarm = false;
   
   private static GlobalIntrusionDetection instance = null;
   
   static 
   {
      try
      {
         instance = new GlobalIntrusionDetection();
      }
      catch (Exception e)
      {
         throw new AssertionError("Whoops WTF happened here we can't create GlobalIntrusionDetection", e);
      }
   }
   
   /**
    * A private as there is only suposed to be one instance of this class
    */
   private GlobalIntrusionDetection()
   {
     
   }
   /**
    * 
    */
   public static GlobalIntrusionDetection getInstance()
   {
      return instance;
   }
   
   /**
    * this function activates all the sirens
    */
   public void soundTheAlarm(IntrusionDetection detectorFunction)
   {
      if (!globalIntrusionAlarm)
      {
//         DigitalOut.getFromOutList("Relay2_2").turnOn();
         activateSirens();
         System.out.println("Intrusion Alarm");
         globalIntrusionAlarm = true;
      }
   }
   /**
    * this function activates temper warning
    */
   public void soundTheTamperAlarm(IntrusionDetection detectorFunction)
   {
      if (!globalTamperAlarm)
      {
//         DigitalOut.getFromOutList("Relay3_2").turnOn();
         activateSirens();
         System.out.println("Tampering Alarm");
         globalTamperAlarm = true;
      }
   }
   /**
    * Reset the IntrusionAlarm
    */
   public void resetIntrusionAlarm(IntrusionDetection detectorFunction)
   {
      if (globalIntrusionAlarm && !otherIntrusionAlarms())
      {
//        DigitalOut.getFromOutList("Relay2_2").turnOff();
         deactivateSirens();
         System.out.println("Intrusion Alarm Restored");
         globalIntrusionAlarm = false;
      }
   }
   /**
    * Reset the TamperAlarm
    */
   public void resetTamperAlarm(IntrusionDetection detectorFunction)
   {
      if (globalTamperAlarm && !otherTamperAlarms())
      {
 //        DigitalOut.getFromOutList("Relay3_2").turnOff();
         deactivateSirens();
         System.out.println("Tamper Alarm Restored");
         globalTamperAlarm = false;
      }
   }
   
   
   /**
    * check the FunctionList and return all the intrusion functions
    * @return ArrayList of all intrusion functions
    */
   private static ArrayList<IntrusionDetection> getIntrusionFunctions()
   {
      ArrayList<GenericFunction> gFunctionList = new ArrayList<>();
      gFunctionList = GenericFunction.getgFunctionList();
      ArrayList<IntrusionDetection> intrusionFunctions = new ArrayList<>();
      for(GenericFunction gFunction : gFunctionList)
      {
         if(gFunction instanceof IntrusionDetection)
         {
            intrusionFunctions.add((IntrusionDetection)gFunction);
         }
      }
      return intrusionFunctions;
   }
   /**
    * test whetehr there are other intrusion function that have a intrusion alarm state
    * @return true if there is at least one other intrusion alarm active, false otherwise
    */
   private boolean otherIntrusionAlarms()
   {
      ArrayList<IntrusionDetection> intrusionFunctions = getIntrusionFunctions();
      for(IntrusionDetection iFunction : intrusionFunctions)
      {
         if (((IntrusionDetection) iFunction).isIntrusionAlarm())
            {
               return true;
            }
      }
      return false;
   }
   /**
    * test whetehr there are other intrusion function that have a tamper alarm state
    * @return true if there is at least one other tamper alarm active, false otherwise
    */
   private boolean otherTamperAlarms()
   {
      ArrayList<IntrusionDetection> intrusionFunctions = getIntrusionFunctions();
      for(IntrusionDetection iFunction : intrusionFunctions)
      {
         if (((IntrusionDetection) iFunction).isTamperAlarm())
            {
               return true;
            }
      }
      return false;
   }
   /**
    * loops all the funtions finding the IntrusionDetection ones and calling their activateSirens method
    */
   private void activateSirens()
   {
      ArrayList<IntrusionDetection> intrusionFunctions = getIntrusionFunctions();
      for(IntrusionDetection iFunction : intrusionFunctions)
      {
         iFunction.activateSirens();
      }
      Lights.sirens(true);
   }
   /**
    * loops all the funtions finding the IntrusionDetection ones and calling their activateSirens method
    */
   private void deactivateSirens()
   {
      ArrayList<IntrusionDetection> intrusionFunctions = getIntrusionFunctions();
      for(IntrusionDetection iFunction : intrusionFunctions)
      {
         iFunction.deactivateSirens();
      }
      Lights.sirens(false);
   }
   
   /**
    * loops all the funtions finding the IntrusionDetection ones and calling setMaintenance method
    */
   public static void setMaintenance(String intrusionFunction)
   {
      ArrayList<IntrusionDetection> intrusionFunctions = getIntrusionFunctions();
      for(IntrusionDetection iFunction : intrusionFunctions)
      {
         if(intrusionFunction.equals(iFunction.getFunctionName())|| intrusionFunction == "")
         {
            iFunction.setMaintenance();
         }
      }
   }
   /**
    * loops all the funtions finding the IntrusionDetection ones and calling unsetMaintenance method
    */
   public static void unsetMaintenance(String intrusionFunction)
   {
      ArrayList<IntrusionDetection> intrusionFunctions = getIntrusionFunctions();
      for(IntrusionDetection iFunction : intrusionFunctions)
      {
         if(intrusionFunction.equals(iFunction.getFunctionName())|| intrusionFunction == "")
         {
            iFunction.unsetMaintenance();
         }
      }
   }
   /**
    * loops all the funtions finding the IntrusionDetection ones and calling the arm method
    */
   public static void arm(String intrusionFunction)
   {
      ArrayList<IntrusionDetection> intrusionFunctions = getIntrusionFunctions();
      for(IntrusionDetection iFunction : intrusionFunctions)
      {
         if(intrusionFunction.equals(iFunction.getFunctionName())|| intrusionFunction == "")
         {
            iFunction.arm();
         }
      }
   }
   /**
    * loops all the funtions finding the IntrusionDetection ones and calling disarm method
    */
   public static void disarm(String intrusionFunction)
   {
      ArrayList<IntrusionDetection> intrusionFunctions = getIntrusionFunctions();
      for(IntrusionDetection iFunction : intrusionFunctions)
      {
         if(intrusionFunction.equals(iFunction.getFunctionName())|| intrusionFunction == "")
         {
            iFunction.disarm();
         }
      }
   }
   /**
    * in order to produce a status overview of the system the subclasses return lists with name value combinations
    */
   @SuppressWarnings("rawtypes")
   public Hashtable<String, Hashtable> getStatus()
   {
      Hashtable<String, String> globalIntrusionDetection = new Hashtable<String, String>();
      String globalIA = "at Rest";
      if(globalIntrusionAlarm)
      {
         globalIA = "Intrusion ALARM";
      }
      String globalTA = "at Rest";
      if(globalTamperAlarm)
      {
         globalTA = "Tamper Alarm";
      }
      globalIntrusionDetection.put("globalIntrusionAlarm", globalIA);
      globalIntrusionDetection.put("globalTamperAlarm", globalTA);
      Hashtable<String, Hashtable> result = new Hashtable<String, Hashtable>();
      result.put("GlobalIntrusionDetection", globalIntrusionDetection);
      return result;
   }
}