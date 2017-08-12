package org.whs542.lib.subsystest;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

/**
 * Linear Slides Subsystem Class
 * Created by Jason on 8/12/2017.
 */

public class LinearSlides {
    private DcMotor linearSlideMotor;
    private double defaultPower = 1.0;

    public LinearSlides(HardwareMap linearSlideMap) {linearSlideMotor = linearSlideMap.dcMotor.get("linear slide")};

    public void runLinearSlides(double power) {linearSlideMotor.setPower(power);}

    public void runLinearSlides(float trigger) {
        if (trigger > 0) runLinearSlides(defaultPower);
        else linearSlideMotor.setPower(0.0);
    }

    public void setDefaultPower(double power) {defaultPower = power;}
}
