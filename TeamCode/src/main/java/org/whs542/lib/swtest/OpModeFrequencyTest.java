package org.whs542.lib.swtest;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

/**
 * Created by Amar2 on 11/20/2016.
 */
@Autonomous(name = "Frequency Test", group = "Tests")
@Disabled
public class OpModeFrequencyTest extends OpMode{

    int i;

    @Override
    public void init() {

    }

    public void start(){
        resetStartTime();
    }

    @Override
    public void loop() {
        i++;
        telemetry.addData("i:", i);
        if(getRuntime() >= 5){
            requestOpModeStop();
        }
    }
}
