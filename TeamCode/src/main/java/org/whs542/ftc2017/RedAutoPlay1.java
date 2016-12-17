package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;
import org.whs542.lib.Position;

/**
 * Red Auto Play 1
 */

@Autonomous(name = "RedAutoPlay1", group = "AutoOp")
@Disabled

public class RedAutoPlay1 extends OpMode {
    WHSRobot robot;

    int state;
    int test;
    boolean firstLoop;

    long particleDelay;
    String stateInfo;
    double[] powers = {0.3, 0.8};
    final int startingPosition = 1; //1 or 2

    //Red: Tools, Gears
    Position[] beaconPositions = {new Position(-1800,900,150), new Position(-1800,-300,150)};
    //firstLoop: align to parallel beacons, second: end of beacons, third: center vortex
    Position[] redPositions = {new Position(-1650,600,100), new Position(-1650,600,150), new Position(0,0,150), new Position(-1800, 1000, 150) };
    Position[] vortexPositions = {new Position(300, 300, 150), new Position(-300, -300, 150)};

    public void init() {
        robot = new WHSRobot(hardwareMap, Alliance.RED);
        state = 0;
        firstLoop = true;
        test = 100;
        particleDelay = 300;
    }

    public void loop()
    {
        switch(state)
        {
            case 0:
                stateInfo = "Turning to vortex and ramping flywheel";
                if(test > 0) {
                    robot.flywheel.setFlywheelPower(powers[startingPosition - 1]);
                    test--;
                }
                if(robot.rotateToTargetInProgress)
                {
                    robot.rotateToVortex(vortexPositions[1]);
                    firstLoop = false;
                }
                else {
                    firstLoop = true;
                    //state++;
                }
                break;
            case 1:
                stateInfo = "Shooting particles";
                if (robot.flywheel.isFlywheelAtCorrectSpeed(powers[startingPosition - 1]))
                {
                    robot.flywheel.operateGate(true);
                    robot.intake.runIntake(1.0);

                    try {
                        Thread.sleep(particleDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    robot.intake.runIntake(0.0);
                    //maybe close particle gate

                    try {
                        Thread.sleep(particleDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    robot.flywheel.setFlywheelPower(0.0);
                    robot.flywheel.operateGate(false);
                    state++;
                }
                break;
            case 2:
                stateInfo = "Driving to target position 1";
                if (firstLoop || robot.driveToTargetInProgress) {
                    robot.driveToTarget(redPositions[0]);
                    firstLoop = false;
                }
                else{
                    firstLoop = true;
                    state++;
                }
                break;
            case 3:
                stateInfo = "Driving to beacon 1";
                if (firstLoop || robot.driveToTargetInProgress) {
                    robot.driveToTarget(beaconPositions[1]);
                    firstLoop = false;
                }
                else {
                    firstLoop = true;
                    state++;
                }
                break;
            case 4:
                stateInfo = "Checking beacon status";
                if (robot.pusher.isBeaconPushed()) state++;
                else state = 100;
                break;
            case 5:
                stateInfo = "Driving to beacon 2";
                if (robot.driveToTargetInProgress) state++;
                else state = 100;
                break;
            case 6:
                stateInfo = "Checking second beacon status";
                if(robot.pusher.isBeaconPushed()) {
                    robot.driveToTarget(redPositions[3]);
                } else state++;
                break;
            case 7:
                stateInfo = "Driving to center vortex";
                if(robot.driveToTargetInProgress) {
                    robot.driveToTarget(redPositions[2]);
                } else stateInfo = "AutoOp done :)";
                break;
        }

        telemetry.addData("State Number: ", stateInfo);
        telemetry.addData("Test #: ", test);
    }
}

