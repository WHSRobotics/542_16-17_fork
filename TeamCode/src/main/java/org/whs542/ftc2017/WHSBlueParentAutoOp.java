package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;
import org.whs542.lib.Coordinate;
import org.whs542.lib.Position;
import org.whs542.lib.Timer;

/**
 * AutoOp containing all premade multipart switchcases
 *
 * @see WHSParentAutoOp
 * @see AutoOpSwitchCaseTemplate
 */
public class WHSBlueParentAutoOp extends OpMode {

    //Byte for controlling which state the case will be in: init(0), loop/init(1-99), or exit(default)
    int state = 0;
    String action;

    WHSRobot robot;

    Timer timer1;

    ////// Carry-over from older parent AutoOp
    boolean firstLoop;
    int test;

    long particleDelay;
    String stateInfo;
    double[] powers = {0.7, 0.8};
    final int startingPosition = 1; //1 or 2
    Alliance allianceColor = Alliance.BLUE;
    String stateInfo; //initial

    //Wheels, Legos, Tools, Gears
    Position vortexPosition;
    //Position[] vortexPositions = {new Position(-300, -300, 150), new Position(300, 300, 150)};
    Position centerVortex = new Position(0, 0, 150);
    Position blueVortex = new Position(300, 300, 150);
    Position redVortex = new Position(-300, -300, 150);

    //Position[] beaconPositions = {new Position(300,1800,150), new Position(-900,1800,150), new Position(-1800,900,150), new Position(-1800,-300,150)};
    Position beacon1a;
    Position beacon1b;
    Position beacon2a;
    Position beacon2b;


    Position[] targetPositionsBlue = {new Position(600, 1500, 150), new Position(-1300, 1500, 150)};
    Coordinate[] startingPositions = {new Coordinate(1500, 300, 150, 180), new Coordinate(1500, 0, 150, 180), new Coordinate(1500, -300, 150, 180)}
    Position[] vortexPositions = {new Position(300, 300, 150), new Position(-300, -300, 150)};
    //firstLoop: align to parallel beacons, second: end of beacons, third: center vortex
    //Position[] redPositions = {new Position(-1650,600,100), new Position(-1650,600,150), new Position(0,0,150)}; //TODO: separate do the same thing as before
    //firstLoop: align to parallel beacons, second: end of beacons, third: center vortex
    //Position[] bluePositions = {new Position(600,1650,150), new Position(-600,1650,150), new Position(0,0,150)};
    //TODO: move into init with red and blue

    Coordinate startingCoord;
    //{new Coordinate(-300, -1500, 150, 180), new Coordinate(1500, -300, 150, 180), new Coordinate(1500, 300, 150, 180)};


    //TODO: Test this
    double flywheelPower = 0.48;




    @Override
    public void init() {

        robot = new WHSRobot(hardwareMap, allianceColor);
        robot.setInitialCoordinate(startingPositions[0]);

        timer1 = new Timer(5, true);

        beacon1a = new Position(580, 1500, 150);
        beacon1b = new Position(573, 1500, 150);
        beacon2a = new Position(-620, 1500, 150);
        beacon2b = new Position(-627, 1500, 150);

        telemetry.log().add("RBT INIT");

    }

    @Override
    public void init_loop() {
        telemetry.addData("Time until Vuforia start", timer1.timeUntilTimerElapsed());
    }

    @Override
    public void loop() {




        switch(state){
            case 0:
                stateInfo = "moving forward";
                robot.driveToTarget(new Position(1400, 300, 150));
                if(!robot.driveToTargetInProgress & !robot.rotateToTargetInProgress)
                    state++;
                break;

            case 1:
                stateInfo = "Turning to vortex";
                robot.rotateToVortex(vortexPositions[0]);
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
                if(robot.pusher.side.toString().equals("BLUE"))
                    robot.driveToTarget(blueVortex);
                else
                    robot.driveToTarget(redVortex);
                if(!robot.driveToTargetInProgress)
                    state++;
                else{}






        }
        telemetry.addData("Robot state:", stateInfo);
        telemetry.addData("State:", state);

        telemetry.addData("Runtime:", time);
    }

        telemetry.addData("Action:", action);
        telemetry.addData("State:", state);

        telemetry.addData("Runtime:", time);

}




