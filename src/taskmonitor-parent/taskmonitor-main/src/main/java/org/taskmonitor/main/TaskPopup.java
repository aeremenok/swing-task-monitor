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
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

/**
 * @author aeremenok 2010
 */
public class TaskPopup
    extends JPopupMenu
{
    private final TaskQueue taskQueue;

    public TaskPopup( final TaskQueue taskQueue )
    {
        this.taskQueue = taskQueue;
    }

    private static void showPopupMenu( final JPopupMenu jpm, final Component source, int x, int y, final int yOffset )
    {
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        screen.height -= 50;
        final Point origin = source.getLocationOnScreen();
        origin.translate( x, y );
        final int height = jpm.getHeight();
        final int width = jpm.getWidth();
        if( origin.x + width > screen.width )
        {
            //x -= (origin.x + width) - screen.width;
            // we prefer kde behaviour
            x -= width;
        }
        if( origin.y + height > screen.height )
        {
            // we prefer kde behaviour
            //y -= (origin.y + height) - screen.height;
            y -= height;
            y += yOffset;
        }
        jpm.show( source, x, y );
    }

    public void setCurrentWorkerQueue( final List<SwingWorker> workers )
    {
        assert SwingUtilities.isEventDispatchThread();
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
     * @author aeremenok 2010
     */
    private final class CancelAndHide
        extends CancelTaskAction
    {
        private CancelAndHide( final SwingWorker worker )
        {
            super( taskQueue, worker );
        }

        @Override
        public void actionPerformed( final ActionEvent e )
        {
            super.actionPerformed( e );
            setVisible( false );
        }
    }

    //    private JLabel createIconLabel( final JMenuItem item )
    //    {
    //        final JLabel label = new JLabel( item.getText() );
    //        label.setEnabled( item.isEnabled() );
    //        return label;
    //    }
    //
    //    private JProgressBar createProgressBar( final JMenuItem item )
    //    {
    //        final JProgressBar progressBar = new JProgressBar();
    //        progressBar.setIndeterminate( true );
    //        progressBar.setEnabled( item.isEnabled() );
    //        return progressBar;
    //    }
}
