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
