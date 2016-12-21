package org.whs542.lib.hwtest;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Amar2 on 10/22/2016.
 */
@TeleOp(name = "ServoTest", group = "tests")
@Disabled
public class ServoTest extends OpMode{

    Servo servo1;

    @Override
    public void init() {
        servo1 = hardwareMap.servo.get("1");
    }

    @Override
    public void loop() {
        if(gamepad1.a) {
            servo1.setPosition(1);
        }
        else if(gamepad1.b){
            servo1.setPosition(-1);
        }
        else {
            servo1.setPosition(0);
        }
    }
}
