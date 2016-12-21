package org.whs542.lib;

/**
 * Created by Lucy Wang on 11/12/2016.
 */

public class Position
{
    double xPos;
    double yPos;
    double zPos;

    public Position(double x, double y, double z)
    {
        xPos = x;
        yPos = y;
        zPos = z;
    }

    public double getX()
    {
        return xPos;
    }

    public double getY() {return yPos;}

    public double getZ()
    {
        return zPos;
    }

    public void setX(double x)
    {
        xPos = x;
    }

    public void setY(double y)
    {
        yPos = y;
    }

    public void setZ(double z)
    {
        zPos = z;
    }
}
