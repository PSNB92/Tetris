package org.psnbtech;

class Clock {
    private float millisecondsPerCycle;
    private long lastUpdate;
    private int elapsedCycles;
    private float excessCycles;
    private boolean isPaused;

    Clock(float cyclesPerSecond) {
        setCyclesPerSecond(cyclesPerSecond);
        reset();
    }

    void setCyclesPerSecond(float cyclesPerSecond) {
        millisecondsPerCycle = 1000f / cyclesPerSecond;
    }

    void reset() {
        elapsedCycles = 0;
        excessCycles = 0;
        lastUpdate = getCurrentTime();
        isPaused = false;
    }

    void update() {
        long currentTime = getCurrentTime();
        float delta = (float) (currentTime - lastUpdate) + excessCycles;

        if (!isPaused) {
            elapsedCycles += (int) Math.floor(delta / millisecondsPerCycle);
            excessCycles = delta % millisecondsPerCycle;
        }

        lastUpdate = currentTime;
    }

    void setPaused(boolean paused) {
        isPaused = paused;
    }

    boolean hasElapsedCycleAndDecrement() {
        return elapsedCycles > 0;
    }

    private static long getCurrentTime() {
        return (System.nanoTime() / 1000000L);
    }
}
