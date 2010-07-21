/**
 * 
 */
package org.taskmonitor.main;

import java.util.HashSet;
import java.util.Set;

/**
 * @author aeremenok 2010
 */
public class TaskQueueListenerSupport
{
    private final Set<TaskQueueListener> listeners = new HashSet<TaskQueueListener>();

    public boolean addListener( final TaskQueueListener e )
    {
        return listeners.add( e );
    }

    /**
     * вызываетс€, когда измен€етс€ состав работающих workers.
     * 
     * @param taskQueueEvent
     */
    public void fireEvent( final TaskQueueEvent taskQueueEvent )
    {
        for( final TaskQueueListener listener : listeners )
        {
            listener.taskQueueChanged( taskQueueEvent );
        }
    }

    public boolean removeListener( final TaskQueueListener listener )
    {
        return listeners.remove( listener );
    }

}
