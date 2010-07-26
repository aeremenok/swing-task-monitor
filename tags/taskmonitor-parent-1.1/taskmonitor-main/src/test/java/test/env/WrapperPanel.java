/**
 * 
 */
package test.env;

import java.awt.BorderLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * @author eav 2009
 */
public class WrapperPanel
    extends JPanel
{
    private final JComponent component;

    public WrapperPanel( final JComponent component )
    {
        this( component, false );
    }

    public WrapperPanel( final JComponent component, final boolean scroll )
    {
        super( new BorderLayout() );
        this.component = component;
        if( scroll )
        {
            add( new JScrollPane( component ), BorderLayout.CENTER );
        }
        else
        {
            add( component, BorderLayout.CENTER );
        }
    }

    public JComponent getComponent()
    {
        return this.component;
    }
}
