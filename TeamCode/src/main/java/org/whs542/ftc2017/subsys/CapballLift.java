package org.whs542.ftc2017.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whs542.lib.Toggler;

/**
 * Created by Lucy Wang on 11/12/2016.
 */

public class CapballLift
{
    private DcMotor capballLeft;
    private DcMotor capballRight;
    private Servo rightServo;
    private Servo leftServo;

    private String cbState;

    private Toggler servoToggle = new Toggler(2);

    private static final double cbPower = 1.0;

    public CapballLift(HardwareMap map)
    {
        capballRight = map.dcMotor.get("cb_rightm");
        capballLeft = map.dcMotor.get("cb_leftm");
        rightServo = map.servo.get("cb_rights");
        leftServo = map.servo.get("cb_lefts");
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

    public String getCBState()
    {
        return cbState;
    }
}
