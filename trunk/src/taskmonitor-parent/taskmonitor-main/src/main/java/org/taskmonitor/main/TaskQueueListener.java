/**
 * 
 */
package org.taskmonitor.main;

/**
 * @author aeremenok 2010
 */
public interface TaskQueueListener
{
    /**
     * действие при изменении состава запущенных worker-ов. Может вызваться при добавлении нового worker, завершении или
     * прерывании старого
     * 
     * @param taskQueueEvent
     */
    void taskQueueChanged( TaskQueueEvent taskQueueEvent );
}
