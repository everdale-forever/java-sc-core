package com.banaanae.javasccore.titan.random;

import java.util.ArrayList;
import java.util.List;

public class LogicMersenneTwisterRandom {
    private final int SEED_COUNT = 624;
    private int idx = 0;
    private final List seeds = new ArrayList<>();
    
    public LogicMersenneTwisterRandom(int seed) {
        this.seeds.add(seed);
        
        for (int i = 1; i < SEED_COUNT; i++) {
            final int seedFill = 1812433253 * (seed ^ (seed >> 30)) + 1812433253;
            this.seeds.set(i, seedFill);
        }
    }
    
    public LogicMersenneTwisterRandom() {
        int seed = 324876476;
        this.seeds.add(seed);
        
        for (int i = 1; i < SEED_COUNT; i++) {
            final int seedFill = 1812433253 * (seed ^ (seed >> 30)) + 1812433253;
            this.seeds.set(i, seedFill);
        }
    }
    
    public void initialize(int seed) {
        this.idx = 0;
        this.seeds.set(0, seed);
        
        for (int i = 1; i < SEED_COUNT; i++) {
            final int seedFill = 1812433253 * (seed ^ (seed >> 30)) + 1812433253;
            this.seeds.set(i, seedFill);
        }
    }
    
    public void generate() {
        for (int i = 0, j = 0; i < SEED_COUNT; i++, j++) {
            final int combined = ((int) this.seeds.get(i % this.seeds.size()) & 0x7fffffff)
                    + ((int) this.seeds.get(j) & -0x80000000);
            int twist = (combined >> 1) ^ (int) this.seeds.get((i + 396) % this.seeds.size());

            if ((combined & 1) == 1)
                twist ^= -0x66F74F21;

            this.seeds.set(j, twist);
        }
    }
    
    public int nextInt() {
        if (this.idx == 0)
            generate();
        
        int seed = (int) this.seeds.get(this.idx);
        this.idx = (this.idx + 1) % SEED_COUNT;
        
        seed ^= seed >> 11;
        seed ^= ((seed << 7) & -1658038656) ^ (((seed ^ ((seed << 7) & -1658038656)) << 15) & -0x103A0000)
                ^ ((seed ^ ((seed << 7) & -1658038656) ^ (((seed ^ ((seed << 7) & -1658038656)) << 15) & -0x103A0000)) >> 18);
        
        return seed;
    }
    
    public int rand(int max) {
        if (max < 1)
            return 0;
        
        int random = Math.abs(nextInt());
        
        return random % max;
    }
}
