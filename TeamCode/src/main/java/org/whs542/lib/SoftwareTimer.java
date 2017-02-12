package org.whs542.lib;

/**
 * Created by WHS on 2/11/2017.
 */

public class SoftwareTimer
{
    public static double expirationTime; //in seconds

    public SoftwareTimer()
    {

    }

    public void set(double timerDuration)
    {
        double currentTime = (double) System.currentTimeMillis() / 1000; //time in seconds
        expirationTime = currentTime + timerDuration;
    }

    public boolean isExpired()
    {
        double currentTime = (double) System.currentTimeMillis() / 1000; //time in seconds
        return (currentTime > expirationTime);
    }
}
