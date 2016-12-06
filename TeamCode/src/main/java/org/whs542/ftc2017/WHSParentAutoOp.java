package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;
import org.whs542.lib.Coordinate;
import org.whs542.lib.OpModeTimer;
import org.whs542.lib.Position;

import java.util.Timer;

/**
 * Parent AutoOp Class
 */

@Autonomous(name = "WHSParentAuto", group = "Auto")
//@Override
public abstract class WHSParentAutoOp extends OpMode {
    WHSRobot robot;
    OpModeTimer timer;

    int state;
    boolean firstLoop;
    int test;

    long particleDelay;
    String stateInfo;
    double[] powers = {0.7, 0.8};
    final int startingPosition = 1; //1 or 2
    Alliance allianceColor = Alliance.BLUE;
    String initialConfig; //initial

    //Wheels, Legos, Tools, Gears
    Position vortexPosition;
    //Position[] vortexPositions = {new Position(-300, -300, 150), new Position(300, 300, 150)};
    Position centerVortex = new Position(0, 0, 150);

    //Position[] beaconPositions = {new Position(300,1800,150), new Position(-900,1800,150), new Position(-1800,900,150), new Position(-1800,-300,150)};
    Position beacon1a;
    Position beacon1b;
    Position beacon2a;
    Position beacon2b;

    Position[] targetPositionsBlue = {new Position(600, 1500, 150), new Position(-1300, 1500, 150)};
    Position[] targetPositionsRed = {new Position(-1500, -600, 150), new Position(-1500, 1300, 150)};
    //firstLoop: align to parallel beacons, second: end of beacons, third: center vortex
    //Position[] redPositions = {new Position(-1650,600,100), new Position(-1650,600,150), new Position(0,0,150)}; //TODO: separate do the same thing as before
    //firstLoop: align to parallel beacons, second: end of beacons, third: center vortex
    //Position[] bluePositions = {new Position(600,1650,150), new Position(-600,1650,150), new Position(0,0,150)};
    //Position[] vortexPositions = {new Position(300, 300, 150), new Position(-300, -300, 150)}; //TODO: move into init with red and blue

    Coordinate startingCoord;
    //{new Coordinate(-300, -1500, 150, 180), new Coordinate(1500, -300, 150, 180), new Coordinate(1500, 300, 150, 180)};


    @Override
    public void init(){

        robot = new WHSRobot(hardwareMap, allianceColor);
        timer = new OpModeTimer();

        if(allianceColor == Alliance.BLUE)
        {//TODO: Create class timer
            if(startingPosition == 1) {
                initialConfig = "Blue on vortex";
                startingCoord = new Coordinate(1575, 300, 150, 180);
            }
            else if(startingPosition == 2){
                initialConfig = "Blue off vortex";
                startingCoord = new Coordinate(1575, -300, 150, 180);
            }
            vortexPosition = new Position(-300, -300, 150);

            beacon1a = new Position(580, 1500, 150);
            beacon1b = new Position(573, 1500, 150);
            beacon2a = new Position(-620, 1500, 150);
            beacon2b = new Position(-627, 1500, 150);
        }
        else if(allianceColor == Alliance.RED) {
            //TODO: add red coordinates into both pleasee
            if (startingPosition == 1) {
                initialConfig = "Red on vortex";
                startingCoord = new Coordinate(-300, -1575, 150, 90);
            }
            else if (startingPosition == 2) {
                initialConfig = "Red off vortex";
                startingCoord = new Coordinate(300, -1575, 150, 90);
            }
            vortexPosition = new Position(300, 300, 150);


            beacon1a = new Position(-1500, -580, 150);
            beacon1b = new Position(-1500, -573, 150);
            beacon2a = new Position(-1500, 620, 150);
            beacon2b = new Position(-1500, 627, 150);
        }
        //DbgLog.msg("Rbt init");
        telemetry.addData("RBT Init", 1);
    }

