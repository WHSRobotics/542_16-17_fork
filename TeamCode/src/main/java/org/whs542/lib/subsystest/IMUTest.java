package org.whs542.lib.subsystest;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.whs542.ftc2017.subsys.Drivetrain;
import org.whs542.ftc2017.subsys.IMU;

/**
 * Created by Jiangda on 11/21/2016.
 */

@Autonomous(name = "IMUTest", group = "Sensor")
//@Disabled

public class IMUTest extends OpMode
{

    IMU imu;
    Drivetrain drivetrain;

    @Override
    public void init(){

        imu = new IMU(hardwareMap);
        drivetrain = new Drivetrain(hardwareMap);

        /*try {
            imu.start();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }*/
    }

    @Override
    public void loop(){

        drivetrain.setLeftPower(0.0);
        drivetrain.setRightPower(0.0);

        double heading = imu.getHeading();

        double[] threeHeading = imu.getThreeHeading();

        double accel = imu.getAccelerationMag();

        String headingValue = String.valueOf(heading);
        String accelValue = String.valueOf(accel);

        telemetry.addData("Heading: ", headingValue);
        telemetry.addData("Acceleration: ", accelValue);

        if(gamepad1.x){
            //imu.calibrateHeading();
        }

        telemetry.addData("x", threeHeading[0]);
        telemetry.addData("y", threeHeading[1]);
        telemetry.addData("z", threeHeading[2]);
    }

}
