package org.whs542.ftc2017;

import android.renderscript.ScriptIntrinsicYuvToRGB;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;
import org.whs542.lib.Position;

/**
 * Created by Jiangda on 11/26/2016.
 */
public class BlueAutoPlay2 extends OpMode {
    WHSRobot robot;

    int state;
    int loop;
    int wait;

    String stateInfo;
    double time1;
    double[] powers = {0.7, 0.8};
    final int startingPosition = 1; //1 or 2

    //Blue: Wheels, Legos
    Position[] beaconPositions = {new Position(300,1800,150), new Position(-900,1800,150)};
    //first: align to parallel beacons, second: end of beacons, third: center vortex
    Position[] bluePositions = {new Position(600,1650,150), new Position(-600,1650,150), new Position(0,0,150)};
    Position[] vortexPositions = {new Position(300, 300, 150), new Position(-300, -300, 150)};

    @Override
    public void init() {
        robot = new WHSRobot(hardwareMap, Alliance.BLUE);
        state = 0;
        wait = 1000;
        loop = 1;
    }
    @Override
    public void loop() {

        switch (state){
            case 0:

                stateInfo = "turning to blue vortex";
                robot.rotateToVortex(vortexPositions[0]);

                if(!robot.rotateToTargetInProgress){
                    state++;
                }
                break;
            case 1:
                stateInfo = "Shooting particles";
                robot.flywheel.setFlywheelPower(powers[startingPosition - 1]);
                if(robot.flywheel.isFlywheelAtCorrectSpeed(powers[startingPosition - 1])){
                    robot.flywheel.operateGateNoToggle(true);
                    robot.intake.runIntake(1.0);
                    if(loop == 1){
                        time1 = (double) System.nanoTime() / 10E9;
                        loop = 2;
                    }
                    double time2 = (double) System.nanoTime() / 10E9;
                    if(time2 - time1 > 0.7){
                        robot.intake.runIntake(0);
                        robot.flywheel.setFlywheelPower(0);
                        state++;
                        loop = 1;
                    }

                }
                break;
            case 2:
                stateInfo = "driving to position 1";
                robot.driveToTarget(bluePositions[0]);
                if(!robot.driveToTargetInProgress){
                    state++;
                }
            case 3:
                stateInfo = "driving to beacon 1";
                robot.driveToTarget(beaconPositions[0]);
                if(!robot.driveToTargetInProgress){
                    state++;
                }
            case 4:
                stateInfo = "determining / pressing first beacon";
                if(!robot.pusher.isBeaconPushed()){
                    robot.driveToTarget(new Position());
                }
                else{
                    state++;
                }
            case 5:
                stateInfo = "moving to second beacon";
                robot.driveToTarget(beaconPositions[1]);
                if(!robot.driveToTargetInProgress){
                    state++;
                }
            case 6:
                stateInfo = "determining / pressing second beacon";
                if(!robot.pusher.isBeaconPushed()){
                    robot.driveToTarget(new Position());
                }
                else{
                    state++;
                }
            case 7:
                stateInfo = "moving to position 2";
                robot.driveToTarget(bluePositions[1]);
                if(!robot.driveToTargetInProgress){
                    state++;
                }
            case 8:
                stateInfo = "Auto Op Done!"


        }
        telemetry.addData("StateInfo:", stateInfo);

    }



}
