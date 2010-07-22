/**
 * 
 */
package org.taskmonitor.main;

import javax.swing.Icon;
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
    public static final String CANCEL_TASK_ACTION_TOOLTIP = "CancelTaskAction.tooltip";
    public static final String CANCEL_TASK_ACTION_ICON    = "CancelTaskAction.icon";
    public static final String PROGRESS_BAR_WIDTH         = "ProgressBar.width";

    /**
     * @return value of {@link TaskUI#CANCEL_TASK_ACTION_ICON}
     */
    public static Icon getCancelTaskIcon()
    {
        return UIManager.getIcon( CANCEL_TASK_ACTION_ICON );
    }

    /**
     * @return value of {@link TaskUI#CANCEL_TASK_ACTION_TOOLTIP} or <u>"Cancel Task"</u> if it is not specified
     */
    public static String getCancelTaskTooltip()
    {
        final String tooltip = UIManager.getString( CANCEL_TASK_ACTION_TOOLTIP );
        if( tooltip == null )
        {
            return "Cancel Task";
        }
        return tooltip;
    }

    /**
     * @return value of {@link TaskUI#PROGRESS_BAR_WIDTH} or <u>75</u> pixels if it is not specified
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
}
