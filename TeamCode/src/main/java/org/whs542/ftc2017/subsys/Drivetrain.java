package org.whs542.ftc2017.subsys;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.*;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whs542.lib.*;

/**
 * Drivetrain Subsystem Class
 */

public class Drivetrain {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    public double[] encoderValues = {0, 0};
    public final double DEADBAND_ENCODERS = 5; //TODO: test this value

    private Toggler orientationSwitch = new Toggler(2);

    //All measurements in millimeters because that is the unit Vuforia uses
    private final double RADIUS_OF_WHEEL = 50;
    private final double CIRC_OF_WHEEL = RADIUS_OF_WHEEL * 2 * Math.PI;
    private final double ENCODER_TICKS_PER_REV = 1120;
    private final double CALIBRATION_FACTOR = 72 / 72.25 * 24 / 24.5 * 0.5;
    private final double ENCODER_TICKS_PER_MM = CALIBRATION_FACTOR * ENCODER_TICKS_PER_REV / CIRC_OF_WHEEL;


    private static final double JOY_THRESHOLD = 0.05;

    private final double MIN_POWER_VALUE = 0.15;
    private final double MM_PER_MAT = 594;

    private final double DEADBAND_MOVE_DISTANCE_MILLI = 40 * ENCODER_TICKS_PER_MM;


    public Drivetrain (HardwareMap driveMap)
    {
        frontRight = driveMap.dcMotor.get("drive_fr");
        frontLeft = driveMap.dcMotor.get("drive_fl");
        backRight = driveMap.dcMotor.get("drive_br");
        backLeft = driveMap.dcMotor.get("drive_bl");

        frontRight.setZeroPowerBehavior( ZeroPowerBehavior.BRAKE);
        frontLeft.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);

        frontLeft.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeft.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void setRunMode( RunMode theMode )
    {
        frontLeft.setMode(theMode);
        frontRight.setMode(theMode);
        backLeft.setMode(theMode);
        backRight.setMode(theMode);
    }

    public void setMaxSpeed(int maxSpeed)
    {
        frontLeft.setMaxSpeed(maxSpeed);
        frontRight.setMaxSpeed(maxSpeed);
        backLeft.setMaxSpeed(maxSpeed);
        backRight.setMaxSpeed(maxSpeed);
    }

    //Set power methods
    public void setRightPower(double power)
    {
        frontRight.setPower(power);
        backRight.setPower(power);
    }

    public void setLeftPower(double power)
    {
        frontLeft.setPower(power);
        backLeft.setPower(power);
    }

    public void setLRPower(double leftPower, double rightPower)
    {
        switch(orientationSwitch.currentState())
        {
            case 0:
                setRightPower(rightPower);
                setLeftPower(leftPower);
                break;
            case 1:
                setRightPower(-rightPower);
                setLeftPower(-leftPower);
                break;
        }
    }

    //A set power method using the cubic function
    public void setLRScaledPower(double leftPower, double rightPower)
    {
        double rightScaledPower = Math.abs(rightPower) > JOY_THRESHOLD ? Math.pow(rightPower, 3) : 0.0;
        double leftScaledPower = Math.abs(leftPower) > JOY_THRESHOLD ? Math.pow(leftPower, 3) : 0.0;

        switch(orientationSwitch.currentState())
        {
            case 0:
                setLeftPower(rightScaledPower);
                setRightPower(leftScaledPower);
                break;
            case 1:
                setRightPower(-rightScaledPower);
                setLeftPower(-leftScaledPower);
                break;
        }
    }

    public double getScaledPower(double power){
        return Math.abs(power) > JOY_THRESHOLD ? Math.pow(power,3) : 0.0;
    }

    //Orientation Switch Methods
    public void setOrientation(boolean trigger)
    {
        orientationSwitch.changeState(trigger);
    }

    public String getOrientation()
    {
        String orientation = "null";
        switch(orientationSwitch.currentState())
        {
            case 0:
                orientation = "Normal";
                break;
            case 1:
                orientation = "Reverse";
                break;
        }
        return orientation;
    }

    public double getFRPower() {
        return frontRight.getPower();
    }

    public double getFLPower(){
        return frontLeft.getPower();
    }

    public double getBRPower(){
        return backRight.getPower();
    }

    public double getBLPower(){
        return backLeft.getPower();
    }

