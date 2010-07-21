/**
 * 
 */
package org.taskmonitor.main;

import javax.swing.Icon;
import javax.swing.UIManager;

/**
 * @author aeremenok 2010
 */
public class TaskUI
{
    public static final String CANCEL_TASK_ACTION_TOOLTIP = "CancelTaskAction.tooltip";
    public static final String CANCEL_TASK_ACTION_ICON    = "CancelTaskAction.icon";
    public static final String PROGRESS_BAR_WIDTH         = "ProgressBar.width";

    public static Icon cancelTaskIcon()
    {
        return UIManager.getIcon( CANCEL_TASK_ACTION_ICON );
    }

    public static String cancelTaskTooltip()
    {
        final String tooltip = UIManager.getString( CANCEL_TASK_ACTION_TOOLTIP );
        if( tooltip == null )
        {
            return "Cancel Task";
        }
        return tooltip;
    }

    public static int progressBarWidth()
    {
        final int width = UIManager.getInt( PROGRESS_BAR_WIDTH );
        if( width == 0 )
        {
            return 75;
        }
        return width;
    }
}
