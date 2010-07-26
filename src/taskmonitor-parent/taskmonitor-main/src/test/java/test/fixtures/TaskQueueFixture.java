/**
 * 
 */
package test.fixtures;

import org.taskmonitor.main.TaskQueue;

/**
 * @author aeremenok 2010
 */
public class TaskQueueFixture
    extends TaskQueue<WorkerFixture>
{
    @Override
    public String getTaskId( final WorkerFixture worker )
    {
        return worker.getTaskId();
    }

    @Override
    protected void cancelExecution( final WorkerFixture worker, final boolean mayInterruptIfRunning )
    {
        worker.interrupt();
    }
}
