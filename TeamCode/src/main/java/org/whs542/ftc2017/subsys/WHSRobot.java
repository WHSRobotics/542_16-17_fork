package org.whs542.ftc2017.subsys;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whs542.lib.Alliance;
import org.whs542.lib.Coordinate;
import org.whs542.lib.Functions;
import org.whs542.lib.Position;
import org.whs542.lib.Toggler;

public class WHSRobot
{
    public Drivetrain drivetrain;
    public Intake intake;
    public Flywheel flywheel;
    //public CapballLift capball;
    public BeaconPusher pusher;
    public Vuforia vuforia;
    public IMU imu;

    public boolean rotateToTargetInProgress = false;
    public boolean driveToTargetInProgress = false;
    boolean drivingInReverse = false;
    public String s = "";
    public String beaconState = "";

    private Toggler beaconToggle = new Toggler(4);
    private Toggler autoBeaconToggle = new Toggler(2);

    private static final double RADIUS_TO_DRIVETRAIN = 365/2; //in mm
    private static final double[] DRIVE_TO_TARGET_POWER_LEVEL = {0.25, 0.4, 0.5, 1.0};
    private static final double DEADBAND_DRIVE_TO_TARGET = 150; //in mm
    private static final double[] DRIVE_TO_TARGET_THRESHOLD = {DEADBAND_DRIVE_TO_TARGET, 300, 600, 1200};
    private static final double[] ROTATE_TO_TARGET_POWER_LEVEL = {0.25, 0.4, 0.8};
    private static final double DEADBAND_ROTATE_TO_TARGET = 3.5; //in degrees
    private static final double[] ROTATE_TO_TARGET_THRESHOLD = {DEADBAND_ROTATE_TO_TARGET, 30, 60};

    //17.85 /2 is center of robot, at 15 for y
    //16.5 / 2 is center of robot, at 15.75 for x

    static final double CAMERA_TO_BODY_X = 154.305; //body frame
    static final double CAMERA_TO_BODY_Y = -190.5; //body frame
    static final double CAMERA_TO_BODY_Z = 0; //body frame
    static final double CAMERA_TO_BODY_ANGLE = Math.atan(CAMERA_TO_BODY_X/CAMERA_TO_BODY_Y) + 90; //Measured CCW from x-body axis

    public Coordinate currentCoord; //field frame

    public WHSRobot(HardwareMap robotMap, Alliance side){
        drivetrain = new Drivetrain(robotMap);
        intake = new Intake(robotMap);
        flywheel = new Flywheel(robotMap);
        //capball = new CapballLift(robotMap);
        pusher = new BeaconPusher(robotMap, side);

        imu = new IMU(robotMap);
        /*try {
            imu.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
*/
        vuforia = new Vuforia();
        try {
            vuforia.start();
        }
        catch(Exception e){
            e.printStackTrace();
        }
        rotateToTargetInProgress = false;
        driveToTargetInProgress = false;
    }

    public WHSRobot(HardwareMap robotMap)
    {
        drivetrain = new Drivetrain(robotMap);
        intake = new Intake(robotMap);
        flywheel = new Flywheel(robotMap);
        //capball = new CapballLift(robotMap);
        //pusher = new BeaconPusher(robotMap, side);
        imu = new IMU(robotMap);

        vuforia = new Vuforia();
        try {
            vuforia.start();
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }
    }

