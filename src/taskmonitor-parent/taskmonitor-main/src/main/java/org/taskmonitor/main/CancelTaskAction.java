/**
 * 
 */
package org.taskmonitor.main;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.SwingWorker;

/**
 * Действие: вызвать у taskQueu.interrupt(worker)
 */
public class CancelTaskAction
    extends AbstractAction
{
    private final TaskQueue   taskQueue;
    private final SwingWorker worker;

    /**
     * Дейсвие становится активным только если taskQueue.isInterruptible( worker )==true
     * 
     * @param taskQueue место нахождения worker
     * @param worker worker для отмены
     */
    public CancelTaskAction( final TaskQueue taskQueue, final SwingWorker worker )
    {
        super( taskQueue.getTitle( worker ), taskQueue.isInterruptible( worker )
            ? TaskUI.cancelTaskIcon() : null );
        putValue( LONG_DESCRIPTION, TaskUI.cancelTaskTooltip() );

        setEnabled( taskQueue.isInterruptible( worker ) );

        this.taskQueue = taskQueue;
        this.worker = worker;
    }

    @Override
    public void actionPerformed( final ActionEvent e )
    {
        taskQueue.interrupt( worker );
    }
}