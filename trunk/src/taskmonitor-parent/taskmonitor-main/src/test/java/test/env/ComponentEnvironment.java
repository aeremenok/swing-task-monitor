/**
 * 
 */
package test.env;

import java.util.concurrent.Callable;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.UIManager;

import org.fest.swing.edt.GuiActionRunner;
import org.fest.swing.edt.GuiQuery;
import org.fest.swing.fixture.FrameFixture;
import org.fest.swing.fixture.GenericComponentFixture;
import org.fest.swing.fixture.JPanelFixture;

import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;

/**
 * @author eav 2010
 * @param <C>
 */
@SuppressWarnings( "restriction" )
public class ComponentEnvironment<C extends JComponent>
    extends Environment
{
    private static final String WRAPPER_PANEL = "wrapper panel";

    private final boolean       scroll;
    private final Callable<C>   guiQuery;
    private FrameFixture        frameFixture;
    private C                   component;

    public ComponentEnvironment( final Callable<C> guiQuery, final boolean scroll )
    {
        this.guiQuery = guiQuery;
        this.scroll = scroll;
    }

    public static <C extends JComponent> ComponentEnvironment<C> fromQuery( final Callable<C> guiQuery )
    {
        return fromQuery( guiQuery, false );
    }

    public static <C extends JComponent> ComponentEnvironment<C> fromQuery( final Callable<C> guiQuery,
        final boolean scroll )
    {
        return new ComponentEnvironment<C>( guiQuery, scroll );
    }

    public <W extends JComponent> GenericComponentFixture<W> derive( final W c )
    {
        return new GenericComponentFixture<W>( getWrapperPanelFixture().robot, c )
        {};
    }

    public C getComponent()
    {
        return this.component;
    }

    public FrameFixture getFrameFixture()
    {
        return this.frameFixture;
    }

    public JPanelFixture getWrapperPanelFixture()
    {
        return getFrameFixture().panel( WRAPPER_PANEL );
    }

    @Override
    public void setUp( final Object test )
        throws Exception
    {
        super.setUp( test );
        final JFrame frame = GuiActionRunner.execute( new GuiQuery<JFrame>()
        {
            @Override
            protected JFrame executeInEDT()
                throws Throwable
            {
                UIManager.setLookAndFeel( new NimbusLookAndFeel() );

                final JFrame frame = new JFrame( "test" );
                component = guiQuery.call();
                final WrapperPanel contentPane = new WrapperPanel( component, scroll );
                contentPane.setName( WRAPPER_PANEL );
                frame.setContentPane( contentPane );

                frame.pack();
                frame.setLocationRelativeTo( null );
                frame.setVisible( true );

                return frame;
            }
        } );
        frameFixture = new FrameFixture( frame );
    }

    @Override
    public void tearDown( final Object test )
        throws Exception
    {
        frameFixture.cleanUp();
        super.tearDown( test );
    }
}
