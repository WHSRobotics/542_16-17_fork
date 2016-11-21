package org.whs542.lib.hwtest;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Amar2 on 11/19/2016.
 */
@Autonomous(name = "RunModeTest", group = "Tests")
public class DcMotorRunModeTest extends OpMode{

    DcMotor a;

    @Override
    public void init() {
        a = hardwareMap.dcMotor.get("a");
        a.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        a.setMaxSpeed(1000);
    }

    public void start(){
        resetStartTime();
    }

    @Override
    public void loop() {
        a.setPower(1);
        telemetry.addData("Enc Ticks", a.getCurrentPosition());
        telemetry.addData("Loop Runtime", getRuntime());
        if(getRuntime() >= 2){
            requestOpModeStop();
        }
    }
}
