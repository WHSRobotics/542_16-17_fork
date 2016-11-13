package org.whs542.ftc2017;

/**
 * Created by Jiangda on 10/31/2016.
 */
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.ftc2017.subsys.IMU;
import org.whs542.ftc2017.subsys.Vuforia;

public abstract class WHSParentAutoOp extends OpMode {
    Vuforia vuforia;
    IMU imu;
    WHSRobot whsRobot;

    @Override
    public void init(){
        vuforia = new Vuforia();
        //DbgLog.msg("Vuforia init");
        telemetry.addData("Vuforia Init", 1);
        imu = new IMU(hardwareMap);
        //DbgLog.msg("IMU init");
        telemetry.addData("IMU Init", 1);
        whsRobot = new WHSRobot(hardwareMap);
        //DbgLog.msg("Rbt init");
        telemetry.addData("RBT Init", 1);
    }

    public abstract void loop(

    );

}
