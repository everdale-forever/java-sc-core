package com.banaanae.javasccore.networking;

import com.banaanae.javasccore.titan.ArrayUtils;

public class Queue {
    public final int QUEUE_FREE = 0;
    public final int QUEUE_BUSY = 1;
    public final int QUEUE_OVERFILLED = 2;
    public final int QUEUE_PUSHED_MORE_THAN_EXPECTED = 3;
    public final int QUEUE_DETECTED_MERGED_PACKETS = 4;
    
    private String originalMerged = "";
    
    byte[] data;
    int maxQueueSize;
    public int state;
    Packet[] unmergedPackets;
    boolean disableQueueBugTxt;
    
    
    public Queue(int maxQueueSize, boolean disableQueueBugTxt) {
        this.data = new byte[0];
        this.maxQueueSize = maxQueueSize;
        this.state = QUEUE_FREE;
        //this.unmergedPackets = Object[0];
        this.disableQueueBugTxt = disableQueueBugTxt;
    }
    
    public void push(byte[] bytes) {
        this.data = ArrayUtils.concat(data, bytes);
        this.state = QUEUE_BUSY;
        
        if (this.data.length > maxQueueSize && maxQueueSize > 0) {
            this.state = QUEUE_OVERFILLED;
        }
        
        if (this.data.length - 7 > ArrayUtils.readUIntBE(data, 2, 3)) {
            this.originalMerged = ArrayUtils.toHex(data);
        }
    }
    
    public boolean isBusy() {
        if (this.data.length == 0)
            return false;
        
        return getQueueExpectedSize() > this.data.length - 7;
    }
    
    public int getQueueExpectedSize() {
        return (int) ArrayUtils.readUIntBE(data, 2, 3);
    }
    
    public Object release() {
        final Object returnableData = (unmergedPackets != null && unmergedPackets.length != 0)
                ? unmergedPackets : this.data;

        this.data = new byte[0];
        this.state = this.QUEUE_FREE;
        this.unmergedPackets = new Packet[0];
        this.originalMerged = "";

        return returnableData;
    }
}
