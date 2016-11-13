package org.whs542.lib.swtest;

/**
 * Created by Jiangda on 10/28/2016.
 */
import org.whs542.lib.Coordinate;
import org.whs542.ftc2017.subsys.Vuforia;
import com.qualcomm.robotcore.eventloop.opmode.*;

@Autonomous(name = "VuforiaTest2",group = "Test")
public class VuforiaTest2 extends OpMode{

    Coordinate coordinate;
    Vuforia vuforia;

    @Override
    public void init(){
        vuforia = new Vuforia();
        telemetry.addData("init", "");
        telemetry.update();
    }

    @Override
    public void loop(){
        coordinate = vuforia.getHeadingAndLocation();

        double xPos = coordinate.returnCoordSingleValue("x");
        double yPos = coordinate.returnCoordSingleValue("y");
        double hdg = coordinate.returnCoordSingleValue("heading");
        String location = "XPos: " + xPos + " Ypos: " + yPos + " Heading: " + hdg;

        telemetry.addData("Data:", location);
        telemetry.update();
    }

}
