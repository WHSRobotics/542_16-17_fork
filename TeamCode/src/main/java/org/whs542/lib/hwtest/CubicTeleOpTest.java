package org.whs542.lib.hwtest;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by Moses Won
 */

@TeleOp(name = "CubicTeleOpTest", group = "HwTest")
@Disabled
public class CubicTeleOpTest extends OpMode{

    DcMotor frontRight;
    DcMotor frontLeft;
    DcMotor backRight;
    DcMotor backLeft;

    @Override
    public void init(){

        frontRight = hardwareMap.dcMotor.get("frontRight");
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");


        backRight.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRight.setDirection(DcMotorSimple.Direction.REVERSE);



    }

    @Override
    public void loop(){

        /*
         * Keep in mind that when the joystick is pushed all the way forward, gamepad1.right_stick_y returns -14
         * The following two lines of code raise the joystick input to a power of 3, making speed control more precise
         */

        double rightScaledPower = -Math.pow(gamepad1.right_stick_y, 3);
        double leftScaledPower = -Math.pow(gamepad1.left_stick_y, 3);

        frontRight.setPower(rightScaledPower);
        frontLeft.setPower(leftScaledPower);
        backRight.setPower(rightScaledPower);
        backLeft.setPower(leftScaledPower);


        telemetry.addData("Right Power", rightScaledPower);
        telemetry.addData("Left Power", leftScaledPower);


    }
}
