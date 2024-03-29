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

import java.awt.Font;

import javax.swing.Icon;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

/**
 * Contains basic UI constants. They can be modified by putting the actual values to the {@link UIManager#getDefaults()}
 * . The keys to use:
 * <ul>
 * <li> {@link TaskUI#CANCEL_TASK_ACTION_ICON}</li>
 * <li> {@link TaskUI#CANCEL_TASK_ACTION_TOOLTIP}</li>
 * <li> {@link TaskUI#PROGRESS_BAR_WIDTH}</li>
 * </ul>
 * 
 * @author aeremenok 2010
 */
public class TaskUI
{
    public static final String CANCEL_TASK_ACTION_TOOLTIP       = "CancelTaskAction.tooltip";
    public static final String CANCEL_TASK_ACTION_ICON          = "CancelTaskAction.icon";
    public static final String CANCEL_TASK_ACTION_TEXT_POSITION = "CancelTaskAction.text.position";

    public static final String BUTTON_FONT                      = "Button.all.font";
    public static final String FIXED_BUTTON_WIDTH               = "Button.all.width";
    public static final String FIXED_BUTTON_MAXCHARS            = "Button.all.maxchars";

    public static final String PROGRESS_BAR_WIDTH               = "ProgressBar.width";
    public static final String DISPLAY_ONLY_ONE_PROGRESS_BAR    = "ProgressBar.display.single";

    /**
     * @return value for {@link TaskUI#CANCEL_TASK_ACTION_ICON}
     */
    public static Icon getCancelTaskIcon()
    {
        return UIManager.getIcon( CANCEL_TASK_ACTION_ICON );
    }

    /**
     * @return value for {@link TaskUI#CANCEL_TASK_ACTION_TOOLTIP} or <u>"Show tasks"</u> if it is not specified
     */
    public static String getCancelTaskTooltip()
    {
        final String tooltip = UIManager.getString( CANCEL_TASK_ACTION_TOOLTIP );
        if( tooltip == null )
        {
            return "Show tasks";
        }
        return tooltip;
    }

    /**
     * @return value for {@link TaskUI#PROGRESS_BAR_WIDTH} or <u>75</u> pixels if it is not specified
     */
    public static int getProgressBarWidth()
    {
        final int width = UIManager.getInt( PROGRESS_BAR_WIDTH );
        if( width == 0 )
        {
            return 75;
        }
        return width;
    }

    public static Font getButtonFont()
    {
        return UIManager.getFont( BUTTON_FONT );
    }

    public static int getFixedButtonsWidth()
    {
        return UIManager.getInt( FIXED_BUTTON_WIDTH );
    }

    public static int getFixedButtonsMaxChars()
    {
        return UIManager.getInt( FIXED_BUTTON_MAXCHARS );
    }

    public static boolean displayOnlyOneProgressBar()
    {
        return UIManager.getBoolean( DISPLAY_ONLY_ONE_PROGRESS_BAR );
    }

    public static int getCancelTaskTextPosition()
    {
        if( UIManager.get( CANCEL_TASK_ACTION_TEXT_POSITION ) == null )
        {
            return SwingConstants.TRAILING;
        }

        final int position = UIManager.getInt( CANCEL_TASK_ACTION_TEXT_POSITION );
        switch( position )
        {
            case SwingConstants.LEADING:
            case SwingConstants.TRAILING:
                return position;
            default:
                return SwingConstants.TRAILING;
        }
    }
}
