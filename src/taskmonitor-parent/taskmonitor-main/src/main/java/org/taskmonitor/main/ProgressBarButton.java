/**
 * 
 */
package org.taskmonitor.main;

import java.awt.Dimension;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

/**
 * @author aeremenok 2010
 */
public class ProgressBarButton
    extends JButton
{
    public ProgressBarButton( final Action a )
    {
        super( a );

        setName( getText() );

        setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );
        add( createIcon() );
        add( Box.createHorizontalGlue() );
        add( createProgressBar() );

        setHideActionText( true );
        setIcon( null );
    }

    private JLabel createIcon()
    {
        return new JLabel( getText(), getIcon(), SwingConstants.CENTER );
    }

    private JProgressBar createProgressBar()
    {
        final JProgressBar progressBar = new JProgressBar();
        progressBar.setMaximumSize( new Dimension( 70, progressBar.getMaximumSize().height ) );
        progressBar.setName( getText() );
        progressBar.setIndeterminate( true );
        return progressBar;
    }

}
