/**
 * 
 */
package org.taskmonitor.main;

import java.math.BigInteger;

import javax.swing.JFrame;
import javax.swing.SwingWorker;

/**
 * @author aeremenok 2010
 */
public class Example
{
    public Example()
    {
        super();

        final TaskMonitor taskMonitor = new TaskMonitor( new MyTaskQueue() );

        final JFrame frame = new JFrame();
        frame.add( taskMonitor );
        // properly init and show components
        // ... 

        // now you can begin executing tasks
        taskMonitor.invoke( new MyWorker<BigInteger, Void>( "Counting grains of desert sand..." )
        {
            @Override
            protected BigInteger doInBackground()
            {
                return measureDesert();
            }
        } );
    }

    private BigInteger measureDesert()
    {
        return new BigInteger( "-1" );
    }

    abstract class MyWorker<T, V>
        extends SwingWorker<T, V>
    {
        String title;

        public MyWorker( final String title )
        {
            super();
            this.title = title;
        }

        public String getTitle()
        {
            return this.title;
        }
    }

    class MyTaskQueue
        extends TaskQueue<MyWorker>
    {
        @Override
        public String getTitle( final MyWorker worker )
        {
            return worker.getTitle();
        }

        @Override
        public boolean isInterruptible( final MyWorker worker )
        { // consider any worker interruptible
            return true;
        }

        @Override
        protected void doIterruption( final MyWorker worker )
        { // simple interruption
            worker.cancel( true );
        }

        @Override
        protected String getTaskId( final MyWorker worker )
        {
            return worker.getTitle();
        }
    }
}
