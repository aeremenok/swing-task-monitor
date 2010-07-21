/**
 * 
 */
package test.fixtures;

import java.util.UUID;

import javax.swing.SwingWorker;

public class WorkerFixture
    extends SwingWorker<Boolean, Boolean>
{
    private boolean      interrupted;
    private final String taskId = UUID.randomUUID().toString();

    public String getTaskId()
    {
        return taskId;
    }

    public void interrupt()
    {
        interrupted = true;
        cancel( true );
    }

    public boolean isInterrupted()
    {
        return interrupted;
    }

    @Override
    public String toString()
    {
        return getTaskId();
    }

    @Override
    protected Boolean doInBackground()
    {
        try
        {
            Thread.sleep( 1000 );
        }
        catch( final InterruptedException e )
        {
            Thread.currentThread().interrupt();
        }
        return true;
    }
}