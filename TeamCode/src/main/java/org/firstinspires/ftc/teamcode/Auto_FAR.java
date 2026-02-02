package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.io.File;

@Autonomous
public class Auto_FAR extends LinearOpMode{
    ShootingController shootingController;
    public static OmniDriveController driveController;

    public boolean hasFinishedShooting;
    public ElapsedTime runtimeTime = new ElapsedTime();

    final MotorDefinition[] DRIVE_MOTOR_DEFINITIONS = RobotUtility.DEFAULT_DRIVE_MOTOR_DEFINITIONS;

    final MotorDefinition[] SHOOT_MOTOR_DEFINITIONS = RobotUtility.DEFAULT_SHOOT_MOTOR_DEFINITIONS;

    @Override
    public void runOpMode() throws InterruptedException {
        driveController = new OmniDriveController();
        RobotUtility.Hardware.Init(hardwareMap, DRIVE_MOTOR_DEFINITIONS, SHOOT_MOTOR_DEFINITIONS, this);

        // initialize shooting controller (handles servos, motor power and telemetry)
        shootingController = new ShootingController(hardwareMap);

        driveController = new OmniDriveController();

        hasFinishedShooting = false;
        runtimeTime.reset();

        RobotUtility.Hardware.Init(hardwareMap, DRIVE_MOTOR_DEFINITIONS, SHOOT_MOTOR_DEFINITIONS, this);


        // Wait for driver to press start
        telemetry.addData("Camera preview on/off", "3 dots, Camera Stream");
        telemetry.addData("Robot Ready.", "");
        telemetry.update();

        waitForStart();

        shootingController.update(
                false,
                true,
                true, false, true);

            driveController.moveRobot(-1, 0,0, 0.5f);
            sleep(200);
            driveController.moveRobot(0, 0,0, 0);

            shootingController.performShootingCycles(3,RobotUtility.SHOOT_FIRE_MS, RobotUtility.SHOOT_REST_MS, this, true);

            telemetry.update();

            runtimeTime.reset();

            driveController.moveRobot(-1, 0,0, 0.5f);
            sleep(670);
            driveController.moveRobot(0, 0,0, 0);

    }
}
