/**
 * 
 */
package org.taskmonitor.main;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingWorker;
import javax.swing.SwingWorker.StateValue;

import org.apache.log4j.Logger;

/**
 * @author aeremenok 2010
 */
public abstract class TaskQueue
{
    private static final Logger            log                      = Logger.getLogger( TaskQueue.class );

    private final Map<String, SwingWorker> workersByTitles          = new HashMap<String, SwingWorker>();
    private final TaskQueueListenerSupport taskQueueListenerSupport = new TaskQueueListenerSupport();

    public boolean addTaskQueueListener( final TaskQueueListener e )
    {
        return taskQueueListenerSupport.addListener( e );
    }

    /**
     * @param worker
     * @return название, фильтра (идентификатор кнопки)
     */
    public abstract String getActionId( final SwingWorker worker );

    /**
     * @param worker
     * @return название, которое отображается пользователю
     */
    public abstract String getTitle( final SwingWorker worker );

    /**
     * выполнить прерывание работы worker
     * 
     * @param worker
     */
    public void interrupt( final SwingWorker worker )
    {
        if( !workersByTitles.containsValue( worker ) )
        {
            return;
        }

        if( isInterruptible( worker ) )
        {
            doIterruption( worker );
        }

        log.debug( "отменена задача " + getActionId( worker ) );
        unregisterTask( worker );
    }

    /**
     * Если worker c таким же названием не выполняется, то запустить worker, добавить его в список выполняющихся
     * worker-ов и оповестить всех об этом
     * 
     * @param worker
     */
    public void invoke( final SwingWorker worker )
    {
        final String title = getActionId( worker );
        if( workersByTitles.containsValue( worker ) )
        {
            log.debug( title + " уже вызван" );
            return;
        }

        worker.getPropertyChangeSupport().addPropertyChangeListener( "state", new CompletionListener( worker ) );

        final List<SwingWorker> oldQueue = takeQueueSnapshot();
        synchronized( workersByTitles )
        {
            workersByTitles.put( title, worker );

            log.debug( "запущена задача " + title );
            worker.execute();

            taskQueueListenerSupport.fireEvent( new TaskQueueEvent( this, oldQueue, takeQueueSnapshot() ) );
        }
    }

    public abstract boolean isInterruptible( final SwingWorker worker );

    private List<SwingWorker> takeQueueSnapshot()
    {
        return new ArrayList<SwingWorker>( workersByTitles.values() );
    }

    /**
     * удалить из очереди задачу worker
     * 
     * @param worker
     */
    private void unregisterTask( final SwingWorker worker )
    {
        if( !workersByTitles.containsValue( worker ) )
        {
            return;
        }

        final String title = getActionId( worker );
        final List<SwingWorker> oldQueue = takeQueueSnapshot();
        synchronized( workersByTitles )
        {
            log.debug( "завершена задача " + title );
            workersByTitles.remove( title );
            taskQueueListenerSupport.fireEvent( new TaskQueueEvent( this, oldQueue, takeQueueSnapshot() ) );
        }
    }

    /**
     * прервать выполнение worker<br>
     * ПРЕДУСЛОВИЕ: isInterruptible( worker ) == true
     * 
     * @param worker
     */
    protected abstract void doIterruption( SwingWorker worker );

    private final class CompletionListener
        implements
        PropertyChangeListener
    {
        private final SwingWorker worker;

        private CompletionListener( final SwingWorker worker )
        {
            this.worker = worker;
        }

        /**
         * вызывается при изменении статуса запущенного worker. Если статус сделался "фоновая работа выполнена", то
         * удалить worker из списка задач
         */
        @Override
        public void propertyChange( final PropertyChangeEvent evt )
        {
            switch( (StateValue) evt.getNewValue() )
            {
                case PENDING:
                case STARTED:
                    break;
                case DONE: // xxx если выставлен done, то doInBackground выполнено. done() - может быть выполнено, а может еще нет
                    unregisterTask( worker );
                    break;
            }
        }

    }
}
