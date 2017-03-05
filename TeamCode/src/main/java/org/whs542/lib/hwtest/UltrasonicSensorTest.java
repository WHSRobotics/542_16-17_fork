package org.whs542.lib.hwtest;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.NXTUltrasonicSensor;

/**
 * Created by Jiangda on 3/4/2017.
 */
@Autonomous(name = "UltrasonicTest", group = "Sensortest")
@Disabled
public class UltrasonicSensorTest extends OpMode {

    NXTUltrasonicSensor uSensor;

    @Override
    public void init(){
        uSensor = new NXTUltrasonicSensor(hardwareMap);
    }

    @Override
    public void loop(){
        telemetry.addData("Sensor Range:", uSensor.getRange());
    }
}
