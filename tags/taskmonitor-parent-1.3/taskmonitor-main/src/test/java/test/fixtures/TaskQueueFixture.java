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
    protected void cancelExecution( final WorkerFixture worker, final boolean mayInterruptIfRunning )
    {
        worker.interrupt();
    }

    @Override
    public String getTitle( final WorkerFixture worker )
    {
        return worker.getTaskId();
    }
}
