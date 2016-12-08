package org.whs542.lib;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whs542.ftc2017.subsys.WHSRobot;

/**
 * Created by Jason on 12/7/2016.
 */

@TeleOp(name = "Drive to Target Test", group = "TeleOp")
public class DriveToTargetTest extends OpMode{
    WHSRobot robot;

    @Override
    public void init() {
        robot = new WHSRobot(hardwareMap, Alliance.BLUE);
        robot.setInitialCoordinate(new Coordinate(1200, 0 , 150, 90));
    }

    @Override
    public void loop() {
        robot.driveToTarget(new Position(1200, 400, 150));
        if (!robot.driveToTargetInProgress){
            requestOpModeStop();
        }
    }
}
