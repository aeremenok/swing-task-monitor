/**
 * 
 */
package org.taskmonitor.main;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.SwingWorker;
import javax.swing.SwingWorker.StateValue;

import org.apache.log4j.Logger;

/**
 * Manages task execution. Since {@link SwingWorker}'s can be used differently, you need to provide the implementation
 * details.
 * 
 * @author aeremenok 2010
 * @param <W> worker type
 */
public abstract class TaskQueue<W extends SwingWorker>
{
    private static final Logger              log                      = Logger.getLogger( TaskQueue.class );

    protected final Map<String, SwingWorker> workersByTitles          = new TreeMap<String, SwingWorker>();
    protected final TaskQueueListenerSupport taskQueueListenerSupport = new TaskQueueListenerSupport();

    /**
     * @param listener will be called, when some tasks are started, completed or interrupted
     * @see TaskQueueListener
     */
    public void addTaskQueueListener( final TaskQueueListener listener )
    {
        taskQueueListenerSupport.addListener( listener );
    }

    /**
     * @param worker a task, that needs to be presented
     * @return a human-readable title to display
     */
    public abstract String getTitle( final W worker );

    /**
     * If the task is still running, does the actual interruption and removes it from the queue. Then notifies
     * listeners.
     * 
     * @param worker a task to interrupt
     */
    public void interrupt( final W worker )
    {
        if( !workersByTitles.containsValue( worker ) )
        {
            return;
        }

        if( isInterruptible( worker ) )
        {
            doIterruption( worker );
        }

        log.debug( "task cancelled: " + getTaskId( worker ) );
        unregisterTask( worker );
    }

    /**
     * If the task is not already started, registers it and begins execution. Then notifies listeners.
     * 
     * @param worker a task to start
     */
    public void invoke( final W worker )
    {
        final String taskId = getTaskId( worker );
        if( workersByTitles.containsValue( worker ) )
        {
            log.debug( "already called task: " + taskId );
            return;
        }

        worker.getPropertyChangeSupport().addPropertyChangeListener( "state", new CompletionListener( worker ) );

        final List<SwingWorker> oldQueue = takeQueueSnapshot();
        synchronized( workersByTitles )
        {
            workersByTitles.put( taskId, worker );

            log.debug( "task started: " + taskId );
            worker.execute();

            taskQueueListenerSupport.fireEvent( new TaskQueueEvent( this, oldQueue, takeQueueSnapshot() ) );
        }
    }

    /**
     * Is called to define whether to enable the cancelling button for the given task.
     * 
     * @param worker will be checked for interruption ability
     * @return <code>true</code> if given worker can be interrupted
     * @see CancelTaskAction
     * @see ProgressBarButton
     */
    public abstract boolean isInterruptible( final W worker );

    /**
     * @param listener will be unregistered
     * @see TaskQueueListener
     */
    public void removeListener( final TaskQueueListener listener )
    {
        this.taskQueueListenerSupport.removeListener( listener );
    }

    /**
     * Does the actual interruption. You may call {@link SwingWorker#cancel(boolean)}, make checks or cleanups.
     * 
     * @param worker a task to interrupt
     */
    protected abstract void doIterruption( W worker );

    /**
     * Is called to provide unique task key when the task is being registered or unregistered.
     * 
     * @param worker a task to be identified
     * @return an identifier, that is unique for each worker
     */
    protected abstract String getTaskId( final W worker );

    /**
     * @return a fresh copy of currently running tasks
     */
    protected List<SwingWorker> takeQueueSnapshot()
    {
        return new ArrayList<SwingWorker>( workersByTitles.values() );
    }

    /**
     * Removes a task from the queue. Then notifies listeners.
     * 
     * @param worker a task to be unregistered
     */
    protected void unregisterTask( final W worker )
    {
        if( !workersByTitles.containsValue( worker ) )
        {
            return;
        }

        final String taskId = getTaskId( worker );
        final List<SwingWorker> oldQueue = takeQueueSnapshot();
        synchronized( workersByTitles )
        {
            log.debug( "task completed: " + taskId );
            workersByTitles.remove( taskId );
            taskQueueListenerSupport.fireEvent( new TaskQueueEvent( this, oldQueue, takeQueueSnapshot() ) );
        }
    }

    /**
     * Tracks the {@link SwingWorker} state. When the state is {@link StateValue#DONE}, removes it from queue using
     * {@link TaskQueue#unregisterTask(SwingWorker)}.
     * 
     * @see SwingWorker#addPropertyChangeListener(PropertyChangeListener)
     * @author aeremenok 2010
     */
    protected final class CompletionListener
        implements
        PropertyChangeListener
    {
        private final W worker;

        private CompletionListener( final W worker )
        {
            this.worker = worker;
        }

        @Override
        public void propertyChange( final PropertyChangeEvent evt )
        {
            switch( (StateValue) evt.getNewValue() )
            {
                case PENDING:
                case STARTED:
                    break;
                case DONE:
                    // SwingWorker#doInBackground() is completed, SwingWorker#done() enqueued in EDT
                    unregisterTask( worker );
                    break;
            }
        }
    }
}
