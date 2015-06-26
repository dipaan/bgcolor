package com.dipaan.bgcolor;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;

/**
 * Manages an in-memory queue. This queue can store a tuple consisting
 * of three values between (0-255) that represent three colors (Red, Green,
 * and Blue). On the first invocation of a fetch, it generates 100 tuples
 * randomly to populate the queue. When the queue runs low, more tuples are
 * generated on the fetch call. On fetch, a tuple is returned from the available
 * queue and moved to the reserved queue. A markUsed call for a given tuple,
 * removes it from the reserved queue. If a markUsed call isn't made for a given
 * tuple within 15 seconds (+1second allowed latency), the tuple is made
 * available again.
 */
public class QueueManager {
    // expected queue size to be maintained at any given point in time
    static final int expectedQueueSize = 100;

    // upper limit for random number generation, e.g. a value of 256 would
    // generate a number randomly between 0 and 255
    static final int randomLimit = 256;

    // time to wait until a tuple is marked used or returned to the available pool
    static final int expirationPeriodMillis = 15 * 1000; // 15 seconds

    static Random random = new java.util.Random();

    Queue<Tuple> availableQueue; // queue of tuples available for fetch
    Queue<Tuple> reservedQueue; // queue of tuples already served to clients

    public QueueManager() {
        availableQueue = new ConcurrentLinkedQueue<Tuple>();
        reservedQueue = new ConcurrentLinkedQueue<Tuple>();
    }

    public Tuple fetchNew() {
        if (availableQueue.size() < expectedQueueSize) {
            populate(expectedQueueSize - availableQueue.size());
        }
        Tuple tuple = availableQueue.poll();
        tuple.setExpiration(expirationPeriodMillis);
        reservedQueue.offer(tuple);
        return tuple;
    }

    public void markUsed(String tupleId) {
        int n = reservedQueue.size();
        while (n > 0) {
            Tuple tuple = reservedQueue.peek();
            if (tuple.getId().equals(tupleId)) {
                reservedQueue.remove(tuple);
            } else if (tuple.isExpired()) {
                reservedQueue.remove(tuple);
                availableQueue.offer(tuple);
            }
            n--;
        }
    }

    public static Tuple generate() {
        byte red = (byte)random.nextInt(randomLimit);
        byte green = (byte)random.nextInt(randomLimit);
        byte blue = (byte)random.nextInt(randomLimit);
        String id = UUID.randomUUID().toString();
        return new Tuple(id, red, green, blue);
    }

    void populate(int count) {
        for (int i = 0; i < count; i++) {
            Tuple tuple = QueueManager.generate();
            availableQueue.offer(tuple);
        }
    }
}
