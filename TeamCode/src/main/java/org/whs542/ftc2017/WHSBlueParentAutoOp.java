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
public abstract class WHSBlueParentAutoOp extends OpMode {

    //Byte for controlling which state the case will be in: init(0), loop/init(1-99), or exit(default)
    byte state = 0;
    String action;

    WHSRobot robot;

    ////// Carry-over from older parent AutoOp
    boolean firstLoop;
    int test;

    long particleDelay;
    String stateInfo;
    double[] powers = {0.7, 0.8};
    final int startingPosition = 1; //1 or 2
    Alliance allianceColor = Alliance.BLUE;
    String initialConfig; //initial

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
    //firstLoop: align to parallel beacons, second: end of beacons, third: center vortex
    //Position[] redPositions = {new Position(-1650,600,100), new Position(-1650,600,150), new Position(0,0,150)}; //TODO: separate do the same thing as before
    //firstLoop: align to parallel beacons, second: end of beacons, third: center vortex
    //Position[] bluePositions = {new Position(600,1650,150), new Position(-600,1650,150), new Position(0,0,150)};
    //Position[] vortexPositions = {new Position(300, 300, 150), new Position(-300, -300, 150)}; //TODO: move into init with red and blue

    Coordinate startingCoord;
    //{new Coordinate(-300, -1500, 150, 180), new Coordinate(1500, -300, 150, 180), new Coordinate(1500, 300, 150, 180)};


    //TODO: Test this
    double flywheelPower = 0.48;




    @Override
    public void init() {

        robot = new WHSRobot(hardwareMap, allianceColor);

        if (startingPosition == 1) {
            initialConfig = "Blue on vortex";
            startingCoord = new Coordinate(1575, 300, 150, 180);
        } else if (startingPosition == 2) {
            initialConfig = "Blue off vortex";
            startingCoord = new Coordinate(1575, -300, 150, 180);
        }
        vortexPosition = new Position(-300, -300, 150);

        beacon1a = new Position(580, 1500, 150);
        beacon1b = new Position(573, 1500, 150);
        beacon2a = new Position(-620, 1500, 150);
        beacon2b = new Position(-627, 1500, 150);

        telemetry.log().add("RBT INIT");

    }

    @Override
    public void start() {
        resetStartTime();
    }

    @Override
    public void loop() {

        switch (action) {

            case "shoot two particles": {
                switch (state) {
                    case 0:
                        robot.rotateToVortex(vortexPosition);
                        state = 1;
                        break;
                    case 1:
                        if (!robot.rotateToTargetInProgress) {
                            state = 2;
                        } else {
                            robot.rotateToVortex(vortexPosition);
                        }
                        break;
                    case 2:
                        robot.flywheel2.runFlywheelNoToggle(flywheelPower);
                        state = 3;
                        break;
                    case 3:
                        try {
                            Thread.sleep(3000);                         //Wait for flywheel to spin up
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        robot.flywheel2.setParticleControlState(true);
                        state = 4;
                        break;
                    case 4:
                        try {
                            Thread.sleep(1500);                         //Wait for PC to go back up, before pulling down
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        robot.flywheel2.setParticleControlState(false);
                        state = 5;
                        break;

                    case 5:
                        try {
                            Thread.sleep(2000);                         //Wait for flywheel speed to normalize
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        robot.flywheel2.setParticleControlState(true);
                        state = 100;
                        break;

                    default:                                            //Exit phase
                        try {
                            Thread.sleep(1500);                         //Wait for PC to go up
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        robot.flywheel2.setParticleControlState(false);
                        robot.flywheel2.runFlywheel(false);

                        state = 0;
                        action = "next action";                         //Advance to the next case
                        break;
                }

            }
            case "drive to cap ball":{
                switch (state){
                    case 0:
                        if(robot.pusher.side.toString().equals("BLUE"))
                            robot.driveToTarget(blueVortex);
                        else
                            robot.driveToTarget(redVortex);
                        if(!robot.driveToTargetInProgress)
                            state++;
                        else{}
                }
            }

            case "claim beacon a":
                switch (state){
                    case 0:

                }
        }

        telemetry.addData("Action:", action);
        telemetry.addData("State:", state);

        telemetry.addData("Runtime:", time);

    }

}


