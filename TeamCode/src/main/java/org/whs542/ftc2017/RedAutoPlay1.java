package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;
import org.whs542.lib.Position;

/**
 * Red Auto Play 1
 */

@Autonomous(name = "RedAutoPlay1", group = "AutoOp")
public class RedAutoPlay1 extends OpMode {
    WHSRobot robot;
    int state;
    int wait;
    int loop;
    String stateInfo;
    double[] powers = {0.7, 0.8};
    final int startingPosition = 1; //1 or 2
    //Red: Tools, Gears
    Position[] beaconPositions = {new Position(-1800,900,150), new Position(-1800,-300,150)};
    //first: align to parallel beacons, second: end of beacons, third: center vortex
    Position[] redPositions = {new Position(-1650,600,100), new Position(-1650,600,150), new Position(0,0,150)};

    public void init() {
        robot = new WHSRobot(hardwareMap, Alliance.RED);
        state = 0;
        wait = 1000;
        loop = 1;
    }

    public void loop()
    {
        switch(state)
        {
            case 0:
                stateInfo = "Turning to vortex";
                if(robot.rotateToTargetInProgress || loop == 1)
                {
                    robot.rotateToVortex();
                    loop = 2;
                }
                else {
                    loop = 1;
                    state++;
                }
                break;
            case 1:
                stateInfo = "Shooting particles";
                robot.flywheel.setFlywheelPower(powers[startingPosition - 1]); //need something to check if it's up to speed
                if(robot.flywheel.isFlywheelAtCorrectSpeed(powers[startingPosition - 1]))
                {
                    robot.flywheel.operateGate(1.0);
                    robot.intake.runIntake(1.0);
                    if(wait > 0)
                    {
                        wait--;
                    }
                    state++;
                    robot.intake.runIntake(0.0);
                    robot.flywheel.setFlywheelPower(0.0);
                    robot.flywheel.operateGate(1.0);
                }
                break;
            case 2:
                stateInfo = "Driving to target position 1";
                if(robot.driveToTargetInProgress || loop == 1)
                {
                    robot.driveToTarget(redPositions[0]);
                    loop = 2;
                }
                else
                {
                    loop = 1;
                    state++;
                }
                break;
            case 3:
                stateInfo = "Driving to beacon 1";
                if(robot.driveToTargetInProgress || loop == 1)
                {
                    robot.driveToTarget(beaconPositions[1]);
                    loop = 2;
                }
                else
                {
                    loop = 1;
                    state++;
                }
                break;
            case 4:
                stateInfo = "Checking beacon status";
                if(!robot.pusher.isBeaconPushed())
                {
                    robot.driveToTarget(new Position());
                }
                else {state++;}
                break;
            case 5:
                stateInfo = "Driving to beacon 2";
                if(robot.driveToTargetInProgress || loop == 1)
                {
                    robot.driveToTarget(beaconPositions[0]);
                    loop = 2;
                }
                else
                {
                    loop = 1;
                    state++;
                }
                break;
            case 6:
                stateInfo = "Checking second beacon status";
                if(!robot.pusher.isBeaconPushed())
                {
                    robot.driveToTarget(new Position());
                }
                else {state++;}
                break;
            case 7:
                stateInfo = "Driving to center vortex";
                if(robot.driveToTargetInProgress || loop == 1)
                {
                    robot.driveToTarget(redPositions[2]);
                    loop = 2;
                }
                else
                {
                    stateInfo = "AutoOp done :)";
                }
                break;
        }

        telemetry.addData("State Number: ", stateInfo);
    }
}

