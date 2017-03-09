package org.whs542.ftc2017.subsys;

import com.qualcomm.hardware.hitechnic.HiTechnicNxtUltrasonicSensor;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsUsbLegacyModule;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.LegacyModule;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.UltrasonicSensor;

/**
 * NXT Ultrasonic Sensor Class
 */

public class NXTUltrasonicSensor{
    public UltrasonicSensor uSensor;

    public NXTUltrasonicSensor(HardwareMap aMap){
        try {
            uSensor = aMap.ultrasonicSensor.get("uSensor");
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
    public double getRange(){
        return uSensor.getUltrasonicLevel()*10;
    }
}
