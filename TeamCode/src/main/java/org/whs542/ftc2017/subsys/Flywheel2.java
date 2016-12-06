package org.whs542.ftc2017.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whs542.lib.Toggler;

/**
 * Created by Lucy on 12/5/2016.
 */

public class Flywheel2
{
    private DcMotor rightFlywheel;
    private DcMotor leftFlywheel;
    private Servo particleControl;

    private double[] powers = {0.3, 0.6, 0.9};
    private double flywheelPower;

    private String flywheelMode;

    private Toggler flywheelPowerToggle = new Toggler(3);
    private Toggler flywheelToggle = new Toggler(2);

    private boolean isFlywheelAtSpeed;
    private boolean isParticleControlUp;

    public Flywheel2(HardwareMap map)
    {
        rightFlywheel = map.dcMotor.get("rightFly");
        leftFlywheel = map.dcMotor.get("leftFly");
        particleControl = map.servo.get("particleControl");
    }

    public void runFlywheel(boolean bumper)
    {
        flywheelToggle.changeState(bumper);
        switch(flywheelToggle.currentState())
        {
            case 0:
                rightFlywheel.setPower(flywheelPower);
                leftFlywheel.setPower(flywheelPower); //TODO: create a setspeed method based on encoder with feedback loop
                break;
            case 1:
                rightFlywheel.setPower(0.0);
                leftFlywheel.setPower(0.0);
                break;
        }
    }

    public void setFlywheelSpeed(boolean up, boolean down)
    {
        flywheelPowerToggle.changeState(up, down);
        switch(flywheelPowerToggle.currentState())
        {
            case 0:
                flywheelMode = "approx. 1 tiles";
                flywheelPower = powers[0];
                break;
            case 1:
                flywheelMode = "approx. 2 tiles";
                flywheelPower = powers[1];
                break;
            case 2:
                flywheelMode = "approx. 3 tiles";
                flywheelPower = powers[2];
                break;
        }
    }

    public String getFlywheelState()
    {
        return flywheelMode;
    }

    public void setPaticleControlState(double trigger)
    {
        if(trigger > 0.1)
        {
            particleControl.setPosition(0.0);
        }
        else
        {
            particleControl.setPosition(1.0);
        }
    }

    public String getParticleControlState()
    {
        String pcState = "";

        if(isParticleControlUp)
        {
            pcState = "Up";
        }
        else
        {
            pcState = "Down";
        }
        return pcState;
    }
}
