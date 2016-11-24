package org.whs542.ftc2017;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.whs542.ftc2017.subsys.Flywheel;

/**
 * Created by jian on 11/5/2016.
 */
@Autonomous(name = "Flywheel Test", group = "Tests")
public class FlywheelTest extends OpMode{

    private Flywheel flywheel;
    String state;

    @Override
    public void init(){

        flywheel = new Flywheel(hardwareMap);

    }

    @Override
    public void loop(){
        flywheel.setFlywheelPower(1.0);
        if(flywheel.isFlywheelAtCorrectSpeed(1.0)){
            state = "At correct speed";
        }
        else {
            state = "Not at correct speed";
        }
        /*double [] encAndTime = flywheel.getCurrentSpeed();
        telemetry.addData("Encoder pre", encAndTime[0]);
        telemetry.addData("Encoder post", encAndTime[1]);
        telemetry.addData("Encoder change", encAndTime[2]);
        telemetry.addData("Time pre", encAndTime[3]);
        telemetry.addData("Time post", encAndTime[4]);
        telemetry.addData("Time change", encAndTime[5]);
        telemetry.addData("Flywheel Speed", encAndTime[6]);*/

        telemetry.addData("Correct Speed?", state);

    }

}
