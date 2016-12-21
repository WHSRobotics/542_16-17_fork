package org.whs542.lib.hwtest;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/**
 * Created by Lucy on 10/10/2016.
 */

@TeleOp(name = "DriveTest", group = "HwTest")
@Deprecated
public class DriveTest extends OpMode
{
    private DcMotor rightFrontMotor;
    private DcMotor rightBackMotor;
    private DcMotor leftFrontMotor;
    private DcMotor leftBackMotor;

    @Override
    public void init()
    {
        //fix left motor direction
        rightFrontMotor = hardwareMap.dcMotor.get("drive_rf"); //motor 1 on oxuvu
        rightBackMotor = hardwareMap.dcMotor.get("drive_rb"); //motor 2 on oxuvu
        leftFrontMotor = hardwareMap.dcMotor.get("drive_lf"); //motor 1 on xuoi
        leftBackMotor = hardwareMap.dcMotor.get("drive_lb"); //motor2 on xuoi
        leftFrontMotor.setDirection(DcMotor.Direction.REVERSE);
        leftBackMotor.setDirection(DcMotor.Direction.REVERSE);
        //rightBackMotor.setDirection(DcMotor.Direction.REVERSE);
        //leftBackMotor.setDirection(DcMotor.Direction.REVERSE);
    }

    @Override
    public void loop()
    {
        rightFrontMotor.setPower(gamepad1.right_stick_y);
        rightBackMotor.setPower(gamepad1.right_stick_y);
        leftFrontMotor.setPower(gamepad1.left_stick_y);
        leftBackMotor.setPower(gamepad1.left_stick_y);

        telemetry.addData("Power:", gamepad1.left_stick_y);
    }
}
