package org.whs542.lib.subsystest;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

/**
 * Created by Lucy on 1/11/2017.
 */

@TeleOp(name = "ParticlePusherTest", group = "SubsystemTest")

public class ParticlePusherTest extends OpMode
{
    //Flywheel2 flywheel;
    Servo particlePusher;

    public void init()
    {
        //flywheel = new Flywheel2(hardwareMap);
        particlePusher = hardwareMap.servo.get("particlePusher");
    }

    public void loop()
    {
        //flywheel.setParticleControlState(gamepad1.right_trigger);
        if(gamepad1.right_trigger > 0.1)
        {
            particlePusher.setPosition(0.1);
        }
        else
        {
            particlePusher.setPosition(0.9);
        }
    }
}
