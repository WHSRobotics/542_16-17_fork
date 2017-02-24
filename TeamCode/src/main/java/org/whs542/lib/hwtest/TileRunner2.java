package org.whs542.lib.hwtest;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whs542.ftc2017.subsys.BeaconPusher;
import org.whs542.ftc2017.subsys.Drivetrain;
import org.whs542.lib.Alliance;

/**
 * Created by Amar2 on 2/23/2017.
 */
@TeleOp(name = "TileRunner 2", group = "bob")
public class TileRunner2 extends OpMode {

    BeaconPusher_TR2 pusher;
    Drivetrain drivetrain;

    @Override
    public void init() {
        pusher = new BeaconPusher_TR2(hardwareMap, Alliance.BLUE);
        drivetrain = new Drivetrain(hardwareMap);
    }

    @Override
    public void loop() {
        drivetrain.setLRScaledPower(gamepad1.left_stick_y, gamepad1.right_stick_y);
        drivetrain.setOrientation(gamepad1.a);
        pusher.extendPusherNoToggle(gamepad1.left_bumper);
        pusher.extendPusherHand(gamepad1.right_bumper);
        telemetry.addData("Left Drivetrain", drivetrain.getScaledPower(gamepad1.left_stick_y));
        telemetry.addData("Right Drivetrain", drivetrain.getScaledPower(gamepad1.right_stick_y));
        telemetry.addData("Orientation", drivetrain.getOrientation());
    }

}