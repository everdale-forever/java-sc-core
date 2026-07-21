package com.banaanae.javasccore.titan;

import java.time.Instant;

public class xTimer {
    public static long getNativeTime() {
        Instant now = Instant.now();
        return now.getEpochSecond() * 1_000_000_000L + now.getNano();
    }
}
