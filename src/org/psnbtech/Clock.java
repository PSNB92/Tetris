package org.psnbtech;

class Clock {
    private float millisPerCycle;
    private long lastUpdate;
    private int elapsedCycles;
    private float excessCycles;
    private boolean isPaused;

    Clock(float cyclesPerSecond) {
        setCyclesPerSecond(cyclesPerSecond);
        reset();
    }

    void setCyclesPerSecond(float cyclesPerSecond) {
        this.millisPerCycle = (1.0f / cyclesPerSecond) * 1000;
    }

    void reset() {
        this.elapsedCycles = 0;
        this.excessCycles = 0.0f;
        this.lastUpdate = getCurrentTime();
        this.isPaused = false;
    }

    /**
     * Updates the clock stats. The number of elapsed cycles, as well as the
     * cycle excess will be calculated only if the clock is not paused. This
     * method should be called every frame even when paused to prevent any
     * nasty surprises with the delta time.
     */
    void update() {
        //Get the current time and calculate the delta time.
        long currUpdate = getCurrentTime();
        float delta = (float) (currUpdate - lastUpdate) + excessCycles;

        //Update the number of elapsed and excess ticks if we're not paused.
        if (!isPaused) {
            this.elapsedCycles += (int) Math.floor(delta / millisPerCycle);
            this.excessCycles = delta % millisPerCycle;
        }

        //Set the last update time for the next update cycle.
        this.lastUpdate = currUpdate;
    }

    void setPaused(boolean paused) {
        this.isPaused = paused;
    }

    boolean hasElapsedCycleAndDecrement() {
        if (elapsedCycles > 0) {
            this.elapsedCycles--;
            return true;
        }
        return false;
    }

    private static long getCurrentTime() {
        return (System.nanoTime() / 1000000L);
    }
}
