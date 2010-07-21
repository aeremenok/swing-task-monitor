package org.taskmonitor.main;

import static java.util.Collections.unmodifiableList;

import java.util.EventObject;
import java.util.LinkedList;
import java.util.List;

import javax.swing.SwingWorker;

/**
 * Created by IntelliJ IDEA. <br>
 * User: mnikolaev<br>
 * Date: 20.07.2010<br>
 */
public class TaskQueueEvent
    extends EventObject
{
    private final List<SwingWorker> oldQueue;
    private final List<SwingWorker> newQueue;

    public TaskQueueEvent( final TaskQueue source, final List<SwingWorker> oldQueue, final List<SwingWorker> newQueue )
    {
        super( source );
        this.oldQueue = unmodifiableList( oldQueue );
        this.newQueue = unmodifiableList( newQueue );
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
        final LinkedList<SwingWorker> added = new LinkedList<SwingWorker>( newQueue );
        added.removeAll( oldQueue );
        return added;
    }

    public List<SwingWorker> removed()
    {
        final LinkedList<SwingWorker> added = new LinkedList<SwingWorker>( oldQueue );
        added.removeAll( newQueue );
        return added;
    }
}
