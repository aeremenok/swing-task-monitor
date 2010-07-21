/**
 * 
 */
package org.taskmonitor.main;

import javax.swing.SwingWorker;

/**
 * @author aeremenok 2010
 * @param <T>
 * @param <V>
 */
public abstract class CommandWorker<T, V>
    extends SwingWorker<T, V>
{
    @Override
    protected T doInBackground()
    {
        return null;
    }

    public void interrupt()
    {}

    public String getActionId()
    {
        return toString();
    }
}
