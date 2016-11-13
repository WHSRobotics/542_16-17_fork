package org.whs542.lib;

/**
 * Created by Lucy Wang on 11/11/2016.
 */

public class Functions
{
    public static double calculateDistance(Coordinate current, Coordinate target)
    {
        double distance;
        distance = Math.sqrt(Math.pow(target.getX() - current.getX(), 2) +
                    Math.pow(target.getY() - current.getY(), 2));
        return distance;
    }

    public static double normalizeAngle(double angle){

        if(angle>180){
            angle=angle-360;
        }
        else if(angle<-180){
            angle=angle+360;
        }
        /*
        else {
            angle=angle;
        }
        */
        return angle;

    }

    public static Position transformCoordinates(double[][] dcm, Position vector)
    {
        Position transformedVector;

        double x = dcm[1][1]*vector.getX() + dcm[1][2]*vector.getY() + dcm[1][3]*vector.getZ();
        double y = dcm[2][1]*vector.getX() + dcm[2][2]*vector.getY() + dcm[2][3]*vector.getZ();
        double z = dcm[3][1]*vector.getX() + dcm[3][2]*vector.getY() + dcm[3][3]*vector.getZ();

        transformedVector = new Position(x,y,z);
        return transformedVector;
    }

    public static double cosd(double degree)
    {
        double rad = degree * Math.PI / 180;
        return Math.cos(rad);
    }

    public static double sind(double degree)
    {
        double rad = degree * Math.PI / 180;
        return Math.sin(rad);
    }

    public static Position addPositions(Position pos1, Position pos2)
    {
        Position sum;

        double x = pos1.getX() + pos2.getX();
        double y = pos1.getY() + pos2.getY();
        double z = pos1.getZ() + pos2.getZ();

        sum = new Position(x,y,z);
        return sum;
    }

    public static Position getPosFromCoord(Coordinate coord)
    {
        Position pos;

        double x = coord.getX();
        double y = coord.getY();
        double z = coord.getZ();

        pos = new Position(x,y,z);
        return pos;
    }

    public static double getHeadingFromCoord(Coordinate coord)
    {
        double heading = coord.getHeading();
        return heading;
    }
}
