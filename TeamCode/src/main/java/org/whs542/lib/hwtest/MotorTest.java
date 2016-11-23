package org.whs542.lib.hwtest;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Amar on 9/19/2016.
 */

@Autonomous(name = "MotorTest", group = "HwTest")

public class MotorTest extends OpMode {

    DcMotor motor1;
    double power = 0.0;

    @Override
    public void init(){
        motor1 = hardwareMap.dcMotor.get("motor1");
    }

    @Override
    public void loop(){

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

    }

}
