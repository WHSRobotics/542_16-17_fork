package org.whs542.lib.swtest;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.whs542.ftc2017.subsys.IMU;

/**
 * Created by Jiangda on 11/21/2016.
 */
@Autonomous(name = "IMUHeadingTest", group = "Sensor")
//@Disabled
public class IMUHeadingTest extends OpMode
{
    IMU imu;

    @Override
    public void init(){

        imu = new IMU(hardwareMap);
        try {
            imu.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void loop(){
        double heading = imu.getHeading();
        double accel = imu.getAccelerationMag();

        String headingValue = String.valueOf(heading);
        String accelValue = String.valueOf(accel);

        telemetry.addData("Heading: ", headingValue);
        telemetry.addData("Acceleration: ", accelValue);

        if(gamepad1.x){
            //imu.calibrateHeading();
        }
    }

}
