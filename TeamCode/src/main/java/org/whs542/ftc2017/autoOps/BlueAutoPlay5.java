package org.whs542.ftc2017.autoops;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;
import org.whs542.lib.Coordinate;
import org.whs542.lib.OpModeTimer;
import org.whs542.lib.Position;
import org.whs542.lib.Timer;

/**
 * Created by jian on 12/2/2016.
 */
@Autonomous(name = "BlueAutoPlay5", group = "Autonomous")
//@Disabled
//@Deprecated

public class BlueAutoPlay5 extends OpMode{
    WHSRobot robot;
    int state;
    String stateInfo;
    double[] powers = {0.75, 0.8};
    final int startingPosition = 1; //1 or 2
    long particleDelay = 300;
    //Wheels, Legos, Tools, Gears
    Position[] beaconPositions = {new Position(300,1800,150), new Position(-900,1800,150), new Position(-1800,900,150), new Position(-1800,-300,150)};
    Position[] redPositions = {new Position(-1650,600,100), new Position(-1650,600,150), new Position(0,0,150), new Position(-1800, 1000, 150) };
    Position[] bluePositions = {new Position(600,1650,150), new Position(-600,1650,150), new Position(0,0,150)};
    Position[] vortexPositions = {new Position(300, 300, 150), new Position(-300, -300, 150)};
    //First coordinate: closest to blue ramp, touching wall; Second: in the middle of blue wall; Third: farthest from blue ramp
    Coordinate[] startingPositions = {new Coordinate(1500, 300, 150, 180), new Coordinate(1500, 0, 150, 180), new Coordinate(1500, -300, 150, 180)};

    Timer timer;

    @Override
    public void init() {
        robot = new WHSRobot(hardwareMap, Alliance.BLUE);
        robot.setInitialCoordinate(startingPositions[0]);
        state = 0;
        timer = new Timer(5);
    }

    @Override
    public void init_loop(){
        telemetry.addData("Time until Vuforia start", timer.timeUntilTimerElapsed());
    }

    @Override
    public void loop() {

        switch(state){
            case 0:
                stateInfo = "moving forward";
                robot.driveToTarget(new Position(1400, 300, 150));
                if(!robot.driveToTargetInProgress)
                    state++;
                break;

            case 1:
                stateInfo = "Turning to vortex";
                robot.rotateToVortex(vortexPositions[0]);
                if (!robot.rotateToTargetInProgress) state++;
                break;
            case 2:
                stateInfo = "Shooting particles";
                robot.flywheel2.runFlywheelNoToggle(powers[startingPosition - 1]); //need something to check if it's up to speed
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                state++;
                break;

            case 3:
                robot.flywheel2.setParticleControlState(true);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                state++;
                break;
            case 4:
                robot.flywheel2.setParticleControlState(false);
                try {
                    Thread.sleep(2500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                state++;
                break;
            case 5:
                robot.flywheel2.setParticleControlState(true);
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                robot.flywheel2.runFlywheelNoToggle(0.0);
                robot.flywheel.operateGate(false);
                state++;
                break;
            case 6:
                robot.driveToTarget(vortexPositions[0]);
                if (!robot.driveToTargetInProgress) {
                    stateInfo = "AutoOp Complete. >~<";
                    state++;
                }
                stateInfo = "Driving to center vortex";
                state++;
                break;
            default: break;
        }
        telemetry.addData("State Info: ", stateInfo);
    }
}


