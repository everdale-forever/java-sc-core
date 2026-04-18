package com.banaanae.javasccore.titan.time;

import java.time.Instant;

public class xTimer {
    public static long getNativeTime() {
        Instant now = Instant.now();
        return now.getEpochSecond() * 1_000_000_000L + now.getNano();
    }
    
    public static long getSecondsSince1970() {
        return Instant.now().getEpochSecond();
    }
}
