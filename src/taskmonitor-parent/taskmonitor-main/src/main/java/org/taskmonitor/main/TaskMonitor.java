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

import static java.awt.BorderLayout.CENTER;
import static org.taskmonitor.main.ProgressBarButton.DisplayMode.SINGLE;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 * A main component, that displays progress and controls. <br>
 * <h1>Usage:</h1>
 * 
 * <pre>
 * TaskMonitor myMonitor = new TaskMonitor( new TaskQueue()
 * {
 *     // ... provide implementation to handle your workers
 * } );
 * myFrame.add( myMonitor );
 * 
 * myMonitor.invoke( new SwingWorker()
 * {
 *     // ... implement background processing
 * } );
 * </pre>
 * 
 * @author aeremenok 2010
 */
public class TaskMonitor
    extends JPanel
{
    /**
     * manages task execution
     */
    protected final TaskQueue         taskQueue;
    /**
     * popups to display tasks, if the queue is longer than one
     */
    protected final TaskPopup         taskPopup;
    /**
     * shows the {@link TaskPopup}
     */
    protected final ProgressBarButton showTaskListButton;

    /**
     * Create a <code>TaskMonitor</code> using the specified <code>TaskQueue</code>
     * 
     * @param taskQueue the <code>TaskQueue</code> to use
     */
    public TaskMonitor( final TaskQueue taskQueue )
    {
        super( new BorderLayout() );
        this.taskQueue = taskQueue;
        taskQueue.addTaskQueueListener( new TaskQueueListener()
        {
            @Override
            public void taskQueueChanged( final TaskQueueEvent taskQueueEvent )
            {
                SwingUtilities.invokeLater( new Runnable()
                {
                    @Override
                    public void run()
                    {
                        processChangedQueue( taskQueueEvent );
                    }
                } );
            }
        } );

        taskPopup = new TaskPopup( taskQueue );
        showTaskListButton = new ProgressBarButton( new ShowTaskList(), SINGLE );

        setVisible( false );
    }

    /**
     * add a task to {@link TaskQueue}, call {@link SwingWorker#execute()} and display progress
     * 
     * @param worker a task to display
     */
    @SuppressWarnings( "unchecked" )
    public void invoke( final SwingWorker worker )
    {
        taskQueue.invoke( worker );
    }

    /**
     * display changes after some tasks were started, completed or interrupted
     * 
     * @param event contains info about the queue before and after the change
     */
    protected void processChangedQueue( final TaskQueueEvent event )
    {
        final List<SwingWorker> changedQueue = event.getNewQueue();

        if( changedQueue.isEmpty() )
        {
            setVisible( false );
            return;
        }

        removeAll();
        if( changedQueue.size() == 1 )
        {
            final SwingWorker first = changedQueue.get( 0 );
            add( new ProgressBarButton( new CancelTaskAction( first, taskQueue ), SINGLE ), CENTER );
        }
        else
        {
            taskPopup.setCurrentWorkerQueue( changedQueue );
            add( showTaskListButton, CENTER );
        }
        setVisible( true );
        updateUI();
    }

    /**
     * shows the {@link TaskPopup}
     * 
     * @author aeremenok 2010
     */
    protected class ShowTaskList
        extends AbstractAction
    {
        protected ShowTaskList()
        {
            super( TaskUI.getCancelTaskTooltip() );
            putValue( LONG_DESCRIPTION, TaskUI.getCancelTaskTooltip() );
        }

        @Override
        public void actionPerformed( final ActionEvent e )
        {
            taskPopup.show( showTaskListButton );
        }
    }
}
