package org.whs542.ftc2017.autoops;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;
import org.whs542.lib.Coordinate;
import org.whs542.lib.Functions;
import org.whs542.lib.Position;
import org.whs542.lib.SoftwareTimer;
import org.whs542.lib.Timer;

/**
 * State Machine Based Auto
 */

@Autonomous(name = "WHSAuto", group = "auto")
//@Disabled
public class WHSAuto extends OpMode {

    WHSRobot robot;

    static int currentState;
    String currentStateName;

    //Starting Coordinates
    static Coordinate[][] startingCoordinateArray = new Coordinate[2][2];
    static final int RED = 0;
    static final int BLUE = 1;
    static final int ON_VORTEX = 0;
    static final int OFF_VORTEX = 1;

    /****** Change these before match, dummy!! ******/
    static final int ALLIANCE = BLUE;
    static final int VORTEX_ALIGNMENT = ON_VORTEX;

    //Beacon Positions
    static Position[][] beaconPostionArray = new Position[2][2];
    static final int BEACON_1 = 0;
    static final int BEACON_2 = 1;
    static final double BEACON_BUTTON_SEPARATION = 140;
    static double[] beaconCaptureHeading = new double[2];
    static Position[] pullOffWallPositions = new Position[2];

    //currentState Definitions
    static final int WARMUP_FLYWHEEL = 0;
    static final int SHOOT_PARTICLE_1 = 1;
    static final int SHOOT_PARTICLE_2 = 2;
    static final int DRIVE_TO_BEACON_WALL = 3;
    static final int CAPTURE_BEACON_1 = 4;
    static final int CAPTURE_BEACON_2 = 5;
    static final int KNOCK_CAP_BALL = 6;
    static final int PARK_ON_CENTER = 7;
    static final int EXIT = 8;
    //currentState enabled array
    static boolean[] stateEnabled = new boolean[9];

    public void defineStateEnabledStatus()
    {
        stateEnabled[WARMUP_FLYWHEEL] = true;
        stateEnabled[SHOOT_PARTICLE_1] = true;
        stateEnabled[SHOOT_PARTICLE_2] = true;
        stateEnabled[CAPTURE_BEACON_1] = true;
        stateEnabled[CAPTURE_BEACON_2] = true;
        stateEnabled[DRIVE_TO_BEACON_WALL] = stateEnabled[CAPTURE_BEACON_1] | stateEnabled[CAPTURE_BEACON_2]; //Should technically be above CAPTURE_BEACON_1
        stateEnabled[KNOCK_CAP_BALL] = false;
        stateEnabled[PARK_ON_CENTER] = true;
        stateEnabled[EXIT] = true;

    }

    //Sub-State Variables
    //Flywheel
    boolean initDownTimer = false;
    //Drive to beacon wall
    boolean driveToBeaconWallComplete = false;
    boolean rotateToParallelWallComplete = false;
    //Beacons
    boolean driveToBeaconComplete = false;
    boolean beaconRetracted = true;
    boolean beaconPushed = false;
    //Beacons & Capball
    boolean pullOffWallComplete = false;
    //Capball
    boolean driveToCapballComplete = false;
    boolean knockOffCapballComplete = false;

    //Beacon Control
    static final double BEACON_RETRACTION_DELAY = 1.2; //In seconds
    static final double BEACON_DRIVE_POWER = 0.70; //Arbitrary value

    //Flywheel Control
    double[] powers = {0.69, 0.8};
    final int startingPosition = 1; //1 or 2
    static final double FLYWHEEL_WARMUP_DELAY = 6.0; //in seconds
    static final double PARTICLE_UP_PUSHER_DELAY = 1.5;
    static final double PARTICLE_DOWN_PUSHER_DELAY = 1.5;

    //Cap Ball Positions
    static Position[] capBallPositions = new Position[2];

    //Vuforia Target Locations
    //Wheels, Legos, Tools, Gears
    Position[] beaconPositions = {new Position(300,1800,150), new Position(-900,1800,150), new Position(-1800,900,150), new Position(-1800,-300,150)};
    Position[] redPositions = {new Position(-1650,600,100), new Position(-1650,600,150), new Position(0,0,150), new Position(-1800, 1000, 150), new Position(-150, 0, 150) };
    Position[] vortexPositions = {new Position(300, 300, 150), new Position(-300, -300, 150)};

