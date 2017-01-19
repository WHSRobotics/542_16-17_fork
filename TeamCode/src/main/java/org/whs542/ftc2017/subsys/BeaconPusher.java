package org.whs542.ftc2017.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.whs542.lib.Alliance;
import org.whs542.lib.Toggler;

/**
 * Beacon Pusher Subsystem Class
 * "A" means servo beaconPusher code
 */

public class BeaconPusher {
    private DcMotor beaconPusher;
    private Servo beaconHand;
    private TouchSensor touchSensor;
    public Color color;
    public Alliance side;

    private final double ENC_TICKS_PER_REV = 560;
    private final double FINAL_POS = ENC_TICKS_PER_REV * 27/8;
    public boolean isBeaconPushed;

    private Toggler beaconToggler = new Toggler(2);
    private Toggler handToggler = new Toggler(2);

    public BeaconPusher(HardwareMap map, Alliance side)
    {
        color = new Color(map);
        this.side = side;

        beaconPusher = map.dcMotor.get("beacon");
        beaconHand = map.servo.get("hand");
        beaconPusher.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        beaconPusher.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        beaconPusher.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        beaconPusher.setDirection(DcMotorSimple.Direction.REVERSE);
        beaconHand.setPosition(0.0);

        touchSensor = map.touchSensor.get("touch");

        isBeaconPushed = false;
    }


    public void extendPusher(boolean lBumper)
    {
        if(!lBumper & beaconPusher.getTargetPosition() > 0){
            beaconToggler.setState(0);
            beaconPusher.setTargetPosition(0);
            beaconPusher.setPower(0.3);
            //A beaconPusher.setPosition(0);
        }
        else if(lBumper & beaconPusher.getTargetPosition() < (int) FINAL_POS)
        {
            beaconToggler.setState(1);
            beaconPusher.setTargetPosition((int) FINAL_POS);
            beaconPusher.setPower(0.3);
            //A beaconPusher.setPosition(1);

        }
        else if(!lBumper & beaconPusher.getTargetPosition() <= 0){
            beaconToggler.setState(0);
            beaconPusher.setPower(0.0);
        }
        else{
            beaconToggler.setState(1);
            beaconPusher.setPower(0.0);
        }

       if(touchSensor.isPressed())
        {
            beaconPusher.setPower(0.0);
            beaconPusher.setTargetPosition(beaconPusher.getCurrentPosition() - 150);
            beaconPusher.setPower(0.3);
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
        if (beaconPusher.getCurrentPosition() <= 0 & !extend) {
            beaconPusher.setPower(0.0);
        } else if ((beaconPusher.getCurrentPosition() >= FINAL_POS & extend) | (touchSensor.isPressed() & extend)) {
            beaconPusher.setPower(0.0);
        } else if (extend /*& !touchSensor.isPressed()*/ & !touchSensor.isPressed()) {
            beaconToggler.setState(1);
            beaconPusher.setTargetPosition((int) FINAL_POS);
            beaconPusher.setPower(0.6);
            //A beaconPusher.setPosition(1);
        } else if (touchSensor.isPressed()) {
            beaconToggler.setState(1);
            beaconPusher.setPower(0.0);
            beaconPusher.setTargetPosition(beaconPusher.getCurrentPosition() - 150);
            beaconPusher.setPower(0.6);
        } else {
            beaconToggler.setState(0);
            beaconPusher.setTargetPosition(0);
            beaconPusher.setPower(0.6);
            //beaconPusher.setPosition(0);
        }
    }



    public boolean isBeaconPushed()
    {
        String status;
        boolean isPressed = false;

        if((color.state.equals("blue") && side == Alliance.BLUE) || (color.state.equals("red") && side == Alliance.RED)){
            status = "Match";
            extendPusher(true);
            extendPusherHand(true);
            isPressed = true;
        }
        else if(color.state.equals("purple")){
            status = "Unknown";
        }
        else {
            status = "Not match";
        }
        return isPressed;
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