    public void driveToTarget(Position targetPos /*field frame*/) {
        estimatePosition();
        estimateHeading();

        Position vectorToTarget = Functions.subtractPositions(targetPos, currentCoord.getPos()); //field frame
        vectorToTarget = field2body(vectorToTarget); //body frame

        double distanceToTarget = Functions.calculateMagnitude(vectorToTarget);

        //TODO: CHECK LOGIC FOR THIS
        double degreesToRotate = Math.atan2(targetPos.getY(), targetPos.getX()); //from -pi to pi rad
        degreesToRotate = degreesToRotate * 180 / Math.PI;
        double targetHeading = Functions.normalizeAngle(currentCoord.getHeading() + degreesToRotate); //-180 to 180 deg
        rotateToTarget(targetHeading);

        if (rotateToTargetInProgress) {
            //if rotating, do nothing
        }
        else {
            if(driveToTargetInProgress == false)
            {
                drivetrain.setLRPower(0.8, 0.8);
            }

            if (distanceToTarget > DRIVE_TO_TARGET_THRESHOLD[3]) {
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

    //When tested, robot kept turning (to 30 deg)
    public void rotateToTarget(double targetHeading /*-180 to 180 deg*/)
    {
        double angleToTarget = targetHeading - currentCoord.getHeading(); //TODO: change estimateHeading() back to currentCorrd.getHeading
        angleToTarget=Functions.normalizeAngle(angleToTarget); //-180 to 180 deg

        if(angleToTarget<-DEADBAND_ROTATE_TO_TARGET){
            /*if(rotateToTargetInProgress == false) {
                drivetrain.setLeftPower(0.8);
                drivetrain.setRightPower(-0.8);
            }*/

            if(angleToTarget < -ROTATE_TO_TARGET_THRESHOLD[2]){
                drivetrain.setLeftPower(ROTATE_TO_TARGET_POWER_LEVEL[2]);
                drivetrain.setRightPower(-ROTATE_TO_TARGET_POWER_LEVEL[2]);
                rotateToTargetInProgress = true;
            }
            else if(angleToTarget < -ROTATE_TO_TARGET_THRESHOLD[1]){
                drivetrain.setLeftPower(ROTATE_TO_TARGET_POWER_LEVEL[1]);
                drivetrain.setRightPower(-ROTATE_TO_TARGET_POWER_LEVEL[1]);
                rotateToTargetInProgress = true;
            }
            else if(angleToTarget < -ROTATE_TO_TARGET_THRESHOLD[0]){
                drivetrain.setLeftPower(ROTATE_TO_TARGET_POWER_LEVEL[0]);
                drivetrain.setRightPower(-ROTATE_TO_TARGET_POWER_LEVEL[0]);
                rotateToTargetInProgress = true;
            }

        }
        else if(angleToTarget>DEADBAND_ROTATE_TO_TARGET)
        {
            /*if(rotateToTargetInProgress == false) {
                drivetrain.setLeftPower(-0.8);
                drivetrain.setRightPower(0.8);
            }*/

            if(angleToTarget > ROTATE_TO_TARGET_THRESHOLD[2]){
                drivetrain.setLeftPower(-ROTATE_TO_TARGET_POWER_LEVEL[2]);
                drivetrain.setRightPower(ROTATE_TO_TARGET_POWER_LEVEL[2]);
                rotateToTargetInProgress = true;
            }
            else if (angleToTarget > ROTATE_TO_TARGET_THRESHOLD[1]){
                drivetrain.setLeftPower(-ROTATE_TO_TARGET_POWER_LEVEL[1]);
                drivetrain.setRightPower(ROTATE_TO_TARGET_POWER_LEVEL[1]);
                rotateToTargetInProgress = true;
            }
            else if (angleToTarget > ROTATE_TO_TARGET_THRESHOLD[0]){
                drivetrain.setLeftPower(-ROTATE_TO_TARGET_POWER_LEVEL[0]);
                drivetrain.setRightPower(ROTATE_TO_TARGET_POWER_LEVEL[0]);
                rotateToTargetInProgress = true;
            }
        }

        else{
            drivetrain.setLeftPower(0.0);
            drivetrain.setRightPower(0.0);
            rotateToTargetInProgress = false;
        }
    }

    public void rotateToVortex(Position vortexPos)
    {
        Position vectorToTarget = Functions.subtractPositions(vortexPos, currentCoord.getPos()); //field frame
        vectorToTarget = field2body(vectorToTarget); //body frame

        double distanceToTarget = Functions.calculateMagnitude(vectorToTarget);

        //TODO: Confirm logic for this
        double degreesToRotate = Math.atan2(vortexPos.getY() - currentCoord.getY(), vortexPos.getX() - currentCoord.getX()); //from -pi to pi rad
        degreesToRotate = degreesToRotate * 180 / Math.PI - imu.getHeading();
        double targetHeading = Functions.normalizeAngle(currentCoord.getHeading() + degreesToRotate); //-180 to 180 deg
        DbgLog.msg("targetHeading: " + targetHeading);
        rotateToTarget(targetHeading);
        DbgLog.msg("rotateToVortex finished");
    }

    public Position estimatePosition()
    {
        //Coordinate vuforiaCoord;
        /* Commented out Jiangda's changes
        for(int i = 0; i < 100; i++){
            if(vuforia.vuforiaIsValid()){
                vuforiaCoord = vuforia.getHeadingAndLocation();
            }
        }
        */

        Position estimatedPos;

        if(vuforia.vuforiaIsValid())
        {
            //vuforiaCoord = coordinate of camera in field frame
            Coordinate vuforiaCoord = vuforia.getHeadingAndLocation();
            vuforiaCoord = getBodyCoordFromVuforiaCoord(vuforiaCoord); //coordinate of body in field frame
            Position currentPos = vuforiaCoord.getPos(); //field frame
            estimatedPos = currentPos;

            //Updates global variable
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

            estimatedPos = new Position(deltaPos.getX(), deltaPos.getY(), currentCoord.getZ());

            //Updates global variable
            currentCoord.setX(deltaPos.getX()); //field frame
            currentCoord.setY(deltaPos.getY()); //field frame
        }
        return estimatedPos;
    }

    public double estimateHeading()
    {
        double currentHeading;
        int i = 0;
        if(i == 1/*vuforia.vuforiaIsValid()*/){
            currentHeading = vuforia.getHeadingAndLocation().getHeading();
            imu.setImuBias(currentHeading);
            currentCoord.setHeading(currentHeading); //updates global variable
        }
        else {
            currentHeading = Functions.normalizeAngle(imu.getHeading() + imu.getImuBias()); //-180 to 180 deg
            currentCoord.setHeading(currentHeading); //updates global variable
        }
        return currentHeading;    }

    public void setInitialCoordinate(Coordinate initCoord)
    {
        currentCoord = initCoord;
        imu.setImuBias(currentCoord.getHeading());
        DbgLog.msg("Initial coordinate set", currentCoord.toString());
    }

    public String getTeleOpBeaconChoice(boolean dpadUp, boolean dpadDown)
    {
        String targetBeacon = "";
        beaconToggle.changeState(dpadUp, dpadDown);
        switch(beaconToggle.currentState())
        {
            case 0:
                targetBeacon = "Wheels";
                break;
            case 1:
                targetBeacon = "Legos";
                break;
            case 2:
                targetBeacon = "Tools";
                break;
            case 3:
                targetBeacon = "Gears";
                break;
        }
        return targetBeacon;
    }

    public void autoMoveToBeacon(boolean b)
    {
        Position[] beaconPositions = {new Position(300,1800,150), new Position(-900,1800,150), new Position(-1800,900,150), new Position(-1800,-300,150)};
        Position[] firstMovement = {new Position(100,100,100), new Position(100,100,100), new Position(100,100,100), new Position(100,100,100)};
        //TODO: CHECK FOR THESE POSITIONS

        autoBeaconToggle.changeState(b);

        switch(autoBeaconToggle.currentState())
        {
            case 0:
                driveToTarget(firstMovement[beaconToggle.currentState()]);
                driveToTarget(beaconPositions[beaconToggle.currentState()]);

                break;
            case 1:
                drivetrain.setLRPower(0.0,0.0);
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

        if(heading < 0)
            heading += 360;
        else if(heading >=360)
            heading -= 360;

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
