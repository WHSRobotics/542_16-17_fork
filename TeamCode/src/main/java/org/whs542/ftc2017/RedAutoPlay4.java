package org.whs542.ftc2017;

/**
 * Red Play 4 NEED TO FIX - LUCY
 */
import com.qualcomm.robotcore.eventloop.opmode.*;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;
import org.whs542.lib.Position;

import java.util.Objects;

//Play 4
@Autonomous(name = "RedAutoPlay4", group = "AutoOp")
public class RedAutoPlay4 extends OpMode{
    WHSRobot robot;
    int state;
    String stateInfo;
    double[] powers = {0.7, 0.8};
    final int startingPosition = 1; //1 or 2
    //Wheels, Legos, Tools, Gears
    Position[] beaconPositions = {new Position(300,1800,150), new Position(-900,1800,150), new Position(-1800,900,150), new Position(-1800,-300,150)};
    //first: align to parallel beacons, second: end of beacons, third: center vortex
    Position[] redPositions = {new Position(-1650,600,100), new Position(-1650,600,150), new Position(0,0,150)};
    //first: align to parallel beacons, second: end of beacons, third: center vortex
    Position[] bluePositions = {new Position(600,1650,150), new Position(-600,1650,150), new Position(0,0,150)};
    //Blue Vortex, Red Vortex
    Position[] vortexPositions = {new Position(300, 300, 150), new Position(-300, -300, 150)};
    //Directions of the wall
    //Blue, Red
    double[] beaconWall = {180, 90};

    public void init() {
        robot = new WHSRobot(hardwareMap, Alliance.RED);
        state = 0;
    }

    public void loop()
    {
        switch(state)
        {
            case 0:

                stateInfo = "Rotating to face flywheel";
                robot.rotateToVortex(vortexPositions[1]);
                if(!robot.rotateToTargetInProgress){
                    state++;
                }
                break;
            case 1:
                stateInfo = "Shoot flywheel";
                robot.flywheel.setFlywheelPower(powers[startingPosition - 1]);
                if(robot.flywheel.isFlywheelAtCorrectSpeed(powers[startingPosition - 1]))
                {
                    robot.flywheel.operateGate(true);
                    try {
                        Thread.sleep(100);                  //Give the particles a little bit of time to reach the flywheel
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.flywheel.setFlywheelPower(0.0);
                    state++;
                }
                break;
            case 2:
                stateInfo = "Driving to target position 1";
                robot.driveToTarget(redPositions[0]);
                if(!robot.driveToTargetInProgress) {
                    state++;
                }
                break;
            case 4:
                stateInfo = "Turning to face beacon";
                robot.rotateToTarget(beaconWall[1]);
                if(!robot.rotateToTargetInProgress){
                    state++;
                }
                break;
            case 5:
                stateInfo = "Driving to beacon 1";
                robot.driveToTarget(redPositions[1]);
                if(!robot.driveToTargetInProgress)
                {
                    state++;
                }
                break;
            case 6:
            stateInfo = "Checking beacon status";
            if (Objects.equals(robot.pusher.isBeaconPushed(), "match")) {
                robot.pusher.extendPusherNoToggle(true);
            }
            state++;
            break;
            case 7:
            stateInfo = "Pressing beacon";
            if (robot.beaconState == "Extended") {
                robot.pusher.extendPusherNoToggle(false);
            }
            state++;
            break;
            case 8:
            stateInfo = "Driving to center vortex";
            if(robot.driveToTargetInProgress)
            {
                robot.driveToTarget(redPositions[2]);
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


