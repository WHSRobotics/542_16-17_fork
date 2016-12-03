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
    String lastLocation;

    @Override
    public void init(){
        vuforia = new Vuforia();
        vuforia.start();
        telemetry.addData("init", "");
        telemetry.update();
    }

    @Override
    public void loop(){
        coordinate = vuforia.getHeadingAndLocation();

        double xPos = coordinate.getX();
        double yPos = coordinate.getY();
        double zPos = coordinate.getZ();
        double hdg = coordinate.getHeading();
        if(xPos != 10000) {
            String location = "Xpos: " + xPos + " Ypos: " + yPos + " Zpos: " + zPos + " Heading: " + hdg;

            telemetry.addData("Data:", location);
            lastLocation = location;
        }
        else{
            telemetry.addData("Data:", lastLocation);
        }
        telemetry.update();
    }

}
