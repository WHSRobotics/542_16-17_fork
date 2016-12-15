package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;

/**
 * Created by Lucy on 11/19/2016.
 */

@TeleOp(name = "WHSBlueTeleop", group = "TeleOp")
public class WHSBlueTeleOp extends OpMode
{
    WHSRobot robot;
    int i;

    public void init()
    {
            robot = new WHSRobot(hardwareMap, Alliance.BLUE);
    }

    public void loop()
    {
        try {
            //Gamepad 1 Controls
            robot.drivetrain.setLRScaledPower(gamepad1.left_stick_y, gamepad1.right_stick_y);
            robot.drivetrain.setOrientation(gamepad1.a);
            robot.intake.runIntake(gamepad1.right_trigger, gamepad1.left_trigger);
            robot.pusher.extendPusher(gamepad1.left_bumper);

            //Gamepad 2 Controls
            robot.flywheel.setFlywheelMode(gamepad2.dpad_up, gamepad2.dpad_down);
            robot.flywheel.rampFlywheel(gamepad2.left_bumper);
            //robot.flywheel.operateGate(gamepad2.left_trigger);
            //robot.capball.liftCB(gamepad2.dpad_up);
            //robot.capball.dropCB(gamepad2.dpad_down);
            //robot.capball.changeServo(gamepad2.right_bumper);
            //robot.capball.changeRatchet(gamepad2.right_trigger);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //Telemetry
        telemetry.addData("Robot Approx. Location", robot.flywheel.getFlywheelMode());
        telemetry.addData("Flywheel Status", robot.flywheel.getFlywheelStatus());
        //telemetry.addData("Flywheel Gate Status", robot.flywheel.getGateStatus());

        telemetry.addData("Auto Beacon Choice", robot.getTeleOpBeaconChoice(gamepad1.dpad_up, gamepad1.dpad_down));

        telemetry.addData("Left Drivetrain", robot.drivetrain.getScaledPower(gamepad1.left_stick_y));
        telemetry.addData("Right Drivetrain", robot.drivetrain.getScaledPower(gamepad1.right_stick_y));
        telemetry.addData("Orientation", robot.drivetrain.getOrientation());

        telemetry.addData("Intake", robot.intake.getIntakeState());

        telemetry.addData("Flywheel Enc", robot.flywheel.getFlywheelEnc());

    }

}
