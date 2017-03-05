package org.whs542.lib.hwtest;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.ODSensor;

/**
 * Created by Jiangda on 3/4/2017.
 */

@Autonomous(name = "ODSensorTest", group = "SensorTest")

public class ODSensorTest extends OpMode {

    ODSensor odSensor;

    @Override
    public void init(){
        odSensor = new ODSensor(hardwareMap);
    }


    @Override
    public void loop(){
        telemetry.addData("Sensor Light Value:", odSensor.opticalDistanceSensor.getLightDetected());

    }
}
