package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.whs542.ftc2017.subsys.Flywheel;

/**
 * Created by jian on 11/5/2016.
 */
public class FlywheelTest {
    public class CubicTeleOp extends OpMode {

        private Flywheel flywheel;

        @Override
        public void init(){

            flywheel = new Flywheel(hardwareMap);




        }

        @Override
        public void loop(){

    /*
     * Keep in mind that when the joystick is pushed all the way forward, gamepad1.right_stick_y returns -14
     * The following two lines of code raise the joystick input to a power of 3, making speed control more precise
     */

            double rightScaledPower = -Math.pow(gamepad1.right_stick_y, 3);
            double leftScaledPower = -Math.pow(gamepad1.left_stick_y, 3);

            telemetry.addData("Right Power", rightScaledPower);
            telemetry.addData("Left Power", leftScaledPower);
            telemetry.addData("Flywheel", flywheel.getFlywheelStatus());
            telemetry.update();


        }
    }

}
