package org.whs542.ftc2017.subsys;

import android.app.Activity;
import android.view.View;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Container class for the color sensor
 */

public class Color {
    ColorSensor colorSensor;    // Hardware Device Object
    boolean bLedOn = false;     //Determine whether the LED is on or not
    String state = "";
    static final int TOLERANCE = 2;

    public Color(HardwareMap colorMap) {

        colorSensor = colorMap.colorSensor.get("colorSensor");

        // Set the LED in the beginning
        colorSensor.enableLed(bLedOn);

    }

    public String state() {
        if (colorSensor.red() - colorSensor.blue() >= TOLERANCE) {state = "red";}
        else if (colorSensor.blue() - colorSensor.red() >=TOLERANCE) {state = "blue";}
        else {state = "purple";}
        return state;
    }

    //Methods to get Red, Green, Blue, or Alpha values from the colorsensor
    public int getR() {return colorSensor.red();}
    public int getG() {return colorSensor.green();}
    public int getB() {return colorSensor.blue();}
    public int getA() {return colorSensor.alpha();}

}
