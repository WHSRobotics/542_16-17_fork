package org.whs542.lib;

/**
 * Created by Jiangda on 12/5/2016.
 */
public class OpModeTimer {
    private double time;
    private double startTime;

    public OpModeTimer()
    {
        time = 0;
    }

    public void start()
    {
        startTime = (double) System.nanoTime() / 10E9;
    }

    public double getTime()
    {
        time = (double) System.nanoTime() / 10E9 - startTime;
        return time;
    }

    public void reset()
    {
        startTime = (double) System.nanoTime() / 10E9;
    }

}
