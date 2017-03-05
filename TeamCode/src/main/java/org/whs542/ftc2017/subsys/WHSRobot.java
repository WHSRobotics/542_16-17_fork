package org.whs542.ftc2017.subsys;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whs542.ftc2017.autoops.WHSAuto;
import org.whs542.lib.Alliance;
import org.whs542.lib.Coordinate;
import org.whs542.lib.Functions;
import org.whs542.lib.Position;
import org.whs542.lib.Toggler;

/**
 * WHSRobot class - container class for all subsystems (including sensors)
 * Contains all methods that require more than one subsystem, which consists of most higher-level functions of the robot.
 */
public class WHSRobot
{
    public Drivetrain drivetrain;
    public Intake intake;
    public Flywheel flywheel;
    public Flywheel2 flywheel2;
    //public CapballLift capball;
    public BeaconPusher pusher;
    public Vuforia vuforia;
    public IMU imu;
    public NXTUltrasonicSensor uSensor;
    public ODSensor odSensor;

    public boolean rotateToTargetInProgress;
    public boolean driveToTargetInProgress;
    public boolean vuforiaTargetDetected;
    public String s = "";
    public String beaconState = "";

    private Toggler beaconToggle = new Toggler(4);

    private static final double RADIUS_TO_DRIVETRAIN = 365/2; //in mm
    private static final double DEADBAND_MAX_DRIVE_HEADING_DEVIATION = 10; //in degrees
    private static final double DEADBAND_MAX_DRIVE_POSITION_DEVIATION = 300; //in mm
    private static final double[] DRIVE_TO_TARGET_POWER_LEVEL = {0.33, 0.6, 0.7, 0.9};
    public static final double DEADBAND_DRIVE_TO_TARGET = 110; //in mm
    private static final double[] DRIVE_TO_TARGET_THRESHOLD = {DEADBAND_DRIVE_TO_TARGET, 300, 600, 1200};
    private static final double[] ROTATE_TO_TARGET_POWER_LEVEL = {0.35, 0.6, 0.75};
    private static final double DEADBAND_ROTATE_TO_TARGET = 3.5; //in degrees
    private static final double[] ROTATE_TO_TARGET_THRESHOLD = {DEADBAND_ROTATE_TO_TARGET, 45, 90};
    private static final double DRIVE_CORRECTION_GAIN = 0.0007;
    public double rightMultiplier = 1.0;

    int[] encP = {0, 0, 0, 0};

    private static final int ENC_TOLERATION = 70;

    //17.85 /2 is center of robot, at 15 for y
    //16.5 / 2 is center of robot, at 15.75 for x

    static final double CAMERA_TO_BODY_X = 154.305; //body frame
    static final double CAMERA_TO_BODY_Y = -190.5; //body frame
    static final double CAMERA_TO_BODY_Z = 0; //body frame
    static final double CAMERA_TO_BODY_ANGLE = Math.atan(CAMERA_TO_BODY_X/CAMERA_TO_BODY_Y) + 90; //Measured CCW from x-body axis

    static final double US_TO_BODY_X = 100; //In mm
    static final double US_TO_BODY_Y = 100;
    static final double US_IDEAL_SPACE = 270;

    private int count = 0;
    private int count2 = 0;

    public Coordinate currentCoord; //field frame
    public double targetHeading; //field frame

    public WHSRobot(HardwareMap robotMap, Alliance side){
        drivetrain = new Drivetrain(robotMap);
        intake = new Intake(robotMap);
        flywheel2 = new Flywheel2(robotMap);
        pusher = new BeaconPusher(robotMap, side);
        //flywheel = new Flywheel(robotMap);
        //capball = new CapballLift(robotMap);

        imu = new IMU(robotMap);
        /*try {
            imu.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }*/

        uSensor = new NXTUltrasonicSensor(robotMap);
        odSensor = new ODSensor(robotMap);
        vuforia = new Vuforia();
        try{
            vuforia.start();
        }
        catch(Exception e){
            e.printStackTrace();
        }

        rotateToTargetInProgress = false;
        driveToTargetInProgress = false;
        vuforiaTargetDetected = false;
    }

