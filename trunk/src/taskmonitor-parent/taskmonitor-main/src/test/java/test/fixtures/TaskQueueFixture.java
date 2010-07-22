/**
 * 
 */
package test.fixtures;

import javax.swing.SwingWorker;

import org.taskmonitor.main.TaskQueue;

/**
 * @author aeremenok 2010
 */
public class TaskQueueFixture
    extends TaskQueue
{
    @Override
    public String getTaskId( final SwingWorker worker )
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
