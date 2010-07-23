/**
 * 
 */
package org.taskmonitor.main;

import static org.testng.Assert.assertEquals;

import java.awt.Container;
import java.awt.Dimension;
import java.net.URL;
import java.util.concurrent.Callable;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
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
    private ComponentEnvironment<MonitorFixture> env;
    private MonitorFixture                       progressBar;
    private JPopupMenuFixture                    menuFixture;
    private final ScreenshotSaver                screenshotSaver = new ScreenshotSaver();

    @Test( timeOut = 15000 )
    public void cancelAll()
        throws InterruptedException
    {
        assert !progressBar.isVisible();

        final WaitingWorker first = newTask( "Task 1" );
        final WaitingWorker second = newTask( "Task 2" );
        final WaitingWorker third = newTask( "Task 3" );

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
        requireMainButtonText( third.getTaskId() );

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

        env = ComponentEnvironment.fromQuery( new Callable<MonitorFixture>()
        {
            @Override
            public MonitorFixture call()
            {
                return new MonitorFixture();
            }
        }, true );
        env.setUp( this );
        env.getFrameFixture().component().setMinimumSize( new Dimension( 300, 65 ) );
        progressBar = env.getComponent();

        menuFixture = new JPopupMenuFixture( env.getWrapperPanelFixture().robot, progressBar.getTaskPopup() );
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

    protected WaitingWorker newTask( final String taskId )
    {
        return new WaitingWorker( taskId );
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
            final Container parent = component.getParent();
            if( parent instanceof ProgressBarButton )
            {
                return parent.getParent() instanceof TaskMonitor;
            }
            return false;
        }
    }

    private static final class WaitingWorker
        extends WorkerFixture
    {
        public WaitingWorker( final String taskId )
        {
            super( taskId );
        }

        volatile boolean hang = true;

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
