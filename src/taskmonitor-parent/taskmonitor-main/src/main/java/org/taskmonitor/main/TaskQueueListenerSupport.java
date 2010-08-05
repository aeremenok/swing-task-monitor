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

import java.util.LinkedList;
import java.util.List;

/**
 * Contains a {@link List} of {@link TaskQueueListener}s and fires events on them, preserving the order of addition.
 *
 * @author aeremenok 2010
 */
public class TaskQueueListenerSupport
{
    protected final List<TaskQueueListener> listeners = new LinkedList<TaskQueueListener>();

    /**
     * Add a new listener if it hasn't been added yet
     *
     * @param listener a listener to add
     */
    public void addListener( final TaskQueueListener listener )
    {
        if( !listeners.contains( listener ) )
        {
            listeners.add( listener );
        }
    }

    /**
     * Notifies all {@link TaskQueueListener}s when tasks are being added or removed from the {@link TaskQueue}
     *
     * @param taskQueueEvent an event to fire
     */
    public void fireEvent( final TaskQueueEvent taskQueueEvent )
    {
        for( final TaskQueueListener listener : listeners )
        {
            listener.taskQueueChanged( taskQueueEvent );
        }
    }

    /**
     * Remove a given listener
     *
     * @param listener a listener to remove
     */
    public void removeListener( final TaskQueueListener listener )
    {
        listeners.remove( listener );
    }
}
