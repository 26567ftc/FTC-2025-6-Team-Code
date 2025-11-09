package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Position;
import org.firstinspires.ftc.robotcore.external.navigation.YawPitchRollAngles;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;

/**
 * Controller class for AprilTag detection and localization.
 * This class encapsulates the AprilTag vision processing functionality and can be used
 * by both TeleOp and Autonomous OpModes.
 */
public class AprilTagController {
    private static final boolean USE_WEBCAM = true;  // true for webcam, false for phone camera

    private final Position cameraPosition;
    private final YawPitchRollAngles cameraOrientation;
    private final AprilTagProcessor aprilTag;
    private final VisionPortal visionPortal;

    /**
     * Creates a new AprilTagController with default camera position and orientation.
     * @param hardwareMap The OpMode's hardware map
     */
    public AprilTagController(HardwareMap hardwareMap) {
        this(hardwareMap, 
             new Position(DistanceUnit.INCH, 0, 0, 0, 0),
             new YawPitchRollAngles(AngleUnit.DEGREES, 0, -90, 0, 0));
    }

    /**
     * Creates a new AprilTagController with custom camera position and orientation.
     * @param hardwareMap The OpMode's hardware map
     * @param cameraPosition Position of camera relative to robot center
     * @param cameraOrientation Orientation of camera relative to robot
     */
    public AprilTagController(HardwareMap hardwareMap, Position cameraPosition, YawPitchRollAngles cameraOrientation) {
        this.cameraPosition = cameraPosition;
        this.cameraOrientation = cameraOrientation;
        
        // Create the AprilTag processor
        aprilTag = new AprilTagProcessor.Builder()
                .setCameraPose(cameraPosition, cameraOrientation)
                .build();

        // Create the vision portal
        VisionPortal.Builder builder = new VisionPortal.Builder();
        if (USE_WEBCAM) {
            builder.setCamera(hardwareMap.get(WebcamName.class, "Webcam 1"));
        } else {
            builder.setCamera(BuiltinCameraDirection.BACK);
        }
        builder.addProcessor(aprilTag);
        visionPortal = builder.build();
    }

    /**
     * Gets the current list of AprilTag detections.
     * @return List of current AprilTag detections
     */
    public List<AprilTagDetection> getCurrentDetections() {
        return aprilTag.getDetections();
    }

    /**
     * Stops the camera stream to save CPU resources.
     */
    public void stopStreaming() {
        visionPortal.stopStreaming();
    }

    /**
     * Resumes the camera stream.
     */
    public void resumeStreaming() {
        visionPortal.resumeStreaming();
    }

    /**
     * Closes the vision portal when it's no longer needed.
     */
    public void close() {
        visionPortal.close();
    }

    /**
     * Updates telemetry with current AprilTag detection information.
     * @param telemetry The OpMode's telemetry object
     */
    public void updateTelemetry(Telemetry telemetry) {
        List<AprilTagDetection> currentDetections = getCurrentDetections();
        telemetry.addData("# AprilTags Detected", currentDetections.size());

        // Step through the list of detections and display info for each one
        for (AprilTagDetection detection : currentDetections) {
            if (detection.metadata != null) {
                telemetry.addLine(String.format("\n==== (ID %d) %s", detection.id, detection.metadata.name));
                // Only use tags that don't have Obelisk in them
                if (!detection.metadata.name.contains("Obelisk")) {
                    telemetry.addLine(String.format("XYZ %6.1f %6.1f %6.1f  (inch)",
                            detection.robotPose.getPosition().x,
                            detection.robotPose.getPosition().y,
                            detection.robotPose.getPosition().z));
                    telemetry.addLine(String.format("PRY %6.1f %6.1f %6.1f  (deg)",
                            detection.robotPose.getOrientation().getPitch(AngleUnit.DEGREES),
                            detection.robotPose.getOrientation().getRoll(AngleUnit.DEGREES),
                            detection.robotPose.getOrientation().getYaw(AngleUnit.DEGREES)));
                }
            } else {
                telemetry.addLine(String.format("\n==== (ID %d) Unknown", detection.id));
                telemetry.addLine(String.format("Center %6.0f %6.0f   (pixels)", 
                    detection.center.x, detection.center.y));
            }
        }

        // Add "key" information to telemetry
        telemetry.addLine("\nkey:\nXYZ = X (Right), Y (Forward), Z (Up) dist.");
        telemetry.addLine("PRY = Pitch, Roll & Yaw (XYZ Rotation)");
    }
}