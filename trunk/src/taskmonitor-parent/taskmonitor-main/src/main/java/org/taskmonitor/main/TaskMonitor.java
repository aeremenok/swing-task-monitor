/**
 * 
 */
package org.taskmonitor.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 * @author aeremenok 2010
 */
public class TaskMonitor
    extends JPanel
    implements
    TaskQueueListener
{
    protected final TaskQueue taskQueue;
    /**
     * контекстное меню из наименований запущенных workers
     */
    protected final TaskPopup taskPopup;
    /**
     * кнопка, содержащая иконку отмены, progressBar и "выберите запос"
     */
    protected final JButton   showTaskListButton;

    public TaskMonitor( final TaskQueue taskQueue )
    {
        super( new BorderLayout() );
        this.taskQueue = taskQueue;
        taskQueue.addTaskQueueListener( this );

        taskPopup = new TaskPopup( taskQueue );
        showTaskListButton = new ProgressBarButton( new ShowTaskList() );

        setVisible( false );
    }

    public void invoke( final SwingWorker worker )
    {
        taskQueue.invoke( worker );
    }

    @Override
    public void taskQueueChanged( final TaskQueueEvent taskQueueEvent )
    {
        final Runnable runnable = new Runnable()
        {
            @Override
            public void run()
            {
                workerQueueChanged( taskQueueEvent );
            }
        };
        SwingUtilities.invokeLater( runnable );
    }

    protected void workerQueueChanged( final TaskQueueEvent taskQueueEvent )
    {
        final List<SwingWorker> workers = taskQueueEvent.getNewQueue();

        if( workers.isEmpty() )
        {
            setVisible( false );
            return;
        }

        removeAll();
        if( workers.size() == 1 )
        {
            final SwingWorker first = workers.get( 0 );
            add( new ProgressBarButton( new CancelTaskAction( taskQueue, first ) ), BorderLayout.CENTER );
        }
        else
        {
            taskPopup.setCurrentWorkerQueue( workers );
            add( showTaskListButton, BorderLayout.CENTER );
        }
        setVisible( true );
        updateUI();
    }

    protected class ShowTaskList
        extends AbstractAction
    {
        public ShowTaskList()
        {
            super( TaskUI.cancelTaskTooltip(), TaskUI.cancelTaskIcon() );
            putValue( LONG_DESCRIPTION, TaskUI.cancelTaskTooltip() );
        }

        @Override
        public void actionPerformed( final ActionEvent e )
        {
            taskPopup.show( showTaskListButton );
        }
    }
}
