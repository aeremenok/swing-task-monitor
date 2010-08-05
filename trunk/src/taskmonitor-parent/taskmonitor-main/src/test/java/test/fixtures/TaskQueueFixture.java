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

import org.taskmonitor.main.TaskQueue;

/**
 * @author aeremenok 2010
 */
public class TaskQueueFixture
    extends TaskQueue<WorkerFixture>
{
    @Override
    protected void cancelExecution( final WorkerFixture worker, final boolean mayInterruptIfRunning )
    {
        worker.interrupt();
    }

    @Override
    public String getTitle( final WorkerFixture worker )
    {
        return worker.getTaskId();
    }
}
