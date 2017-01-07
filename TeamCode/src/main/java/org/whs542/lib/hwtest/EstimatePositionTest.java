package org.whs542.lib.hwtest;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;
import org.whs542.lib.Coordinate;

/**
 * Created by Lucy on 12/21/2016.
 */

public class EstimatePositionTest extends OpMode
{
    WHSRobot robot;

    public void init()
    {
        robot = new WHSRobot(hardwareMap, Alliance.BLUE);
        robot.setInitialCoordinate(new Coordinate(0,0,0,0));
    }

    public void loop()
    {
        robot.estimatePosition();

        telemetry.addData("Rx:", robot.currentCoord.getX());
        telemetry.addData("Ry:", robot.currentCoord.getY());
        telemetry.addData("Hdg:", robot.currentCoord.getHeading());
        telemetry.update();
    }
}
