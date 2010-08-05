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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.SwingWorker;

/**
 * Initiates a {@link SwingWorker} interruption by the {@link TaskQueue}. Is enabled if and only if the
 * {@link TaskQueue#isInterruptible(SwingWorker)} returns <code>true</code>.
 *
 * @author aeremenok 2010
 */
@SuppressWarnings( "unchecked" )
public class CancelTaskAction
    extends AbstractAction
{
    private final TaskQueue   taskQueue;
    private final SwingWorker worker;

    /**
     * @param worker to be interrupted
     * @param taskQueue controls the given worker
     */
    public CancelTaskAction( final SwingWorker worker, final TaskQueue taskQueue )
    {
        super( taskQueue.getTitle( worker ), taskQueue.isInterruptible( worker )
            ? TaskUI.getCancelTaskIcon() : null );
        putValue( LONG_DESCRIPTION, TaskUI.getCancelTaskTooltip() );

        setEnabled( taskQueue.isInterruptible( worker ) );

        this.taskQueue = taskQueue;
        this.worker = worker;
    }

    @Override
    public void actionPerformed( final ActionEvent e )
    {
        taskQueue.interrupt( worker );
    }
}
