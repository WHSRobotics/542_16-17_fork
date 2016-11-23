package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whs542.ftc2017.subsys.WHSRobot;

/**
 * Created by Lucy on 11/19/2016.
 */

@TeleOp(name = "WHSTeleOp", group = "WHSTeleOp")
public class WHSTeleOp extends OpMode
{
    WHSRobot robot;

    public void init()
    {
        robot = new WHSRobot(hardwareMap);
    }

    public void loop()
    {
        //Gamepad 1 Controls
        robot.drivetrain.setLRScaledPower(gamepad1.left_stick_y, gamepad1.right_stick_y);
        robot.drivetrain.setOrientation(gamepad1.a);
        robot.intake.runIntake(gamepad1.right_trigger, gamepad1.left_trigger, 1.0);
        robot.capball.dropFork(gamepad1.dpad_down);
        robot.capball.liftCapball(gamepad1.dpad_up);
        robot.capball.changeServo(gamepad1.right_bumper);

        //Gamepad 2 Controls
        robot.flywheel.getFlywheelMode(gamepad2.a);
        robot.flywheel.rampFlywheel(gamepad2.right_bumper);
        robot.flywheel.operateGate(gamepad2.right_trigger);

        //Telemetry
        telemetry.addData("Robot Approx. Location: ", robot.flywheel.getFlywheelMode(gamepad2.a));
        telemetry.addData("FWheelStat:", robot.flywheel.getFlywheelStatus());
        telemetry.addData("FGateStat:", robot.flywheel.getGateStatus());

        telemetry.addData("LeftStick Y:", gamepad1.left_stick_y);
        telemetry.addData("RightStick Y:", gamepad1.right_stick_y);
        telemetry.addData("Orientation:", robot.drivetrain.getOrientation());

        telemetry.addData("Intake:", robot.intake.getIntakeDirection(gamepad1.left_bumper, gamepad1.left_trigger));

        telemetry.addData("Capball Fork:", robot.capball.getForkState());
        telemetry.addData("Capball Lift:", robot.capball.getLiftState());
    }

}
