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

    DcMotor motor;
    //double power = 0.0;

    @Override
    public void init(){
        motor = hardwareMap.dcMotor.get("motor");
    }

    @Override
    public void loop(){
        if(gamepad1.a)
        {
            motor.setPower(0.5);
        }
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
