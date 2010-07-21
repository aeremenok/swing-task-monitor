/**
 * 
 */
package org.taskmonitor.main;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.SwingWorker;

/**
 * ��������: ������� � taskQueu.interrupt(worker)
 */
public class CancelTaskAction
    extends AbstractAction
{
    private final TaskQueue   taskQueue;
    private final SwingWorker worker;

    /**
     * ������� ���������� �������� ������ ���� taskQueue.isInterruptible( worker )==true
     * 
     * @param taskQueue ����� ���������� worker
     * @param worker worker ��� ������
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