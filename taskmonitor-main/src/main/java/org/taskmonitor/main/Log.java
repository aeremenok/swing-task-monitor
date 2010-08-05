/**
 * 
 */
package org.taskmonitor.main;

/**
 * @author aeremenok 2010
 */
public class Log
{
    private static LogService logService = new LogService()
                                         {
                                             @Override
                                             public void debug( final String message )
                                             {}
                                         };

    public static void debug( final String message )
    {
        logService.debug( message );
    }

    public static void setLogService( final LogService logService )
    {
        if( logService != null )
        {
            Log.logService = logService;
        }
    }

    public static interface LogService
    {
        void debug( final String message );
    }
}
