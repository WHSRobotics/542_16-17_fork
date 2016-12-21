package org.whs542.lib.subsystest;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whs542.ftc2017.subsys.BeaconPusher;
import org.whs542.lib.Alliance;

/**
 * Created by Amar2 on 11/24/2016.
 */
@TeleOp(name = "Color Test", group = "Tests")
//@Disabled

public class BeaconDetectionTest extends OpMode{

    BeaconPusher pusher;
    Alliance side = Alliance.BLUE;


    @Override
    public void init() {
        pusher = new BeaconPusher(hardwareMap, side);
    }

    @Override
    public void loop() {

        pusher.extendPusher(gamepad1.a);
        telemetry.addData("To Push?", pusher.isBeaconPushed());
        telemetry.addData("Color val", pusher.color.state());
        telemetry.addData("R", pusher.color.getR());
        telemetry.addData("G", pusher.color.getG());
        telemetry.addData("B", pusher.color.getB());
        telemetry.addData("A", pusher.color.getA());


    }
}
