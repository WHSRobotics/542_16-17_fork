package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whs542.ftc2017.subsys.WHSRobot;

/**
 * Created by Amar2 on 10/22/2016.
 */
@TeleOp(name = "TeleOpTest", group = "WHSTeleOp")
public class TestTeleOp extends OpMode{

    WHSRobot robot;
    //IMU imu;


    @Override
    public void init() {
        robot = new WHSRobot(hardwareMap);

    }

    @Override
    public void loop() {
        //Gamepad 1
        robot.drivetrain.setLRScaledPower(gamepad1.left_stick_y, gamepad1.right_stick_y);
        robot.drivetrain.setOrientation(gamepad1.a);
        robot.intake.runIntake(gamepad1.right_trigger, gamepad1.left_trigger, 1.0);
        robot.capball.dropFork(gamepad1.dpad_down);
        robot.capball.liftCapball(gamepad1.dpad_up);

        //Gamepad 2
        //TODO: FIX THESE WHENEVER POSSIBLE, add power inc/dec and fix run
        //robot.flywheel.testRun(gamepad2.dpad_up, gamepad1.dpad_down);
        robot.flywheel.getFlywheelMode(gamepad2.a);
        robot.flywheel.operateGate(gamepad2.right_trigger);

        //Telemetry
        telemetry.addData("Robot Approx. Location: ", robot.flywheel.getFlywheelMode(gamepad2.a));
        telemetry.addData("FWheelStat:", robot.flywheel.getFlywheelStatus());
        telemetry.addData("FGateStat:", robot.flywheel.getGateStatus());

        telemetry.addData("LeftStick Y:", gamepad1.left_stick_y);
        telemetry.addData("RightStick Y:", gamepad1.right_stick_y);
        telemetry.addData("Orientation:", robot.drivetrain.getOrientation());

        telemetry.addData("Intake:", robot.intake.getIntakeDirection(gamepad1.left_bumper, gamepad1.left_trigger));

        //telemetry.addData("Flywheel Speed:", robot.flywheel.getSpeed());

        telemetry.addData("Capball Fork:", robot.capball.getForkState());
        telemetry.addData("Capball Lift:", robot.capball.getLiftState());

        //test for flywheel velocity vs distance
        /*robot.flywheel.test(gamepad1.a, 1);
        robot.flywheel.test(gamepad1.a, 0.9);
        robot.flywheel.test(gamepad1.a, 0.8);
        robot.flywheel.test(gamepad1.a, 0.7);
        robot.flywheel.test(gamepad1.a, 0.6);

        telemetry.addData("Velocity", robot.flywheel.getCurrentVelocity());*/
    }
}
