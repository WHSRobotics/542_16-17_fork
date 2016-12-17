package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;
import org.whs542.lib.Coordinate;
import org.whs542.lib.Position;

/**
 * Blue Auto Play 1
 */

@Autonomous(name = "BlueAutoPlay1", group = "AutoOp")
@Disabled

public class BlueAutoPlay1 extends OpMode {
    WHSRobot robot;

    int state;
    boolean firstLoop;
    int test;

    long particleDelay;
    String stateInfo;
    double[] powers = {0.7, 0.8};
    final int startingPosition = 1; //1 or 2
    Alliance side = Alliance.BLUE;

    //Blue: Wheels, Legos
    Position[] beaconPositions = {new Position(300,1800,150), new Position(-900,1800,150)};
    //firstLoop: align to parallel beacons, second: end of beacons, third: center vortex
    Position[] bluePositions = {new Position(600,1650,150), new Position(-600,1650,150), new Position(0,0,150)};
    //Blue first coord, red is second
    Position[] vortexPositions = {new Position(300, 300, 150), new Position(-300, -300, 150)};

    Coordinate[] startingPositions = {new Coordinate(-300, -1500, 150, 180), new Coordinate(1500, -300, 150, 180), new Coordinate(1500, 300, 150, 180)};


    public void init() {
        robot = new WHSRobot(hardwareMap, side);
        robot.setInitialCoordinate(startingPositions[1]);
        state = 0;
        test = 100;
        firstLoop = true;
    }

    public void loop()
    {
        robot.estimatePosition();
        robot.estimateHeading();

        switch(state)
        {
            case 0:
                stateInfo = "Moving forward";
                robot.driveToTarget(new Position(startingPositions[1].getX() - 500 , startingPositions[1].getY(), 150));
                if(!robot.driveToTargetInProgress){
                    state++;
                }
                break;

            case 1:
                stateInfo = "Turning to vortex";
                /*if(test > 0) {
                    robot.flywheel.setFlywheelPower(powers[startingPosition - 1]); //TODO: change location for this so that it starts a little later
                    test--;
                }*/
                if(firstLoop || robot.rotateToTargetInProgress)
                {

                    robot.rotateToTarget(30);
                    firstLoop = false;
                }
                else {
                    firstLoop = true;
                    //state++;
                }
                telemetry.addData("Loop:", firstLoop);
                break;
            case 2:
                stateInfo = "Shooting particles";
                robot.flywheel.setFlywheelPower(powers[startingPosition - 1]); //need something to check if it's up to speed
                if(robot.flywheel.isFlywheelAtCorrectSpeed(powers[startingPosition - 1]))
                {
                    robot.flywheel.operateGate(true);
                    robot.intake.runIntake(1.0);
                    if(test > 0)
                    {
                        test--;
                    }
                    state++;
                    robot.intake.runIntake(0.0);
                    robot.flywheel.setFlywheelPower(0.0);
                    robot.flywheel.operateGate(false);
                }
                break;
            case 3:
                stateInfo = "Driving to target position 1";
                if(firstLoop || robot.driveToTargetInProgress)
                {
                    robot.driveToTarget(bluePositions[0]);
                    firstLoop = false;
                }
                else
                {
                    firstLoop = true;
                    state++;
                }
                break;
            case 4:
                stateInfo = "Driving to beacon 1";
                if(firstLoop || robot.driveToTargetInProgress)
                {
                    robot.driveToTarget(beaconPositions[1]);
                    firstLoop = false;
                }
                else
                {
                    firstLoop = true;
                    state++;
                }
                break;
            case 5:
                stateInfo = "Checking beacon status";
                if(!robot.pusher.isBeaconPushed())
                {
                    robot.driveToTarget(new Position(-890,1800,150));
                }
                else {state++;}
                break;
            case 6:
                stateInfo = "Driving to beacon 2";
                if(firstLoop || robot.driveToTargetInProgress)
                {
                    robot.driveToTarget(beaconPositions[0]);
                    firstLoop = false;
                }
                else
                {
                    firstLoop = true;
                    state++;
                }
                break;
            case 7:
                stateInfo = "Checking second beacon status";
                if(!robot.pusher.isBeaconPushed())
                {
                    robot.driveToTarget(new Position(310,1800,150));
                }
                else {state++;}
                break;
            case 8:
                stateInfo = "Driving to position 2";
                if(firstLoop || robot.driveToTargetInProgress){
                    robot.driveToTarget(beaconPositions[1]);
                    firstLoop = false;
                }
                else{
                    firstLoop = true;
                    state++;
                }


            case 9:
                stateInfo = "Driving to center vortex";
                if(firstLoop || robot.driveToTargetInProgress)
                {
                    robot.driveToTarget(bluePositions[2]);
                    firstLoop = false;
                }
                else
                {
                    stateInfo = "AutoOp done :)";
                }
                break;
        }

        telemetry.addData("State Number", stateInfo);
        telemetry.addData("Robot Position", "(" + robot.currentCoord.getX() + "," + robot.currentCoord.getY() + ")") ;
        telemetry.addData("Robot Heading", robot.currentCoord.getHeading());
        telemetry.addData("Rotate to target in progress?", robot.rotateToTargetInProgress);
        telemetry.addData("Drive to target in progress?", robot.driveToTargetInProgress);
    }
}
