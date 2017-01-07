package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whs542.ftc2017.subsys.WHSRobot;

/**
 * Created by Lucy on 12/17/2016.
 */

@TeleOp(name = "WHS Teleop 2", group = "WHSTeleOp")
//@Disabled

public class WHSTeleOp2 extends OpMode{

    WHSRobot robot;

    @Override
    public void init() {
        robot = new WHSRobot(hardwareMap);
    }

    @Override
    public void loop() {
        //Gamepad 1
        robot.drivetrain.setLRScaledPower(gamepad1.left_stick_y, gamepad1.right_stick_y);
        robot.drivetrain.setOrientation(gamepad1.a);
        robot.intake.runIntake(gamepad1.right_trigger, gamepad1.left_trigger);
        robot.pusher.extendPusherNoToggle(gamepad1.left_bumper);

        //Gamepad 2
        robot.flywheel2.runFlywheel(gamepad2.left_bumper);
        double power = robot.flywheel2.setFlywheelSpeedExperimental(gamepad2.dpad_up, gamepad2.dpad_down);
        robot.flywheel2.setParticleControlState(gamepad2.left_trigger);

        //Telemetry
        telemetry.addData("Flywheel Power", power);
        telemetry.addData("Orientation", robot.drivetrain.getOrientation());
        telemetry.addData("PC", robot.flywheel2.getParticleControlState());

        telemetry.addData("LeftStick Y:", gamepad1.left_stick_y);
        telemetry.addData("RightStick Y:", gamepad1.right_stick_y);

        telemetry.addData("Intake:", robot.intake.getIntakeState());
    }
}
