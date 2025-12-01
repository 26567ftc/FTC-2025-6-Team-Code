package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Encapsulates shooter and feeder logic previously embedded in the LinearOpMode.
 * Reads controls from RobotUtility.Hardware.shootGamepad and drives the shooter motors
 * and servos registered in RobotUtility.Hardware (motors) and the provided HardwareMap (servos).
 */
public class ShootingController {
    private boolean enableShooter = false;
    private float shootPower = RobotUtility.DEFAULT_SHOOT_POWER;
    private final DcMotor feeder;

    public ShootingController(HardwareMap hardwareMap) {
        feeder = hardwareMap.get(DcMotor.class, "shooterFeeder");
        feeder.setDirection(DcMotor.Direction.FORWARD);
    }

    /**
     * Call this each loop to update shooter state from gamepad and write telemetry.
     */
    public void update(){
        updateAuto(
                /*Set Shooter Power LOW input*/ RobotUtility.Hardware.shootGamepad.dpadDownWasReleased(),
                /*Set Shooter Power HIGH input*/ RobotUtility.Hardware.shootGamepad.dpadUpWasReleased(),
                /*Enable Shooter input*/ RobotUtility.Hardware.shootGamepad.yWasReleased(),
                /*Hold to fire Fire (Feeder) Input*/ RobotUtility.Hardware.shootGamepad.a
                );
    }
    public void updateAuto(
            boolean setDefaultPow,
            boolean setFarPow,
            boolean toggleShoter,
            boolean fire)
    {
        // Shoot power selection
        if (setDefaultPow)
            shootPower = RobotUtility.DEFAULT_SHOOT_POWER;
        else if (setFarPow)
            shootPower = RobotUtility.FAR_SHOOT_POWER;

        // Enable/disable shooter
        if (toggleShoter) enableShooter = !enableShooter;
        if (!enableShooter) shootPower = 0;

        // Set shoot motors
        RobotUtility.Hardware.shootMotor.setPower(shootPower);


        if(fire)
            feeder.setPower(1);
        else
            feeder.setPower(0);
    }

    public void printTelemetry(Telemetry telemetry) {
        telemetry.addLine("");
        telemetry.addLine("Shooter Info:");
        telemetry.addLine("Shoot Enabled: " + enableShooter);
        if (!enableShooter) telemetry.addLine("Shoot Disabled. Enable it by pressing: Y");
        telemetry.addLine("Shoot Power: " + shootPower);
        telemetry.addLine("");

        telemetry.addLine("");

        telemetry.addLine("Fire by pressing & holding 'A'");
    }
}
