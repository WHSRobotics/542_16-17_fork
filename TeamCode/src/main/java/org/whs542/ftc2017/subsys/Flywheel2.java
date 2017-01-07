package org.whs542.ftc2017.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whs542.lib.Toggler;

/**
 * Flywheel subsystem class, for newer 2-wheel flywheel design
 */

public class Flywheel2
{
    private DcMotor rightFlywheel;
    private DcMotor leftFlywheel;
    private Servo particleControl;

    private double[] powers = {0.70, 0.75, 0.80, 0.85};
    private double flywheelPower;

    private String flywheelMode;

    private Toggler flywheelPowerToggle = new Toggler(4);
    private Toggler flywheelExperimental = new Toggler(101);
    private Toggler flywheelToggle = new Toggler(2);

    private boolean isFlywheelAtSpeed;
    private boolean isParticleControlUp;

    public  Flywheel2(HardwareMap map)
    {
        rightFlywheel = map.dcMotor.get("rightFly");
        leftFlywheel = map.dcMotor.get("leftFly");
        particleControl = map.servo.get("particleControl");

        flywheelPower = 0.0; //Default flywheelPower

        rightFlywheel.setDirection(DcMotorSimple.Direction.REVERSE);

        rightFlywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFlywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFlywheel.setMaxSpeed(1000);
        leftFlywheel.setMaxSpeed(1000);

        rightFlywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftFlywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        isParticleControlUp = false;
        isFlywheelAtSpeed = false;

        flywheelExperimental.setState(40);
    }

    public void runFlywheelNoToggle(double power){
        leftFlywheel.setPower(power);
        rightFlywheel.setPower(power);
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
                flywheelMode = "Approx. 2 - 0.70";
                flywheelPower = powers[0];
                break;
            case 1:
                flywheelMode = "Diag. 2 - 0.75";
                flywheelPower = powers[1];
                break;
            case 2:
                flywheelMode = "Approx. 3 - 0.80";
                flywheelPower = powers[2];
                break;
            case 3:
                flywheelMode = "Diag. 3 - 0.85";
                flywheelPower = powers[3];
                break;
        }
    }

    public double setFlywheelSpeedExperimental(boolean up, boolean down)
    {
        flywheelExperimental.changeState(up, down);
        double power = flywheelExperimental.currentState();
        if(power == 0)
            flywheelPower = 0;
        else
        {
            power = power / 100;
            flywheelPower = power;
        }
        return power;
    }

    public void setParticleControlState(double trigger)
    {
        if(trigger > 0.1)
        {
            particleControl.setPosition(0.1);
            isParticleControlUp = true;
        }
        else
        {
            particleControl.setPosition(0.9);
            isParticleControlUp = false;
        }
    }

    public void setParticleControlState(boolean trigger)
    {
        if(trigger)
        {
            particleControl.setPosition(0.1);
            isParticleControlUp = true;
        }
        else
        {
            particleControl.setPosition(1.0);
            isParticleControlUp = false;
        }
    }


    public String getFlywheelMode()
    {
        return flywheelMode;
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
