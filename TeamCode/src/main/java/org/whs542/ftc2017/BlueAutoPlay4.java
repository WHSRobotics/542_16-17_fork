package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;
import org.whs542.lib.Coordinate;
import org.whs542.lib.Functions;
import org.whs542.lib.Position;

import java.util.Objects;

/**
 * Created by jian on 12/2/2016.
 */

//Play 4
@Autonomous(name = "BlueAutoPlay4", group = "AutoOp")
//@Disabled
public class BlueAutoPlay4 extends OpMode {

    WHSRobot robot;
    int state;
    String stateInfo;
    double[] powers = {0.7, 0.8};
    final int startingPosition = 1; //1 or 2
    final long particleDelay = 300; //in milliseconds
    Alliance side = Alliance.BLUE;
    //Wheels, Legos, Tools, Gears
    Position[] beaconPositions = {new Position(300,1800,150), new Position(-900,1800,150), new Position(-1800,900,150), new Position(-1800,-300,150)};

    //firstLoop: align to parallel beacons, second: end of beacons, third: center vortex
    //Position[] redPositions = {new Position(-1650,600,100), new Position(-1650,600,150), new Position(0,0,150)};
    //firstLoop: get close to beacons, second: get in front / within pushing range of beacon, third: center vortex, fourth: if the beacon color doesn't match
    Position[] redPositions = {new Position(-1200, 900, 150), new Position(-1300, 900, 150), new Position(0, 0, 150), new Position(-1300, 800, 150)};

    //firstLoop: align to parallel beacons, second: end of beacons, third: center vortex
    Position[] bluePositions = {new Position(600,1650,150), new Position(-600,1650,150), new Position(0,0,150)};
    //Blue Vortex, Red Vortex
    Position[] vortexPositions = {new Position(300, 300, 150), new Position(-300, -300, 150)};
    //Directions the robot needs to face to parallel walls
    double blueBeaconWall = Functions.normalizeAngle(180);
    double redBeaconWall = Functions.normalizeAngle(90);
    //Direction to be perpendicular with red beacon wall
    Coordinate[] startingPositions = {new Coordinate(-300, -1500, 150, 90), new Coordinate(0, -1500, 150, 90), new Coordinate(300, -1500, 150, 90)};

    public void init() {
        robot = new WHSRobot(hardwareMap, Alliance.BLUE);
        state = 0;
    }

    public void loop()
    {
        switch(state)
        {
            case 0:
                stateInfo = "Rotating to face flywheel";
                robot.rotateToVortex(vortexPositions[1]); //FIXME
                if(!robot.rotateToTargetInProgress){
                    state++;
                }
                break;
            case 1:
                stateInfo = "Shoot flywheel";
                robot.flywheel.setFlywheelPower(powers[startingPosition - 1]);
                if(robot.flywheel.isFlywheelAtCorrectSpeed(powers[startingPosition - 1]))
                {
                    robot.flywheel.operateGateNoToggle(true);
                    try {
                        Thread.sleep(particleDelay);                  //Give the particles a little bit of time to reach the flywheel
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.flywheel.setFlywheelPower(0.0);
                    state++;
                }
                break;
            case 2:
                stateInfo = "Driving to target position 1";
                robot.driveToTarget(bluePositions[0]);
                if(!robot.driveToTargetInProgress) {
                    state++;
                }
                break;
            case 3:
                stateInfo = "Driving closer to beacon";
                robot.driveToTarget(bluePositions[1]);
                if(!robot.rotateToTargetInProgress){
                    state++;
                }
                break;
            case 4:
                stateInfo = "Turning to parallel wall";
                robot.rotateToTarget(blueBeaconWall);
                if(!robot.rotateToTargetInProgress){
                    state++;
                }
                break;
            case 5:
                stateInfo = "Checking beacon status, pressing if match";
                if (robot.pusher.isBeaconPushed()) {
                    state++;
                }
                else {                          //If the beacon color does not match, go to case 100
                    state = 100;
                }
                break;
            case 6:
                stateInfo = "Depressing beacon";
                if (Objects.equals(robot.beaconState, "Extended")) {
                    robot.pusher.extendPusherNoToggle(false);
                }
                state++;
                break;
            case 7:
                stateInfo = "Driving to center vortex";
                robot.driveToTarget(bluePositions[2]);
                if(!robot.driveToTargetInProgress) {
                    stateInfo = "AutoOp done :) (if you made it this far, congratz)";
                    state++;
                }
                break;
            case 100: /*This case will only run if the beacon does not match*/
                stateInfo = "Beacon did not match, moving forwards";
                robot.driveToTarget(bluePositions[3]);
                if (!robot.driveToTargetInProgress){
                    state = 5;
                }
                break;
            default: break;
        }

        telemetry.addData("State Info: ", stateInfo);
        telemetry.addData("State Number", state);
    }
}