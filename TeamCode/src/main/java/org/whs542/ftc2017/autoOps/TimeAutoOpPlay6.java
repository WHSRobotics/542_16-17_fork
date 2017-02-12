package org.whs542.ftc2017.autoops;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.whs542.ftc2017.subsys.WHSRobot;
import org.whs542.lib.Alliance;

/**
 * A purely time and encoder based Auto
 */

@Autonomous(name = "TimeBlueAutoPlay6", group = "Auto")
//@Disabled
//@Deprecated

public class TimeAutoOpPlay6 extends OpMode{

    WHSRobot robot;
    int state;
    final static double FLYWHEEL_POWER = 0.35;

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
                //robot.flywheel.operateGateNoToggle(false);
                robot.flywheel2.runFlywheelNoToggle(FLYWHEEL_POWER);
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
                robot.flywheel2.setParticleControlState(0.2);
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
                robot.flywheel2.setParticleControlState(0.0);
                robot.flywheel2.runFlywheelNoToggle(0.0);
                state++;
                break;
            case 5:
                if(getRuntime() > 20){
                    state++;
                }
                break;

            case 6:
                robot.drivetrain.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                state++;
                break;
            case 7:
                robot.drivetrain.moveDistanceMilli2(-1300);
                break;
            default: break;
        }

        /*
        switch (currentState) {
            case 0:
                robot.flywheel.operateGate(false);
                robot.flywheel.setFlywheelPower(FLYWHEEL_POWER);
                currentState++;
                break;
            case 1:
                try {
                    Thread.sleep(6000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentState++;
                break;
            case 2:
                robot.flywheel.operateGate(true);
                currentState++;
                break;
            case 3:
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                currentState++;
                break;
            case 4:
                robot.flywheel.setFlywheelPower(0.0);
                currentState++;
                break;
            case 5:
                robot.drivetrain.setRunMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                currentState++;
                break;
            case 6:
                robot.drivetrain.moveDistanceMilli2(-1300);
                if (time > 20) {
                    currentState++;
                }
                break;

            case 7:
                robot.drivetrain.setLRPower(0, 0);
                break;

            default:
                break;
        }
        */
        telemetry.addData("State", state);
        telemetry.addData("EncTicks", robot.drivetrain.getEncoderPosition());

    }
}
