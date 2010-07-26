/**
 * 
 */
package org.taskmonitor.main;

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.reverse;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Arrays.asList;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import test.env.Environment;
import test.fixtures.TaskQueueFixture;
import test.fixtures.WorkerFixture;

/**
 * @author aeremenok 2010
 */
@SuppressWarnings( "unchecked" )
public class TaskQueueTest
{
    private Environment environment;

    @Test( timeOut = 30000 )
    public void cancelSome()
        throws InterruptedException,
        ExecutionException
    {
        final TaskQueueFixture progressBar = createFixture();

        final List<WorkerFixture> toBeCompleted = asList( newTask( "Task1" ), newTask( "Task2" ), newTask( "Task3" ) );
        final List<WorkerFixture> toBeCancelled = asList( newTask( "Task4" ), newTask( "Task5" ), newTask( "Task6" ) );

        final List<WorkerFixture> concat = newArrayList( concat( toBeCompleted, toBeCancelled ) );
        Collections.shuffle( concat );

        for( final WorkerFixture testWorker : concat )
        {
            progressBar.invoke( testWorker );
        }
        for( final WorkerFixture testWorker : toBeCancelled )
        {
            progressBar.interrupt( testWorker );
        }

        for( final WorkerFixture testWorker : toBeCancelled )
        {
            assert testWorker.isInterrupted();
            assert testWorker.isCancelled();
        }

        for( final WorkerFixture testWorker : toBeCompleted )
        {
            assert testWorker.get();
            assert !testWorker.isInterrupted();
        }
    }

    @Test( timeOut = 30000 )
    public void invokeMultiple()
        throws InterruptedException,
        ExecutionException
    {
        final TaskQueueFixture progressBar = createFixture();

        final List<WorkerFixture> testWorkers = asList( newTask( "Task1" ), newTask( "Task2" ), newTask( "Task3" ) );

        for( final WorkerFixture testWorker : testWorkers )
        {
            progressBar.invoke( testWorker );
        }

        for( final WorkerFixture testWorker : reverse( testWorkers ) )
        {
            assert testWorker.get();
            assert !testWorker.isInterrupted();
        }
    }

    @BeforeClass
    public void setUp()
        throws Exception
    {
        environment = new Environment();
        environment.setUp( this );
    }

    @AfterClass
    public void tearDown()
        throws Exception
    {
        environment.tearDown( this );
    }

    protected TaskQueueFixture createFixture()
    {
        return new TaskQueueFixture();
    }

    protected WorkerFixture newTask( final String taskId )
    {
        return new WorkerFixture( taskId );
    }
}
