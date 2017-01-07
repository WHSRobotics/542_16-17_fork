package org.whs542.ftc2017.autoops;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;
import org.whs542.lib.Coordinate;
import org.whs542.lib.Position;
import org.whs542.lib.Timer;

/**
 * Red Auto Play 5
 */
@Autonomous(name = "RedAutoPlay5", group = "Autonomous  ")
//@Disabled
//@Deprecated

public class RedAutoPlay5 extends OpMode
{
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

    //First coordinate: closest to red ramp, touching wall; Second: in the middle of red wall; Third: farthest from red ramp
    Coordinate[] startingPositions = {new Coordinate(-300, -1500, 150, 90), new Coordinate(0, -1500, 150, 90), new Coordinate(300, -1500, 150, 90)};
    Position[] capballPositions = {new Position(300, 450, 150), new Position(-450, -300, 150)};

    Timer timer;
    boolean loop;

    @Override
    public void init() {
        robot = new WHSRobot(hardwareMap, Alliance.BLUE);
        robot.setInitialCoordinate(startingPositions[0]);
        state = 1;
        timer = new Timer(5, true);
        loop = true;
    }

    @Override
    public void init_loop(){
        telemetry.addData("Time until Vuforia start", timer.timeUntilTimerElapsed());
    }

    @Override
    public void loop() {

        robot.estimateHeading();
        robot.estimatePosition();

        switch(state){
            case 0:
                stateInfo = "moving forward";
                robot.driveToTarget(new Position(-300, -1400, 150));
                if(!robot.driveToTargetInProgress & !robot.rotateToTargetInProgress)
                    state++;
                break;

            case 1:
                stateInfo = "Turning to vortex";
                robot.rotateToVortex(vortexPositions[1]);
                if (!robot.rotateToTargetInProgress & !robot.rotateToTargetInProgress)
                    state++;
                break;
            case 2:
                stateInfo = "Shooting particles";
                robot.flywheel2.runFlywheelNoToggle(powers[startingPosition - 1]); //need something to check if it's up to speed
                try {
                    Thread.sleep(4500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                state++;
                break;

            case 3:
                stateInfo = "Shooting first particle";
                robot.flywheel2.setParticleControlState(true);
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                state++;
                break;
            case 4:
                stateInfo = "Lowering particle control";
                robot.flywheel2.setParticleControlState(false);
                try {
                    Thread.sleep(3500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                state++;
                break;
            case 5:
                stateInfo = "Shooting second particle";
                robot.flywheel2.setParticleControlState(true);
                try {
                    Thread.sleep(2000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                robot.flywheel2.runFlywheelNoToggle(0.0);
                robot.flywheel2.setParticleControlState(false);
                state++;
                break;
            case 6:
                stateInfo = "Driving to center vortex";
                robot.driveToTarget(capballPositions[1]);
                if (!robot.driveToTargetInProgress & !robot.rotateToTargetInProgress) {
                    loop = false;
                    state++;
                }
                break;
            case 7:
                stateInfo = "Rotating to knock capball";
                robot.rotateToTarget(-45);
                if(!robot.rotateToTargetInProgress){
                    state++;
                }
                break;
            case 8:
                stateInfo = "Driving to center vortex";
                robot.driveToTarget(redPositions[2]);
                if(!robot.driveToTargetInProgress & !robot.rotateToTargetInProgress){
                    stateInfo = "Auto Op Done!! :p :)";
                }


            default: break;
        }
        telemetry.addData("State Info: ", stateInfo);
        telemetry.addData("Rx", robot.estimatePosition().getX());
        telemetry.addData("Ry", robot.estimatePosition().getY());
        telemetry.addData("Rh", robot.estimateHeading());
        telemetry.addData("DriveToTargetInProgress:", robot.driveToTargetInProgress);
        telemetry.addData("RotateToTargetInProgress", robot.rotateToTargetInProgress);
        telemetry.addData("time", time);

    }
}


