package local.redlan.glados;

import local.redlan.glados.DB.DB_GladosDAO;

/**
 * a handler extending of IO_EventHandler adding functionality specifically for temperature inputs.
 * @author WarpsR
 */
public class IO_EventMeasurementHandler extends IO_EventHandler
{
   /**
    * Database Access Object in order for us to save our measurements to the database
    */
   private DB_GladosDAO dAO = Glados.getGlados().getGladosDAO();
   /**
    * constructor that creates the IO_temperatureHandler and sets it's dispatcher
    * @param dispatcher the dispatcher that will be supplying the events.
    */
   public IO_EventMeasurementHandler(Dispatcher dispatcher)
   {
      super(dispatcher);
      // TODO Auto-generated constructor stub
   }
   
   /**
    * process the Int value of an event adding it to the list, calculating the new average and comparing it to the old
    * If there is a difference write it to the command line (obviously we want to do more useful stuff later)
    * @param i int value of an event
    */
   @Override
   protected void porcesIO(IO_Event event)
   {
       IO_Event_Measurement measurement = (IO_Event_Measurement)event;
       if( dAO.setMyMeasurments(measurement.getEventSource().getSourceName(), measurement.getIntvalue(), measurement.getRawValue()) )
       {
          //System.out.println("saved");
       }
       else
       {
          //System.out.println("not saved");
       // TODO handle the error that occurred
       }
   }
}
