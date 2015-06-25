package com.dipaan.bgcolor;

import org.junit.After;
import org.junit.Before;
import static org.junit.Assert.*;

public class QueueManagerTest {

    /**
     * Test of fetchNew method, of class QueueManager.
     * @throws java.lang.InterruptedException
     */
    @org.junit.Test
    public void testFetchNew() throws InterruptedException {
        QueueManager instance = new QueueManager();
        Tuple result = instance.fetchNew();
        assertEquals(QueueManager.expectedQueueSize - 1,
            instance.availableQueue.size());
        assertEquals(1, instance.reservedQueue.size());
        Tuple reserved = instance.reservedQueue.peek();
        assertEquals(result.getId(), reserved.getId());
        assertEquals(result.getValue(), reserved.getValue());
        assertFalse(result.isExpired());
        Thread.sleep(16000);
        assertTrue(result.isExpired());
    }

    /**
     * Test of markUsed method, of class QueueManager.
     */
    @org.junit.Test
    public void testMarkUsed() {
        Tuple tuple = QueueManager.generate();
        QueueManager instance = new QueueManager();
        instance.reservedQueue.offer(tuple);
        instance.markUsed(tuple.getId());
        assertEquals(0, instance.reservedQueue.size());
    }

    /**
     * Test of generate method, of class QueueManager.
     */
    @org.junit.Test
    public void testGenerate() {
        Tuple result1 = QueueManager.generate();
        Tuple result2 = QueueManager.generate();
        assertNotEquals(result1.getId(), result2.getId());
        assertNotEquals(result1.getValue(), result2.getValue());
    }

    /**
     * Test of populate method, of class QueueManager.
     */
    @org.junit.Test
    public void testPopulate() {
        int count = 10;
        QueueManager instance = new QueueManager();
        instance.populate(count);
        assertEquals(10, instance.availableQueue.size());
    }
    
}
