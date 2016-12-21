package org.whs542.lib.subsystest;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whs542.ftc2017.subsys.Flywheel;
import org.whs542.ftc2017.subsys.Flywheel2;
import org.whs542.lib.Toggler;

/**
 * Created by Amar2 on 12/16/2016.
 */
@TeleOp(name = "Flywheel Speed Test", group = "ilikepi")
public class FlywheelSpeedTest extends OpMode{

    Flywheel2 flywheel;
    Toggler tog = new Toggler(20);


    @Override
    public void init() {
        flywheel = new Flywheel2(hardwareMap);
    }

    @Override
    public void loop() {
        tog.changeState(gamepad1.dpad_up, gamepad1.dpad_down);
        flywheel.runFlywheelNoToggle(tog.currentState()*0.05);
        telemetry.addData("Flywheel Power", tog.currentState()*0.05);
    }
}
