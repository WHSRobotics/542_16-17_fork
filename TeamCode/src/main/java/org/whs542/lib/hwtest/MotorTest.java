package org.whs542.lib.hwtest;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Amar on 9/19/2016.
 */

@TeleOp(name = "MotorTest", group = "HwTest")
public class MotorTest extends OpMode {

    DcMotor motor1;
    DcMotor motor2;
    //double power = 0.0;

    @Override
    public void init(){
        motor1 = hardwareMap.dcMotor.get("rightFly");
        motor2 = hardwareMap.dcMotor.get("leftFly");
        motor1.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motor2.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    @Override
    public void loop(){
        if(gamepad1.a)
        {
            motor1.setPower(1.0);
            motor2.setPower(1.0);
        }
        else if(Math.abs(gamepad1.left_stick_y) >= 0.05){

        }
        else{
            motor1.setPower(0);
            motor2.setPower(0);
        }

        telemetry.addData("Enc Ticks 1", motor1.getCurrentPosition());
        telemetry.addData("Enc Ticks 2", motor2.getCurrentPosition());


        /*
        if(power >= -1.0){
            power += 0.01;
        }
        else if(power >= 1.0){
            power += -0.01;
        }

        motor1.setPower(power);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        */
    }

}
