/*
 * Copyright (C) 2010 Andrey Yeremenok (eav1986__at__gmail__com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

/**
 *
 */
package org.taskmonitor.main;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingWorker;
import javax.swing.SwingWorker.StateValue;

/**
 * Manages task execution. Since {@link SwingWorker}'s can be used differently, you need to provide the implementation
 * details.
 *
 * @author aeremenok 2010
 * @param <W> worker type
 */
public abstract class TaskQueue<W extends SwingWorker>
{
    protected final List<SwingWorker>        workers                  = new LinkedList<SwingWorker>();
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
     * Provides a human-readable title, that will be displayed to user.
     *
     * @param worker a task, that needs to be presented
     * @return a human-readable title to display.
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
        if( !workers.contains( worker ) )
        {
            return;
        }

        if( isInterruptible( worker ) )
        {
            cancelExecution( worker, false );
        }

        Log.debug( "task cancelled: " + getTitle( worker ) );
        unregisterTask( worker );
    }

    /**
     * If the task is not already started, registers it and begins execution. Then notifies listeners.
     *
     * @param worker a task to start
     */
    public void invoke( final W worker )
    {
        if( workers.contains( worker ) )
        {
            Log.debug( "already called task: " + getTitle( worker ) );
            return;
        }

        worker.getPropertyChangeSupport().addPropertyChangeListener( "state", new CompletionListener( worker ) );

        final List<SwingWorker> oldQueue = takeQueueSnapshot();
        synchronized( workers )
        {
            workers.add( worker );

            Log.debug( "task started: " + getTitle( worker ) );
            startExecution( worker );

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
     * Starts the background execution of a task. You may override this to make some preparations.
     *
     * @param worker a task to start
     */
    protected void startExecution( final W worker )
    {
        worker.execute();
    }

    /**
     * @return a fresh copy of currently running tasks
     */
    protected List<SwingWorker> takeQueueSnapshot()
    {
        return new ArrayList<SwingWorker>( workers );
    }

    /**
     * Removes a task from the queue. Then notifies listeners.
     *
     * @param worker a task to be unregistered
     */
    protected void unregisterTask( final W worker )
    {
        if( !workers.contains( worker ) )
        {
            return;
        }

        final List<SwingWorker> oldQueue = takeQueueSnapshot();
        synchronized( workers )
        {
            Log.debug( "task completed: " + getTitle( worker ) );
            workers.remove( worker );
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
                    // SwingWorker#doInBackground() is completed, SwingWorker#done() enqueued to EDT
                    unregisterTask( worker );
                    break;
            }
        }
    }
}
