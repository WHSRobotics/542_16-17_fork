package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whs542.ftc2017.subsys.WHSRobotSimple;
import org.whs542.lib.Toggler;

/**
 * Competition TeleOp class
 */

@TeleOp(name = "WHSTeleop", group = "WHSTeleOp")
//@Disabled

public class WHSTeleop extends OpMode
{
    //Test Code
    //Toggler speedToggle = new Toggler(60);
    //End test code
    WHSRobotSimple robot;
    int i;

    public void init()
    {

        robot = new WHSRobotSimple(hardwareMap);
        //speedToggle.setState(28);
    }

    public void loop()
    {
        try {
            //Test code
            //speedToggle.changeState(gamepad1.a, gamepad1.b);
            //robot.flywheel2.rightFlywheel.setMaxSpeed(speedToggle.currentState()*25);
            //robot.flywheel2.leftFlywheel.setMaxSpeed(speedToggle.currentState()*25);
            //End test code
            //Gamepad 1 Controls
            robot.drivetrain.setLRScaledPower(gamepad1.left_stick_y, gamepad1.right_stick_y);
            robot.drivetrain.setOrientation(gamepad1.a);
            robot.intake.runIntake(gamepad1.right_trigger, gamepad1.left_trigger);
            robot.pusher.extendPusherNoToggle(gamepad1.left_bumper);
            robot.pusher.extendPusherHand(gamepad1.right_bumper);

            //Gamepad 2 Controls
            robot.flywheel2.setFlywheelSpeed(gamepad2.dpad_up, gamepad2.dpad_down);
            robot.flywheel2.runFlywheel(gamepad2.left_bumper);
            robot.flywheel2.setParticleControlState(gamepad2.left_trigger);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

        //Test code
        //telemetry.addData("Max flywheel speed", speedToggle.currentState()*25);
        //Test code

        //Telemetry
        telemetry.addData("Robot Approx. Location", robot.flywheel2.getFlywheelMode());
        telemetry.addData("Flywheel Speed", robot.flywheel2.getFlywheelMode());
        telemetry.addData("Flywheel State", robot.flywheel2.getFlywheelState());
        telemetry.addData("Particle Control Status", robot.flywheel2.getParticleControlState());
        telemetry.addData("Gamepad2 L Trig", gamepad2.left_trigger);
        telemetry.addData("Beaconpusher Enc", robot.pusher.beaconPusher.getCurrentPosition());

        telemetry.addData("Left Drivetrain", robot.drivetrain.getScaledPower(gamepad1.left_stick_y));
        telemetry.addData("Right Drivetrain", robot.drivetrain.getScaledPower(gamepad1.right_stick_y));
        telemetry.addData("Orientation", robot.drivetrain.getOrientation());

        telemetry.addData("Intake", robot.intake.getIntakeState());
    }

}
