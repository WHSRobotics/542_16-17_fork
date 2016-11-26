package org.whs542.ftc2017.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Intake Subsystem Class
 */

public class Intake {

    private DcMotor intakeMotor;

    private String intakeState;
    private double defaultPower = 1.0;

    public Intake(HardwareMap intakeMap){
        intakeMotor = intakeMap.dcMotor.get("intake");
    }

    public void runIntake(double power)
    {
        intakeMotor.setPower(power);
    }

    public void runIntake(float rTrigger, float lTrigger)
    {
        if(rTrigger > 0.05)
        {
            intakeState = "Intake";
            intakeMotor.setPower(defaultPower);
        }
        else if(lTrigger > 0.05)
        {
            intakeState = "Outtake";
            intakeMotor.setPower(-defaultPower);
        }
        else
        {
            intakeState = "Not Moving";
            intakeMotor.setPower(0.0);
        }
    }

    public String getIntakeState()
    {
        return intakeState;
    }

    public void setDefaultPower(double power){
        defaultPower = power;
    }
}
