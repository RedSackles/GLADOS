package local.redlan.glados;
/**
 * the interface Watchable defines functions that need to be implemented by an object in order for it to be watched by a watcher.
 * @author WarpsR
 */
public interface Watchable
{
   public Boolean watch();
   public String getNewValue();
   public int getIOPutIDNumber();
   public IO_EventSourceAble getEventSource();
}
