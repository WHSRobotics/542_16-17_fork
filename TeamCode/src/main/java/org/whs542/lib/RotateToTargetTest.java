package org.whs542.lib;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whs542.ftc2017.subsys.WHSRobot;

/**
 * Created by Amar2 on 12/2/2016.
 */
@TeleOp(name = "Rotate to target test", group = "uhawuiwwb%^ujq3gu34hu3iY*Gyuuiuor")
public class RotateToTargetTest extends OpMode {

    WHSRobot robot;

    @Override
    public void init() {
        robot = new WHSRobot(hardwareMap, Alliance.BLUE);
        robot.setInitialCoordinate(new Coordinate(1200, 0 , 150, 180));
    }

    @Override
    public void loop() {

        robot.rotateToTarget(150);
        if(!robot.rotateToTargetInProgress){
            requestOpModeStop();
        }

    }

}
