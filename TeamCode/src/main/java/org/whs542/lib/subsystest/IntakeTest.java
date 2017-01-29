package org.whs542.lib.subsystest;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whs542.ftc2017.subsys.Intake;

/**
 * Test OpMode for {@link Intake}
 */

@TeleOp(name = "IntakeTest", group = "tests")
@Disabled
public class IntakeTest extends OpMode {

    Intake intake;

    @Override
    public void init() {
        intake = new Intake(hardwareMap);
    }

    @Override
    public void loop() {
        intake.runIntake(gamepad1.left_trigger, gamepad1.right_trigger);
    }
}
