package org.whs542.ftc2017.autoOps;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.Flywheel2;
import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;
import org.whs542.lib.Coordinate;
import org.whs542.lib.Position;

/**
 * Created by JK Jewik on 1/6/2017.
 */

@Autonomous

public class BlueAutoPlay6 extends OpMode {
    WHSRobot robot;
    int state;
    int loop;
    String stateInfo;
    final static double FLYWHEEL_POWER = 0.4;

    @Override
    public void init() {
        robot = new WHSRobot(hardwareMap, Alliance.BLUE);
        state = 0;
        loop = 1000;
    }

    @Override
    public void loop() {
        switch (state) {
            case 0:
                stateInfo = "Prepping flywheel for fire";
                robot.flywheel2.runFlywheelNoToggle(FLYWHEEL_POWER);
                state++;
                break;
            case 1:
                stateInfo = "Wait for speed to build up";
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                state++;
                break;
            case 2:
                stateInfo = "Fire";
                robot.flywheel2.setParticleControlState(true);
                if (loop > 0) loop--;
                else state++;
                break;
            case 3:
                stateInfo = "Wind down the flywheel speed";
                robot.flywheel2.runFlywheelNoToggle(0.0);
                state++;
                break;
            case 4:
                stateInfo = "Drive to Capball";
                robot.driveToTarget(new Position(0, 0, 0));
                if (!robot.driveToTargetInProgress) state++;
                break;
            case 5:
                stateInfo = "Reached Capball, all done!";
                break;
            default:
                break;
        }
        telemetry.addData("State: ", stateInfo);
    }
}
