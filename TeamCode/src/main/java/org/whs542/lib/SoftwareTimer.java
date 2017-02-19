package org.whs542.lib;

/**
 * Simplified Timer Class
 *
 * @see Timer
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
