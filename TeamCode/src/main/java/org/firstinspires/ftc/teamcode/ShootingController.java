package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Encapsulates shooter and feeder logic previously embedded in the LinearOpMode.
 * Reads controls from RobotUtility.Hardware.shootGamepad and drives the shooter motors
 * and servos registered in RobotUtility.Hardware (motors) and the provided HardwareMap (servos).
 */
public class ShootingController {


    private boolean enableShooter = false;
    private float shootPower = RobotUtility.DEFAULT_SHOOT_POWER;

    private final Servo shooterFeeder;
    private final DcMotor intakeFeeder;
    boolean enableIntake = false;

    public ShootingController(HardwareMap hardwareMap) {
        shooterFeeder = hardwareMap.get(Servo.class, "shootFeeder");
        shooterFeeder.setDirection(Servo.Direction.FORWARD);
        shooterFeeder.setPosition(RobotUtility.FEEDER_ACTIVE_ANGLE);

        intakeFeeder = hardwareMap.get(DcMotor.class, "intakeFeeder");
        intakeFeeder.setDirection(DcMotor.Direction.FORWARD);
    }

    /**
     * Call this each loop to update shooter state from gamepad and write telemetry.
     */
    public void update(Telemetry telemetry) {
        // Shoot power selection
        if (RobotUtility.Hardware.shootGamepad.dpadDownWasReleased())
            shootPower = RobotUtility.DEFAULT_SHOOT_POWER;
        else if (RobotUtility.Hardware.shootGamepad.dpadUpWasReleased())
            shootPower = RobotUtility.FAR_SHOOT_POWER;

        // Enable/disable shooter
        if (RobotUtility.Hardware.shootGamepad.yWasReleased()) enableShooter = !enableShooter;
        if (!enableShooter) shootPower = 0;

        // Set shoot motors (motors are expected to be initialized by RobotUtility.Hardware.Init)
        RobotUtility.Hardware.shootRightMotor.setPower(shootPower);
        RobotUtility.Hardware.shootLeftMotor.setPower(shootPower);

        // Shooter feeder control (A to fire)
        if (!RobotUtility.Hardware.shootGamepad.a) shooterFeeder.setPosition(RobotUtility.FEEDER_ACTIVE_ANGLE
);
        else shooterFeeder.setPosition(RobotUtility.FEEDER_REST_ANGLE);

        // Intake feeder control (X to active)
        if (RobotUtility.Hardware.shootGamepad.xWasReleased())
            enableIntake = !enableIntake;

        if(enableIntake)
            intakeFeeder.setPower(1);
        else
            intakeFeeder.setPower(0);

        // Telemetry
        telemetry.addLine("");
        telemetry.addLine("Shooter Info:");
        telemetry.addLine("Shoot Enabled: " + enableShooter);
        if (!enableShooter) telemetry.addLine("Shoot Disabled. Enable it by pressing: Y");
        telemetry.addLine("Shoot Power: " + shootPower);
        telemetry.addLine("");

        telemetry.addLine("Feeder Info:");
        telemetry.addData("Servo Position", shooterFeeder.getPosition());
        telemetry.addData("Active Servo Position", RobotUtility.FEEDER_ACTIVE_ANGLE

);
        telemetry.addData("Rest Servo Position", RobotUtility.FEEDER_REST_ANGLE);
        telemetry.addLine("");

        telemetry.addLine("Intake Info:");
        telemetry.addData("Enabled:", enableIntake);

        telemetry.addLine("");

        telemetry.addLine("Fire by pressing & holding 'A'");
    }
}
