package com.banaanae.javasccore.networking;

public class Queue {
    private final int QUEUE_FREE = 0;
    private final int QUEUE_BUSY = 1;
    private final int QUEUE_OVERFILLED = 2;
    private final int QUEUE_PUSHED_MORE_THAN_EXPECTED = 3;
    private final int QUEUE_DETECTED_MERGED_PACKETS = 4;
    
    private final String originalMerged = "";
    
    byte[] data;
    int maxQueueSize = 0;
    int state = -1;
    
    
    public Queue(int maxQueueSize, boolean disableQueuebugtxtFile) {
        this.data = {};
    }
}
