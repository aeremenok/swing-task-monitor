/**
 * 
 */
package org.taskmonitor.main;

/**
 * Observes the task collection of the {@link TaskQueue}.
 * 
 * @author aeremenok 2010
 */
public interface TaskQueueListener
{
    /**
     * Called by the {@link TaskQueue} when tasks are started, completed or interrupted
     * 
     * @param taskQueueEvent contains an old and a new collection of runnig tasks
     */
    void taskQueueChanged( TaskQueueEvent taskQueueEvent );
}
