package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;
import org.whs542.lib.Coordinate;
import org.whs542.lib.Position;
import org.whs542.lib.Timer;

/**
 * AutoOp containing all premade multipart switchcases
 *
 * @see WHSParentAutoOp
 * @see AutoOpSwitchCaseTemplate
 */
@Autonomous(name = "WHSBlueParentAuto", group = "Auto")
//@Disabled
public class WHSBlueParentAutoOp extends OpMode {

    //Byte for controlling which state the case will be in: init(0), loop/init(1-99), or exit(default)
    int state = 2;
    String action;

    WHSRobot robot;

    Timer timer1;

    ////// Carry-over from older parent AutoOp
    boolean firstLoop;
    //int test;

    long particleDelay;
    double[] powers = {0.7, 0.8};
    final int startingPosition = 1; //1 or 2
    Alliance allianceColor = Alliance.BLUE;
    String stateInfo; //initial

    //Wheels, Legos, Tools, Gears
    //Position vortexPosition;
    //Position[] vortexPositions = {new Position(-300, -300, 150), new Position(300, 300, 150)};
    //Position centerVortex = new Position(0, 0, 150);
    //Position blueVortex = new Position(300, 300, 150);
    //Position redVortex = new Position(-300, -300, 150);

    //Position[] beaconPositions = {new Position(300,1800,150), new Position(-900,1800,150), new Position(-1800,900,150), new Position(-1800,-300,150)};
    Position beacon1a;
    Position beacon1b;
    Position beacon2a;
    Position beacon2b;


    //Position[] targetPositionsBlue = {new Position(600, 1500, 150), new Position(-1300, 1500, 150)};
    Coordinate[] startingPositions = {new Coordinate(1500, 300, 150, 180), new Coordinate(1500, 0, 150, 180), new Coordinate(1500, -300, 150, 180)};
    Position[] vortexPositions = {new Position(300, 300, 150), new Position(-300, -300, 150)};
    //firstLoop: align to parallel beacons, second: end of beacons, third: center vortex
    //Position[] redPositions = {new Position(-1650,600,100), new Position(-1650,600,150), new Position(0,0,150)}; //TODO: separate do the same thing as before
    //firstLoop: align to parallel beacons, second: end of beacons, third: center vortex
    Position[] bluePositions = {new Position(600,1500,150), new Position(-1500,1300,150), new Position(0,0,150)};
    //TODO: move into init with red and blue

    Coordinate startingCoord;
    //{new Coordinate(-300, -1500, 150, 180), new Coordinate(1500, -300, 150, 180), new Coordinate(1500, 300, 150, 180)};


    //TODO: Test this
    //double flywheelPower = 0.48;

    int loopCycles = 0;


    @Override
    public void init() {

        robot = new WHSRobot(hardwareMap, allianceColor);
        robot.setInitialCoordinate(startingPositions[0]);

        timer1 = new Timer(5, true);

        beacon1a = new Position(580, 1500, 150);
        beacon1b = new Position(510, 1500, 150);
        beacon2a = new Position(-670, 1500, 150);
        beacon2b = new Position(-690, 1500, 150);

        telemetry.log().add("RBT INIT");

    }

    @Override
    public void init_loop() {
        telemetry.addData("Time until Vuforia start", timer1.timeUntilTimerElapsed());
    }

    @Override
    public void loop() {
        robot.estimateHeading();
        robot.estimatePosition();
        loopCycles++;

        switch(state){
            /*  case 0:
                stateInfo = "moving forward";
                robot.driveToTarget(new Position(1400, 300, 150));
                if(!robot.driveToTargetInProgress & !robot.rotateToTargetInProgress)
                    state++;
                break;

            case 1:
                stateInfo = "Turning to vortex";
                robot.rotateToVortex(vortexPositions[0]);
                if (!robot.rotateToTargetInProgress & !robot.rotateToTargetInProgress)
                    state++;
                break;*/
            case 2:
                stateInfo = "Shooting particles";
                robot.flywheel2.runFlywheelNoToggle(powers[startingPosition - 1]); //need something to check if it's up to speed
                try {
                    Thread.sleep(4500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                state++;
                break;

            case 3:
                stateInfo = "Shooting first particle";
                robot.flywheel2.setParticleControlState(true);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                state++;
                break;
            case 4:
                stateInfo = "Lowering particle control";
                robot.flywheel2.setParticleControlState(false);
                try {
                    Thread.sleep(3500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                state++;
                break;
            case 5:
                stateInfo = "Shooting second particle";
                robot.flywheel2.setParticleControlState(true);
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                robot.flywheel2.runFlywheelNoToggle(0.0);
                robot.flywheel2.setParticleControlState(false);
                state++;
                break;
            case 6:
                stateInfo = "Moving to before 1st beacon";
                robot.driveToTarget(bluePositions[0]);
                if(!robot.driveToTargetInProgress & !robot.rotateToTargetInProgress)
                    state++;
                break;
            case 7:
                stateInfo = "Driving to beacon 1a";
                robot.driveToTarget(beacon1a);
                if(!robot.driveToTargetInProgress & !robot.rotateToTargetInProgress)
                    state++;
                break;
            case 8:
                stateInfo = "Scanning beacon 1a";
                if(robot.pusher.isBeaconPushed()){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.pusher.extendPusher(false);
                    state = 11;
                }
                else{
                    state++;
                }
                break;
            case 9:
                stateInfo = "Driving to beacon 1b";
                robot.driveToTarget(beacon1b);
                if(!robot.driveToTargetInProgress & !robot.rotateToTargetInProgress)
                    state++;
                break;
            case 10:
                stateInfo = "Scanning beacon 1b";
                if(robot.pusher.isBeaconPushed()) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.pusher.extendPusher(false);
                    state++;
                }
                else{
                    state++;
                }
                break;
            case 11:
                stateInfo = "Driving to beacon 2a";
                robot.driveToTarget(beacon2a);
                if(!robot.driveToTargetInProgress & !robot.rotateToTargetInProgress){
                    state++;
                }
                break;
            case 12:
                stateInfo = "Scanning beacon 2a";
                if(robot.pusher.isBeaconPushed()){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.pusher.extendPusher(false);
                    state = 15;
                }
                else{
                    state++;
                }
                break;
            case 13:
                stateInfo = "Driving to beacon 2b";
                robot.driveToTarget(beacon2b);
                if(!robot.driveToTargetInProgress & !robot.rotateToTargetInProgress)
                    state++;
                break;
            case 14:
                stateInfo = "Scanning beacon 2b";
                if(robot.pusher.isBeaconPushed()){
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.pusher.extendPusher(false);
                    state++;
                }
                else{
                    //state++;
                }
                break;
            case 15:
                stateInfo = "Auto Op Complete!! Victory Dance";
                //robot.drivetrain.setLRPower(-1.0, 1.0);
                break;


        }
        telemetry.addData("Robot state:", stateInfo);
        telemetry.addData("Using Vuforia?", robot.vuforiaTargetDetected );
        telemetry.addData("Rx", robot.estimatePosition().getX());
        telemetry.addData("Ry", robot.estimatePosition().getY());
        telemetry.addData("Rh", robot.estimateHeading());
        telemetry.addData("State:", state);
        telemetry.addData("Runtime:", time);
        telemetry.addData("Loop Cycles:", loopCycles);
        telemetry.addData("Color R:", robot.pusher.color.getR());
        telemetry.addData("Color B:", robot.pusher.color.getB());
    }

}