    //First coordinate: closest to red ramp, touching wall; Second: in the middle of red wall; Third: farthest from red ramp
    @Deprecated Coordinate[] startingPositions = {new Coordinate(-300, -1500, 150, 90), new Coordinate(0, -1500, 150, 90), new Coordinate(300, -1500, 150, 90)};
    Position[] capballPositions = {new Position(300, 450, 150), new Position(-450, -450, 150)};

    //Timers
    Timer vuforiaInitTimer;
    SoftwareTimer flywheelWarmUpTimer;
    SoftwareTimer particleUpTimer;
    SoftwareTimer particleDownTimer;
    SoftwareTimer beaconRetractionTimer;

    //Main State Variables
    boolean loop;
    boolean performStateEntry;
    boolean performStateExit;

    @Override
    public void init() {

        robot = new WHSRobot(hardwareMap, ALLIANCE == RED ? Alliance.RED : Alliance.BLUE);
        currentState = 0;
        vuforiaInitTimer = new Timer(5, true);
        flywheelWarmUpTimer = new SoftwareTimer();
        particleUpTimer = new SoftwareTimer();
        particleDownTimer = new SoftwareTimer();
        loop = true;

        defineStateEnabledStatus();

        performStateEntry = true;
        performStateExit = false;

        startingCoordinateArray[RED][ON_VORTEX] = new Coordinate(-300, -1570, 150, 90);
        startingCoordinateArray[RED][OFF_VORTEX] = new Coordinate(300, -1570, 150, 90);
        startingCoordinateArray[BLUE][ON_VORTEX] = new Coordinate(1570, 300, 150, 180);
        startingCoordinateArray[BLUE][OFF_VORTEX] = new Coordinate(1570, -300, 150, 180);
        robot.setInitialCoordinate(startingCoordinateArray[ALLIANCE][VORTEX_ALIGNMENT]);

        //Robot positions such that color sensor is always off the beacon button, in the +x direction (blue) or +y (red)
        beaconPostionArray[RED][BEACON_1] =  new Position(-1490, 1060 + 2*WHSRobot.DEADBAND_DRIVE_TO_TARGET, 150);    //Far beacon
        beaconPostionArray[RED][BEACON_2] =  new Position(-1490, 140  + 2*WHSRobot.DEADBAND_DRIVE_TO_TARGET, 150);    //Near beacon
        beaconPostionArray[BLUE][BEACON_1] = new Position( 460 + 2*WHSRobot.DEADBAND_DRIVE_TO_TARGET, 1490,  150);    //Near beacon
        beaconPostionArray[BLUE][BEACON_2] = new Position(-740 + 2*WHSRobot.DEADBAND_DRIVE_TO_TARGET, 1490,  150);    //Far beacon
        //Directions robot is facing to parallel with beacons wall (in degrees)
        beaconCaptureHeading[RED] = 0;
        beaconCaptureHeading[BLUE] = Functions.normalizeAngle(180);
        pullOffWallPositions[RED] = new Position(-300,-900,150);
        pullOffWallPositions[BLUE] = new Position(900,900,150);

        capBallPositions[BLUE] = new Position(680,1490,150);
        capBallPositions[RED] = new Position(520,1490,150);
    }

    @Override
    public void init_loop() {
        telemetry.addData("Time until Vuforia start", vuforiaInitTimer.timeUntilTimerElapsed());
    }

