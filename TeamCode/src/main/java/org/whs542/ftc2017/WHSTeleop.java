package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whs542.ftc2017.subsys.WHSRobotSimple;

/**
 * Created by Lucy on 11/19/2016.
 */

@TeleOp(name = "WHSTeleop", group = "WHSTeleOp")
//@Disabled

public class WHSTeleop extends OpMode
{
    WHSRobotSimple robot;
    int i;

    public void init()
    {
            robot = new WHSRobotSimple(hardwareMap);
    }

    public void loop()
    {
        try {
            //Gamepad 1 Controls
            robot.drivetrain.setLRScaledPower(gamepad1.left_stick_y, gamepad1.right_stick_y);
            robot.drivetrain.setOrientation(gamepad1.a);
            robot.intake.runIntake(gamepad1.right_trigger, gamepad1.left_trigger);
            robot.pusher.extendPusherNoToggle(gamepad1.left_bumper);

            //Gamepad 2 Controls
            robot.flywheel2.setFlywheelSpeed(gamepad2.dpad_up, gamepad2.dpad_down);
            robot.flywheel2.runFlywheel(gamepad2.left_bumper);
            robot.flywheel2.setParticleControlState(gamepad2.left_trigger);
            robot.pusher.extendPusherHandNoToggle(gamepad2.right_trigger);

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //Telemetry
        telemetry.addData("Robot Approx. Location", robot.flywheel2.getFlywheelMode());
        telemetry.addData("Flywheel Status", robot.flywheel2.getFlywheelMode());
        telemetry.addData("Flywheel Gate Status", robot.flywheel2.getParticleControlState());

        telemetry.addData("Left Drivetrain", robot.drivetrain.getScaledPower(gamepad1.left_stick_y));
        telemetry.addData("Right Drivetrain", robot.drivetrain.getScaledPower(gamepad1.right_stick_y));
        telemetry.addData("Orientation", robot.drivetrain.getOrientation());

        telemetry.addData("Intake", robot.intake.getIntakeState());
    }

}
