package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Position;

/**
 * Created by Amar2 on 11/22/2016.
 */
public class RedAutoPlay2 extends OpMode {
//Play 2
    WHSRobot robot;
    int state;
    String stateInfo;
    double[] powers = {0.7, 0.8};
    final int startingPosition = 1; //1 or 2
    //Wheels, Legos, Tools, Gears
    Position[] beaconPositions = {new Position(300,1800,150), new Position(-900,1800,150), new Position(-1800,900,150), new Position(-1800,-300,150)};
    Position[] redPositions = {new Position(100,100,100), new Position(100,100,100), new Position(0,0,0)};
    Position[] bluePositions = {new Position(100,100,100), new Position(100,100,100), new Position(0,0,0)};

    @Override
    public void init() {
        robot = new WHSRobot(hardwareMap);
        state = 0;
    }

    @Override
    public void loop() {

        switch (state){
            case 0:
                robot.rotateToTarget();
                robot.flywheel.setFlywheelPower();
                //wait until the flywheel is at the correct speed
                while(!robot.flywheel.isFlywheelAtCorrectSpeed()){}
                robot.flywheel.releaseParticle();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                state = 1;
                break;
            case 1:
                while(robot.driveToTargetInProgress) {
                    robot.driveToTarget(new Position(0, -300, 150));
                }
                state = 2;
                break;
            case 2:
                break;


        }

    }
}
