package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.IMU;

/**
 * Created by Amar2 on 10/22/2016.
 */
@TeleOp(name = "TeleOpTest", group = "TeleOp")
public class TestTeleOp extends OpMode{

    WHSRobot robot;
    //IMU imu;


    @Override
    public void init() {
        robot = new WHSRobot(hardwareMap);

    }

    @Override
    public void loop() {
        robot.drivetrain.setLRScaledPower(gamepad1.left_stick_y, gamepad1.right_stick_y);
        robot.drivetrain.setOrientation(gamepad1.a);
        robot.intake.runIntake(gamepad1.left_bumper, gamepad1.left_trigger, 1.0);
        robot.flywheel.run(gamepad1.right_bumper, 0.5);
        robot.flywheel.operateGate(gamepad1.right_trigger);

        //Telemetry
        telemetry.addData("LeftStick Y:", gamepad1.left_stick_y);
        telemetry.addData("RightStick Y:", gamepad1.right_stick_y);
        telemetry.addData("Orientation:", robot.drivetrain.getOrientation());

        telemetry.addData("Trigger:", gamepad1.left_trigger);

        telemetry.addData("FWheelStat:", robot.flywheel.getFlywheelStatus());
        telemetry.addData("FGateStat:", robot.flywheel.getGateStatus());

        //test for flywheel velocity vs distance
        /*robot.flywheel.test(gamepad1.a, 1);
        robot.flywheel.test(gamepad1.a, 0.9);
        robot.flywheel.test(gamepad1.a, 0.8);
        robot.flywheel.test(gamepad1.a, 0.7);
        robot.flywheel.test(gamepad1.a, 0.6);

        telemetry.addData("Velocity", robot.flywheel.getCurrentVelocity());*/
    }
}
