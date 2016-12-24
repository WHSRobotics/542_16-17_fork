package org.whs542.lib.hwtest;

/**
 * Created by Jiangda on 10/10/2016.
 */

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name = "Test 180 Servo", group = "Tests")
@Disabled
public class Test180Servo extends LinearOpMode{

    @Override
    public void runOpMode() throws InterruptedException{
        Servo servo = this.hardwareMap.servo.get("servo");

        waitForStart();


        for(int i = 0; i < 3; i++){
            servo.setPosition(1);
            Thread.sleep(1000);
            servo.setPosition(0);
            Thread.sleep(1000);
        }
    }
}
