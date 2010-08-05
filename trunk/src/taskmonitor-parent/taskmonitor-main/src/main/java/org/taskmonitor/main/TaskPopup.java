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

import java.awt.Component;
import java.awt.Dimension;
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

    protected static int fitXinScreen( final Dimension screen, final JPopupMenu menu, final Component invoker,
        final int x )
    {
        final int menuWidth = menu.getPreferredSize().width;
        final int invokerLeft = invoker.getLocationOnScreen().x;
        final int menuRight = invokerLeft + menuWidth;

        if( menuRight < screen.width )
        {
            return x;
        }

        return x - (menuRight - screen.width);
    }

    protected static int fitYinScreen( final Dimension screen, final JPopupMenu menu, final Component invoker,
        final int y )
    {
        final int invokerHeight = invoker.getHeight();
        // at first actual menu height == 0
        final int menuHeight = menu.getPreferredSize().height;

        final int invokerTop = invoker.getLocationOnScreen().y;
        final int menuBottom = invokerTop + invokerHeight + menuHeight;

        if( menuBottom < screen.height )
        { // popup below the invoker
            return y + invokerHeight;
        }

        // popup above the invoker
        return y - menuHeight;
    }

    /**
     * Makes the popup appear inside the screen vertical bounds. Assumes that the invoker is inside the screen bounds.
     *
     * @param menu
     * @param invoker
     * @param x
     * @param y
     */
    protected static void showPopupMenu( final JPopupMenu menu, final Component invoker, final int x, final int y )
    {
        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        final int fitXinScreen = fitXinScreen( screen, menu, invoker, x );
        final int fitYinScreen = fitYinScreen( screen, menu, invoker, y );
        menu.show( invoker, fitXinScreen, fitYinScreen );
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
        showPopupMenu( this, invoker, 0, 0 );
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
