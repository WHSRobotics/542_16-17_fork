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
    final static double FLYWHEEL_POWER = 0.1;

    @Override
    public void init() {
        robot = new WHSRobot(hardwareMap, Alliance.BLUE);
        state = 0;
    }

    public void start(){
        resetStartTime();
    }

    @Override
    public void loop() {

        switch (state){
            case 0:
                robot.flywheel.setFlywheelPower(FLYWHEEL_POWER);
                state++;
                break;
            case 1:
                try {
                    Thread.sleep(4000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                state++;
                break;
            case 2:
                robot.flywheel.operateGateNoToggle(true);
                state++;
                break;
            case 3:
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                state++;
                break;
            case 4:
                robot.flywheel.setFlywheelPower(0.0);
                state++;
                break;
            case 5:
                if(getRuntime() > 25){
                    state++;
                }
                break;

            case 6:
                robot.drivetrain.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                state++;
                break;
            case 7:
                robot.drivetrain.moveDistanceMilli2(1300);
                state++;
                break;
            default: break;
        }
        telemetry.addData("We are screwed (state num)", state);

    }
}
