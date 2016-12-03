package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;
import org.whs542.lib.Position;

/**
 * Created by Lucy on 12/3/2016.
 */

@Autonomous(name = "MoveTest", group = "TestAutoOp")
//@Disabled
public class MoveTest extends OpMode {

    WHSRobot robot;
    int state;
    boolean loop;

    public void init()
    {
        robot = new WHSRobot(hardwareMap, Alliance.RED);
        state = 0;
        loop = true;
    }

    public void loop()
    {
        switch(state)
        {
            case 0:
                if(loop || robot.driveToTargetInProgress) {
                    robot.driveToTarget(new Position(0, 0, 0));
                    loop = false;
                }
                else
                {
                    loop = true;
                }
        }
        telemetry.addData("Loop", loop);
    }

}
