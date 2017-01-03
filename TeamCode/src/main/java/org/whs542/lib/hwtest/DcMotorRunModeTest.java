package org.whs542.lib.hwtest;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.w3c.dom.Document;
import org.whs542.lib.Toggler;

/**
 * Created by Amar2 on 11/19/2016.
 */
@Autonomous(name = "RunModeTest", group = "Tests")
//@Disabled

public class DcMotorRunModeTest extends OpMode{

    DcMotor a;
    //DcMotor b;
    Toggler tog = new Toggler(40);
    Toggler tog2 = new Toggler(2);
    int target;

    @Override
    public void init() {
        a = hardwareMap.dcMotor.get("beacon");
        //b = hardwareMap.dcMotor.get("b");
        a.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        //b.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //a.setTargetPosition(20000);
        //b.setTargetPosition(20000);
    }

    public void start(){
        resetStartTime();
    }

    @Override
    public void loop() {
        tog2.changeState(gamepad1.x);
        tog.changeState(gamepad1.dpad_up, gamepad1.dpad_down);
        target = (tog.currentState()+1)*100;
        a.setTargetPosition(target);
        //b.setMaxSpeed(power);


        if(tog2.currentState() == 1 /*&& a.getCurrentPosition() <= 20000*/){
            a.setPower(1.0);
            //b.setPower(1.0);
        }
        else if (gamepad1.a || gamepad1.b){
            if (gamepad1.a) a.setPower(0.1);
            //if (gamepad1.b) b.setPower(1.0);
        }
        else {
            a.setPower(0);
            //b.setPower(0);
        }

        if(gamepad1.y){
            a.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            //b.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            a.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            //b.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        telemetry.addData("A Enc Ticks", a.getCurrentPosition());
        //telemetry.addData("B Enc Ticks", b.getCurrentPosition());
        telemetry.addData("Is busy?", a.isBusy());
        telemetry.addData("Power:", a.getPower());
        telemetry.addData("Target position", target);
        telemetry.addData("State", tog2.currentState());
        telemetry.addData("Loop Runtime", getRuntime());

    }
}
