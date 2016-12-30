package org.whs542.lib.hwtest;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;
import org.whs542.lib.Coordinate;
import org.whs542.lib.Position;

/**
 * Created by Jason on 12/7/2016.
 */

@Autonomous(name = "Drive to Target Test", group = "TeleOp")
public class DriveToTargetTest extends OpMode{
    WHSRobot robot;

    @Override
    public void init() {
        robot = new WHSRobot(hardwareMap, Alliance.BLUE);
        robot.setInitialCoordinate(new Coordinate(0, 0 , 0, 0));
    }

    @Override
    public void loop() {
        robot.estimateHeading();
        robot.estimatePosition();


        robot.driveToTarget(new Position(0, 600, 0));
        /*
        if (!robot.driveToTargetInProgress){
            requestOpModeStop();
        }
        */
    }
}
