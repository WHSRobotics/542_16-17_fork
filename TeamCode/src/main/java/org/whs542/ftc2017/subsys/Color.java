package org.whs542.ftc2017.subsys;

import android.app.Activity;
import android.view.View;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ColorSensor;


/**
 * Created by Jason on 11/22/2016.
 */

public class Color {
    ColorSensor color_sensor;    // Hardware Device Object
    float hsvValues[] = {0F, 0F, 0F};   // hsvValues is an array that will hold the hue, saturation, and value information.
    final float values[] = hsvValues;   // values is a reference to the hsvValues array.
    boolean bLedOn = false; //Determine whether the LED is on or not
    String state = "";

    public Color(HardwareMap colorMap) {
        color_sensor = colorMap.colorSensor.get("color_sensor");

        // get a reference to the RelativeLayout so we can change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        final View relativeLayout = ((Activity) colorMap.appContext).findViewById(com.qualcomm.ftcrobotcontroller.R.id.RelativeLayout);

        // Set the LED in the beginning
        color_sensor.enableLed(bLedOn);

        // convert the RGB values to HSV values.
        android.graphics.Color.RGBToHSV(color_sensor.red() * 8, color_sensor.green() * 8, color_sensor.blue() * 8, hsvValues);

        // change the background color to match the color detected by the RGB sensor.
        // pass a reference to the hue, saturation, and value array as an argument
        // to the HSVToColor method.
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(android.graphics.Color.HSVToColor(0xff, values));
            }
        });
    }

    public String state() {
        if (color_sensor.red() - color_sensor.blue() >= 3) {state = "red";}
        else if (color_sensor.blue() - color_sensor.red() >=3) {state = "blue";}
        else {state = "purple";}

        return state;
    }

}
