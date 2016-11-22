package org.whs542.ftc2017;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.ftc2017.subsys.IMU;
import org.whs542.ftc2017.subsys.Vuforia;

/**
 * Created by Jiangda on 11/4/2016.
 */

@Autonomous( name = "MoveTestAutoOp", group = "test" )
public class MoveTestAutoOp extends LinearOpMode{

    //Vuforia vuforia;
    IMU imu;
    WHSRobot whsRobot;

    //int caseNumber;
    @Override
    public void runOpMode()
    {
        //vuforia = new Vuforia();
        DbgLog.msg("Vuforia init");
        telemetry.addData("Vuforia Init", 1);
        imu = new IMU(hardwareMap);
        DbgLog.msg("IMU init");
        telemetry.addData("IMU Init", 1);
        whsRobot = new WHSRobot(hardwareMap);
        DbgLog.msg("Rbt init");
        telemetry.addData("RBT Init", 1);
        //caseNumber = 0;

        waitForStart();
        final double MM_PER_MAT = 594;
        whsRobot.drivetrain.moveDistanceMilli2(4 * MM_PER_MAT, imu);
        whsRobot.drivetrain.moveDistanceMilli2(-4 * MM_PER_MAT, imu);
        requestOpModeStop();
    }




    /*@Override
    public void loop(){
        while (caseNumber == 0) {

            caseNumber = 1;
        }
    }*/

}



