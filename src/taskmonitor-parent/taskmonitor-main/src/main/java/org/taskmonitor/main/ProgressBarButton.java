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

import static org.taskmonitor.main.ProgressBarButton.DisplayMode.MULTIPLE;
import static org.taskmonitor.main.ProgressBarButton.DisplayMode.SINGLE;
import static org.taskmonitor.main.TaskUI.displayOnlyOneProgressBar;
import static org.taskmonitor.main.TaskUI.getProgressBarWidth;

import java.awt.Dimension;

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingConstants;

/**
 * Displays an iterruption icon, a task title and an indeterminate progressbar<br>
 * todo listen for SwingWorker progress
 * 
 * @author aeremenok 2010
 * @see JProgressBar
 */
public class ProgressBarButton
    extends JButton
{
    public ProgressBarButton( final Action a )
    {
        this( a, DisplayMode.SINGLE );
    }

    public ProgressBarButton( final Action a, final DisplayMode displayMode )
    {
        super( a );

        final int fixedButtonsWidth = TaskUI.getFixedButtonsWidth();
        if( fixedButtonsWidth > 0 )
        {
            final Dimension exactSize = new Dimension( fixedButtonsWidth, getPreferredSize().height );
            setMinimumSize( exactSize );
            setMaximumSize( exactSize );
        }

        setName( getText() );

        setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );
        add( createIconLabel() );
        if( displayMode == SINGLE || displayMode == MULTIPLE && !displayOnlyOneProgressBar() )
        {
            add( Box.createHorizontalGlue() );
            add( createProgressBar() );
        }

        setHideActionText( true );
        setIcon( null );
    }

    protected String shorten( final String text, final int maxChars )
    {
        if( text.length() < maxChars )
        {
            return text;
        }
        return text.substring( 0, maxChars - 3 ) + "...";
    }

    protected Box createIconLabel()
    {
        final Box box = Box.createHorizontalBox();
        final JLabel textLabel = new JLabel( getText(), SwingConstants.CENTER );
        final JLabel iconLabel = new JLabel( getIcon(), SwingConstants.CENTER );

        final int maxChars = TaskUI.getFixedButtonsMaxChars();
        if( maxChars > 0 )
        {
            textLabel.setText( shorten( getText(), maxChars ) );
        }

        if( TaskUI.getCancelTaskTextPosition() == SwingConstants.LEADING )
        {
            box.add( textLabel );
            box.add( Box.createHorizontalGlue() );
            box.add( iconLabel );
        }
        else
        {
            box.add( iconLabel );
            box.add( Box.createHorizontalGlue() );
            box.add( textLabel );
        }
        return box;
    }

    protected JProgressBar createProgressBar()
    {
        final JProgressBar progressBar = new JProgressBar();
        progressBar.setName( getText() );
        progressBar.setIndeterminate( true );

        // make all progressbars' width equal
        final int progressBarWidth = getProgressBarWidth() > 0
            ? getProgressBarWidth() : 70;
        progressBar.setMaximumSize( new Dimension( progressBarWidth, progressBar.getMaximumSize().height ) );

        return progressBar;
    }

    public static enum DisplayMode
    {
            SINGLE,
            MULTIPLE;
    }
}
