package org.whs542.ftc2017.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whs542.lib.Toggler;

import java.util.concurrent.TimeUnit;

/**
 * Flywheel Subsystem Class
 */

public class Flywheel
{
    public DcMotor flywheel;
    private Servo flywheelGate;

    private boolean isFlywheelRunning;
    private boolean isFlywheelAtSpeed;
    private boolean isGateOpen;

    private final double[] teleflywheelPowers = {0.5, 0.7, 1.0}; //3 different mat location types
    public final double[] autoFlywheelPowers = {}; //TODO: test for these
    private double flywheelPower;

    private Toggler flyToggler = new Toggler(2);
    private Toggler flyModeToggler = new Toggler(3);
    private Toggler gateToggler = new Toggler(2);

    private final int MAX_SPEED = 2100; //ticks per sec
    private final double MIN_SPEED = 1000;

    private static double CIRCUMFERENCE = Math.PI * /*insert radius*/; //11.43 is diameter of wheel
    //private double TICKS_PER_SEC;
    private static double TICKS_PER_REV = 1120;
    //private double METERS_PER_SEC = CIRCUMFERENCE*TICKS_PER_SEC/ TICKS_PER_REV;
    private static double MAX_VELOCITY = 23.08397;

    //private double flySpeed = 0;
    //Toggler toggler = new Toggler(20);

    //private Coordinate Vortex = new Coordinate(304.8,304.8,304.8,1);

    public Flywheel(HardwareMap map)
    {
        flywheel = map.dcMotor.get("leftFly");
        flywheelGate = map.servo.get("flywheelGate");

        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel.setMaxSpeed(MAX_SPEED);

        flywheelPower = 0.0; //Default flywheelPower

        isFlywheelRunning = false;
        isGateOpen = false;
        isFlywheelAtSpeed = false;
    }

    //Spins flywheels if right bumper is pressed, stops spinning if pressed again.
    public void rampFlywheel(boolean rBumper)
    {
        flyToggler.changeState(rBumper);
        switch(flyToggler.currentState())
        {
            case 0:
                flywheel.setPower(flywheelPower);
                isFlywheelRunning = true;
                break;
            case 1:
                flywheel.setPower(0.0);
                isFlywheelRunning = false;
        }
    }

    //use dpad and put linear slides on joysticks
    public String getFlywheelMode(boolean dpadUp, boolean dpadDown)
    {
        flyModeToggler.changeState(dpadUp,dpadDown);
        String flywheelMode = "";

        switch(flyModeToggler.currentState())
        {
            case 0:
                flywheelMode = "approx. 1 tiles";
                flywheelPower = teleflywheelPowers[0];
                break;
            case 1:
                flywheelMode = "approx. 2 tiles";
                flywheelPower = teleflywheelPowers[1];
                break;
            case 2:
                flywheelMode = "approx. 3 tiles";
                flywheelPower = teleflywheelPowers[2];
                break;
        }
        return flywheelMode;
    }

    public boolean isFlyWheelAtRightSpeed(double targetSpeed)
    {
        if(Math.abs(getCurrentSpeed()) - Math.abs(targetSpeed) > 0.05)
        {
            isFlywheelAtSpeed = false;
        }
        else
        {
            isFlywheelAtSpeed = true;
        }
        return isFlywheelAtSpeed;
    }

    //Might change these to state and cases. Not necessary for now
    public void run(boolean b1, double powerIn)
    {
        flyModeToggler.changeState(b1);
        if(flyModeToggler.currentState() == 1)
        {
            isFlywheelRunning = true;
            flywheel.setPower(powerIn);
        }
        else
        {
            isFlywheelRunning = false;
            flywheel.setPower(0);
        }
    }

    public void setFlywheelPower(double power)
    {
        flywheel.setPower(power);
    }

    public void operateGate(boolean a)
    {
        gateToggler.changeState(a);
        switch(gateToggler.currentState())
        {
            case 0:
                flywheelGate.setPosition(0.0);
                isGateOpen = false;
                break;
            case 2:
                flywheelGate.setPosition(1.0);
                isGateOpen = true;
                break;
        }
    }

    public void operateGate(float trigger)
    {
        boolean triggerPressed;
        if(trigger > 0.05) {triggerPressed = true;}
        else {triggerPressed = false;}

        operateGate(triggerPressed);
    }

    public String getFlywheelStatus()
    {
        if(isFlywheelRunning)
            return "Spinning";
        else
            return "Not spinning";
    }

    public String getGateStatus()
    {
        if(isGateOpen)
            return "Default";
        else
            return "Not Default";
    }

    public void releaseParticle(boolean b2){
        flywheelGate.setPosition(120);
        flywheelGate.setPosition(0);
    }

    public double findPower(){
        //current position = use vuforia, take picture
        //calculate distance
        flywheel.setMaxSpeed((int) MAX_SPEED);
        double velocity = 1; //distance/ constant if it's linear, ticks per second
        flywheelPower = velocity/MAX_SPEED;
        return flywheelPower;
        //https://ftc-tricks.com/dc-motors/
    }

    public void shoot(boolean b1, boolean b2, double joystick){
        if (b1)
        {
            //thread.sleep
            //target position face vortex
            setFlywheelPower(findPower());
            run(b1, 1.0);
            boolean loop = true;
            while (loop) {
                if (flywheel.getPower() == flywheelPower && b2) {releaseParticle(b2);}
                else if (b1){loop = false;}
                else if (joystick != 0 ){loop = false;}
            }
        }
    }

    public void test(boolean b1, double velocity){
        flywheelPower = velocity/MAX_VELOCITY;
        flyModeToggler.changeState(b1);
        if(flyModeToggler.currentState() == 1)
        {
            isFlywheelRunning = true;
            flywheel.setPower(flywheelPower);
        }
        else
        {
            isFlywheelRunning = false;
            flywheel.setPower(0);
        }
    }

    //TODO: Fix this so it stops returning NaN or Infinity (probably result of divide by 0)
    //Possible cause: Encoder positions may not update until a loop refresh
    public double getCurrentSpeed(){

        int encoder1 = flywheel.getCurrentPosition();
        double time1 = (double) (System.nanoTime()) / 1000000000;
        double time2 = 0;
        double deltaTime = 0;
        while(deltaTime < 0.3){
            time2 = (double) (System.nanoTime()) / 1000000000;
            deltaTime = time2-time1;
        }
        int encoder2 = flywheel.getCurrentPosition();
        double enc = encoder2-encoder1;

        return Math.abs(enc / deltaTime);

    }





    public boolean isFlywheelAtCorrectSpeed(double power) {

        boolean status;
        double targetSpeed = power * MAX_SPEED;


        if(Math.abs(targetSpeed-getCurrentSpeed()) <= 50){
            status = true;
        }
        else{
            status = false;
        }

        return status;

    }

    /*
    public double getCurrentVelocity(){
        return (flywheel.getPower()*MAX_VELOCITY);
    }
    */

    /*
    public void testRun(boolean bool1, boolean bool2){

        toggler.changeState(bool1, bool2);

        flywheel.setFlywheelPower((double) (toggler.currentState()) / 20);
    }
    public double getSpeed(){
        return (double) (toggler.currentState()) / 20;
    }
    */
}
