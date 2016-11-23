package org.whs542.ftc2017;

/**
 * Created by Jiangda on 10/30/2016.
 */
import com.qualcomm.robotcore.eventloop.opmode.*;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Position;

@Autonomous(name = "AutoOpTest", group = "AutoOp")
public class TestAutoOp extends OpMode{
    WHSRobot robot;
    int state;
    String stateInfo;
    double[] powers = {0.7, 0.8};
    final int startingPosition = 1; //1 or 2
    //Wheels, Legos, Tools, Gears
    Position[] beaconPositions = {new Position(300,1800,150), new Position(-900,1800,150), new Position(-1800,900,150), new Position(-1800,-300,150)};
    Position[] redPositions = {new Position(100,100,100), new Position(100,100,100), new Position(0,0,0)};
    Position[] bluePositions = {new Position(100,100,100), new Position(100,100,100), new Position(0,0,0)};

    public void init() {
        robot = new WHSRobot(hardwareMap);
        state = 0;
    }

    public void loop()
    {
        switch(state)
        {
            case 0:
                stateInfo = "Shoot flywheel";
                robot.flywheel.setFlywheelPower(powers[startingPosition - 1]); //need something to check if it's up to speed
                if(robot.flywheel.isFlyWheelAtRightSpeed(powers[startingPosition - 1]))
                {
                    robot.flywheel.operateGate(true);
                    state = 1;
                }
                break;
            case 1:
                stateInfo = "Driving to target position 1";
                if(robot.driveToTargetInProgress)
                {
                    robot.driveToTarget(redPositions[0]);
                }
                else
                {
                    state = 2;
                }
                break;
            case 2:
                stateInfo = "Driving to beacon 1";
                if(robot.driveToTargetInProgress)
                {
                    robot.driveToTarget(redPositions[1]);
                }
                else
                {
                    state = 3;
                }
                break;
            case 3:
                stateInfo = "Checking beacon status";
                //Check beacon status
                state = 4;
                break;
            case 4:
                stateInfo = "Driving to beacon 2";
                //Move forward until we see second beacon
                state = 5;
                break;
            case 5:
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


