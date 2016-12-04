package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;
import org.whs542.lib.Coordinate;
import org.whs542.lib.Functions;
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
        telemetry.log().add("Init");
        robot.setInitialCoordinate(new Coordinate(0, 1200, 150, Functions.normalizeAngle(270)));
    }

    public void loop()
    {
        switch (state) {

            case 0:
                if (loop || robot.driveToTargetInProgress) {
                    robot.driveToTarget(new Position(0, 0, 0));
                    loop = false;
                } else {
                    loop = true;
                    state++;
                }
                break;

        }

        telemetry.addData("Loop", loop);
        telemetry.addData("State", state);
        telemetry.addData("Drive to target in progress?", robot.driveToTargetInProgress);
        telemetry.addData("Rotate to target in progress?", robot.rotateToTargetInProgress);
        telemetry.addData("Vuforia Valid?", robot.vuforia.vuforiaIsValid());
        telemetry.addData("Vx", robot.vuforia.getHeadingAndLocation().getX());
        telemetry.addData("Vy", robot.vuforia.getHeadingAndLocation().getY());
        telemetry.addData("Vheading", robot.vuforia.getHeadingAndLocation().getHeading());
        telemetry.addData("Rx", robot.estimatePosition().getX());
        telemetry.addData("Ry", robot.estimatePosition().getY());
        telemetry.addData("Rh", robot.estimateHeading());

    }

}
