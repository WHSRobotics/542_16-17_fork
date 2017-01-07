package org.whs542.lib.subsystest;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whs542.ftc2017.subsys.BeaconPusher;
import org.whs542.lib.Alliance;

/**
 * Created by Lucy on 11/22/2016.
 */

@TeleOp(name = "BeaconTest", group = "TeleOp")
//@Disabled
public class BeaconTest extends OpMode
{
    BeaconPusher beaconPusher;
    Alliance side = Alliance.BLUE;

    public void init() {
        beaconPusher = new BeaconPusher(hardwareMap, side);
    }

    public void loop()
    {
        beaconPusher.extendPusherNoToggle(gamepad1.left_bumper);
        telemetry.addData("Beacon status:", beaconPusher.getBeaconPusherStatus());
    }
}
