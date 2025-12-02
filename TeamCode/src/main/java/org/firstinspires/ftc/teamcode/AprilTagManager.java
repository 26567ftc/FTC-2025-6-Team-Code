package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;
import org.firstinspires.ftc.robotcore.external.hardware.camera.BuiltinCameraDirection;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.ExposureControl;
import org.firstinspires.ftc.robotcore.external.hardware.camera.controls.GainControl;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;
import org.firstinspires.ftc.vision.apriltag.AprilTagProcessor;

import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Encapsulates AprilTag processing and camera control so OpModes can reuse it.
 */
public class AprilTagManager {
    private final HardwareMap hardwareMap;
    private final boolean useWebcam;
    private final String webcamName;

    public boolean targetFound;
    public AprilTagDetection desiredTag;

    private VisionPortal visionPortal;
    private AprilTagProcessor aprilTag;

    public AprilTagManager(HardwareMap hardwareMap, boolean useWebcam, String webcamName) {
        this.hardwareMap = hardwareMap;
        this.useWebcam = useWebcam;
        this.webcamName = webcamName;
    }

    public void init() {
        aprilTag = new AprilTagProcessor.Builder().build();
        aprilTag.setDecimation(2);

        if (useWebcam) {
            visionPortal = new VisionPortal.Builder()
                    .setCamera(hardwareMap.get(WebcamName.class, webcamName))
                    .addProcessor(aprilTag)
                    .build();
        } else {
            visionPortal = new VisionPortal.Builder()
                    .setCamera(BuiltinCameraDirection.BACK)
                    .addProcessor(aprilTag)
                    .build();
        }
    }

    public boolean getDetected() {
        List<AprilTagDetection> currentDetections = aprilTag.getDetections();
        for (AprilTagDetection detection : currentDetections)
            if (detection.metadata != null)
                if (RobotUtility.FilterTagID(detection.id)) { //  Check to see if we want to track towards this tag.
                    targetFound = true;
                    desiredTag = detection;
                    break;
                }
                else  targetFound = false;


        return targetFound;
    }

    public void setManualExposure(int exposureMS, int gain) {
        if (visionPortal == null) return;

        // Wait until streaming
        while (visionPortal.getCameraState() != VisionPortal.CameraState.STREAMING) {
            try { Thread.sleep(20); } catch (InterruptedException ignored) {}
        }

        ExposureControl exposureControl = visionPortal.getCameraControl(ExposureControl.class);
        if (exposureControl.getMode() != ExposureControl.Mode.Manual) {
            exposureControl.setMode(ExposureControl.Mode.Manual);
            try { Thread.sleep(50); } catch (InterruptedException ignored) {}
        }
        exposureControl.setExposure((long)exposureMS, TimeUnit.MILLISECONDS);
        try { Thread.sleep(20); } catch (InterruptedException ignored) {}
        GainControl gainControl = visionPortal.getCameraControl(GainControl.class);
        gainControl.setGain(gain);
    }
}
