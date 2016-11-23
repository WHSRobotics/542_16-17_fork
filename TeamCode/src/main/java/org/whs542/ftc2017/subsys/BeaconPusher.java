package org.whs542.ftc2017.subsys;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whs542.lib.Toggler;

/**
 * Created by Lucy on 11/22/2016.
 */

public class BeaconPusher {
    private Servo beaconPusher;

    private Toggler beaconToggler = new Toggler(2);

    public BeaconPusher(HardwareMap map) {
        beaconPusher = map.servo.get("beacon");
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