package org.whs542.ftc2017.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.*;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whs542.lib.Coordinate;
import org.whs542.lib.Functions;
import org.whs542.lib.Toggler;

/**
 * Created by Amar2 on 10/22/2016.
 */
public class Drivetrain {

    private DcMotor frontLeft;
    private DcMotor frontRight;
    private DcMotor backLeft;
    private DcMotor backRight;

    private Toggler orientationSwitch = new Toggler(2);

    //All measurements in millimeters because that is the unit Vuforia uses
    private final double RADIUS_OF_WHEEL = 50;
    private final double CIRC_OF_WHEEL = RADIUS_OF_WHEEL * 2 * Math.PI;
    private final double ENCODER_TICKS_PER_REV = 1120;
    private final double ENCODER_TICKS_PER_MM = ENCODER_TICKS_PER_REV / CIRC_OF_WHEEL;
    private static final double JOY_THRESHOLD = 0.05;

    private final double MIN_POWER_VALUE = 0.15;


    public Drivetrain (HardwareMap driveMap)
    {
        frontRight = driveMap.dcMotor.get("drive_fr");
        frontLeft = driveMap.dcMotor.get("drive_fl");
        backRight = driveMap.dcMotor.get("drive_br");
        backLeft = driveMap.dcMotor.get("drive_bl");

        frontRight.setZeroPowerBehavior( ZeroPowerBehavior.BRAKE );
        frontLeft.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
        backRight.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
        backLeft.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);

        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);
        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
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
        double rightScaledPower = Math.abs(rightPower) > JOY_THRESHOLD ? Math.pow(rightPower,3) : 0.0;
        double leftScaledPower = Math.abs(leftPower) > JOY_THRESHOLD ? Math.pow(leftPower,3) : 0.0;

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
    public void move(Coordinate target, Vuforia vuforia, IMU imu){
        Coordinate current = vuforia.getHeadingAndLocation();

        double distance = Functions.calculateDistance(current, target);

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


        //Move robot forward the calculated distance, using IMU as check
        this.moveDistanceMilli(distance, imu);



    }

    //Moves a certain distance forwards or backwards using encoders. Includes IMU as check. Negative = backwards.
    public void moveDistanceMilli(double distanceMM, IMU imu){

        int encoderPosition = (int) (24 / 24.5 * 0.5 * distanceMM * ENCODER_TICKS_PER_MM);

        this.setRunMode( RunMode.STOP_AND_RESET_ENCODER );
        this.setRunMode( RunMode.RUN_TO_POSITION );
        this.setMaxSpeed(4000);
        this.setTargetPosition(encoderPosition);

        while( Math.abs(getEncoderPosition() )
            < Math.abs( encoderPosition )) {

            this.setLRPower(0.3, 0.3);

            //If the acceleration measured by the accelerometer exceeds a certain threshold, indicating
            //that the robot slammed into something, stop the robot.
            if( imu.getAccelerationMag() > 10.0 ){
                this.setLRPower(0, 0);
                System.exit(0);
            }
        }
    }

    public void moveDistanceMilli2(double distanceMM, IMU imu)
    {
        int targetPosition =   (int) (24 / 24.5 * 0.5 * distanceMM * ENCODER_TICKS_PER_MM);

        this.setRunMode( RunMode.STOP_AND_RESET_ENCODER );
        this.setRunMode(RunMode.RUN_TO_POSITION);
        this.setMaxSpeed(4000);
        this.setTargetPosition(targetPosition);
        int stage = 0;
        while(Math.abs(targetPosition) - Math.abs(getEncoderPosition()) > 50)
        {
            double distanceToGo = Math.abs(getDistanceToGo(targetPosition)) - Math.abs(getDistanceToGo(getEncoderPosition()));

            switch (stage) {
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
                    break;

            }

            //If the acceleration measured by the accelerometer exceeds a certain threshold, indicating
            //that the robot slammed into something, stop the robot.
            if( imu.getAccelerationMag() > 10.0 ){
                this.setLRPower(0, 0);
            }
        }
    }


    //Tells the robot how much left (positive value) or right (negative) to turn based on the initial heading, from 0
    //to 359.9, and the final heading, also from 0 to 360. Accounts for the jump from 359.9 to 0.
    public void turn( double destinationDegrees, double currentDegrees, IMU imu){
        this.setRunMode( RunMode.RUN_WITHOUT_ENCODER );
        double difference = turnValue(destinationDegrees, currentDegrees);
        double differenceAbs = Math.abs( difference );
        double dir = difference / differenceAbs;

        boolean turning = true;
        double heading = imu.getHeading();
        double amtToTurn = this.turnValue(destinationDegrees, heading);
        //Stops turning when the robot is within 1.5 degrees of target heading
        if( Math.abs( amtToTurn ) < 1.5 ){
            turning = false;
        }
        //Turn robot loop
        while( turning ){
            //Don't worry about this line. It basically scales down the motor power so when the robot finishes
            //Turning it should be barely moving and not stop abruptly.
            this.setLRPower( -( 1 - MIN_POWER_VALUE ) * amtToTurn / differenceAbs - dir * MIN_POWER_VALUE,
            ( 1 - MIN_POWER_VALUE ) * amtToTurn / differenceAbs + dir * MIN_POWER_VALUE);

            heading = imu.getHeading();
            amtToTurn = this.turnValue(destinationDegrees, heading);
            //Stops turning when the robot is within 1.5 degrees of target heading
            if( Math.abs( amtToTurn ) < 1.5 ){
                turning = false;
            }
        }
    }


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


    public double getEncoderPosition()
    {
        double position = frontRight.getCurrentPosition() + frontLeft.getCurrentPosition() + backRight.getCurrentPosition() + backLeft.getCurrentPosition();
        return position * 0.25;
    }
    //Converts an encoder value to a distance in millimeters.
    public double getDistanceToGo(double encoderValue){
        double distanceToGo = encoderValue * (1 / ENCODER_TICKS_PER_MM);
        return distanceToGo;
    }
}

