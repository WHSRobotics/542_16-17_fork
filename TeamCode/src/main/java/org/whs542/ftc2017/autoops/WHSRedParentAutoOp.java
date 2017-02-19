package org.whs542.ftc2017.autoops;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.AutoOpSwitchCaseTemplate;
import org.whs542.ftc2017.autoops.WHSParentAutoOp;
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
public abstract class WHSRedParentAutoOp extends OpMode {

    WHSRobot robot;
    Timer timer;

    int state;
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

    //Position[] beaconPositions = {new Position(300,1800,150), new Position(-900,1800,150), new Position(-1800,900,150), new Position(-1800,-300,150)};
    Position beacon1a;
    Position beacon1b;
    Position beacon2a;
    Position beacon2b;

    Position[] targetPositionsBlue = {new Position(600, 1500, 150), new Position(-1300, 1500, 150)};
    Position[] targetPositionsRed = {new Position(-1500, -600, 150), new Position(-1500, 1300, 150)};
    //firstLoop: align to parallel beacons, second: end of beacons, third: center vortex
    //Position[] redPositions = {new Position(-1650,600,100), new Position(-1650,600,150), new Position(0,0,150)}; //TODO: separate do the same thing as before
    //firstLoop: align to parallel beacons, second: end of beacons, third: center vortex
    //Position[] bluePositions = {new Position(600,1650,150), new Position(-600,1650,150), new Position(0,0,150)};
    //Position[] vortexPositions = {new Position(300, 300, 150), new Position(-300, -300, 150)}; //TODO: move into init with red and blue

    Coordinate startingCoord;
    //{new Coordinate(-300, -1500, 150, 180), new Coordinate(1500, -300, 150, 180), new Coordinate(1500, 300, 150, 180)};

    @Override
    public void init(){
        //TODO: add red coordinates into both pleasee
        if (startingPosition == 1) {
            initialConfig = "Red on vortex";
            startingCoord = new Coordinate(-300, -1575, 150, 90);
        }
        else if (startingPosition == 2) {
            initialConfig = "Red off vortex";
            startingCoord = new Coordinate(300, -1575, 150, 90);
        }
        vortexPosition = new Position(300, 300, 150);


        beacon1a = new Position(-1500, -580, 150);
        beacon1b = new Position(-1500, -573, 150);
        beacon2a = new Position(-1500, 620, 150);
        beacon2b = new Position(-1500, 627, 150);
    }



}
