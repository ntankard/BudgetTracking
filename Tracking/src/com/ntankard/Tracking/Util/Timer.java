package com.ntankard.Tracking.Util;

public class Timer {

    /**
     * The start time recorded when the object was created
     */
    private long start;

    /**
     * Constructor, state the timer
     */
    public Timer() {
        this.start = System.currentTimeMillis();
    }

    /**
     * Print the time since the object was constructed or the last time stopPrint was called
     */
    public void stopPrint() {
        stopPrint("");
    }

    /**
     * Print the time since the object was constructed or the last time stopPrint was called
     *
     * @param string A message to add
     */
    public void stopPrint(String string) {
        long end = System.currentTimeMillis();
        long delta = end - start;
        System.out.println(string + ": " + delta);
        start = System.currentTimeMillis();
    }
}
