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

    boolean rotateToTargetInProgress;
    boolean driveToTargetInProgress;
    boolean drivingInReverse = false;

    static final double RADIUS_TO_DRIVETRAIN = 365/2; //in mm
    static final double[] DRIVE_TO_TARGET_POWER_LEVEL = {0.125, 0.25, 0.5, 1.0};
    static final double DEADBAND_DRIVE_TO_TARGET = 150; //in mm
    static final double[] DRIVE_TO_TARGET_THRESHOLD = {DEADBAND_DRIVE_TO_TARGET, 300, 600, 1200};
    static final double POWER_ROTATE_TO_TARGET = 0.3;
    static final double DEADBAND_ROTATE_TO_TARGET = 3.5; //in degrees

    static final double CAMERA_TO_BODY_X = 0; //body frame
    static final double CAMERA_TO_BODY_Y = -RADIUS_TO_DRIVETRAIN; //body frame
    static final double CAMERA_TO_BODY_Z = 0; //body frame
    static final double CAMERA_TO_BODY_ANGLE = 90; //Measured CCW from x-body axis

    Coordinate currentCoord; //field frame

    public WHSRobot(HardwareMap robotMap){
        drivetrain = new Drivetrain(robotMap);
        intake = new Intake(robotMap);
        flywheel = new Flywheel(robotMap);
        //capball = new CapballLift(robotMap);
        vuforia = new Vuforia();
        imu = new IMU(robotMap);
    }

    public void driveToTarget(Position targetPos /*field frame*/) {
        Position vectorToTarget = Functions.subtractPositions(targetPos, currentCoord.getPos()); //field frame
        vectorToTarget = field2body(vectorToTarget); //body frame

        double distanceToTarget = Functions.calculateMagnitude(vectorToTarget);

        double degreesToRotate = Math.atan2(targetPos.getY(), targetPos.getX()); //from -pi to pi rad
        degreesToRotate = degreesToRotate * 180 / Math.PI;
        double targetHeading = Functions.normalizeAngle(currentCoord.getHeading() + degreesToRotate); //-180 to 180 deg
        rotateToTarget(targetHeading);

        if (rotateToTargetInProgress) {
            //if rotating, do nothing
        } else {
            if (distanceToTarget > DRIVE_TO_TARGET_THRESHOLD[3]) { //TODO:fix a stop value because it will never hit 0
                drivetrain.setRightPower(DRIVE_TO_TARGET_POWER_LEVEL[3]);
                drivetrain.setLeftPower(DRIVE_TO_TARGET_POWER_LEVEL[3]);
                driveToTargetInProgress = true;
            }
            else if (distanceToTarget > DRIVE_TO_TARGET_THRESHOLD[2]) {
                drivetrain.setRightPower(DRIVE_TO_TARGET_POWER_LEVEL[2]);
                drivetrain.setLeftPower(DRIVE_TO_TARGET_POWER_LEVEL[2]);
                driveToTargetInProgress = true;
            }
            else if (distanceToTarget > DRIVE_TO_TARGET_THRESHOLD[1]) {
                drivetrain.setRightPower(DRIVE_TO_TARGET_POWER_LEVEL[1]);
                drivetrain.setLeftPower(DRIVE_TO_TARGET_POWER_LEVEL[1]);
                driveToTargetInProgress = true;
            }
            else if (distanceToTarget > DRIVE_TO_TARGET_THRESHOLD[0]) {
                drivetrain.setRightPower(DRIVE_TO_TARGET_POWER_LEVEL[0]);
                drivetrain.setLeftPower(DRIVE_TO_TARGET_POWER_LEVEL[0]);
                driveToTargetInProgress = true;
            }
            else {
                drivetrain.setRightPower(0.0);
                drivetrain.setLeftPower(0.0);
                driveToTargetInProgress = false;
            }
        }
    }

    public void rotateToTarget(double targetHeading /*-180 to 180 deg*/){

        double angleToTarget = targetHeading-currentCoord.getHeading();
        angleToTarget=Functions.normalizeAngle(angleToTarget); //-180 to 180 deg

        if(angleToTarget<-DEADBAND_ROTATE_TO_TARGET){
            drivetrain.setLeftPower(POWER_ROTATE_TO_TARGET);
            drivetrain.setRightPower(-POWER_ROTATE_TO_TARGET);
            rotateToTargetInProgress = true;
        }
        else if(angleToTarget>DEADBAND_ROTATE_TO_TARGET)
        {
            drivetrain.setLeftPower(-POWER_ROTATE_TO_TARGET);
            drivetrain.setRightPower(POWER_ROTATE_TO_TARGET);
            rotateToTargetInProgress = true;
        }
        else{
            drivetrain.setLeftPower(0.0);
            drivetrain.setRightPower(0.0);
            rotateToTargetInProgress = false;
        }
    }

    public void estimatePosition()
    {
        if(vuforia.vuforiaIsValid())
        {
            Coordinate vuforiaCoord = vuforia.getHeadingAndLocation(); //coordinate of camera in field frame
            vuforiaCoord = getBodyCoordFromVuforiaCoord(vuforiaCoord); //coordinate of body in field frame
            Position currentPos = vuforiaCoord.getPos(); //field frame
            currentCoord.setPos(currentPos); //field frame

        }
        else
        {
            //using encoders to estimate position from original location
            double[] encoderValues = drivetrain.getEncoderDistance();
            double encoderPosL = encoderValues[0];
            double encoderPosR = encoderValues[1];
            double deltaPosX;
            double deltaPosY;

            if(drivetrain.isLeftRightEqual(encoderPosL, encoderPosR))
            {
                deltaPosX = 0.5 * (encoderPosL + encoderPosR);
                deltaPosY = 0;
            }
            else
            {
                double turnRadiusL = (2 * RADIUS_TO_DRIVETRAIN * encoderPosL) / (encoderPosR - encoderPosL);
                double turnAngle = (encoderPosR - encoderPosL) / (2 * RADIUS_TO_DRIVETRAIN);

                deltaPosX = (turnRadiusL + RADIUS_TO_DRIVETRAIN) * Math.sin(turnAngle);
                deltaPosY = (turnRadiusL + RADIUS_TO_DRIVETRAIN) * (1 - Math.cos(turnAngle));
            }

            double x = currentCoord.getX() + deltaPosX;//encoder values in the right movement
            double y = currentCoord.getY() + deltaPosY;

            Position deltaPos = new Position(x, y, 0); //body frame
            deltaPos = body2field(deltaPos); //field frame

            currentCoord.setX(deltaPos.getX()); //field frame
            currentCoord.setY(deltaPos.getY()); //field frame
        }

    }

    public void estimateHeading()
    {
        double currentHeading;

        if(vuforia.vuforiaIsValid()){
            currentHeading=vuforia.getHeadingAndLocation().getHeading();
            imu.setImuBias(currentHeading);
            currentCoord.setHeading(currentHeading);
        }
        else {
            currentHeading = Functions.normalizeAngle(imu.getHeading() + imu.getImuBias()); //-180 to 180 deg
            currentCoord.setHeading(currentHeading);
        }
    }

    public Coordinate getBodyCoordFromVuforiaCoord(Coordinate vuforiaCoord /*field frame*/)
    {
        Coordinate bodyCoord;

        Position vuforiaPos = Functions.getPosFromCoord(vuforiaCoord); //field frame

        Position cameraToBodyPos = new Position(CAMERA_TO_BODY_X, CAMERA_TO_BODY_Y, CAMERA_TO_BODY_Z); //body frame
        cameraToBodyPos = body2field(cameraToBodyPos); //field frame

        Position bodyPos = Functions.addPositions(vuforiaPos, cameraToBodyPos); //field frame

        double heading = vuforiaCoord.getHeading() - CAMERA_TO_BODY_ANGLE; //field frame

        bodyCoord = new Coordinate(bodyPos, heading); //field frame
        return bodyCoord;
    }

    public Position body2field(Position bodyVector)
    {
        Position fieldVector;
        double heading = currentCoord.getHeading();

        double[][] C_b2f = {{Functions.cosd(heading),  -Functions.sind(heading),  0},
                            {Functions.sind(heading),   Functions.cosd(heading),  0},
                            {0,                         0,                        1}};

        fieldVector = Functions.transformCoordinates(C_b2f,bodyVector);
        return fieldVector;

    }

    public Position field2body(Position fieldVector)
    {
        Position bodyVector;
        double heading = currentCoord.getHeading();

        double[][] C_f2b = {{ Functions.cosd(heading),   Functions.sind(heading),  0},
                            {-Functions.sind(heading),   Functions.cosd(heading),  0},
                            { 0,                         0,                        1}};

        bodyVector = Functions.transformCoordinates(C_f2b,fieldVector);
        return bodyVector;

    }

    public Position front2back(Position frontVector)
    {
        Position backVector;
        double heading = 180;

        double[][] C_f2b = {{ -1,  0, 0},
                            {  0, -1, 0},
                            {  0,  0, 1}};

        backVector = Functions.transformCoordinates(C_f2b,frontVector);
        return backVector;
    }
}
