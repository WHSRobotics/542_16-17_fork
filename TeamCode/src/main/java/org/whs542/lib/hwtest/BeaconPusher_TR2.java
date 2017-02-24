package org.whs542.lib.hwtest;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.whs542.ftc2017.subsys.Color;
import org.whs542.lib.Alliance;
import org.whs542.lib.Toggler;

/**
 * Beacon Pusher Subsystem Class, for 2nd TileRunner
 * "A" means servo beaconPusher code
 */

public class BeaconPusher_TR2 {
    public DcMotor beaconPusher;
    private Servo beaconHand;
    public Alliance side;

    private final double ENC_TICKS_PER_REV = 560;
    private final double FINAL_POS = ENC_TICKS_PER_REV * 1.96;
    private final double ENC_DEADBAND = 30;
    private boolean isBeaconPushed;

    private Toggler beaconToggler = new Toggler(2);
    private Toggler handToggler = new Toggler(2);

    public BeaconPusher_TR2(HardwareMap map, Alliance side)
    {
        this.side = side;

        beaconPusher = map.dcMotor.get("beacon");
        beaconHand = map.servo.get("hand");
        beaconPusher.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        beaconPusher.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        beaconPusher.setDirection(DcMotorSimple.Direction.REVERSE);
        beaconPusher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        beaconHand.setPosition(0.0);


        isBeaconPushed = false;
    }


    public void extendPusher(boolean lBumper)
    {
        if(!lBumper & beaconPusher.getCurrentPosition() > 0){
            beaconToggler.setState(0);
            beaconPusher.setTargetPosition(0);
            beaconPusher.setPower(0.3);
            //A beaconPusher.setPosition(0);
        }
        else if(lBumper & beaconPusher.getCurrentPosition() < (int) FINAL_POS)
        {
            beaconToggler.setState(1);
            beaconPusher.setTargetPosition((int) FINAL_POS);
            beaconPusher.setPower(0.3);
            //A beaconPusher.setPosition(1);

        }
        else if(!lBumper & beaconPusher.getCurrentPosition() <= 0){

            beaconToggler.setState(0);
            beaconPusher.setPower(0);
        }
        else{
            beaconToggler.setState(1);
            beaconPusher.setPower(0);
        }


    }

    public void extendPusherHand(boolean trigger)
    {
        if (trigger) {
            beaconHand.setPosition(0.3);
            handToggler.setState(1);
        }
        else {
            beaconHand.setPosition(0.0);
            handToggler.setState(0);
        }
    }

    public void extendPusherNoToggle(boolean extend) {
        if (Math.abs(beaconPusher.getCurrentPosition()) < ENC_DEADBAND & !extend) {
            beaconPusher.setPower(0);
        } else if (Math.abs(beaconPusher.getCurrentPosition() - FINAL_POS) < ENC_DEADBAND  & extend) {
            beaconPusher.setPower(0);
        } else if (/*& !touchSensor.isPressed()*/extend) {
            beaconToggler.setState(1);
            beaconPusher.setTargetPosition((int) FINAL_POS);
            beaconPusher.setPower(0.6);
            //A beaconPusher.setPosition(1);

        } else {
            beaconToggler.setState(0);
            beaconPusher.setTargetPosition(0);
            beaconPusher.setPower(0.6);
            //beaconPusher.setPosition(0);
        }
    }




    public String getBeaconPusherStatus()
    {
        String state = "";
        String state2 = "";
        String state3 = "";
        switch (beaconToggler.currentState())
        {
            case 0:
                state = "Main arm retracted; ";
                break;
            case 1:
                state = "Main arm extended; ";
                break;
        }
        switch(handToggler.currentState())
        {
            case 0:
                state2 = "hand retracted";
                break;
            case 1:
                state2 = "hand extended";
                break;
        }

        state3 = state + state2;
        return state3;
    }



}