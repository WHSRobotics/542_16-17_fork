package org.whs542.lib;

/**
 * Created by Jiangda on 12/5/2016.
 * @deprecated Use {@link Timer} or {@link SoftwareTimer} instead
 */
@Deprecated
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
    //FIXME This doesn't work
    public void pause(double millisec){
        OpModeTimer timer = new OpModeTimer();
        timer.start();
        while(timer.getTime() * 1000 < millisec){}
    }






}
