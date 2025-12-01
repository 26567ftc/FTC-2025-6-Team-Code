package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import java.util.List;

@TeleOp(name="Omni Drive To AprilTag", group = "Concept")
public class AutoDriveToAprilTagOpMode extends LinearOpMode {
    private AprilTagManager aprilTagManager;

    OmniDriveController driveController = null;
    // Use default motor definitions from RobotUtility
    final MotorDefinition[] DRIVE_MOTOR_DEFINITIONS = RobotUtility.DEFAULT_DRIVE_MOTOR_DEFINITIONS;
    final MotorDefinition SHOOT_MOTOR_DEFINITION = RobotUtility.SHOOT_MOTOR_DEFINITION;

    @Override public void runOpMode() {


        // Initialize the AprilTag manager
        aprilTagManager = new AprilTagManager(hardwareMap, true, RobotUtility.DEFAULT_WEBCAM_NAME);
        aprilTagManager.init();
        driveController = new OmniDriveController();
        RobotUtility.Hardware.Init(hardwareMap, DRIVE_MOTOR_DEFINITIONS, SHOOT_MOTOR_DEFINITION, this);

        aprilTagManager.setManualExposure(6, 250);  // Use low exposure time to reduce motion blur

        // Wait for driver to press start
        telemetry.addData("Camera preview on/off", "3 dots, Camera Stream");
        telemetry.addData(">", "Touch START to start OpMode");
        telemetry.update();
        waitForStart();

        while (opModeIsActive())
        {
            if (aprilTagManager.getDetected()) {
                AprilTagDetection desiredTag = aprilTagManager.desiredTag;

                if (RobotUtility.Hardware.DriveGamepad.left_bumper)
                    driveController.autoDriveToAprilTag(desiredTag, telemetry);

                telemetry.addData("\n>","HOLD Left-Bumper to Drive to Target\n");
                telemetry.addData("Found", "ID %d (%s)", desiredTag.id, desiredTag.metadata.name);
                telemetry.addData("Range",  "%5.1f inches", desiredTag.ftcPose.range);
                telemetry.addData("Bearing","%3.0f degrees", desiredTag.ftcPose.bearing);
                telemetry.addData("Yaw","%3.0f degrees", desiredTag.ftcPose.yaw);
            }

            telemetry.addData("\n>","Drive using joysticks to find valid target\n");

            driveController.moveRobot(RobotUtility.Hardware.DriveGamepad.left_stick_y,
                    -RobotUtility.Hardware.DriveGamepad.left_stick_x,
                    -RobotUtility.Hardware.DriveGamepad.right_stick_x, 0.5f);


            telemetry.update();
            sleep(10);
        }
    }
}
