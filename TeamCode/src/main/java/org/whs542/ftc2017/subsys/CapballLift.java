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
    private DcMotor capballFork;
    private DcMotor capballLift;
    private Servo rightServo;
    private Servo leftServo;

    private String liftState;

    private Toggler forkToggle = new Toggler(2);
    private Toggler servoToggle = new Toggler(2);

    public CapballLift(HardwareMap map)
    {
        capballFork = map.dcMotor.get("dropFork");
        capballLift = map.dcMotor.get("liftBall");
    }

    public void dropFork(boolean trigger)
    {
        forkToggle.changeState(trigger);
        switch(forkToggle.currentState())
        {
            case 0:
                capballFork.setPower(1.0);
                break;
            case 1:
                capballFork.setPower(0.0);
                break;
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

    public void liftCapball(boolean trigger)
    {
        if(trigger)
        {
            capballLift.setPower(1.0);
            liftState = "Lifting";
        }
        else
        {
            capballLift.setPower(0.0);
            liftState = "Not lifting";
        }
    }

    public String getForkState()
    {
        String state = "";
        switch(forkToggle.currentState())
        {
            case 0:
                state = "Dropping Fork";
                break;
            case 1:
                state = "Not moving fork";
        }
        return state;
    }

    public String getLiftState()
    {
        return liftState;
    }
}
