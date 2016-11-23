package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.whs542.ftc2017.subsys.Flywheel;

/**
 * Created by Jiangda on 10/1/2016.
 */

@TeleOp(name = "CubicTeleOp", group = "HwTest")
public class CubicTeleOp extends OpMode {

    private DcMotor frontRight;
    private DcMotor frontLeft;
    private DcMotor backRight;
    private DcMotor backLeft;

    private Flywheel flywheel;

    @Override
    public void init(){

        frontRight = hardwareMap.dcMotor.get("frontRight");
        frontLeft = hardwareMap.dcMotor.get("frontLeft");
        backRight = hardwareMap.dcMotor.get("backRight");
        backLeft = hardwareMap.dcMotor.get("backLeft");

        flywheel = new Flywheel(hardwareMap);


        backRight.setDirection(DcMotor.Direction.FORWARD);
        frontRight.setDirection(DcMotor.Direction.FORWARD);
        backLeft.setDirection(DcMotor.Direction.FORWARD);
        backRight.setDirection(DcMotor.Direction.FORWARD);



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


        //flywheel.run(gamepad1.right_bumper);


        telemetry.addData("Right Power", rightScaledPower);
        telemetry.addData("Left Power", leftScaledPower);
        telemetry.addData("Flywheel", flywheel.getFlywheelStatus());
        telemetry.update();


    }
}


