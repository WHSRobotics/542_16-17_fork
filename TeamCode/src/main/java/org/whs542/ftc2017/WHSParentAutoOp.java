package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.ftc2017.subsys.IMU;
import org.whs542.ftc2017.subsys.Vuforia;
import org.whs542.lib.Alliance;
import org.whs542.lib.Position;

/**
 * Parent AutoOp Class
 */

public abstract class WHSParentAutoOp extends OpMode {
    WHSRobot robot;

    Alliance allianceColor = Alliance.BLUE;
    final int startingPosition = 1; //1 or 2

    //Wheels, Legos, Tools, Gears
    Position[] beaconPositions = {new Position(300,1800,150), new Position(-900,1800,150), new Position(-1800,900,150), new Position(-1800,-300,150)};
    //first: align to parallel beacons, second: end of beacons, third: center vortex
    Position[] redPositions = {new Position(-1650,600,100), new Position(-1650,600,150), new Position(0,0,150)};
    //first: align to parallel beacons, second: end of beacons, third: center vortex
    Position[] bluePositions = {new Position(600,1650,150), new Position(-600,1650,150), new Position(0,0,150)};

    @Override
    public void init(){
        robot = new WHSRobot(hardwareMap, allianceColor);
        //DbgLog.msg("Rbt init");
        telemetry.addData("RBT Init", 1);
    }

    public abstract void loop(

    );

}
