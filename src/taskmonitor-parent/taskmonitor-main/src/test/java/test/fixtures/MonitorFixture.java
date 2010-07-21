/**
 * 
 */
package test.fixtures;

import org.taskmonitor.main.TaskMonitor;
import org.taskmonitor.main.TaskPopup;

public class MonitorFixture
    extends TaskMonitor
{
    public MonitorFixture()
    {
        super( new TaskQueueFixture() );
    }

    public TaskPopup getTaskPopup()
    {
        return taskPopup;
    }
}