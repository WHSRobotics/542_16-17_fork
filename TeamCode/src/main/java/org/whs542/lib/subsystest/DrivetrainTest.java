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
@Disabled
public class DrivetrainTest extends OpMode {

    Drivetrain drivetrain;
    Toggler scaleTog = new Toggler(2);
    String mode = "";
    double[] power = new double[2];

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

        if(scaleTog.currentState() == 0){
            drivetrain.setLRPower(gamepad1.left_stick_y, gamepad1.right_stick_y);
            mode = "Normal";
        }
        else {
            drivetrain.setLRScaledPower(gamepad1.left_stick_y, gamepad1.right_stick_y);
            mode = "Scaled";
        }

        telemetry.addData("Mode:", mode);
        telemetry.addData("Orientation:", drivetrain.getOrientation());
        telemetry.addData("LeftStickY:", gamepad1.left_stick_y);
        telemetry.addData("RightStickY:", gamepad1.right_stick_y);
        telemetry.addData("Scaled L", drivetrain.getScaledPower(gamepad1.left_stick_y));
        telemetry.addData("Scaled R", drivetrain.getScaledPower(gamepad1.right_stick_y));

    }
}