    public WHSRobot(HardwareMap robotMap)
    {
        drivetrain = new Drivetrain(robotMap);
        intake = new Intake(robotMap);
        flywheel2 = new Flywheel2(robotMap);
        pusher = new BeaconPusher(robotMap, Alliance.BLUE);
        //flywheel = new Flywheel(robotMap);
        //capball = new CapballLift(robotMap);

        imu = new IMU(robotMap);

        uSensor = new NXTUltrasonicSensor(robotMap);
        odSensor = new ODSensor(robotMap);
        vuforia = new Vuforia();
        try {
            vuforia.start();
        }
        catch(NullPointerException e){
            e.printStackTrace();
        }

        rotateToTargetInProgress = false;
        driveToTargetInProgress = false;
        vuforiaTargetDetected = false;
    }

    /*public void driveToTarget(Position targetPos *//*field frame*//*)
    {
        Position vectorToTarget = Functions.subtractPositions(targetPos, currentCoord.getPos()); //field frame
        vectorToTarget = field2body(vectorToTarget); //body frame

        double distanceToTarget = Functions.calculateMagnitude(vectorToTarget);

        double degreesToRotate = Math.atan2(vectorToTarget.getY(), vectorToTarget.getX()); //from -pi to pi rad
        //double degreesToRotate = Math.atan2(targetPos.getY(), targetPos.getX()); //from -pi to pi rad
        degreesToRotate = degreesToRotate * 180 / Math.PI;
        double targetHeading = Functions.normalizeAngle(currentCoord.getHeading() + degreesToRotate); //-180 to 180 deg
        rotateToTarget(targetHeading);

        if (rotateToTargetInProgress) {
            //if rotating, do nothing
        }
        else {
            *//*if(driveToTargetInProgress == false)
            {
                drivetrain.setLRPower(0.8, 0.8);
            }*//*

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
    }*/

    public void driveToTarget(Position targetPos /*field frame*/)
    {
        Position vectorToTarget = Functions.subtractPositions(targetPos, currentCoord.getPos()); //field frame
        vectorToTarget = field2body(vectorToTarget); //body frame

        double distanceToTarget = Functions.calculateMagnitude(vectorToTarget);

        double degreesToRotate = Math.atan2(vectorToTarget.getY(), vectorToTarget.getX()); //from -pi to pi rad
        //double degreesToRotate = Math.atan2(targetPos.getY(), targetPos.getX()); //from -pi to pi rad
        degreesToRotate = degreesToRotate * 180 / Math.PI;
        /*double*/ targetHeading = Functions.normalizeAngle(currentCoord.getHeading() + degreesToRotate); //-180 to 180 deg

        if(count == 0) {
            rotateToTarget(targetHeading);
            count++;
        }
        else if(rotateToTargetInProgress) {
            rotateToTarget(targetHeading);
        }
        else if(driveToTargetInProgress)
        {
            if(targetHeading - currentCoord.getHeading() > DEADBAND_ROTATE_TO_TARGET/3)
            {
                rightMultiplier = rightMultiplier + DRIVE_CORRECTION_GAIN;
            }
            else if(targetHeading - currentCoord.getHeading() < -DEADBAND_ROTATE_TO_TARGET/3)
            {
                rightMultiplier = rightMultiplier - DRIVE_CORRECTION_GAIN;
            }

            if((Math.abs(targetHeading - currentCoord.getHeading()) > DEADBAND_MAX_DRIVE_HEADING_DEVIATION)
                    && (distanceToTarget > DEADBAND_MAX_DRIVE_POSITION_DEVIATION))
            {
                rotateToTarget(targetHeading);
            }

            /*if(count % 30 == 0 & distanceToTarget > 600) {

                rotateToTarget(targetHeading);
                count++;
            }
            else
            {
                count++;
            }*/
        }


        if (rotateToTargetInProgress) {
            //if rotating, do nothing
        }
        else {
            drivetrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

            if (distanceToTarget > DRIVE_TO_TARGET_THRESHOLD[3]) {
                drivetrain.setRightPower(DRIVE_TO_TARGET_POWER_LEVEL[3] * rightMultiplier);
                drivetrain.setLeftPower(DRIVE_TO_TARGET_POWER_LEVEL[3]);
                driveToTargetInProgress = true;
            }
            else if (distanceToTarget > DRIVE_TO_TARGET_THRESHOLD[2]) {
                drivetrain.setRightPower(DRIVE_TO_TARGET_POWER_LEVEL[2] * rightMultiplier);
                drivetrain.setLeftPower(DRIVE_TO_TARGET_POWER_LEVEL[2]);
                driveToTargetInProgress = true;
            }
            else if (distanceToTarget > DRIVE_TO_TARGET_THRESHOLD[1]) {
                drivetrain.setRightPower(DRIVE_TO_TARGET_POWER_LEVEL[1] * rightMultiplier);
                drivetrain.setLeftPower(DRIVE_TO_TARGET_POWER_LEVEL[1]);
                driveToTargetInProgress = true;
            }
            else if (distanceToTarget > DRIVE_TO_TARGET_THRESHOLD[0]) {
                drivetrain.setRightPower(DRIVE_TO_TARGET_POWER_LEVEL[0] * rightMultiplier);
                drivetrain.setLeftPower(DRIVE_TO_TARGET_POWER_LEVEL[0]);
                driveToTargetInProgress = true;
            }
            else {
                drivetrain.setRightPower(0.0);
                drivetrain.setLeftPower(0.0);
                driveToTargetInProgress = false;
                rotateToTargetInProgress = false;

                count = 0;
            }
        }
    }

