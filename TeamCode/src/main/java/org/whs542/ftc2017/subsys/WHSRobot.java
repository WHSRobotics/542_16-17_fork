package org.whs542.ftc2017.subsys;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whs542.lib.Coordinate;
import org.whs542.lib.Functions;
import org.whs542.lib.Position;

/**
 * Created by Amar2 on 10/22/2016.
 */
public class WHSRobot
{
    public Drivetrain drivetrain;
    public Intake intake;
    public Flywheel flywheel;
    public CapballLift capball;
    public Vuforia vuforia;
    public IMU imu;

    static final double POWER_DRIVE_TO_TARGET = 1.0;
    static final double POWER_ROTATE_TO_TARGET = 0.5;
    static final double DEADBAND_ROTATE_TO_TARGET = 3.5;

    static final double CAMERA_TO_BODY_X = 225;
    static final double CAMERA_TO_BODY_Y = 0;
    static final double CAMERA_TO_BODY_Z = 0;
    static final double CAMERA_TO_BODY_ANGLE = 180; //Measured CCW from x-body axis

    public double heading;

    public WHSRobot(HardwareMap robotMap){
        drivetrain = new Drivetrain(robotMap);
        intake = new Intake(robotMap);
        flywheel = new Flywheel(robotMap);
        capball = new CapballLift(robotMap);
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

    public Coordinate estimatePosition(Coordinate vuforiaCoordinate, double encoderDistL, double encoderDistR)
    {
        Coordinate currentCoord;

        if(vuforia.vuforiaIsValid())
        {
            currentCoord = vuforia.getHeadingAndLocation(); //Vuforia frame (camera frame)
            currentCoord = vuforia2body(currentCoord);
        }
        else
        {
            //estimate using encoders
        }

        return currentCoord;

    }

    public double estimateHeading(Coordinate vuforiaCoordinate, IMU imu)
    {
        
    }

    public Coordinate vuforia2body(Coordinate vuforiaCoord)
    {
        Coordinate bodyCoord;

        Position vuforiaPos = Functions.getPosFromCoord(vuforiaCoord);
        Position cameraToBodyPos = new Position(CAMERA_TO_BODY_X, CAMERA_TO_BODY_Y, CAMERA_TO_BODY_Z);

        cameraToBodyPos = body2field(cameraToBodyPos);

        Position bodyPos = Functions.addPositions(vuforiaPos, cameraToBodyPos);

        double heading = vuforiaCoord.getHeading() - CAMERA_TO_BODY_ANGLE;

        bodyCoord = new Coordinate(bodyPos, heading);
        return bodyCoord;
    }

    public Position body2field(Position bodyVector)
    {
        Position fieldVector;
        double heading = 0; //TODO: EDIT THIS AFTER WRITING ESTIMATEHEADING()

        double[][] C_b2f = {{Functions.cosd(heading),  -Functions.sind(heading),  0},
                            {Functions.sind(heading),   Functions.cosd(heading),  0},
                            {0,                         0,                        1}};

        fieldVector = Functions.transformCoordinates(C_b2f,bodyVector);
        return fieldVector;

    }

    public Position field2body(Position fieldVector)
    {
        Position bodyVector;
        double heading = 0; //TODO: EDIT THIS AFTER WRITING ESTIMATEHEADING()

        double[][] C_b2f = {{ Functions.cosd(heading),   Functions.sind(heading),  0},
                            {-Functions.sind(heading),   Functions.cosd(heading),  0},
                            { 0,                         0,                        1}};

        bodyVector = Functions.transformCoordinates(C_b2f,fieldVector);
        return bodyVector;

    }
}
