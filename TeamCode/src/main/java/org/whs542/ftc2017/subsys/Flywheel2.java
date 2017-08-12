package org.whs542.ftc2017.subsys;

import com.qualcomm.ftccommon.DbgLog;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whs542.lib.SoftwareTimer;
import org.whs542.lib.Toggler;

/**
 * Flywheel subsystem class, for newer 2-wheel flywheel design
 */

public class Flywheel2
{
    public DcMotor rightFlywheel;
    public DcMotor leftFlywheel;
    private Servo particleControl;
    private Servo particleGate;
    private boolean isParticleGateOpen;

    private double[] powers = {0.60, 0.65, 0.70, 0.75, 0.80, 0.85};
    private double flywheelPower;

    private static final double UP_POSITION = 0.33;
    private static final double DOWN_POSITION = 1.0;

    private String flywheelMode;
    private String flywheelState;

    private Toggler flywheelPowerToggle = new Toggler(6);
    private Toggler flywheelExperimental = new Toggler(101);
    private Toggler flywheelToggle = new Toggler(2);

    private SoftwareTimer flywheelStartUpTimer;
    private static final double FLYWEEL_STARTUP_DELAY = 2.0;

    public boolean loopInit;
    private boolean isFlywheelAtSpeed;
    private boolean isParticleControlUp;

    private int count;

    public Flywheel2(HardwareMap map)
    {
        rightFlywheel = map.dcMotor.get("rightFly");
        leftFlywheel = map.dcMotor.get("leftFly");
        particleControl = map.servo.get("particleControl");
        this.setParticleControlState(false);
        //particleGate = map.servo.get("particleGate");
        flywheelStartUpTimer = new SoftwareTimer();

        flywheelPower = 0.0; //Default flywheelPower

        rightFlywheel.setDirection(DcMotorSimple.Direction.REVERSE);

        rightFlywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFlywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        rightFlywheel.setMaxSpeed(750);
        leftFlywheel.setMaxSpeed(750);

        count = 0;

        rightFlywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        leftFlywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        loopInit = true;
        isParticleControlUp = false;
        isFlywheelAtSpeed = false;

        flywheelExperimental.setState(40);
        isParticleGateOpen = false;
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
                flywheelState = "Flywheel Stopped";
                rightFlywheel.setPower(0.0);
                leftFlywheel.setPower(0.0);
                loopInit = true;
                break;
            case 1:
                if(loopInit) {
                    flywheelStartUpTimer.set(FLYWEEL_STARTUP_DELAY);
                    loopInit = false;
                }
                else
                {
                    if(!flywheelStartUpTimer.isExpired())
                    {
                        flywheelState = "Flywheel Warmup";
                        rightFlywheel.setPower(1.0);
                        leftFlywheel.setPower(1.0);
                    }
                    else if(flywheelStartUpTimer.isExpired()) {
                        flywheelState = "Flywheel Started - Regular";
                        rightFlywheel.setPower(flywheelPower);
                        leftFlywheel.setPower(flywheelPower);
                    }
                }
                break;
        }
        if(count%50 == 0) {
            DbgLog.msg("WHSr" + rightFlywheel.getCurrentPosition());
            DbgLog.msg("WHSl" + leftFlywheel.getCurrentPosition());
        }
        count++;
    }

    public void setFlywheelSpeed(boolean up, boolean down)
    {
        flywheelPowerToggle.changeState(up, down);
        switch(flywheelPowerToggle.currentState())
        {
            case 0:
                flywheelMode = "0.60";
                flywheelPower = powers[0];
                break;
            case 1:
                flywheelMode = "0.65";
                flywheelPower = powers[1];
                break;
            case 2:
                flywheelMode = "0.70";
                flywheelPower = powers[2];
                break;
            case 3:
                flywheelMode = "0.75";
                flywheelPower = powers[3];
                break;
            case 4:
                flywheelMode = "0.80";
                flywheelPower = powers[4];
                break;
            case 5:
                flywheelMode = "0.85";
                flywheelPower = powers[5];
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
            particleControl.setPosition(UP_POSITION);
            isParticleControlUp = true;
        }
        else
        {
            particleControl.setPosition(DOWN_POSITION);
            isParticleControlUp = false;
        }
    }

    public void setParticleControlState(boolean trigger)
    {
        if(trigger)
        {
            particleControl.setPosition(UP_POSITION);
            isParticleControlUp = true;
        }
        else
        {
            particleControl.setPosition(DOWN_POSITION);
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

    public String getFlywheelState()
    {
        return flywheelState;
    }
}
