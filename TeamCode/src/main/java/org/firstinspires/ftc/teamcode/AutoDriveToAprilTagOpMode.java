package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import java.util.List;

@TeleOp(name="Omni Drive To AprilTag", group = "Concept")
public class AutoDriveToAprilTagOpMode extends LinearOpMode {

    // Use shared constants from RobotUtility
    private AprilTagDetection desiredTag = null;     // Used to hold the data for a detected AprilTag
    private AprilTagManager aprilTagManager;

    OmniDriveController driveController = null;
    // Use default motor definitions from RobotUtility
    final MotorDefinition[] DRIVE_MOTOR_DEFINITIONS = RobotUtility.DEFAULT_DRIVE_MOTOR_DEFINITIONS;
    final MotorDefinition[] SHOOT_MOTOR_DEFINITIONS = RobotUtility.DEFAULT_SHOOT_MOTOR_DEFINITIONS;

    @Override public void runOpMode() {
        boolean targetFound     = false;    // Set to true when an AprilTag target is detected
        double  drive           = 0;        // Desired forward power/speed (-1 to +1)
        double  strafe          = 0;        // Desired strafe power/speed (-1 to +1)
        double  turn            = 0;        // Desired turning power/speed (-1 to +1)

        // Initialize the AprilTag manager
        aprilTagManager = new AprilTagManager(hardwareMap, true, RobotUtility.DEFAULT_WEBCAM_NAME);
        aprilTagManager.init();
        driveController = new OmniDriveController();
        RobotUtility.Hardware.Init(hardwareMap, DRIVE_MOTOR_DEFINITIONS, SHOOT_MOTOR_DEFINITIONS, this);

        aprilTagManager.setManualExposure(6, 250);  // Use low exposure time to reduce motion blur

        // Wait for driver to press start
        telemetry.addData("Camera preview on/off", "3 dots, Camera Stream");
        telemetry.addData(">", "Touch START to start OpMode");
        telemetry.update();
        waitForStart();

        while (opModeIsActive())
        {
            targetFound = false;
            desiredTag  = null;
            
            List<AprilTagDetection> currentDetections = aprilTagManager.getDetections();
            for (AprilTagDetection detection : currentDetections) {
                // Look to see if we have size info on this tag.
                if (detection.metadata != null) {
                    //  Check to see if we want to track towards this tag.
                    if ((RobotUtility.DESIRED_TAG_ID < 0) || (detection.id == RobotUtility.DESIRED_TAG_ID)) {
                        targetFound = true;
                        desiredTag = detection;
                        break;
                    } else {
                        // This tag is in the library, but we do not want to track it right now.
                        telemetry.addData("Skipping", "Tag ID %d is not desired", detection.id);
                    }
                } else {
                    // This tag is NOT in the library, so we don't have enough information to track to it.
                    telemetry.addData("Unknown", "Tag ID %d is not in TagLibrary", detection.id);
                }
            }

            // Tell the driver what we see, and what to do.
                if (targetFound) {
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

    // AprilTag initialization and exposure control is handled by AprilTagManager
}
