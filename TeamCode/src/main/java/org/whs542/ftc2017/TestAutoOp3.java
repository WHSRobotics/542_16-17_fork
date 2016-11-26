package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Position;

/**
 * Created by Lucy on 11/23/2016.
 */

//Play 3
@Autonomous(name = "AutoOpTest3", group = "Autonomous")
public class TestAutoOp3 extends OpMode
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

    public void init() {
        robot = new WHSRobot(hardwareMap);
        state = 0;
    }

   public void loop() {

        switch (state){
            case 0:
                stateInfo = "Shoot flywheel";
                robot.flywheel.setFlywheelPower(powers[startingPosition - 1]); //need something to check if it's up to speed
                if (true) {
                    robot.flywheel.operateGate(true);
                    state = 1;
                }
                break;
            case 1:
                stateInfo = "Driving to position 1";
                if (robot.driveToTargetInProgress) {
                    robot.driveToTarget(redPositions[0]);
                } else {
                    state = 2;
                }
                break;
            case 2:
                stateInfo = "Driving to beacon 1";
                if (robot.driveToTargetInProgress) {
                    robot.driveToTarget(redPositions[1]);
                } else {
                    state = 3;
                }
                break;
            case 3:
                stateInfo = "Checking beacon status";
                if (robot.color = )
                state = 4;
                break;
        }

    }
}
