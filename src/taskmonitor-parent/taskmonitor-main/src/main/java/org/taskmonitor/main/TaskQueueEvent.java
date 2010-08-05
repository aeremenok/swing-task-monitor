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

package org.taskmonitor.main;

import static java.util.Collections.unmodifiableList;

import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingWorker;

/**
 * A snapshot of task collection that is created by the {@link TaskQueue} when tasks are started, completed or
 * interrupted. Contains the queue before change, after change and the differences between them. <u>All of them are
 * unmodifiable.</u>
 *
 * @author aeremenok 2010
 * @author mnikolaev 2010
 */
public class TaskQueueEvent
    extends EventObject
{
    protected final List<SwingWorker> oldQueue;
    protected final List<SwingWorker> newQueue;

    protected final List<SwingWorker> added;
    protected final List<SwingWorker> removed;

    /**
     * Remembers queue snapshots and calculates their difference
     *
     * @param source a {@link TaskQueue} that created this event
     * @param oldQueue a task list before change
     * @param newQueue a task list after change
     */
    public TaskQueueEvent( final TaskQueue source, final List<SwingWorker> oldQueue, final List<SwingWorker> newQueue )
    {
        super( source );
        this.oldQueue = unmodifiableList( oldQueue );
        this.newQueue = unmodifiableList( newQueue );

        final List<SwingWorker> added = new LinkedList<SwingWorker>( newQueue );
        added.removeAll( oldQueue );
        this.added = unmodifiableList( added );

        final List<SwingWorker> removed = new LinkedList<SwingWorker>( oldQueue );
        removed.removeAll( newQueue );
        this.removed = unmodifiableList( removed );
    }

    public List<SwingWorker> getOldQueue()
    {
        return oldQueue;
    }

    public List<SwingWorker> getNewQueue()
    {
        return newQueue;
    }

    public List<SwingWorker> added()
    {
        return added;
    }

    public List<SwingWorker> removed()
    {
        return removed;
    }
}
