package org.whs542.lib.swtest;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ThreadPool;

/**
 * Created by Amar2 on 1/11/2017.
 */

@Autonomous(name = "LinearOpModeTest", group = "tests")
public class LinearOpModeTest extends LinearOpMode {
    int count = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        Thread.sleep(10000);
        waitForStart();
        while(opModeIsActive()){

            while (true) {
                count++;
                telemetry.addData("runtime", getRuntime());
                telemetry.addData("count", count);
                telemetry.update();
            }
        }

    }

}
