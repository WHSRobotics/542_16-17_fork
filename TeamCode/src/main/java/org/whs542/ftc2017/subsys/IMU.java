package org.whs542.ftc2017.subsys;

import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.hardware.adafruit.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.whs542.lib.Functions;

import java.util.Locale;

/**
 * IMU Class
 */

public class IMU {

    private double imuBias = 0;
    private double calibration = 0;
    private double initialHeading = 0;

    BNO055IMU imu;
    BNO055IMU.Parameters parameters;

    public IMU(HardwareMap theMap, double initialH){
        imu = theMap.get(BNO055IMU.class, "imu");

        parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        //File file = AppUtil.getInstance().getSettingsFile("AdafruitIMUCalibration.json");
        //parameters.calibrationDataFile = ReadWriteFile.readFile(file);
        //parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".
        imu.initialize(parameters);
        //initialHeading = initialH;
        //setHeading(initialHeading);
    }

    public IMU(HardwareMap theMap) {
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.calibrationDataFile = "AdafruitIMUCalibration.json"; // see the calibration sample opmode
        parameters.loggingEnabled      = true;
        parameters.loggingTag          = "IMU";
        parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();


        imu = theMap.get(BNO055IMU.class, "imu");
        imu.initialize(parameters);
    }

    //@Override
    public void run(){

        //File file = AppUtil.getInstance().getSettingsFile("AdafruitIMUCalibration.json");
        //parameters.calibrationDataFile = ReadWriteFile.readFile(file);
        //parameters.accelerationIntegrationAlgorithm = new JustLoggingAccelerationIntegrator();

        // Retrieve and initialize the IMU. We expect the IMU to be attached to an I2C port
        // on a Core Device Interface Module, configured to be a sensor of type "AdaFruit IMU",
        // and named "imu".

    }

    public double[] getThreeHeading()
    {
        double xheading = imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.XYZ).firstAngle;// - calibration;
        double yheading = imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.XYZ).secondAngle;// - calibration;
        double zheading = imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.XYZ).thirdAngle; //- calibration;

        //xheading = Functions.normalizeAngle(xheading);
        //yheading = Functions.normalizeAngle(yheading);
        //zheading = Functions.normalizeAngle(zheading);

        double[] threeHeading = {xheading,yheading,zheading};
        return threeHeading;//-180 to 180 deg
    }

    public double getHeading(){
        double heading = imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.XYZ).thirdAngle - calibration;
        heading = Functions.normalizeAngle(heading); //-180 to 180 deg
        return heading;
    }
    public void zeroHeading(){
        calibration = imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.XYZ).thirdAngle ;
    }

    /*public void setHeading(double setValue){
         calibration = imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.XYZ).firstAngle - setValue;
    }*/


    //Returns the magnitude of the acceleration, not the direction.
    public double getAccelerationMag(){
        double xAccel = imu.getLinearAcceleration().xAccel;
        double yAccel = imu.getLinearAcceleration().yAccel;
        double zAccel = imu.getLinearAcceleration().zAccel;

        double accelMag =
        Math.sqrt(
            Math.pow( xAccel, 2 ) + Math.pow( yAccel, 2 ) + Math.pow( zAccel, 2 )
        );
        return accelMag;
    }

    public void setImuBias(double vuforiaHeading){
        imuBias = Functions.normalizeAngle(vuforiaHeading - getHeading()); //-180 to 180 deg
    }

    public double getImuBias() {return imuBias;}

}

