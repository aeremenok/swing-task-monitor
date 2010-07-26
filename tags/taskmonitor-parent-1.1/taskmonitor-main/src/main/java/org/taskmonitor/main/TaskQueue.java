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
     * Is called to provide an unique task key when the task is being registered or unregistered.
     * 
     * @param worker a task to be identified
     * @return an identifier, that is unique for each worker
     */
    public abstract String getTaskId( final W worker );

    /**
     * Provides a human-readable title, that will be displayed to user. By default, returns
     * {@link #getTaskId(SwingWorker)}. The difference is that {@link #getTaskId(SwingWorker)} shoud return an unique
     * value for each {@link SwingWorker}, and {@link #getTitle(SwingWorker)} can return any value.
     * 
     * @param worker a task, that needs to be presented
     * @return a human-readable title to display.
     */
    public String getTitle( final W worker )
    {
        return getTaskId( worker );
    }

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
            cancelExecution( worker, false );
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
     * Is called to define, whether to enable the cancelling button for the given task. By default, any worker is
     * considered to be interruptible. You may override this to disble interruption of some tasks.
     * 
     * @param worker will be checked for interruption ability
     * @return <code>true</code> if given worker can be interrupted
     * @see CancelTaskAction
     * @see ProgressBarButton
     */
    public boolean isInterruptible( final W worker )
    {
        return true;
    }

    /**
     * @param listener will be unregistered
     * @see TaskQueueListener
     */
    public void removeListener( final TaskQueueListener listener )
    {
        this.taskQueueListenerSupport.removeListener( listener );
    }

    /**
     * Does the actual interruption. By default, calls {@link SwingWorker#cancel(boolean)}. You may override this to
     * make checks or cleanups.
     * 
     * @param worker a task to interrupt
     * @param mayInterruptIfRunning @see {@link SwingWorker#cancel(boolean)}. By default, is <code>false</code>
     */
    protected void cancelExecution( final W worker, final boolean mayInterruptIfRunning )
    {
        worker.cancel( mayInterruptIfRunning );
    }

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
