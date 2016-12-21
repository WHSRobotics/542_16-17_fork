package org.whs542.lib.hwtest;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Created by Lucy on 10/17/2016.
 */
@Deprecated
public class MoveTest
{
    DcMotor rightFront;
    DcMotor rightBack;
    DcMotor leftFront;
    DcMotor leftBack;

    //once phone is around 50 inches from the picture it should be more accurate to us Vuforia
    //

    public MoveTest(HardwareMap map)
    {
        rightFront = map.dcMotor.get("mtest_rf");
        rightBack = map.dcMotor.get("mtest_rb");
        leftFront = map.dcMotor.get("mtest_lf");
        leftBack = map.dcMotor.get("mtest_lb");
    }

    public void setPower(double rightPower, double leftPower)
    {

    }

    public void getSensorValue() {

    }
}
