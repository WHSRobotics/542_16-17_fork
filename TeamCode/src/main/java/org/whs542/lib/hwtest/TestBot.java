package org.whs542.lib.hwtest;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * This class can be extended whenever using the testbot that Moses made
 * It declares the bare-bones of the testbot (4 motors) and adds the total runtime to the telemetry
 * There are also functions for setting the right and left power
 *
 * @see TestBotDemo
 */

public abstract class TestBot extends OpMode{

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

        telemetry.addData("RUNTIME:", time);

    }


    public void setRightPower(double power){
        frontRight.setPower(power);
        backRight.setPower(power);
    }

    public void setLeftPower(double power){
        frontLeft.setPower(power);
        backLeft.setPower(power);
    }

    public void setRLPower(double leftPower, double rightPower){
        frontLeft.setPower(leftPower);
        backLeft.setPower(leftPower);
        frontRight.setPower(rightPower);
        backRight.setPower(rightPower);
    }

    //A set power method using the cubic function that Moses made
    //params left and right can just be joystick values
    public void setRLScaledPower(double leftPower, double rightPower){

        double rightScaledPower = -Math.pow(rightPower, 3);
        double leftScaledPower = -Math.pow(leftPower, 3);

        setRightPower(rightScaledPower);
        setLeftPower(leftScaledPower);

    }
}
