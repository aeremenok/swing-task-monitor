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