    //When tested, robot kept turning (to 30 deg)
    public void rotateToTarget(double targetHeading /*-180 to 180 deg*/)
    {
        double angleToTarget = targetHeading - currentCoord.getHeading(); //TODO: change estimateHeading() back to currentCorrd.getHeading
        angleToTarget=Functions.normalizeAngle(angleToTarget); //-180 to 180 deg

        drivetrain.setRunMode(DcMotor.RunMode.RUN_USING_ENCODER);

        if(angleToTarget<-DEADBAND_ROTATE_TO_TARGET)
        {
            //consecutive = 0;
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
            } else if(angleToTarget < -ROTATE_TO_TARGET_THRESHOLD[0]){
                drivetrain.setLeftPower(ROTATE_TO_TARGET_POWER_LEVEL[0]);
                drivetrain.setRightPower(-ROTATE_TO_TARGET_POWER_LEVEL[0]);
                rotateToTargetInProgress = true;
            }

        }
        else if(angleToTarget>DEADBAND_ROTATE_TO_TARGET)
        {
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
            /*int[] encP2;
            if(count2 == 0) {
                encP = drivetrain.markEncoders();
                drivetrain.setRunMode(DcMotor.RunMode.RUN_TO_POSITION);
                drivetrain.setTP(encP);
                count++;
            }
            drivetrain.setLeftPower(0.3);
            drivetrain.setRightPower(0.3);
            encP2 = drivetrain.markEncoders();
            if(encCheck(encP, encP2))
            {
                rotateToTargetInProgress = false;
                count2 = 0;
            }*/
            /*if(consecutive < 3){
                consecutive++;
            }
            else {
                rotateToTargetInProgress = false;
                consecutive = 0;
            }
            */
        }
    }

    public boolean encCheck(int[] arr1, int[] arr2)
    {
        boolean withinLim = true;
        int length = arr1.length;
        for(int i = 0; i < length; i++)
        {
            if(Math.abs(arr1[i] - arr2[i]) > ENC_TOLERATION)
            {
                withinLim = false;
            }
            break;
        }
        return withinLim;
    }

    public void rotateToVortex(Position vortexPos)
    {
        Position vectorToTarget = Functions.subtractPositions(vortexPos, currentCoord.getPos()); //field frame
        vectorToTarget = field2body(vectorToTarget); //body frame

        double distanceToTarget = Functions.calculateMagnitude(vectorToTarget);

        //TODO: Confirm logic for this
        double degreesToRotate = Math.atan2(vectorToTarget.getY(), vectorToTarget.getX()); //from -pi to pi rad
        degreesToRotate = degreesToRotate * 180 / Math.PI;
        double targetHeading = Functions.normalizeAngle(currentCoord.getHeading() + degreesToRotate); //-180 to 180 deg
        DbgLog.msg("targetHeading: " + targetHeading);
        rotateToTarget(targetHeading);
        DbgLog.msg("rotateToVortex finished");
    }

    public Position estimatePosition()
    {
        //Coordinate vuforiaCoord;
        /* Commented out Jiangda's changes
        for(int i = 0; i < 100; i++){+

            if(vuforia.vuforiaIsValid()){
                vuforiaCoord = vuforia.getHeadingAndLocation();
            }
        }
        */

        Position estimatedPos;
        if(rotateToTargetInProgress)
        {
            //if rotating, do NOT update position and get rid of encoder values as it turns
            double[] encoderValues = drivetrain.getEncoderDistance();

            estimatedPos = currentCoord.getPos();
        }
        else {
            if (vuforia.vuforiaIsValid() & false) { //TODO: change Vuforia back to true when the ILT is over
                vuforiaTargetDetected = true;
                //vuforiaCoord = coordinate of camera in field frame
                Coordinate vuforiaCoord = vuforia.getHeadingAndLocation();
                vuforiaCoord = getBodyCoordFromVuforiaCoord(vuforiaCoord); //coordinate of body in field frame
                Position currentPos = vuforiaCoord.getPos(); //field frame
                estimatedPos = currentPos;

                //Updates global variable
                //currentCoord.setPos(currentPos); //field frame
            } else if (driveToTargetInProgress & !rotateToTargetInProgress) {
                vuforiaTargetDetected = false;
                double[] encoderValues = drivetrain.getEncoderDistance();
                double encoderPosL = encoderValues[0];
                double encoderPosR = encoderValues[1];

                double encoderAvg = (encoderPosL + encoderPosR) * 0.5;

                double hdg = currentCoord.getHeading();
                double dist = Functions.encToMM(encoderAvg);

                double xPos = currentCoord.getX() + dist * Functions.cosd(hdg);
                double yPos = currentCoord.getY() + dist * Functions.sind(hdg);

                estimatedPos = new Position(xPos, yPos, currentCoord.getZ());

                currentCoord.setX(xPos);
                currentCoord.setY(yPos);
            } else if (rotateToTargetInProgress) {
                drivetrain.getEncoderDistance();
                estimatedPos = currentCoord.getPos();

            } else {
                estimatedPos = currentCoord.getPos();
            }
        }
        //using encoders to estimate position from original location
        /* this is Lucy's code
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
        */

        return estimatedPos;
    }

    public double estimateHeading()
    {
        double currentHeading;
        if(vuforia.vuforiaIsValid() && !rotateToTargetInProgress && false){ //TODO: change Vuforia back to true when the ILT is over
            vuforiaTargetDetected = true;
            currentHeading = vuforia.getHeadingAndLocation().getHeading();
            imu.setImuBias(currentHeading);
            currentCoord.setHeading(currentHeading); //updates global variable
        }
        else {
            vuforiaTargetDetected = false;
            currentHeading = Functions.normalizeAngle(imu.getHeading() + imu.getImuBias()); //-180 to 180 deg
            currentCoord.setHeading(currentHeading); //updates global variable
        }
        return currentHeading;
    }

    public double usPosError()
    {
        double error;
        double range = uSensor.getRange();
        double headingError = usNormalize(imu.getHeading());
        if (headingError >= 0)
        {
            headingError = Math.abs(headingError);
            error = range * Functions.cosd(headingError) + US_TO_BODY_Y * Functions.cosd(headingError) - US_TO_BODY_X * Math.sin(headingError) - US_IDEAL_SPACE;
        }
        else
        {
            headingError = Math.abs(headingError);
            error = range * Functions.cosd(headingError) + US_TO_BODY_Y * Math.cos(headingError) + US_TO_BODY_X * Functions.sind(headingError) - US_IDEAL_SPACE;
        }
        return error;
    }

    public double usNormalize(double IMUHeading)
    {
        if(WHSAuto.ALLIANCE == 0)//Red Alliance
        {
            return Functions.normalizeAngle(IMUHeading + 90);
        }
        else//Blue Alliance
        {
            return Functions.normalizeAngle(IMUHeading - 180);
        }
    }


    public void setInitialCoordinate(Coordinate initCoord)
    {
        currentCoord = initCoord;
        imu.setImuBias(currentCoord.getHeading());
        DbgLog.msg("Initial coordinate set", currentCoord.toString());
    }

    public void setCoordinate(Coordinate coordinate)
    {
        currentCoord = coordinate;
        imu.setImuBias(currentCoord.getHeading());
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

    /*public void autoMoveToBeacon(boolean b)
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
    }*/

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