    //Runs all four motors to a certain encoder position; holds all motors actively thereat
    public void setTargetPosition(int position)
    {
        frontLeft.setTargetPosition(position);
        frontRight.setTargetPosition(position);
        backLeft.setTargetPosition(position);
        backRight.setTargetPosition(position);
    }

    //Input: destination coordinate object(values are in millimeters)
    //Output: robot automatically moves to that point on the field.
    /**
     * Moves the robot to a target location
     * @deprecated use {@link WHSRobot#driveToTarget(Position)}
     */
    @Deprecated
    public void move(Coordinate target, Vuforia vuforia, IMU imu){
        Coordinate current = vuforia.getHeadingAndLocation();

        //double distance = Functions.calculateDistance(current, target);

        //Distance to go in x and y units (mm) which can be positive or negative, from current to destination position
        double xPosToGo = target.getX() - current.getX();
        double yPosToGo = target.getY() - current.getY();

        //InitialOrientation is the initial heading that the robot aligns to before moving forward
        double movingOrientation = 180 * Math.atan2( yPosToGo, xPosToGo ) / Math.PI;

        if( movingOrientation < 0 )
        {
            movingOrientation += 360;
        }

        //Turn robot to the desired orientation
        //this.turn(movingOrientation, current.returnCoordSingleValue("heading"), imu);
        //TODO: Add rotateToTarget() here

    }

    //setRunMode MUST be run before this
    //Moves a certain distance forwards or backwards using encoders. Includes IMU as check. Negative = backwards.
    public void moveDistanceMilli2(double distanceMM /*,IMU imu*/)
    {
        int targetPosition = (int) (distanceMM * ENCODER_TICKS_PER_MM);

        //this.setRunMode( RunMode.STOP_AND_RESET_ENCODER);
        this.setRunMode(RunMode.RUN_TO_POSITION);
        this.setMaxSpeed(2100);
        this.setTargetPosition(targetPosition);

        if(Math.abs(targetPosition) - Math.abs(getRightEncoderPosition()) > DEADBAND_MOVE_DISTANCE_MILLI | Math.abs(targetPosition) - Math.abs(getLeftEncoderPosition()) > DEADBAND_MOVE_DISTANCE_MILLI){
            setLRPower(0.5, 0.5);
        }
        else {
            setLRPower(0.0, 0.0);
        }


        //imu.zeroHeading();
        /*
        if(Math.abs(targetPosition) - Math.abs(getRightEncoderPosition()) > 50 | Math.abs(targetPosition) - Math.abs(getLeftEncoderPosition()) > 50)
        {
            double distanceToGo = Math.abs(distanceMM) - Math.abs(getDistanceToGo(getEncoderPosition()));
            //double headingError = imu.getHeading();

            DbgLog.msg("Distance to go in mm:" + Double.toString(distanceToGo));
            DbgLog.msg("FrontRight encoder:" + Double.toString(frontRight.getCurrentPosition()));
            if( distanceToGo > MM_PER_MAT * 3){
                this.setLRPower(1, 1);
            }
            else if (distanceToGo <= MM_PER_MAT * 3 & distanceToGo > MM_PER_MAT * 2) {
                double powerScale = 0.5 * (distanceToGo - MM_PER_MAT * 2) / MM_PER_MAT;
                this.setLRPower(0.3 + powerScale, 0.3 + powerScale);
            }
            else if (distanceToGo <= MM_PER_MAT * 2 & distanceToGo > MM_PER_MAT){
                double powerScale2 = 0.4 * (distanceToGo - MM_PER_MAT ) / MM_PER_MAT;
                this.setLRPower(0.3 + powerScale2, 0.3 + powerScale2);
            }
            else if (distanceToGo <= MM_PER_MAT & distanceToGo > DEADBAND_MOVE_DISTANCE_MILLI){
                this.setLRPower(0.3, 0.3);
            }
            else if(distanceToGo < DEADBAND_MOVE_DISTANCE_MILLI){
                this.setLRPower(0, 0);
            }
         }
         */
            /*switch (stage) {
                case 0:
                    while (distanceToGo > 1220) {
                        this.setLRPower(1, 1);
                    }
                    stage = 1;
                    break;
                case 1:
                    while (distanceToGo > 610) {
                        this.setLRPower(0.3, 0.3);
                    }
                    stage = 2;
                    break;
                case 2:
                    while (distanceToGo <= 610) {
                        this.setLRPower(0.1, 0.1);
                    }
                    stage = 3;
                    break;
                case 3:
                    this.setLRPower(0.0, 0.0);

            }*/
            /*
            //Stop and turn
            if(Math.abs(headingError) > 1){
                this.setLRPower(0, 0);
                this.turn(0, headingError, imu);
            }
            //If the acceleration measured by the accelerometer exceeds a certain threshold, indicating
            //that the robot slammed into something, stop the robot.
            if(imu.getAccelerationMag() > 10.0){
                this.setLRPower(0, 0);
            }
            */

    }


