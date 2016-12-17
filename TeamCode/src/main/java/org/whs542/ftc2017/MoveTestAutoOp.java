package org.whs542.ftc2017;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whs542.ftc2017.subsys.Drivetrain;
import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.ftc2017.subsys.IMU;
import org.whs542.ftc2017.subsys.Vuforia;
import org.whs542.lib.hwtest.TestDrivetrain;

/**
 * Created by Jiangda on 11/4/2016.
 */

@Autonomous( name = "MoveTestAutoOp", group = "test" )
@Disabled

public class MoveTestAutoOp extends LinearOpMode {

    //Vuforia vuforia;
    //IMU imu;
    //WHSRobot whsRobot;
    private TestDrivetrain drivetrain;
    DcMotor frontRight;
    DcMotor frontLeft;

    //int caseNumber;
    @Override
    public void runOpMode() {
        //vuforia = new Vuforia();
        DbgLog.msg("Vuforia init");
        telemetry.addData("Vuforia Init", 1);
        //imu = new IMU(hardwareMap);
        DbgLog.msg("IMU init");
        telemetry.addData("IMU Init", 1);
        //whsRobot = new WHSRobot(hardwareMap);
       drivetrain = new TestDrivetrain(hardwareMap);
        DbgLog.msg("Rbt init");
        telemetry.addData("RBT Init", 1);
        //caseNumber = 0;
        waitForStart();
        final double MM_PER_MAT = 594;

        drivetrain.moveDistanceMilli2(8 * MM_PER_MAT);
        drivetrain.moveDistanceMilli2(-8 * MM_PER_MAT);




    }




    /*@Override
    public void firstLoop(){
        while (caseNumber == 0) {

            caseNumber = 1;
        }
    }*/

}



