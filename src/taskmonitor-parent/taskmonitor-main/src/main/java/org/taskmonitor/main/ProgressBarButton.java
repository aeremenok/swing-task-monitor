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
        super( a );

        setName( getText() );

        setLayout( new BoxLayout( this, BoxLayout.X_AXIS ) );
        add( createIconLabel() );
        add( Box.createHorizontalGlue() );
        add( createProgressBar() );

        setHideActionText( true );
        setIcon( null );
    }

    protected JLabel createIconLabel()
    {
        return new JLabel( getText(), getIcon(), SwingConstants.CENTER );
    }

    protected JProgressBar createProgressBar()
    {
        final JProgressBar progressBar = new JProgressBar();
        progressBar.setName( getText() );
        progressBar.setIndeterminate( true );

        // make all progressbars' width equal
        progressBar.setMaximumSize( new Dimension( 70, progressBar.getMaximumSize().height ) );

        return progressBar;
    }
}
