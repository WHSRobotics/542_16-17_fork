package org.whs542.ftc2017.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whs542.lib.Coordinate;
import org.whs542.lib.Toggler;

/**
 * Created by Jiangda on 10/1/2016.
 */
public class Flywheel {

    private DcMotor rightFly;
    private DcMotor leftFly;
    private Servo flywheelGate;
    //private Servo particleRelease;

    private static double CIRCUMFERENCE = Math.PI * 11.43; //11.43 is diameter of wheel
    private double TICKS_PER_SEC;
    private static double TICKS_PER_REV = 1120;
    private double METERS_PER_SEC = CIRCUMFERENCE*TICKS_PER_SEC/ TICKS_PER_REV;
    private static double MAX_VELOCITY = 23.08397;

    private boolean status;
    private boolean gateStatus;
    private final double MAX_SPEED = 4000; //ticks per sec

    private Coordinate currentPosition;
    private Coordinate Vortex = new Coordinate(304.8,304.8,304.8,1);
    double power = 1;                   //Defualt power level. Can be changed via the alternate contructor, or using the method setPower

    private Toggler flyToggler = new Toggler(2);
    private Toggler gateToggler = new Toggler(2);

   //HardwareMap: basically tells the program where each device is located on the robot. The name in argument(e.g. "rightFly") should correspond exactly to the name on the phone.
    public Flywheel(HardwareMap map){
        rightFly = map.dcMotor.get("rightFly");
        leftFly = map.dcMotor.get("leftFly");
        flywheelGate = map.servo.get("flywheelGate");
        status = false;
        rightFly.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        leftFly.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //rightFly.setMaxSpeed(4000);
        //leftFly.setMaxSpeed(4000);
        //particleRelease = aMap.servo.get("particleRelease");
    }

    //The core of the flywheel program. Spins flywheels if right bumper is pressed, stops spinning if pressed again.
    public void run(boolean b1){
        flyToggler.changeState(b1);
        if(flyToggler.currentState() == 1){
            status = true;
            rightFly.setPower(power);
            leftFly.setPower(-power);
        }
        else{
            status = false;
            rightFly.setPower(0);
            leftFly.setPower(0);
        }
    }

    //Might change these to state and cases. Not necessary for now
    public void run(boolean b1, double powerIn)
    {
        flyToggler.changeState(b1);
        if(flyToggler.currentState() == 1)
        {
            status = true;
            rightFly.setPower(powerIn);
            leftFly.setPower(-powerIn);
        }
        else
        {
            status = false;
            rightFly.setPower(0);
            leftFly.setPower(0);
        }
    }

    public void operateGate(boolean a)
    {
        gateToggler.changeState(a);
        if(gateToggler.currentState() == 1)
        {
            gateStatus = true;
            flywheelGate.setPosition(1.0);
        }
        else
        {
            gateStatus = false;
            flywheelGate.setPosition(0.0);
        }
    }

    public void operateGate(float trigger)
    {
        boolean triggerPressed;
        if(trigger > 0.05) {triggerPressed = true;}
        else {triggerPressed = false;}

        gateToggler.changeState(triggerPressed);
        if(gateToggler.currentState() == 1)
        {
            gateStatus = true;
            flywheelGate.setPosition(1.0);
        }
        else
        {
            gateStatus = false;
            flywheelGate.setPosition(0.0);
        }
    }


    public String getFlywheelStatus()
    {
        if(status)
            return "Spinning";
        else
            return "Not spinning";
    }

    public String getGateStatus()
    {
        if(gateStatus)
            return "Default";
        else
            return "Not Default";
    }

    public void releaseParticle(boolean b2){
        flywheelGate.setPosition(120);
        flywheelGate.setPosition(0);
    }

    public void setPower(double power){
        this.power = power;
    }

    public double findPower(){
        //current position = use vuforia, take picture
        //calculate distance
        rightFly.setMaxSpeed((int) MAX_SPEED);
        double velocity = 1; //distance/ constant if it's linear, ticks per second
        power = velocity/MAX_SPEED;
        return power;
        //https://ftc-tricks.com/dc-motors/
    }

    public void shoot(boolean b1, boolean b2, double joystick){
        if (b1)
        {
            //thread.sleep
            //target position face vortex
            setPower(findPower());
            run(b1);
            boolean loop = true;
            while (loop) {
                if (rightFly.getPower() == power && b2) {releaseParticle(b2);}
                else if (b1){loop = false;}
                else if (joystick != 0 ){loop = false;}
            }
        }
    }

    public void test(boolean b1, double velocity){
        power = velocity/MAX_VELOCITY;
        flyToggler.changeState(b1);
        if(flyToggler.currentState() == 1)
        {
            status = true;
            rightFly.setPower(power);
            leftFly.setPower(-power);
        }
        else
        {
            status = false;
            rightFly.setPower(0);
            leftFly.setPower(0);
        }
    }

    public double getCurrentVelocity(){
        return (rightFly.getPower()*MAX_VELOCITY);
    }
}
