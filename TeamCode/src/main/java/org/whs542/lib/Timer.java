package org.whs542.lib;

/**
 * Timer object
 */
public class Timer {

    private long time;                          //The amount of time the timer should last for
    private long startTime;                     //The time at which the timer was started
    private long timeWhenPaused;                //The time when the timer was paused
    private long timeInPause;                   //The total time that the timer has spent being paused

    public Timer(double time) {
        this.time = (long) (time*10E9);
    }

    public Timer(long time, boolean init) {
        this.time = (long) (time*10E9);
        if (init) init();
    }

    /**
     * Initializes the timer. Must be run before any other timer methods are called.
     */
    public void init() {
        startTime = System.nanoTime();
    }

    /**
     * Pauses the timer.
     * This does not technically "pause" the timer, time still continues going on,
     * just sets a reference variable
     */
    public void pause() {
        timeWhenPaused = getElapsedTime();
    }

    /**
     * Starts the timer again after it has been stopped
     */
    public void start() {
        timeInPause += getElapsedTime()-timeWhenPaused;
    }

    /**
     * Gets the amount of time the timer has been running for, since init, minus the time spent
     * while paused.
     * @return The time elapsed, in milli-seconds
     */
    private long getElapsedTime() {
        return System.nanoTime() - startTime - timeInPause;
    }

    /**
     * Tells whether or not the timer is elapsed, according to time variable set when the Timer was instantiated
     * @return Whether or not the timer is elapsed
     */
    public boolean isTimerElapsed() {
        return getElapsedTime() >= time;
    }

    /**
     * Returns the time until the timer has elapsed, as a double
     * @return The time until the timer has elapsed, as a double
     */
    public double timeUntilTimerElapsed() {
        return (getElapsedTime() - time)/10E9;
    }

    /**
     * Resets the timer
     */
    public void reset() {
        startTime = System.nanoTime();
        timeInPause = 0;
    }

}
