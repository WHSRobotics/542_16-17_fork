package org.whs542.lib.subsystest;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whs542.ftc2017.subsys.BeaconPusher;
import org.whs542.lib.Alliance;

/**
 * Created by Amar2 on 2/3/2017.
 */
@Autonomous(name = "AutoBeaconPusherTest", group = "test")
//@Disabled
public class AutoBeaconTest extends OpMode {

    BeaconPusher pusher;

    @Override
    public void init() {
        pusher = new BeaconPusher(hardwareMap, Alliance.BLUE);
    }

    @Override
    public void loop() {

        //telemetry.addData("Is beacon pushed", pusher.isBeaconPushed());
        telemetry.addData("Side equals Alliance.Blue?", pusher.side.equals(Alliance.BLUE));
        telemetry.addData("Color state equals blue?", pusher.color.state().equals("blue"));
        telemetry.addData("isBeaconCorrectColor() reading", pusher.isBeaconCorrectColor());
        telemetry.addData("Color State", pusher.color.state());

    }
}