    public void loop() {
        int i = 1;
        if(i == 1) {
            timer.start();
            i = 2;
        }
        robot.estimatePosition();
        robot.estimateHeading();

        switch (state) {
            case 0:
                //TODO: init a new timer
                stateInfo = "Moving forward";
                if (allianceColor == Alliance.BLUE) {
                    robot.driveToTarget(new Position(startingCoord.getX() - 100, startingCoord.getY(), 150));
                } else if (allianceColor == Alliance.RED) {
                    robot.driveToTarget(new Position(startingCoord.getX(), startingCoord.getY() + 100, 150));
                }

                if (!robot.rotateToTargetInProgress && !robot.driveToTargetInProgress) {
                    state++;
                }
                break;
            case 1:
                stateInfo = "Turning to vortex";
                robot.flywheel.setFlywheelPower(powers[startingPosition - 1]); //TODO: change location for this so that it starts a little later
                robot.rotateToVortex(vortexPosition);
                if (!robot.rotateToTargetInProgress) {
                    state++;
                }
                break;
            case 2:
                stateInfo = "Shooting particles";
                if (robot.flywheel.isFlywheelAtCorrectSpeed(powers[startingPosition - 1])) {
                    robot.flywheel.operateGate(true);
                    robot.intake.runIntake(1.0);
                    if (test > 0) {
                        test--;
                    }
                    robot.intake.runIntake(0.0);
                    robot.flywheel.setFlywheelPower(0.0);
                    robot.flywheel.operateGate(false);
                    state++;
                }
                break;
            case 3:
                stateInfo = "Driving to target position 1";
                if (firstLoop || robot.driveToTargetInProgress) {
                    if(allianceColor == Alliance.RED)
                        robot.driveToTarget(targetPositionsRed[0]);
                    else
                        robot.driveToTarget(targetPositionsBlue[0]);
                    firstLoop = false;
                } else {
                    firstLoop = true;
                    state++;
                }
                break;
            case 4:
                stateInfo = "Driving to beacon 1"; //beacon 1a
                if (firstLoop || robot.driveToTargetInProgress) {
                    robot.driveToTarget(beacon1a); //beacon 1b
                    firstLoop = false;
                } else {
                    firstLoop = true;
                    state++;
                }
                break;
            case 5:
                stateInfo = "Checking beacon status";
                if (!robot.pusher.isBeaconPushed()) //TODO: change this soon
                    robot.driveToTarget(beacon1b);
                 else
                    state++;
                break;
            case 6:
                stateInfo = "Driving to beacon 2";
                if (firstLoop || robot.driveToTargetInProgress) {
                    robot.driveToTarget(beacon2a);
                    firstLoop = false;
                } else {
                    firstLoop = true;
                    state++;
                }
                break;
            case 7:
                stateInfo = "Checking second beacon status";
                if (!robot.pusher.isBeaconPushed()) {
                    robot.driveToTarget(beacon2b);
                } else {
                    state++;
                }
                break;
            case 8:
                stateInfo = "Driving to position 2";
                if (firstLoop || robot.driveToTargetInProgress) {
                    if(allianceColor == Alliance.RED)
                        robot.driveToTarget(targetPositionsRed[1]);
                    else
                        robot.driveToTarget(targetPositionsBlue[1]);
                    firstLoop = false;
                } else {
                    firstLoop = true;
                    state++;
                } //TODO: figure out what this is for
            case 9:
                stateInfo = "Driving to center vortex";
                if (firstLoop || robot.driveToTargetInProgress) {
                    robot.driveToTarget(centerVortex);
                    firstLoop = false;
                } else {
                    stateInfo = "AutoOp done :)";
                }
                break;
        }

        telemetry.addData("State Number", stateInfo);
        telemetry.addData("Robot Position", "(" + robot.currentCoord.getX() + "," + robot.currentCoord.getY() + ")");
        telemetry.addData("Robot Heading", robot.currentCoord.getHeading());
    }
}
