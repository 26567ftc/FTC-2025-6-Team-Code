package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@TeleOp(name="OmniDrive_LinearOpMode_Delegated", group="Robot")
public class LinearOpModeDelegatedController extends LinearOpMode {
    // Shooting handled by ShootingController
    ShootingController shootingController;

    AprilTagManager aprilTagManager;

    public static OmniDriveController driveController;
    public static ElapsedTime runtime = new ElapsedTime();

        final MotorDefinition[] DRIVE_MOTOR_DEFINITIONS = RobotUtility.DEFAULT_DRIVE_MOTOR_DEFINITIONS;

        final MotorDefinition[] SHOOT_MOTOR_DEFINITIONS = RobotUtility.DEFAULT_SHOOT_MOTOR_DEFINITIONS;


    @Override
    public void runOpMode() {
        driveController = new OmniDriveController();
        RobotUtility.Hardware.Init(hardwareMap, DRIVE_MOTOR_DEFINITIONS, SHOOT_MOTOR_DEFINITIONS, this);

        // initialize shooting controller (handles servos, motor power and telemetry)
        shootingController = new ShootingController(hardwareMap);

        //Initialize April Tag Detection
        aprilTagManager = new AprilTagManager(hardwareMap, true, RobotUtility.DEFAULT_WEBCAM_NAME, 0);
        aprilTagManager.init();
        driveController = new OmniDriveController();
        RobotUtility.Hardware.Init(hardwareMap, DRIVE_MOTOR_DEFINITIONS, SHOOT_MOTOR_DEFINITIONS, this);

        aprilTagManager.setManualExposure(6, 250);  // Use low exposure time to reduce motion blur

        // Wait for driver to press start
        telemetry.addData("Camera preview on/off", "3 dots, Camera Stream");
        telemetry.addLine("Robot Ready.");
        telemetry.update();

        waitForStart();
        runtime.reset();


        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());

            handleAutoDrive();
            handleDrive();

            driveController.printHeader(telemetry);
            driveController.printMotorPowerInfo(telemetry);

            // Shooting logic and telemetry now handled by ShootingController
            shootingController.update();
            
            telemetry.update();
        }
    }

    public void handleAutoDrive() {
        if (aprilTagManager.getDetected()) {
            AprilTagDetection desiredTag = aprilTagManager.desiredTag;

            if (RobotUtility.Hardware.DriveGamepad.dpad_right)
                driveController.autoDriveToAprilTag(desiredTag, telemetry);

            else if (RobotUtility.Hardware.DriveGamepad.dpad_left)
                driveController.autoDriveToAprilTag(desiredTag, telemetry, 0);

            telemetry.addData("\n>","HOLD Left-Bumper to Drive to Target\n");
            telemetry.addData("Found", "ID %d (%s)", desiredTag.id, desiredTag.metadata.name);
            telemetry.addData("Range",  "%5.1f inches", desiredTag.ftcPose.range);
            telemetry.addData("Bearing","%3.0f degrees", desiredTag.ftcPose.bearing);
            telemetry.addData("Yaw","%3.0f degrees", desiredTag.ftcPose.yaw);
        }
    }

    private void handleDrive() {
        float speedCoef = RobotUtility.DEFAULT_SPEED_COEF;

        if (RobotUtility.Hardware.DriveGamepad.left_bumper)
            speedCoef = RobotUtility.SLOW_SPEED_COEF;
        else if (RobotUtility.Hardware.DriveGamepad.right_bumper)
            speedCoef = RobotUtility.FAST_SPEED_COEF;

        double yawFromTriggers = (-RobotUtility.Hardware.DriveGamepad.left_trigger + RobotUtility.Hardware.DriveGamepad.right_trigger) * 0.85;
        OmniDriveController.DriveInput input = new OmniDriveController.DriveInput(
                RobotUtility.Hardware.DriveGamepad.left_stick_y,
                -RobotUtility.Hardware.DriveGamepad.left_stick_x,
                -(RobotUtility.Hardware.DriveGamepad.right_stick_x + yawFromTriggers),
                speedCoef
        );

        driveController.moveRobot(input);
    }
}
