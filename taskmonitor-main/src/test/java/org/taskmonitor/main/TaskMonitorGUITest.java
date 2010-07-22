/**
 * 
 */
package org.taskmonitor.main;

import static org.testng.Assert.assertEquals;

import java.awt.Container;
import java.awt.Dimension;
import java.util.concurrent.Callable;

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

    @Test( timeOut = 15000 )
    public void cancelAll()
        throws InterruptedException
    {
        assert !progressBar.isVisible();

        final WaitingWorker first = createTestWorker();
        final WaitingWorker second = createTestWorker();
        final WaitingWorker third = createTestWorker();

        invokeAndExpect( first, first.getTaskId() );
        invokeAndExpect( second, TaskMonitorGUITest.class.getSimpleName() );
        invokeAndExpect( third, TaskMonitorGUITest.class.getSimpleName() );

        assert progressBar.isVisible();
        pack();

        clickMainButton();
        requireMenuItemCount( 3 );
        clickAtMenuItem( first );
        requireMainButtonText( TaskMonitorGUITest.class.getSimpleName() );

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
        //        UIManager.getDefaults().put( TaskUI.CANCEL_TASK_ACTION_ICON, IMG.icon( IMG.REMOVE_ICON_PATH ) );
        UIManager.getDefaults().put( TaskUI.CANCEL_TASK_ACTION_TOOLTIP, TaskMonitorGUITest.class.getSimpleName() );

        env = ComponentEnvironment.fromQuery( new Callable<MonitorFixture>()
        {
            @Override
            public MonitorFixture call()
            {
                return new MonitorFixture();
            }
        }, true );
        env.setUp( this );
        env.getFrameFixture().component().setMinimumSize( new Dimension( 640, 70 ) );
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
        pack();
    }

    private void invokeAndExpect( final SwingWorker worker, final String text )
        throws InterruptedException
    {
        progressBar.invoke( worker );
        Thread.sleep( 1000 );
        requireMainButtonText( text );
    }

    private void pack()
    {
        env.getFrameFixture().component().pack();
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

    protected WaitingWorker createTestWorker()
    {
        return new WaitingWorker();
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
