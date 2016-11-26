package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;
import org.whs542.lib.Position;

import java.util.Objects;

/**
 * Created by Lucy on 11/23/2016.
 */

//Play 3
@Autonomous(name = "AutoOpTest3", group = "Autonomous")
public class RedAutoPlay3 extends OpMode
{
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
    Position[] vortexPositions = {new Position(300, 300, 150), new Position(-300, -300, 150)};

    public void init() {
        robot = new WHSRobot(hardwareMap, Alliance.RED);
        state = 0;
    }

   public void loop() {

        switch (state){
            case 0:
                stateInfo = "rotating to red vortex";
                robot.rotateToVortex(vortexPositions[1]);
                if(!robot.rotateToTargetInProgress){
                    state++;
                }
            case 1:
                stateInfo = "Shoot flywheel";
                robot.flywheel.setFlywheelPower(powers[startingPosition - 1]); //need something to check if it's up to speed
                if (true) {
                    robot.flywheel.operateGate(1.0);
                    state = 1;
                }
                break;
            case 2:
                stateInfo = "Driving to position 1";
                if (robot.driveToTargetInProgress) {
                    robot.driveToTarget(redPositions[0]);
                } else {
                    state = 2;
                }
                break;
            case 3:
                stateInfo = "Driving to beacon 1";
                if (robot.driveToTargetInProgress) {
                    robot.driveToTarget(redPositions[1]);
                } else {
                    state = 3;
                }
                break;
            case 4:

            case 5:
                stateInfo = "Checking beacon status";
                if (robot.pusher.isBeaconPushed()) {
                    robot.pusher.extendPusherNoToggle(true);
                }
                else if(Objects.equals(robot.pusher.isBeaconPushed(), "notmatch")){

                }
                state = 5;
                break;
            case 6:
                stateInfo = "Pressing beacon";
                if (robot.beaconState == "Extended") {
                    robot.pusher.extendPusherNoToggle(false);
                }
                state = 6;
                break;
            case 7:
                stateInfo = "Driving to center vortex";
                if (robot.driveToTargetInProgress) {
                    robot.driveToTarget(redPositions[2]);
                } else {
                    stateInfo = "AutoOp done :)";
                    state = 7;
                }
                break;
            default: break;
        }
       telemetry.addData("State Number: ", stateInfo);
    }
}
