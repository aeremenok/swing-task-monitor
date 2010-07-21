/**
 * 
 */
package org.taskmonitor.main;

import javax.swing.SwingWorker;

/**
 * @author aeremenok 2010
 */
public class TaskQueueFixture
    extends TaskQueue
{
    @Override
    public String getActionId( final SwingWorker worker )
    {
        return ((WorkerFixture) worker).getTaskId();
    }

    @Override
    public String getTitle( final SwingWorker worker )
    {
        return worker.toString();
    }

    @Override
    public boolean isInterruptible( final SwingWorker worker )
    {
        return true;
    }

    @Override
    protected void doIterruption( final SwingWorker worker )
    {
        ((WorkerFixture) worker).interrupt();
    }
}
