package org.whs542.ftc2017.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whs542.lib.Toggler;

/**
 * Capball Lift Subsystem Class
 */

public class CapballLift
{
    private DcMotor capballLeft;
    private DcMotor capballRight;

    private Servo rightServo;
    private Servo leftServo;
    private Servo ratchetServo;

    private String cbState;

    private Toggler servoToggle = new Toggler(2);
    private Toggler ratchetToggle = new Toggler(2);

    private static final double cbPower = 1.0;

    public CapballLift(HardwareMap map)
    {
        capballRight = map.dcMotor.get("cb_rm");
        capballLeft = map.dcMotor.get("cb_lm");
        rightServo = map.servo.get("cb_rs");
        leftServo = map.servo.get("cb_ls");
        ratchetServo = map.servo.get("cb_ratchet");
    }

    public void liftCB(boolean dpadUp)
    {
        if(dpadUp) {
            capballLeft.setPower(cbPower);
            capballRight.setPower(cbPower);
            cbState = "Lifting";
        }
        else
        {
            capballLeft.setPower(0.0);
            capballRight.setPower(0.0);
            cbState = "Not moving";
        }
    }

    public void dropCB(boolean dpadDown)
    {
        if(dpadDown) {
            capballLeft.setPower(cbPower);
            capballRight.setPower(cbPower);
            cbState = "Dropping";
        }
        else
        {
            capballLeft.setPower(0.0);
            capballRight.setPower(0.0);
            cbState = "Not moving";
        }
    }

    public void changeServo(boolean rBumper)
    {
        servoToggle.changeState(rBumper);
        switch(servoToggle.currentState())
        {
            case 0:
                rightServo.setPosition(1.0);
                leftServo.setPosition(1.0);
                break;
            case 1:
                rightServo.setPosition(0.0);
                leftServo.setPosition(0.0);
                break;
        }
    }

    public void changeRatchet(float lTrigger)
    {
        boolean pressed;

        if(lTrigger > 0.05) {pressed = true;}
        else {pressed = false;}

        ratchetToggle.changeState(pressed);
        switch(ratchetToggle.currentState())
        {
            case 0:
                ratchetServo.setPosition(1.0);
                break;
            case 1:
                ratchetServo.setPosition(1.0);
                break;
        }
    }

    public String getCBState()
    {
        return cbState;
    }
}