package org.whs542.ftc2017.subsys;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whs542.lib.Alliance;

/**
 * Created by Lucy on 1/6/2017.
 */

public class WHSRobotSimple
{
    public Drivetrain drivetrain;
    public Intake intake;
    public Flywheel2 flywheel2;
    public BeaconPusher pusher;

    public WHSRobotSimple(HardwareMap robotMap, Alliance side){
        drivetrain = new Drivetrain(robotMap);
        intake = new Intake(robotMap);
        flywheel2 = new Flywheel2(robotMap);
        pusher = new BeaconPusher(robotMap, side);
    }

    public WHSRobotSimple(HardwareMap robotMap)
    {
        drivetrain = new Drivetrain(robotMap);
        intake = new Intake(robotMap);
        flywheel2 = new Flywheel2(robotMap);
        pusher = new BeaconPusher(robotMap, Alliance.BLUE);
    }
}
