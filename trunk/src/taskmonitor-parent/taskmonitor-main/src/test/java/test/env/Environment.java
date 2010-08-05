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
package test.env;

import java.lang.Thread.UncaughtExceptionHandler;
import java.net.URL;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import org.taskmonitor.main.Log;
import org.taskmonitor.main.Log.LogService;
import org.testng.ITest;

/**
 * @author eav 2010
 */
public class Environment
    implements
    UncaughtExceptionHandler
{
    protected final Logger log = Logger.getLogger( getClass() );

    @SuppressWarnings( "unused" )
    public void setUp( final Object test )
        throws Exception
    {
        final URL url = getClass().getClassLoader().getResource( "log4j.properties" );
        assert url != null;
        PropertyConfigurator.configure( url );

        Log.setLogService( new LogService()
        {
            @Override
            public void debug( final String message )
            {
                log.debug( message );
            }
        } );
        Thread.setDefaultUncaughtExceptionHandler( this );
        log.debug( testName( test ) + "> env set up" );
    }

    @SuppressWarnings( "unused" )
    public void tearDown( final Object test )
        throws Exception
    {
        log.debug( testName( test ) + "> env tear down" );
    }

    @Override
    public void uncaughtException( final Thread t, final Throwable e )
    {
        log.debug( t + ": " + e, e );
    }

    protected String testName( final Object test )
    {
        if( test instanceof ITest )
        {
            return ((ITest) test).getTestName();
        }
        return String.valueOf( test );
    }
}
