package org.whs542.lib.hwtest;

import android.app.Activity;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;

/**
 * Created by Jason on 11/21/2016.
 */
@Autonomous(name = "ColorTest.java", group = "hwtest")
@Disabled
public class ColorTest extends OpMode {
    ColorSensor color1;    // Hardware Device Object

    @Override
    public void init() {
        color1 = hardwareMap.colorSensor.get("1");
    }

    @Override
    public void loop() {

        // hsvValues is an array that will hold the hue, saturation, and value information.
        float hsvValues[] = {0F,0F,0F};

        // values is a reference to the hsvValues array.
        final float values[] = hsvValues;

        // get a reference to the RelativeLayout so we can change the background
        // color of the Robot Controller app to match the hue detected by the RGB sensor.
        final View relativeLayout = ((Activity) hardwareMap.appContext).findViewById(com.qualcomm.ftcrobotcontroller.R.id.RelativeLayout);

        boolean bLedOn = false;

        // Set the LED in the beginning
        color1.enableLed(bLedOn);

        // convert the RGB values to HSV values.
        android.graphics.Color.RGBToHSV(color1.red() * 8, color1.green() * 8, color1.blue() * 8, hsvValues);

        // send the info back to driver station using telemetry function.
        telemetry.addData("LED", bLedOn ? "On" : "Off");
        telemetry.addData("Clear", color1.alpha());
        telemetry.addData("Red  ", color1.red());
        telemetry.addData("Green", color1.green());
        telemetry.addData("Blue ", color1.blue());
        telemetry.addData("Hue", hsvValues[0]);

        // change the background color to match the color detected by the RGB sensor.
        // pass a reference to the hue, saturation, and value array as an argument
        // to the HSVToColor method.
        relativeLayout.post(new Runnable() {
            public void run() {
                relativeLayout.setBackgroundColor(android.graphics.Color.HSVToColor(0xff, values));
            }
        });

        telemetry.update();
    }
}

