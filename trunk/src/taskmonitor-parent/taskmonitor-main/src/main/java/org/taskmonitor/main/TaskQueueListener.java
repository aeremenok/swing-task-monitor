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
     * �������� ��� ��������� ������� ���������� worker-��. ����� ��������� ��� ���������� ������ worker, ���������� ���
     * ���������� �������
     * 
     * @param taskQueueEvent
     */
    void taskQueueChanged( TaskQueueEvent taskQueueEvent );
}
