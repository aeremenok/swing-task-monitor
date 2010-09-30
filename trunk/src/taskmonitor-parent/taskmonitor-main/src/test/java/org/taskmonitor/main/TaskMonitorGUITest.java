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

import static org.testng.Assert.assertEquals;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Frame;
import java.net.URL;
import java.util.concurrent.Callable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.SwingWorker;
import javax.swing.UIManager;

import org.fest.swing.core.GenericTypeMatcher;
import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiTask;
import org.fest.swing.fixture.JButtonFixture;
import org.fest.swing.fixture.JPopupMenuFixture;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import test.env.ComponentEnvironment;
import test.env.ScreenshotSaver;
import test.fixtures.MonitorFixture;
import test.fixtures.WorkerFixture;

/**
 * @author aeremenok 2010
 */
@Test( sequential = true )
public class TaskMonitorGUITest
{
    private ComponentEnvironment<MonitorFixture.Holder> env;
    private MonitorFixture                              progressBar;
    private JPopupMenuFixture                           menuFixture;
    private final ScreenshotSaver                       screenshotSaver = new ScreenshotSaver();

    @Test( timeOut = 15000 )
    public void cancelAll()
        throws InterruptedException
    {
        assert !progressBar.isVisible();

        final WaitingWorker first = newTask( "Task Name" );
        final WaitingWorker second = newTask( "Very Long Task Name" );
        final WaitingWorker third = newTask( "Very Very So Much Long Task Name Goes Here Blah-blah-blah Lots of letters" );

        invokeAndExpect( first, first.getTaskId() );
        invokeAndExpect( second, TaskUI.getCancelTaskTooltip() );
        invokeAndExpect( third, TaskUI.getCancelTaskTooltip() );

        assert progressBar.isVisible();

        clickMainButton();
        requireMenuItemCount( 3 );
        takeScreenshot();
        clickAtMenuItem( first );
        requireMainButtonText( TaskUI.getCancelTaskTooltip() );

        clickMainButton();
        requireMenuItemCount( 2 );
        clickAtMenuItem( second );
        requireMainButtonText( third.getTaskId().substring( 0, 27 ) + "..." );

        Thread.sleep( 1000 );
        clickMainButton();
        assert !progressBar.isVisible();
    }

    @BeforeClass
    public void setUp()
        throws Exception
    {
        final URL imageUrl = getClass().getClassLoader().getResource( "stop.png" );
        assert imageUrl != null;
        UIManager.getDefaults().put( TaskUI.CANCEL_TASK_ACTION_ICON, new ImageIcon( imageUrl ) );
        UIManager.getDefaults().put( TaskUI.DISPLAY_ONLY_ONE_PROGRESS_BAR, true );
        UIManager.getDefaults().put( TaskUI.CANCEL_TASK_ACTION_TEXT_POSITION, SwingConstants.LEADING );
        UIManager.getDefaults().put( TaskUI.FIXED_BUTTON_MAXCHARS, 30 );
        UIManager.getDefaults().put( TaskUI.FIXED_BUTTON_WIDTH, 700 );

        env = ComponentEnvironment.fromQuery( new Callable<MonitorFixture.Holder>()
        {
            @Override
            public MonitorFixture.Holder call()
            {
                return new MonitorFixture.Holder();
            }
        } );
        env.setUp( this );
        progressBar = env.getComponent().getMonitorFixture();

        menuFixture = new JPopupMenuFixture( env.getWrapperPanelFixture().robot, progressBar.getTaskPopup() );

        final Frame frame = env.getFrameFixture().target;
        frame.setMinimumSize( new Dimension( 800, 600 ) );
        frame.pack();
        frame.setLocationRelativeTo( null );

        // todo add tests using different locations on the screen
        //        final Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        //        env.getFrameFixture().component().setLocation( screen.width / 2, screen.height - 70 );
    }

    @AfterClass
    public void tearDown()
        throws Exception
    {
        env.tearDown( this );
    }

    private void clickAtMenuItem( final WaitingWorker item )
    {
        new JButtonFixture( menuFixture.robot, item.getTaskId() ).click();
    }

    private void clickMainButton()
    {
        env.getWrapperPanelFixture().button().click();
    }

    private void invokeAndExpect( final SwingWorker worker, final String text )
        throws InterruptedException
    {
        progressBar.invoke( worker );
        Thread.sleep( 1000 );
        requireMainButtonText( text );
    }

    private WaitingWorker newTask( final String taskId )
    {
        return new WaitingWorker( taskId );
    }

    private void requireMainButtonText( final String text )
    {
        env.getWrapperPanelFixture().label( BelongsToMainButton.INSTANCE ).requireText( text );
    }

    private void requireMenuItemCount( final int count )
    {
        GuiActionRunner.execute( new GuiTask()
        {
            @Override
            protected void executeInEDT()
            {
                menuFixture.requireVisible();
                assertEquals( menuFixture.component().getComponentCount(), count );
            }
        } );
    }

    private void takeScreenshot()
    {
        screenshotSaver.saveDesktopScreenshot( System.getProperty( "user.home" ) + "/taskmon" );
    }

    /**
     * @author aeremenok 2010
     */
    private static final class BelongsToMainButton
        extends GenericTypeMatcher<JLabel>
    {
        public final static BelongsToMainButton INSTANCE = new BelongsToMainButton();

        private BelongsToMainButton()
        {
            super( JLabel.class );
        }

        @Override
        protected boolean isMatching( final JLabel component )
        {
            final String text = component.getText();
            if( text == null || "".equals( text ) )
            {
                return false;
            }
            final Container parent = component.getParent();
            if( parent == null )
            {
                return false;
            }

            final Container grandParent = parent.getParent();
            if( grandParent instanceof ProgressBarButton )
            {
                return grandParent.getParent() instanceof TaskMonitor;
            }
            return false;
        }
    }

    private static final class WaitingWorker
        extends WorkerFixture
    {
        volatile boolean hang = true;

        public WaitingWorker( final String taskId )
        {
            super( taskId );
        }

        @Override
        protected Boolean doInBackground()
        {
            while( hang )
            {
                try
                {
                    Thread.sleep( 500 );
                }
                catch( final InterruptedException e )
                {
                    hang = false;
                }
            }
            return true;
        }
    }
}
