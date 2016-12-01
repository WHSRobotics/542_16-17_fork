package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;
import org.whs542.lib.Position;

/**
 * Created by Jiangda on 11/25/2016.
 */
public class RedAutoPlay5 extends OpMode {

    WHSRobot robot;
    int state;
    String stateInfo;
    double[] powers = {0.7, 0.8};
    final int startingPosition = 1; //1 or 2
    long particleDelay = 300;
    //Wheels, Legos, Tools, Gears
    Position[] beaconPositions = {new Position(300,1800,150), new Position(-900,1800,150), new Position(-1800,900,150), new Position(-1800,-300,150)};
    Position[] redPositions = {new Position(-1650,600,100), new Position(-1650,600,150), new Position(0,0,150), new Position(-1800, 1000, 150) };
    Position[] vortexPositions = {new Position(300, 300, 150), new Position(-300, -300, 150)};

    @Override
    public void init() {
        robot = new WHSRobot(hardwareMap, Alliance.RED);
        state = 0;
    }

    @Override
    public void loop() {

        switch(state){
            case 0:
                stateInfo = "Turning to vortex";
                robot.rotateToVortex(vortexPositions[1]);
                if (!robot.rotateToTargetInProgress) state++;
                break;
            case 1:
                stateInfo = "Shooting particles";
                robot.flywheel.setFlywheelPower(powers[startingPosition - 1]); //need something to check if it's up to speed
                if (robot.flywheel.isFlywheelAtCorrectSpeed(powers[startingPosition - 1])) {
                    robot.flywheel.operateGateNoToggle(true);
                    robot.intake.runIntake(1.0);
                    try {
                        Thread.sleep(particleDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    robot.intake.runIntake(0.0);
                    robot.flywheel.setFlywheelPower(0.0);
                    robot.flywheel.operateGateNoToggle(false);
                    state++;
                }
                break;
            case 2:
                robot.driveToTarget(redPositions[2]);
                if (!robot.driveToTargetInProgress) {
                    stateInfo = "AutoOp Complete. >~<";
                    state++;
                }
                stateInfo = "Driving to center vortex";
                break;
            default: break;
        }
        telemetry.addData("State Info: ", stateInfo);
    }
}
