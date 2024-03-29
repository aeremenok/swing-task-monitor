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
package test.fixtures;

import java.awt.FlowLayout;

import javax.swing.JPanel;

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

    public static class Holder
        extends JPanel
    {
        private final MonitorFixture monitorFixture = new MonitorFixture();

        public Holder()
        {
            super( new FlowLayout() );
            add( monitorFixture );
        }

        public TaskPopup getTaskPopup()
        {
            return this.monitorFixture.getTaskPopup();
        }

        public MonitorFixture getMonitorFixture()
        {
            return this.monitorFixture;
        }
    }
}
