package org.whs542.ftc2017.subsys;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whs542.lib.Coordinate;
import org.whs542.lib.Functions;

/**
 * Created by Amar2 on 10/22/2016.
 */
public class WHSRobot
{
    public Drivetrain drivetrain;
    public Intake intake;
    public Flywheel flywheel;
    public Vuforia vuforia;
    public IMU imu;

    static final double POWER_DRIVE_TO_TARGET = 1.0;
    static final double POWER_ROTATE_TO_TARGET = 0.5;
    static final double DEADBAND_ROTATE_TO_TARGET = 3.5;

    public WHSRobot(HardwareMap robotMap){
        drivetrain = new Drivetrain(robotMap);
        intake = new Intake(robotMap);
        flywheel = new Flywheel(robotMap);
        vuforia = new Vuforia();
        imu = new IMU(robotMap);
    }

    public void driveToTarget(Coordinate targetPos, Coordinate currentPos){

        double distanceToTarget = Functions.calculateDistance(targetPos, currentPos);

        if(distanceToTarget>0){
            drivetrain.setRightPower(POWER_DRIVE_TO_TARGET);
            drivetrain.setLeftPower(POWER_DRIVE_TO_TARGET);
        }
        else{
            drivetrain.setLRPower(0.0, 0.0);
        }

    }

    public void rotateToTarget(double targetHeading, double currentHeading){

        double angleToTarget = targetHeading-currentHeading;
        angleToTarget=Functions.normalizeAngle(angleToTarget);

        if(angleToTarget<-DEADBAND_ROTATE_TO_TARGET){
            drivetrain.setLeftPower(POWER_ROTATE_TO_TARGET);
            drivetrain.setRightPower(-POWER_ROTATE_TO_TARGET);
        }
        else if(angleToTarget>DEADBAND_ROTATE_TO_TARGET){
            drivetrain.setLeftPower(-POWER_ROTATE_TO_TARGET);
            drivetrain.setRightPower(POWER_ROTATE_TO_TARGET);
        }
        else{
            drivetrain.setLeftPower(0.0);
            drivetrain.setRightPower(0.0);
        }

    }

    public Coordinate estimatePosition(Coordinate vuforiaCoordinate, double[] positionEnc)
    {
        Coordinate currentPos;

        if(vuforia.vuforiaIsValid())
        {
            currentPos = vuforia.getHeadingAndLocation();
        }
        else
        {

        }

    }
}
