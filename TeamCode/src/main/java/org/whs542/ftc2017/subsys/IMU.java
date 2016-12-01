package org.whs542.ftc2017.subsys;

import com.qualcomm.hardware.adafruit.BNO055IMU;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.whs542.lib.Functions;

/**
 * IMU Class
 */

public class IMU {

    private double imuBias = 0;
    private double calibration = 0;

    BNO055IMU imu;
    BNO055IMU.Parameters parameters;

    public IMU(HardwareMap theMap, double initialHeading){
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
        this.setHeading(initialHeading);
    }

    public IMU(HardwareMap theMap){
        imu = theMap.get(BNO055IMU.class, "imu");
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();
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

    }

    public void initalize(){
        imu.initialize(parameters);
    }

    public double getHeading(){
        double heading = imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.XYZ).thirdAngle - calibration;
        return Functions.normalizeAngle(heading); //-180 to 180 deg
    }
    public void zeroHeading(){
        calibration = imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.XYZ).thirdAngle;
    }

    public void setHeading(double setValue){
         calibration = imu.getAngularOrientation().toAxesReference(AxesReference.INTRINSIC).toAxesOrder(AxesOrder.XYZ).thirdAngle - setValue;
    }


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

