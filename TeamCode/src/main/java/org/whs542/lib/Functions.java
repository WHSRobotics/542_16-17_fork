package org.whs542.lib;

/**
 * Created by Lucy Wang on 11/11/2016.
 */

public class Functions
{
    public static double calculateDistance(Position current, Position target)
    {
        double distance;
        distance = Math.sqrt(Math.pow(target.getX() - current.getX(), 2) +
                    Math.pow(target.getY() - current.getY(), 2));
        return distance;
    }

    /** Converts angles from 0-360 to -180-180 */
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

        double x = dcm[0][0]*vector.getX() + dcm[0][1]*vector.getY() + dcm[0][2]*vector.getZ();
        double y = dcm[1][0]*vector.getX() + dcm[1][1]*vector.getY() + dcm[1][2]*vector.getZ();
        double z = dcm[2][0]*vector.getX() + dcm[2][1]*vector.getY() + dcm[2][2]*vector.getZ();

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

    public static Position subtractPositions(Position pos1, Position pos2)
    {
        Position difference;

        double x = pos1.getX() - pos2.getX();
        double y = pos1.getY() - pos2.getY();
        double z = pos1.getZ() - pos2.getZ();

        difference = new Position(x,y,z);
        return difference;
    }

    public static double calculateMagnitude(Position pos)
    {
        double magnitude = Math.pow(pos.getX(), 2) + Math.pow(pos.getY(), 2);
        magnitude = Math.sqrt(magnitude);
        return magnitude;
    }

    /**Converts Coordinates to Positions */
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

    private static final double RADIUS_OF_WHEEL = 50;
    private static final double CIRC_OF_WHEEL = RADIUS_OF_WHEEL * 2 * Math.PI;
    private static final double ENCODER_TICKS_PER_REV = 1120;
    //private static final double CALIBRATION_FACTOR = 72 / 72.25 * 24 / 24.5 * 0.5;
    private static final double CALIBRATION_FACTOR = 72 / 72.25 * 24 / 24.5 * 0.73;
    private static final double ENCODER_TICKS_PER_MM = CALIBRATION_FACTOR * ENCODER_TICKS_PER_REV / CIRC_OF_WHEEL;

    public static double encToMM(double encoderTicks)
    {
        return encoderTicks * (1/ENCODER_TICKS_PER_MM);
    }

    public static double mmToEnc(double MM)
    {
        return MM * ENCODER_TICKS_PER_MM;
    }
}
