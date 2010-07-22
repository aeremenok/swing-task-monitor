/**
 * 
 */
package org.taskmonitor.main;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.JPopupMenu;
import javax.swing.SwingWorker;

/**
 * Displays a list of {@link ProgressBarButton}s for the current task queue. Each button interrupts a task.
 * 
 * @author aeremenok 2010
 */
public class TaskPopup
    extends JPopupMenu
{
    protected final TaskQueue taskQueue;

    /**
     * @param taskQueue is used to create {@link CancelTaskAction}s for each {@link ProgressBarButton}
     */
    public TaskPopup( final TaskQueue taskQueue )
    {
        this.taskQueue = taskQueue;
    }

    /**
     * Makes the popup apper inside the screen bounds
     * 
     * @param jpm
     * @param source
     * @param x
     * @param y
     * @param yOffset
     */
    protected static void showPopupMenu( final JPopupMenu jpm, final Component source, int x, int y, final int yOffset )
    { // fixme still bad behavior, popup better
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        screen.height -= 50;

        final Point origin = source.getLocationOnScreen();
        origin.translate( x, y );

        final int height = jpm.getHeight();
        final int width = jpm.getWidth();

        if( origin.x + width > screen.width )
        {
            x -= width;
        }
        if( origin.y + height > screen.height )
        {
            y -= height;
            y += yOffset;
        }

        jpm.show( source, x, y );
    }

    /**
     * Called by {@link TaskMonitor} when the task collection changes
     * 
     * @param workers new task collection
     */
    public void setCurrentWorkerQueue( final List<SwingWorker> workers )
    {
        removeAll();
        for( final SwingWorker worker : workers )
        {
            add( new ProgressBarButton( new CancelAndHide( worker ) ) );
        }
    }

    public void show( final Component invoker )
    {
        showPopupMenu( this, invoker, 0, 0, 0 );
    }

    /**
     * Does interruption then hides the popup.
     * 
     * @author aeremenok 2010
     */
    protected class CancelAndHide
        extends CancelTaskAction
    {
        protected CancelAndHide( final SwingWorker worker )
        {
            super( worker, taskQueue );
        }

        @Override
        public void actionPerformed( final ActionEvent e )
        {
            super.actionPerformed( e );
            setVisible( false );
        }
    }
}
