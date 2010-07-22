/**
 * 
 */
package org.taskmonitor.main;

import java.util.LinkedList;
import java.util.List;

/**
 * Contains a {@link List} of {@link TaskQueueListener}s and fires events on them, preserving the order of addition.
 * 
 * @author aeremenok 2010
 */
public class TaskQueueListenerSupport
{
    protected final List<TaskQueueListener> listeners = new LinkedList<TaskQueueListener>();

    /**
     * Add a new listener if it hasn't been added yet
     * 
     * @param listener a listener to add
     */
    public void addListener( final TaskQueueListener listener )
    {
        if( !listeners.contains( listener ) )
        {
            listeners.add( listener );
        }
    }

    /**
     * Notifies all {@link TaskQueueListener}s when tasks are being added or removed from the {@link TaskQueue}
     * 
     * @param taskQueueEvent an event to fire
     */
    public void fireEvent( final TaskQueueEvent taskQueueEvent )
    {
        for( final TaskQueueListener listener : listeners )
        {
            listener.taskQueueChanged( taskQueueEvent );
        }
    }

    /**
     * Remove a given listener
     * 
     * @param listener a listener to remove
     */
    public void removeListener( final TaskQueueListener listener )
    {
        listeners.remove( listener );
    }
}
