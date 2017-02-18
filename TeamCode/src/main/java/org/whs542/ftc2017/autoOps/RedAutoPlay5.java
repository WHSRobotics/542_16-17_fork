package org.whs542.ftc2017.autoops;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;
import org.whs542.lib.Coordinate;
import org.whs542.lib.Position;
import org.whs542.lib.SoftwareTimer;
import org.whs542.lib.Timer;

/**
 * Red Auto Play 5
 */
@Autonomous(name = "RedAutoPlay5", group = "Autonomous  ")
//@Disabled
//@Deprecated

public class RedAutoPlay5 extends OpMode
{
    WHSRobot robot;

    static int currentState;
    String currentStateName;

    //currentState definitions
    static final int WARMUP_FLYWHEEL = 0;
    static final int SHOOT_PARTICLE_1 = 1;
    static final int SHOOT_PARTICLE_2 = 2;
    static final int CAPTURE_BEACON_1 = 3;
    static final int CAPTURE_BEACON_2 = 4;
    static final int KNOCK_CAP_BALL = 5;
    static final int PARK_ON_CENTER = 6;
    static final int EXIT = 7;
    //currentState enabled array
    static boolean[] stateEnabled = new boolean[8];

    public void defineStateEnabledStatus()
    {
        stateEnabled[WARMUP_FLYWHEEL] = true;
        stateEnabled[SHOOT_PARTICLE_1] = true;
        stateEnabled[SHOOT_PARTICLE_2] = true;
        stateEnabled[CAPTURE_BEACON_1] = false;
        stateEnabled[CAPTURE_BEACON_2] = false;
        stateEnabled[KNOCK_CAP_BALL] = true;
        stateEnabled[PARK_ON_CENTER] = true;
        stateEnabled[EXIT] = true;
    }

    boolean initDownTimer = false;

    boolean pullOffWallComplete = false;
    boolean driveToCapballComplete = false;
    boolean knockOffCapballComplete = false;

    double[] powers = {0.67, 0.8};
    final int startingPosition = 1; //1 or 2
    static final double FLYWHEEL_WARMUP_DELAY = 6.0; //in seconds
    static final double PARTICLE_UP_PUSHER_DELAY = 1.5;
    static final double PARTICLE_DOWN_PUSHER_DELAY = 1.5;

    //Wheels, Legos, Tools, Gears
    Position[] beaconPositions = {new Position(300,1800,150), new Position(-900,1800,150), new Position(-1800,900,150), new Position(-1800,-300,150)};
    Position[] redPositions = {new Position(-1650,600,100), new Position(-1650,600,150), new Position(0,0,150), new Position(-1800, 1000, 150), new Position(-150, 0, 150) };
    Position[] vortexPositions = {new Position(300, 300, 150), new Position(-300, -300, 150)};

    //First coordinate: closest to red ramp, touching wall; Second: in the middle of red wall; Third: farthest from red ramp
    Coordinate[] startingPositions = {new Coordinate(-300, -1500, 150, 90), new Coordinate(0, -1500, 150, 90), new Coordinate(300, -1500, 150, 90)};
    Position[] capballPositions = {new Position(300, 450, 150), new Position(-450, -450, 150)};

    Timer vuforiaInitTimer;
    SoftwareTimer flywheelWarmUpTimer;
    SoftwareTimer particleUpTimer;
    SoftwareTimer particleDownTimer;

    boolean loop;
    boolean performStateEntry;
    boolean performStateExit;

    @Override
    public void init() {
        robot = new WHSRobot(hardwareMap, Alliance.RED);
        robot.setInitialCoordinate(startingPositions[0]);
        currentState = 0;
        vuforiaInitTimer = new Timer(5, true);
        flywheelWarmUpTimer = new SoftwareTimer();
        particleUpTimer = new SoftwareTimer();
        particleDownTimer = new SoftwareTimer();
        loop = true;

        defineStateEnabledStatus();

        performStateEntry = true;
        performStateExit = false;
    }

    @Override
    public void init_loop(){
        telemetry.addData("Time until Vuforia start", vuforiaInitTimer.timeUntilTimerElapsed());
    }

    @Override
    public void loop() {

        robot.estimateHeading();
        robot.estimatePosition();

        switch(currentState){
            /*case 0:
                currentStateName = "moving forward";
                robot.driveToTarget(new Position(-300, -1400, 150));
                if(!robot.driveToTargetInProgress & !robot.rotateToTargetInProgress)
                    advanceState();
                break;

            case 1:
                currentStateName = "Turning to vortex";
                robot.rotateToVortex(vortexPositions[1]);
                if (!robot.rotateToTargetInProgress & !robot.rotateToTargetInProgress)
                    advanceState();
                break;*/
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
            case KNOCK_CAP_BALL:
                //state entry
                if(performStateEntry) {
                    currentStateName = "Moving forward again";
                    pullOffWallComplete = true;
                    performStateEntry = false;
                }

                //state processing
                if(!pullOffWallComplete)
                {
                    robot.driveToTarget(new Position(-300, -900, 150));
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


