package org.whs542.lib.hwtest;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;
import org.whs542.lib.Coordinate;
import org.whs542.lib.OpModeTimer;

/**
 * Created by Amar2 on 12/2/2016.
 */
@Autonomous(name = "Rotate to target test", group = "")
//@Disabled
public class RotateToTargetTest extends OpMode {

    WHSRobot robot;

    @Override
    public void init() {
        robot = new WHSRobot(hardwareMap, Alliance.BLUE);
        robot.setInitialCoordinate(new Coordinate(1200, 0 , 150, 89));
    }

    @Override
    public void loop() {

        robot.estimateHeading();
        robot.rotateToTarget(0);
        if (!robot.rotateToTargetInProgress) {
            requestOpModeStop();
        }


        double heading = robot.imu.getHeading();

        telemetry.addData("Hdg:", heading);

    }

    @Override
    public void stop(){
        robot.vuforia.interrupt();

    }

}
