/*
 * Copyright (C) 2010 Andrey Yeremenok (eav1986__at__gmail__com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */

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
    }
}