    //Tells the robot how much left (positive value) or right (negative) to turn based on the initial heading, from 0
    //to 359.9, and the final heading, also from 0 to 360. Accounts for the jump from 359.9 to 0.
    /**
     * The old method for turning a given number of degrees
     * @deprecated use {@link WHSRobot#rotateToTarget(double)}
     */
    @Deprecated
    public void turn( double destinationDegrees, double currentDegrees, IMU imu){
        this.setRunMode(RunMode.RUN_WITHOUT_ENCODER);
        double difference = turnValue(destinationDegrees, currentDegrees);
        double differenceAbs = Math.abs( difference );
        double dir = difference / differenceAbs;

        boolean turning = true;
        double heading = imu.getHeading();
        double amtToTurn = this.turnValue(destinationDegrees, heading);
        //Stops turning when the robot is within 1.5 degrees of target heading
        if( Math.abs( amtToTurn ) < 0.5 ){
            turning = false;
        }
        //Turn robot loop
        while( turning ){
            //Turn the robot at minimum power
            this.setLRPower( -MIN_POWER_VALUE * dir, MIN_POWER_VALUE * dir);

            heading = imu.getHeading();
            amtToTurn = this.turnValue(destinationDegrees, heading);
            //Stops turning when the robot is within 1.5 degrees of target heading
            if( Math.abs( amtToTurn ) < 0.5 ){
                turning = false;
            }

        }
        this.setLRPower(0, 0);
    }

    /**
     * A supplementary method for {@link #turn(double, double, IMU)}
     * @deprecated along with {@link #turn(double, double, IMU)} Use {@link WHSRobot#rotateToTarget(double)} instead
     */
    @Deprecated
    public double turnValue(double destinationDegrees, double currentDegrees){
        double difference = destinationDegrees - currentDegrees;
        if( Math.abs( difference ) > 180){
            if( difference < 0){
                difference += 360;
            }
            else{
                difference -= 360;
            }
        }
        return difference;
    }


    public double[] getEncoderDistance()
    {
        double currentLeft = getLeftEncoderPosition();
        double currentRight = getRightEncoderPosition();

        double[] encoderDistances = {currentLeft - encoderValues[0], currentRight - encoderValues[1]};

        encoderValues[0] = currentLeft;
        encoderValues[1] = currentRight;

        return encoderDistances;
    }

    public double getEncoderPosition() {
        double position = frontRight.getCurrentPosition() + frontLeft.getCurrentPosition() + backRight.getCurrentPosition() + backLeft.getCurrentPosition();
        return position * 0.25;
    }
    //Converts an encoder value to a distance in millimeters.
    public double getDistanceToGo(double encoderValue) {
        double distanceToGo = encoderValue * (1 / ENCODER_TICKS_PER_MM);
        return distanceToGo;
    }

    public double getRightEncoderPosition()
    {
        double rightTotal = backRight.getCurrentPosition() + frontRight.getCurrentPosition();
        return rightTotal * 0.5;
    }

    public double getLeftEncoderPosition()
    {
        double leftTotal = backLeft.getCurrentPosition() +frontLeft.getCurrentPosition();
        return leftTotal * 0.5;
    }

    public boolean isLeftRightEqual(double leftVal, double rightVal)
    {
        boolean equal;

        if(leftVal == 0 && rightVal == 0)
        {
            equal = true;
        }
        else
        {
            double percentDifference;
            if(Math.abs(rightVal) > Math.abs(leftVal)) {
                percentDifference = 100* Math.abs(leftVal - rightVal) / Math.abs(rightVal);
            }
            else
            {
                percentDifference = 100* Math.abs(leftVal - rightVal) / Math.abs(leftVal);
            }

            if(percentDifference < DEADBAND_ENCODERS)
            {
                equal = true;
            }
            else
            {
                equal = false;
            }
        }
        return equal;

    }
}

