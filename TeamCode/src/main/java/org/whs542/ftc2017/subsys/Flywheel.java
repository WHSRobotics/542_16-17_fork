package org.whs542.ftc2017.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.whs542.lib.Toggler;

import java.util.concurrent.TimeUnit;

/**
 * Flywheel Subsystem Class, for old one wheel Flywheel design
 * @deprecated see {@link Flywheel2} for newer 2-wheel flywheel
 */
@Deprecated
public class Flywheel
{
    public DcMotor flywheel;
    private Servo flywheelGate;

    private boolean isFlywheelRunning;
    private boolean isFlywheelAtSpeed;
    private boolean isGateOpen;

    private final double[] teleflywheelPowers = {0.15, 0.17, 0.19}; //3 different mat location types
    private double flywheelPower;
    private final int MAX_SPEED = 2600; //ticks per sec
    private final double MIN_SPEED = 1000;

    private Toggler flyToggler = new Toggler(2);
    private Toggler flyModeToggler = new Toggler(3);
    private Toggler gateToggler = new Toggler(2);

    private String flywheelStatus;
    private String flywheelMode = "";

    /*
    private static double CIRCUMFERENCE = Math.PI  /*insert radius*/; //11.43 is diameter of wheel
    //private double TICKS_PER_SEC;
    private static double TICKS_PER_REV = 1120;
    //private double METERS_PER_SEC = CIRCUMFERENCE*TICKS_PER_SEC/ TICKS_PER_REV; *.
    //private static double MAX_VELOCITY = 23.08397;

    //private Coordinate Vortex = new Coordinate(304.8,304.8,304.8,1);

    public Flywheel(HardwareMap map)
    {
        flywheel = map.dcMotor.get("flywheel");
        flywheelGate = map.servo.get("flywheelGate");

        flywheel.setDirection(DcMotorSimple.Direction.REVERSE);
        flywheel.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        flywheel.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        flywheel.setMaxSpeed(MAX_SPEED);

        flywheelPower = 0.0; //Default flywheelPower

        isFlywheelRunning = false;
        isGateOpen = false;
        isFlywheelAtSpeed = false;
    }

    //Spins flywheels if right bumper is pressed, stops spinning if pressed again.
    public void rampFlywheel(boolean lBumper)
    {
        flyToggler.changeState(lBumper);
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
    public void setFlywheelMode(boolean dpadUp, boolean dpadDown)
    {
        flyModeToggler.changeState(dpadUp,dpadDown);

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
            case 3:
                flywheelMode = "approx. 4 tiles";
                flywheelPower = teleflywheelPowers[3];
                break;
        }
    }

    public String getFlywheelMode(){
        return flywheelMode;
    }

    public void setFlywheelPower(double power)
    {
        flywheel.setPower(power);
    }



    public void setFlywheelMaxSpeed(int speed /*Enc Ticks/sec*/){
        flywheel.setMaxSpeed(speed);

    }

    public int getFlywheelEnc(){
        return flywheel.getCurrentPosition();
    }


    public void operateGate(boolean trigger)
    {
        /*boolean triggerPressed;
        if(trigger > 0.05) {triggerPressed = true;}
        else {triggerPressed = false;}

        gateToggler.changeState(triggerPressed);
        switch (gateToggler.currentState()) {
            case 0:
                flywheelGate.setPosition(0.0);
                isGateOpen = false;
                break;
            case 1:
                flywheelGate.setPosition(0.5);
                isGateOpen = true;
                break;*/

        if(trigger){
            flywheelGate.setPosition(0.5);
        }
        else{
            flywheelGate.setPosition(0.0);
        }
    }




    public String getGateStatus()
    {
        if(isGateOpen)
            return "Open";
        else
            return "Closed";
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

    public void operateFlywheelNoToggle(double power){
        flywheel.setPower(power);
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
