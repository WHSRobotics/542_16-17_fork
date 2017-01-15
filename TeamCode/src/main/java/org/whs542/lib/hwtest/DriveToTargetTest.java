package org.whs542.lib.hwtest;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.ftc2017.subsys.WHSRobotSimple;
import org.whs542.lib.Alliance;
import org.whs542.lib.Coordinate;
import org.whs542.lib.Functions;
import org.whs542.lib.Position;

/**
 * Created by Jason on 12/7/2016.
 */

@Autonomous(name = "Drive to Target Test", group = "TeleOp")
public class DriveToTargetTest extends OpMode{
    WHSRobot robot;
    int state;

    @Override
    public void init() {

        robot = new WHSRobot(hardwareMap, Alliance.BLUE);
        robot.setInitialCoordinate(new Coordinate(0, 0 , 0, 0));
        state = 0;
        telemetry.log().add("Wait for vuforia!!!");
        //telemetry.setMsTransmissionInterval(100);
    }

    @Override
    public void loop() {
        robot.estimateHeading();
        robot.estimatePosition();

        switch(state){
            case 0:
                robot.driveToTarget(new Position(0, 1800, 0));
                if(!robot.driveToTargetInProgress)
                   state++;
                break;
            case 1:
                robot.driveToTarget(new Position(-1200, 1800, 0));
                if (!robot.driveToTargetInProgress)
                    state++;
                break;
            case 2:
                robot.driveToTarget(new Position(-1200, 0, 0));
                if(!robot.driveToTargetInProgress)
                    state++;
                break;
        }






        /*switch(state)
        {
            case 0:
                robot.rotateToTargetInProgress = true;
                state++;
                break;
            case 1:
                Position vectorToTarget = Functions.subtractPositions(new Position(0, 3000, 0), robot.currentCoord.getPos()); //field frame
                vectorToTarget = robot.field2body(vectorToTarget); //body frame

                double distanceToTarget = Functions.calculateMagnitude(vectorToTarget);

                double degreesToRotate = Math.atan2(vectorToTarget.getY(), vectorToTarget.getX()); //from -pi to pi rad
                //double degreesToRotate = Math.atan2(targetPos.getY(), targetPos.getX()); //from -pi to pi rad
                degreesToRotate = degreesToRotate * 180 / Math.PI;
                double targetHeading = Functions.normalizeAngle(robot.currentCoord.getHeading() + degreesToRotate); //-180 to 180 deg
                robot.rotateToTarget(targetHeading);
                if(!robot.rotateToTargetInProgress)
                {
                    state++;
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case 2:
                robot.driveToTarget(new Position(0, 3000, 0));
                break;


        }*/


        telemetry.addData("Rx:", robot.currentCoord.getX());
        telemetry.addData("Ry:", robot.currentCoord.getY());
        telemetry.addData("Hdg:", robot.currentCoord.getHeading());
        telemetry.addData("TargetHdg", robot.targetHeading);
        telemetry.addData("RotateToTargetInProgress?", robot.rotateToTargetInProgress);
        telemetry.addData("DriveToTargetInProgress?", robot.driveToTargetInProgress);
        telemetry.addData("MsTransInterval", telemetry.getMsTransmissionInterval());
        telemetry.update();
        /*
        if (!robot.driveToTargetInProgress){
            requestOpModeStop();
        }
        */
    }
}
