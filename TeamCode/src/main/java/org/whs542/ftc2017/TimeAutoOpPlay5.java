package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;

/**
 * We're Screwed
 * A purely time and encoder based Auto
 */
@Autonomous(name = "TimeBlueAutoPlay5", group = "Auto")
public class TimeAutoOpPlay5 extends OpMode{

    WHSRobot robot;
    int state;

    @Override
    public void init() {
        robot = new WHSRobot(hardwareMap, Alliance.BLUE);
        state = 0;
    }

    @Override
    public void loop() {

        switch (state){
            case 0:
                robot.drivetrain.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                state = 1;
                break;
            case 1:
                robot.drivetrain.moveDistanceMilli2(1300);
                state = 2;
                break;
            default: break;
        }
        telemetry.addData("We are screwed (state num)", state);

    }
}
