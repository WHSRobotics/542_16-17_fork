package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;

/**
 * Created by Lucy on 12/30/2016.
 */

@Autonomous(name = "NullOp", group = "Autonomous")
public class NullOp extends OpMode{

    WHSRobot robot;

    public void init()
    {
        robot = new WHSRobot(hardwareMap, Alliance.BLUE);
    }

    public void loop()
    {

    }

}
