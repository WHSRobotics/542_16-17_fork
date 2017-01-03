package org.whs542.ftc2017.autoops;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;
import org.whs542.lib.Coordinate;
import org.whs542.lib.Position;

/**
 * Blue Auto Play 2
 */

@Autonomous(name = "BlueAutoPlay2", group = "AutoOp")
@Disabled
@Deprecated

public class BlueAutoPlay2 extends OpMode {
    WHSRobot robot;

    int state;
    int loop;
    int wait;

    String stateInfo;
    String debug = "";
    double time1;
    double[] powers = {0.7, 0.8};
    final int startingPosition = 1; //1 or 2

    //Blue: Wheels, Legos
    Position[] beaconPositions = {new Position(300,1800,150), new Position(-900,1800,150)};
    //firstLoop: align to parallel beacons, second: end of beacons, third: center vortex
    Position[] bluePositions = {new Position(600,1650,150), new Position(-600,1650,150), new Position(0,0,150)};
    Position[] vortexPositions = {new Position(300, 300, 150), new Position(-300, -300, 150)};

    Coordinate[] startingPositions = {new Coordinate(-300, -1500, 150, 90), new Coordinate(0, -1500, 150, 90), new Coordinate(300, -1500, 150, 90)};

    @Override
    public void init() {
        DbgLog.msg("Starting init");
        robot = new WHSRobot(hardwareMap, Alliance.BLUE);
        //robot.setInitialCoordinate(startingCoord[0]);
        state = -1;
        wait = 1000;
        loop = 0;
        DbgLog.msg("Init complete");
    }
    @Override
    public void loop() {
        DbgLog.msg("Start of firstLoop " + debug);
        switch (state){
            case -1:
                DbgLog.msg("Starting case -1");
                //robot.vuforia.initializeVuforia();
                DbgLog.msg("Vuforia initialized");

                //robot.imu.initalize();
                DbgLog.msg("IMU initialized");
                robot.setInitialCoordinate(startingPositions[1]);
                state = 1;
                debug += "-1 ";
                break;
            case 0:
                DbgLog.msg("Starting case 0");
                stateInfo = "Starting case 0";

                robot.rotateToVortex(vortexPositions[0]);

                DbgLog.msg("Robot has rotated to Vortex");
                DbgLog.msg("rotateToTargetInProgress: " + robot.rotateToTargetInProgress);

                state = 1;
                if(!robot.rotateToTargetInProgress){
                    state++;
                }

                debug += "0 ";
                break;
            case 1:
                DbgLog.msg("Starting case 1");
                stateInfo = "Shooting particles";
                //robot.flywheel.setFlywheelPower(powers[startingPosition - 1]);
                robot.flywheel.setFlywheelPower(/*powers[startingPosition - 1]*/1.0);
                DbgLog.msg("Power has been set");
                //if(robot.flywheel.flywheel.getPower() == 1){
                telemetry.addData("Speed of flywheel ", robot.flywheel.getCurrentSpeed());
                if(robot.flywheel.isFlywheelAtCorrectSpeed(/*powers[startingPosition - 1]*/1.0)){
                    DbgLog.msg("Flywheel is at correct speed");
                    robot.flywheel.operateGate(true);
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
                    DbgLog.msg("Particle firing finished");

                }
                debug += "1 ";

                break;
            case 2:
                DbgLog.msg("Starting case 2");
                stateInfo = "driving to position 1";
                robot.driveToTarget(bluePositions[0]);
                DbgLog.msg("Driving to target");
                if(!robot.driveToTargetInProgress){
                    DbgLog.msg("Driving finished");
                    state++;
                }
                debug += "2 ";
                break;
            case 3:
                stateInfo = "driving to beacon 1";
                robot.driveToTarget(beaconPositions[0]);
                if(!robot.driveToTargetInProgress){
                    state++;
                }
                break;
            case 4:
                stateInfo = "determining / pressing firstLoop beacon";
                if(!robot.pusher.isBeaconPushed()){
                    robot.driveToTarget(new Position(-890,1800,150));
                }
                else{
                    state++;
                }
                break;
            case 5:
                stateInfo = "moving to second beacon";
                robot.driveToTarget(beaconPositions[1]);
                if(!robot.driveToTargetInProgress){
                    state++;
                }
                break;
            case 6:
                stateInfo = "determining / pressing second beacon";
                if(!robot.pusher.isBeaconPushed()){

                    robot.driveToTarget(new Position(310,1800,150));
                }
                else{
                    state++;
                }
                break;
            case 7:
                stateInfo = "moving to position 2";
                robot.driveToTarget(bluePositions[1]);
                if(!robot.driveToTargetInProgress){
                    state++;
                }
                break;
            case 8:
                stateInfo = "Auto Op Done!";
                break;


        }
        telemetry.addData("StateInfo:", stateInfo);

    }



}
