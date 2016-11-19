package org.whs542.lib;

/**
 * Created by Jiangda on 10/22/2016.
 */
public class Coordinate {
    private double xPos;
    private double yPos;
    private double zPos;
    private double orientation;

    public Coordinate(double xPosition, double yPosition, double zPosition, double orientationInput){
        xPos = xPosition;
        yPos = yPosition;
        zPos = zPosition;
        orientation = orientationInput;
    }

    public Coordinate(Position pos, double heading)
    {
        xPos = pos.getX();
        yPos = pos.getY();
        zPos = pos.getZ();
        orientation = heading;
    }

    public Coordinate returnCoord(){
        return this;
    }

    public double getX()
    {
        return xPos;
    }

    public double getY()
    {
        return yPos;
    }

    public double getZ()
    {
        return zPos;
    }

    public double getHeading()
    {
        return orientation;
    }

    public Position getPos()
    {
        Position pos = new Position(xPos,yPos,zPos);
        return pos;
    }

    public void setPos(Position pos)
    {
        xPos = pos.getX();
        yPos = pos.getY();
        zPos = pos.getZ();
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

    public void setHeading(double heading)
    {
        orientation = heading;
    }
}
