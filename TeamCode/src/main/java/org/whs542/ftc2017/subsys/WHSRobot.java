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

    Coordinate oldCoord;

    public WHSRobot(HardwareMap robotMap){
        drivetrain = new Drivetrain(robotMap);
        intake = new Intake(robotMap);
        flywheel = new Flywheel(robotMap);
        //capball = new CapballLift(robotMap);
        vuforia = new Vuforia();
        imu = new IMU(robotMap);
    }

    public boolean driveToTarget(Coordinate targetPos, Coordinate currentPos){

        double distanceToTarget = Functions.calculateDistance(targetPos, currentPos);
        boolean status;

        if(distanceToTarget>0){ //TODO:fix a stop value because it will never hit 0
            drivetrain.setRightPower(POWER_DRIVE_TO_TARGET);
            drivetrain.setLeftPower(POWER_DRIVE_TO_TARGET);
            status = false;
        }
        else {
            drivetrain.setLRPower(0.0, 0.0);
            status = true;
        }
        return status;
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

    public void setOldCoord(Coordinate oldCoordIn){
        oldCoord = oldCoordIn;
    }


    //Method setOldCoord MUST be run before this
    public Coordinate estimatePosition(Coordinate vuforiaCoordinate, double encoderDistL, double encoderDistR)
    {
        Coordinate currentCoord = new Coordinate(0,0,0,0);

        if(vuforia.vuforiaIsValid())
        {
            currentCoord = vuforia.getHeadingAndLocation(); //Vuforia frame (camera frame)
            currentCoord = vuforia2body(currentCoord);
        }
        else
        {
            //using encoders to estimate position

        }

        return currentCoord;

    }

    /**
     * Method to find to check if the value from Vuforia is valid
     * If it is not, it returns the value from the IMU instead
     *
     * Method imu.setImuBias() MUST be run before this
     *
     * @param vuforiaCoordinate The coordinate from Vuforia. This should be the RAW coordinate,
     *                          with no normalization done to the angles
     * @return The heading, either from Vuforia or the IMU. This is a normalized angle
     */
    public double estimateHeading(Coordinate vuforiaCoordinate)
    {
        double currentHeading;

        if(vuforia.vuforiaIsValid()){
            currentHeading=Functions.normalizeAngle(vuforia.getHeadingAndLocation().getHeading());
        }
        else {
            currentHeading=Functions.normalizeAngle(Math.abs(imu.getHeading()+imu.getImuBias()));
        }

        return currentHeading;

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
