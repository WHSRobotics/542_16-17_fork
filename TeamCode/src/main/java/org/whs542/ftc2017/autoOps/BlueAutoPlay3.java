package org.whs542.ftc2017.autoOps;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;
import org.whs542.lib.Coordinate;
import org.whs542.lib.Functions;
import org.whs542.lib.Position;

/**
 *
 */

@Autonomous(name = "BlueAutoOp3", group = "Autonomous")
@Disabled
@Deprecated

public class BlueAutoPlay3 extends OpMode
{
    WHSRobot robot;
    int state;
    String stateInfo;
    double[] powers = {0.7, 0.8};
    final int startingPosition = 1; //1 or 2
    final long particleDelay = 300;
    //Wheels, Legos, Tools, Gears
    Position[] beaconPositions = {new Position(300,1800,150), new Position(-900,1800,150), new Position(-1800,900,150), new Position(-1800,-300,150)};
    //firstLoop: align to parallel beacons, second: end of beacons, third: center vortex
    Position[] redPositions = {new Position(-1650,600,100), new Position(-1650,600,150), new Position(0,0,150)};
    //firstLoop: align to parallel beacons, second: end of beacons, third: center vortex
    Position[] bluePositions = {new Position(600,1650,150), new Position(-600,1650,150), new Position(0,0,150)};
    Position[] vortexPositions = {new Position(300, 300, 150), new Position(-300, -300, 150)};
    double redBeaconWall = Functions.normalizeAngle(90);
    //Direction to be perpendicular with red beacon wall
    Coordinate[] startingPositions = {new Coordinate(-300, -1500, 150, 90), new Coordinate(0, -1500, 150, 90), new Coordinate(300, -1500, 150, 90)};
    public void init() {
        robot = new WHSRobot(hardwareMap, Alliance.RED);
        state = 0;
    }

    public void loop() {

        switch (state) {
            case 0:
                stateInfo = "Rotating to face flywheel";
                robot.rotateToVortex(vortexPositions[1]);
                if (!robot.rotateToTargetInProgress) {
                    state++;
                }
                break;
            case 1:
                stateInfo = "Shoot flywheel";
                robot.flywheel.setFlywheelPower(powers[startingPosition - 1]);
                if (robot.flywheel.isFlywheelAtCorrectSpeed(powers[startingPosition - 1])) {
                    robot.intake.runIntake(1.0);
                    robot.flywheel.operateGate(true);
                    try {
                        Thread.sleep(particleDelay);        //Give the particles a little bit of time to reach the flywheel
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
                if (!robot.driveToTargetInProgress) {
                    state++;
                }
                break;
            case 3:
                stateInfo = "Driving closer to beacon";
                robot.driveToTarget(redPositions[1]);
                if (!robot.driveToTargetInProgress) {
                    state++;
                }
                break;
            case 4:
                stateInfo = "Checking beacon status";
                if (robot.pusher.isBeaconPushed()) {          //Beacon color matches
                    state++;
                } else {
                    state = 6;                             //Beacon color does not match
                }
                state++;
                break;
            case 5:
                stateInfo = "Driving to center vortex";
                robot.driveToTarget(redPositions[2]);
                if (!robot.driveToTargetInProgress) {
                    stateInfo = "AutoOp done :)";
                    state++;
                }
                break;
            case 6: //This case will only run if the beacon does not match
                stateInfo = "Beacon did not match, moving forwards";
                robot.driveToTarget(redPositions[3]);
                if (!robot.driveToTargetInProgress) {
                    state = 4;
                }
                break;
            default: break;
        }
        telemetry.addData("State Number: ", stateInfo);
    }
}