    @Override
    public void loop() {

        robot.estimateHeading();
        robot.estimatePosition();

        switch(currentState){

            case WARMUP_FLYWHEEL:
                currentStateName = "warm up flywheel";
                //State entry
                if(performStateEntry)
                {
                    flywheelWarmUpTimer.set(FLYWHEEL_WARMUP_DELAY);
                    performStateEntry = false;
                }

                //State processing
                robot.flywheel2.runFlywheelNoToggle(powers[startingPosition - 1]);

                //State exit criteria
                if(flywheelWarmUpTimer.isExpired())
                {
                    performStateExit = true;
                }

                //State exit
                if(performStateExit)
                {
                    advanceState();
                    performStateEntry = true;
                    performStateExit = false;
                }
                break;
            case SHOOT_PARTICLE_1:
                currentStateName = "shoot particle 1";
                //State entry
                if(performStateEntry)
                {
                    particleUpTimer.set(PARTICLE_UP_PUSHER_DELAY);
                    robot.flywheel2.setParticleControlState(true);
                    initDownTimer = true;
                    performStateEntry = false;
                }

                //State processing
                if(particleUpTimer.isExpired())
                {
                    if(initDownTimer)
                    {
                        particleDownTimer.set(PARTICLE_DOWN_PUSHER_DELAY);
                        robot.flywheel2.setParticleControlState(false);
                        initDownTimer = false;
                    }
                }

                //State exit criteria
                if(particleDownTimer.isExpired())
                {
                    performStateExit = true;
                }

                //State exit
                if(performStateExit)
                {
                    advanceState();
                    performStateEntry = true;
                    performStateExit = false;
                }
                break;
            case SHOOT_PARTICLE_2:
                currentStateName = "shoot particle 2";
                //State entry
                if(performStateEntry)
                {
                    particleUpTimer.set(PARTICLE_UP_PUSHER_DELAY);
                    robot.flywheel2.setParticleControlState(true);
                    initDownTimer = true;
                    performStateEntry = false;
                }

                //State processing
                if(particleUpTimer.isExpired())
                {
                    if(initDownTimer)
                    {
                        particleDownTimer.set(PARTICLE_DOWN_PUSHER_DELAY);
                        robot.flywheel2.setParticleControlState(false);
                        initDownTimer = false;
                    }
                }

                //State exit criteria
                if(particleDownTimer.isExpired())
                {
                    performStateExit = true;
                }

                //State exit
                if(performStateExit)
                {
                    advanceState();
                    performStateEntry = true;
                    performStateExit = false;
                    robot.flywheel2.runFlywheelNoToggle(0.0);
                }
                break;

            case DRIVE_TO_BEACON_WALL:
                //State Entry
                if(performStateEntry){
                    currentStateName = "driving to beacon wall";
                    pullOffWallComplete = false;
                    driveToBeaconWallComplete = false;
                    performStateEntry = false;
                    rotateToParallelWallComplete = false;
                }

                if(!pullOffWallComplete)
                {
                    currentStateName = "pulling off wall";
                    robot.driveToTarget(pullOffWallPositions[ALLIANCE]/*new Position(-300, -900, 150)*/);
                    if(!robot.driveToTargetInProgress & !robot.rotateToTargetInProgress) {
                        pullOffWallComplete = true;
                    }
                }
                else if(!driveToBeaconWallComplete)
                {
                    currentStateName = "driving to beacon wall";
                    robot.driveToTarget(beaconPostionArray[ALLIANCE][0]);
                    if(!robot.driveToTargetInProgress & !robot.rotateToTargetInProgress){
                        driveToBeaconWallComplete = true;
                    }
                }
                else if(!rotateToParallelWallComplete){
                    currentStateName = "rotating to parallele wall";
                    robot.rotateToTarget(beaconCaptureHeading[ALLIANCE]);
                    if(!robot.driveToTargetInProgress & !robot.rotateToTargetInProgress){
                        rotateToParallelWallComplete = true;
                    }
                }
                //State exit criteria
                else {
                    performStateExit = false;
                }

                //Perform state exit
                if(performStateExit){
                    advanceState();
                    performStateEntry = true;
                    performStateExit = false;
                }

                break;

            case CAPTURE_BEACON_1:

                //State entry
                if(performStateEntry){
                    performStateEntry = false;
                    beaconRetracted = false;
                    beaconPushed = false;
                }

                if(!beaconPushed){
                    robot.drivetrain.setLRPower(BEACON_DRIVE_POWER, BEACON_DRIVE_POWER);
                    if (robot.pusher.isBeaconPushed()){
                        beaconPushed = true;
                        beaconRetractionTimer.set(BEACON_RETRACTION_DELAY);
                        robot.drivetrain.setLRPower(0.0, 0.0);
                    }
                }
                else if(!beaconRetracted){
                    if(beaconRetractionTimer.isExpired()){
                        robot.pusher.extendPusherNoToggle(false);
                        robot.pusher.extendPusherHand(false);
                    }
                }
                //State exit criteria
                else {
                    performStateExit = true;
                }

                //Perform state exit
                if(performStateExit)
                {
                    advanceState();
                    performStateEntry = true;
                    performStateExit = false;
                }
                break;

            case CAPTURE_BEACON_2:

                //State entry
                if(performStateEntry){
                    performStateEntry = false;
                    beaconRetracted = false;
                    beaconPushed = false;
                    driveToBeaconComplete = false;
                }

                if (!driveToBeaconComplete){
                    robot.driveToTarget(beaconPostionArray[ALLIANCE][BEACON_2]);
                    if(robot.driveToTargetInProgress & robot.rotateToTargetInProgress){
                        driveToBeaconComplete = true;
                    }
                }
                if(!beaconPushed){
                    robot.drivetrain.setLRPower(BEACON_DRIVE_POWER, BEACON_DRIVE_POWER);
                    if (robot.pusher.isBeaconPushed()){
                        beaconPushed = true;
                        beaconRetractionTimer.set(BEACON_RETRACTION_DELAY);
                        robot.drivetrain.setLRPower(0.0, 0.0);
                    }
                }
                else if(!beaconRetracted){
                    if(beaconRetractionTimer.isExpired()){
                        robot.pusher.extendPusherNoToggle(false);
                        robot.pusher.extendPusherHand(false);
                    }
                }
                //State exit criteria
                else {
                    performStateExit = true;
                }

                //Perform state exit
                if(performStateExit)
                {
                    advanceState();
                    performStateEntry = true;
                    performStateExit = false;
                }
                break;

            case KNOCK_CAP_BALL:
                //State Entry
                if(performStateEntry) {
                    currentStateName = "Moving forward again";
                    //pullOffWallComplete = true;
                    performStateEntry = false;
                }

                //state processing
                if(!pullOffWallComplete)
                {
                    robot.driveToTarget(pullOffWallPositions[ALLIANCE]);
                    if(!robot.driveToTargetInProgress & !robot.rotateToTargetInProgress) {
                        pullOffWallComplete = true;
                    }
                }
                else if(!driveToCapballComplete){
                    robot.driveToTarget(capballPositions[1]);
                    if(!robot.driveToTargetInProgress & !robot.rotateToTargetInProgress) {
                        driveToCapballComplete = true;
                    }
                }
                else if(!knockOffCapballComplete)
                {
                    robot.rotateToTarget(-40);
                    if(!robot.driveToTargetInProgress & !robot.rotateToTargetInProgress) {
                        knockOffCapballComplete = true;
                    }
                }
                //state exit criteria
                else
                {
                    performStateExit = true;
                }

                //state exit
                if(performStateExit)
                {
                    advanceState();
                    performStateEntry = true;
                    performStateExit = false;
                }
                break;


            case PARK_ON_CENTER:
                currentStateName = "park on center";

                //state entry
                if(performStateEntry)
                {
                    performStateEntry = false;
                }

                //state processing
                robot.driveToTarget(new Position(-350, 200, 150));

                //state exit criteria
                if(!robot.driveToTargetInProgress & !robot.rotateToTargetInProgress){
                    performStateExit = true;
                }

                //state exit
                if(performStateExit)
                {
                    advanceState();
                    performStateEntry = true;
                    performStateExit = false;
                }
                break;
            case EXIT:
                robot.drivetrain.setLRPower(0.0, 0.0);
                currentStateName = "Auto Op Done!!";
                break;

            default:
                break;
        }
        telemetry.addData("State Info: ", currentStateName);
        telemetry.addData("Rx", robot.currentCoord.getX());
        telemetry.addData("Ry", robot.currentCoord.getY());
        telemetry.addData("Rh", robot.currentCoord.getHeading());
        telemetry.addData("DriveToTargetInProgress:", robot.driveToTargetInProgress);
        telemetry.addData("RotateToTargetInProgress", robot.rotateToTargetInProgress);
        telemetry.addData("time", time);
        telemetry.addData("Current State: ", currentState);
        telemetry.addData("Vuforia valid?", robot.vuforia.vuforiaIsValid());
        telemetry.addData("Pull off Wall Complete: ", pullOffWallComplete);
        telemetry.addData("Drive to Capball Complete: ", driveToCapballComplete);
        telemetry.addData("Knock off Capball Complete: ", knockOffCapballComplete);



    }

    public void advanceState()
    {
        if(stateEnabled[(currentState + 1)])
        {
            currentState = currentState + 1;
        }
        else
        {
            currentState = currentState + 1;
            advanceState();
        }
    }
}