package org.whs542.lib.subsystest;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whs542.ftc2017.subsys.Drivetrain;
import org.whs542.lib.Toggler;

/**
 * Test OpMode for {@link Drivetrain}
 */
@TeleOp(name = "DrivetrainTest", group = "tests")
//@Disabled
public class DrivetrainTest extends OpMode {

    Drivetrain drivetrain;
    Toggler scaleTog = new Toggler(3);
    String mode = "";
    double[] power = new double[2];
    Toggler powerTog = new Toggler(50);

    @Override
    public void init() {
        drivetrain = new Drivetrain(hardwareMap);
        telemetry.log().add("mode (scaled/normal) switch : gamepad1-x");
        telemetry.log().add("orientation switch : gamepad1-a");
    }

    @Override
    public void loop() {

        scaleTog.changeState(gamepad1.x);
        drivetrain.setOrientation(gamepad1.a);
        powerTog.changeState(gamepad1.dpad_up, gamepad1.dpad_down);

        if(scaleTog.currentState() == 0){
            drivetrain.setLRPower(gamepad1.left_stick_y, gamepad1.right_stick_y);
            mode = "Normal";
        }
        else if (scaleTog.currentState() == 1){
            drivetrain.setLRScaledPower(gamepad1.left_stick_y, gamepad1.right_stick_y);
            mode = "Scaled";
        }
        else {
            if(gamepad1.b) {
                drivetrain.setLRPower(powerTog.currentState() * 0.02, powerTog.currentState() * 0.02);
            }
            else {
                drivetrain.setLRPower(0.0, 0.0);
            }
            mode = "Step";
        }


        telemetry.addData("Mode:", mode);
        telemetry.addData("Orientation:", drivetrain.getOrientation());
        telemetry.addData("LeftStickY:", gamepad1.left_stick_y);
        telemetry.addData("RightStickY:", gamepad1.right_stick_y);
        telemetry.addData("Scaled L", drivetrain.getScaledPower(gamepad1.left_stick_y));
        telemetry.addData("Scaled R", drivetrain.getScaledPower(gamepad1.right_stick_y));
        telemetry.addData("Power (step)", powerTog.currentState()*0.02);
    }
}
