package org.whs542.ftc2017.subsys;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;

/**
 * Created by Jiangda on 3/4/2017.
 */
public class ODSensor {
    public OpticalDistanceSensor opticalDistanceSensor;

    public boolean bLedOn = true;

    public final double THRESHOLD_VALUE = 0.1;

    public ODSensor(HardwareMap aMap){
        opticalDistanceSensor = aMap.opticalDistanceSensor.get("odSensor");
        opticalDistanceSensor.enableLed(bLedOn);
    }

    public boolean isLineDetected(){
        if(opticalDistanceSensor.getLightDetected() > THRESHOLD_VALUE)
            return true;
        else
            return false;
    }




}
