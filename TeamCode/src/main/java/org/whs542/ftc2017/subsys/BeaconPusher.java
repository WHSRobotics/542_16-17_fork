package org.whs542.ftc2017.subsys;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whs542.lib.Alliance;
import org.whs542.lib.Toggler;

/**
 * Beacon Pusher Subsystem Class
 */

public class BeaconPusher {
    private Servo beaconPusher;
    public Color color;
    Alliance side;

    private Toggler beaconToggler = new Toggler(2);

    public BeaconPusher(HardwareMap map, Alliance side) {
        beaconPusher = map.servo.get("beacon");
        color = new Color(map);
        this.side = side;
    }

    public void extendBeacon(boolean lBumper)
    {
        beaconToggler.changeState(lBumper);
        switch(beaconToggler.currentState())
        {
            case 0:
                beaconPusher.setPosition(1.0);
                break;
            case 1:
                beaconPusher.setPosition(0.0);
                break;
        }
    }

    public void extendBeaconNoToggle(boolean extend){
        if(extend){
            beaconPusher.setPosition(1.0);
        }
        else {
            beaconPusher.setPosition(0.0);
        }
    }

    public String analyzeBeacon(){

        String s;

        if((color.state.equals("blue") && side == Alliance.BLUE) || (color.state.equals("red") && side == Alliance.RED)){
            s = "match";
        }
        else if(color.state.equals("purple")){
            s = "unknown";
        }
        else {
            s = "notmatch";
        }

        return s;

    }

    public String getBeaconPusherStatus()
    {
        String state = "";
        switch (beaconToggler.currentState())
        {
            case 0:
                state = "Extended";
                break;
            case 1:
                state = "Not extended";
                break;
        }
        return state;
    }



}