package org.whs542.lib;

/**
 * Class for storing positions on the field
 * @see Coordinate - Alternative class, with heading
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
