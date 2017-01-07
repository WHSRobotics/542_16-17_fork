package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Coordinate;
import org.whs542.lib.Timer;

/**
 * Created by Amar2 on 10/22/2016.
 */
@TeleOp(name = "TeleOpTest", group = "WHSBlueTeleOp")
//@Disabled

public class TestTeleOp extends OpMode{

    WHSRobot robot;
    Timer timer;

    @Override
    public void init() {
        robot = new WHSRobot(hardwareMap);
        robot.setInitialCoordinate(new Coordinate(300,300,300,90));
        timer = new Timer(5, true);

    }

    @Override
    public void init_loop(){
        telemetry.addData("Time until Vuforia start", timer.timeUntilTimerElapsed());
    }

    @Override
    public void loop() {
        //Gamepad 1
        robot.drivetrain.setLRScaledPower(gamepad1.left_stick_y, gamepad1.right_stick_y);
        robot.drivetrain.setOrientation(gamepad1.a);
        robot.intake.runIntake(gamepad1.right_trigger, gamepad1.left_trigger);
        robot.pusher.extendPusherNoToggle(gamepad1.left_bumper);

        //robot.drivetrain.setLRScaledPower(gamepad1.left_stick_y, gamepad1.right_stick_y);
        //robot.drivetrain.setOrientation(gamepad1.a);
        //robot.intake.runIntake(gamepad1.right_trigger, gamepad1.left_trigger);
        //robot.capball.liftCB(gamepad1.dpad_up);
        //robot.capball.dropCB(gamepad1.dpad_down);

        //Gamepad 2
        //TODO: FIX THESE WHENEVER POSSIBLE, add power inc/dec and fix run
        robot.flywheel2.runFlywheel(gamepad2.left_bumper);
        robot.flywheel2.setFlywheelSpeed(gamepad2.dpad_up, gamepad2.dpad_down); //comment in this line when done testing flywheel speeds
        //double power = robot.flywheel2.setFlywheelSpeedExperimental(gamepad2.dpad_up, gamepad2.dpad_down);
        robot.flywheel2.setParticleControlState(gamepad2.left_trigger);

        //Telemetry
        //telemetry.addData("Robot Approx. Location: ", robot.flywheel.setFlywheelMode(gamepad2.dpad_up, gamepad2.dpad_down));
        telemetry.addData("Rx", robot.estimatePosition().getX());
        telemetry.addData("Ry", robot.estimatePosition().getY());
        telemetry.addData("Rh", robot.estimateHeading());
        telemetry.addData("Encoder L", robot.drivetrain.getLeftEncoderPosition());
        telemetry.addData("Encoder R", robot.drivetrain.getRightEncoderPosition());

        telemetry.addData("FWheelStat:", robot.flywheel2.getFlywheelMode());
        //telemetry.addData("FGateStat:", robot.flywheel.getGateStatus());
        //telemetry.addData("Flywheel power (for calib)", power);

        telemetry.addData("LeftStick Y:", gamepad1.left_stick_y);
        telemetry.addData("RightStick Y:", gamepad1.right_stick_y);
        telemetry.addData("Orientation:", robot.drivetrain.getOrientation());

        telemetry.addData("Intake:", robot.intake.getIntakeState());

        telemetry.addData("PC", robot.flywheel2.getParticleControlState());

        //telemetry.addData("Flywheel Speed:", robot.flywheel.getSpeed());

        //telemetry.addData("Capball Lift:", robot.capball.getCBState());

        //test for flywheel velocity vs distance
        /*robot.flywheel.test(gamepad1.a, 1);
        robot.flywheel.test(gamepad1.a, 0.9);
        robot.flywheel.test(gamepad1.a, 0.8);
        robot.flywheel.test(gamepad1.a, 0.7);
        robot.flywheel.test(gamepad1.a, 0.6);

        telemetry.addData("Velocity", robot.flywheel.getCurrentVelocity());*/
    }
}
