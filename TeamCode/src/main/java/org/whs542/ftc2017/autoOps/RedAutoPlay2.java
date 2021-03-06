package org.whs542.ftc2017.autoops;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Coordinate;
import org.whs542.lib.Position;

/**
 * Red Auto Play 3
 */

@Autonomous(name = "Red Auto Play 3", group = "WHSAutoOp")
@Disabled
@Deprecated

public class RedAutoPlay2 extends OpMode {
    WHSRobot robot;
    int state;
    String stateInfo;
    double[] powers = {0.7, 0.8};
    final int startingPosition = 1; //1 or 2
    //Wheels, Legos, Tools, Gears
    Position[] beaconPositions = {new Position(300,1800,150), new Position(-900,1800,150), new Position(-1800,900,150), new Position(-1800,-300,150)};
    Position[] redPositions = {new Position(100,100,100), new Position(100,100,100), new Position(0,0,0)};
    Position[] bluePositions = {new Position(100,100,100), new Position(100,100,100), new Position(0,0,0)};
    Position[] vortexPositions = {new Position(300, 300, 150), new Position(-300, -300, 150)};
    Coordinate[] startingPositions = {new Coordinate(-300, -1500, 150, 90), new Coordinate(0, -1500, 150, 90), new Coordinate(300, -1500, 150, 90)};

    @Override
    public void init() {
        robot = new WHSRobot(hardwareMap);
        state = 0;
    }

    @Override
    public void loop() {

        switch (state){
            case 0:

                stateInfo = "turning to red vortex";
                robot.rotateToVortex(vortexPositions[1]);

                if(!robot.rotateToTargetInProgress){
                    state++;
                }
                break;
            case 1:
                stateInfo = "Shooting particles";
                robot.flywheel.setFlywheelPower(powers[startingPosition - 1]);
                if(robot.flywheel.isFlywheelAtCorrectSpeed(powers[startingPosition - 1])){
                    robot.flywheel.operateGate(true);

                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.flywheel.setFlywheelPower(0);
                    state++;
                }
                break;
            case 2:
     stateInfo = "driving to position 1";
                robot.driveToTarget(redPositions[0]);
                if(!robot.driveToTargetInProgress){
                    state++;
                }
            case 3:

                stateInfo = "driving to center";
                robot.driveToTarget(new Position(0, -300, 150));
                if(!robot.driveToTargetInProgress) {
                    state++;
                }
                break;
            case 4:
                break;


        }
        telemetry.addData("StateInfo:", stateInfo);

    }
}
