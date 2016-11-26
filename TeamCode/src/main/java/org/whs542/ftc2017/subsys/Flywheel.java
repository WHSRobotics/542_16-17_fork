package org.whs542.ftc2017.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
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
    private final int MAX_SPEED = 2100; //ticks per sec
    private final double MIN_SPEED = 1000;

    private Toggler flyToggler = new Toggler(2);
    private Toggler flyModeToggler = new Toggler(3);
    private Toggler gateToggler = new Toggler(2);

    private String flywheelStatus;
    /*
    private static double CIRCUMFERENCE = Math.PI  /*insert radius*/; //11.43 is diameter of wheel
    //private double TICKS_PER_SEC;
    private static double TICKS_PER_REV = 1120;
    //private double METERS_PER_SEC = CIRCUMFERENCE*TICKS_PER_SEC/ TICKS_PER_REV; *.
    //private static double MAX_VELOCITY = 23.08397;

    //private Coordinate Vortex = new Coordinate(304.8,304.8,304.8,1);

    public Flywheel(HardwareMap map)
    {
        flywheel = map.dcMotor.get("leftFly");
        flywheelGate = map.servo.get("flywheelGate");

        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel.setMaxSpeed(MAX_SPEED);

        flywheelPower = 1.0; //Default flywheelPower

        isFlywheelRunning = false;
        isGateOpen = false;
        isFlywheelAtSpeed = false;
    }

    //Spins flywheels if right bumper is pressed, stops spinning if pressed again.
    public void rampFlywheel(boolean rBumper)
    {
        flyToggler.changeState(rBumper);
        switch (flyToggler.currentState()) {
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

    //Might change these to state and cases. Not necessary for now
    /*
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
*/
    public void setFlywheelPower(double power)
    {
        flywheel.setPower(power);
    }

    public void operateGate(double trigger)
    {
        boolean triggerPressed;
        if(trigger > 0.05) {triggerPressed = true;}
        else {triggerPressed = false;}

        if(triggerPressed == true){
            flywheelGate.setPosition(1.0);
            while(flywheelGate.getPosition() <= 1.0)
                if(flywheelGate.getPosition() == 1.0)
                    flywheelGate.setPosition(0.0);

        }

        /*gateToggler.changeState(triggerPressed);
        switch (gateToggler.currentState()) {
            case 0:
                flywheelGate.setPosition(1.0);
                isGateOpen = true;
                break;
            case 2:
                flywheelGate.setPosition(0.0);
                isGateOpen = false;
                break;
        }*/
    }

    public void operateGateNoToggle(boolean button){
        if(button){
            flywheelGate.setPosition(1.0);
        }
        else{
            flywheelGate.setPosition(0.0);
        }
    }

    public String getGateStatus()
    {
        if(isGateOpen)
            return "Default";
        else
            return "Not Default";
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

    /*
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
    */

    /*
    public void test(boolean b1, double velocity){
        flywheelPower = velocity/1.0; //ORIGINAL DIVIDED BY MAX_VELOCITY
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
*/
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

    public boolean isFlywheelAtCorrectSpeed(double power)
    {
        double targetSpeed = power * MAX_SPEED;

        if(Math.abs(targetSpeed-getCurrentSpeed()) <= 50){
            isFlywheelAtSpeed = true;
            flywheelStatus = "At speed. Ready to shoot.";
        }
        else{
            isFlywheelAtSpeed = false;
            flywheelStatus = "Not at speed yet. Don't shoot yet.";
        }

        return isFlywheelAtSpeed;
    }

    public String getFlywheelStatus()
    {
        return flywheelStatus;
    }

    /* Test Program for getCurrentSpeed()
    public double[] getCurrentSpeedTest(){

        int encoder1 = flywheel.getCurrentPosition();
        double time1 = TimeUnit.NANOSECONDS.toSeconds(System.nanoTime());

        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        double d1 = (flywheel.getCurrentPosition()-encoder1);
        double d2 = (TimeUnit.NANOSECONDS.toSeconds(System.nanoTime())-time1);
    */

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
