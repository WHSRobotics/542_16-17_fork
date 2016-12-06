package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;

/**
 * Created by Amar2 on 12/3/2016.
 */
public class ActuatorTest extends OpMode{

    WHSRobot robot;


    @Override
    public void init() {
        robot = new WHSRobot(hardwareMap, Alliance.BLUE);
    }

    public void start(){
        resetStartTime();
    }


    @Override
    public void loop() {

        if (getRuntime() > 0 && getRuntime() < 4) robot.drivetrain.setLeftPower(1.0);
        else if(getRuntime() > 4 && getRuntime() < 8) robot.drivetrain.setLeftPower(-1.0);
        else robot.drivetrain.setLeftPower(0.0);

        if (getRuntime() > 8 && getRuntime() < 12) robot.drivetrain.setRightPower(1.0);
        else if (getRuntime() > 12 && getRuntime() < 16) robot.drivetrain.setRightPower(-1.0);
        else robot.drivetrain.setRightPower(0.0);

        if (getRuntime() > 16 && getRuntime() < 20) robot.intake.runIntake(1.0);
        else if (getRuntime() > 24 && getRuntime() < 28) robot.intake.runIntake(-1.0);
        else robot.intake.runIntake(0.0);

        if (getRuntime() > 28 && getRuntime() < 32) robot.flywheel.rampFlywheel(true);
        else {
            robot.flywheel.rampFlywheel(true);
            robot.flywheel.rampFlywheel(false);
        }

        if (getRuntime() > 32 && getRuntime() > 36) robot.flywheel.operateGate(true);
        else robot.flywheel.operateGate(false);
        

    }
}
