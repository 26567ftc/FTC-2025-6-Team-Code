package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.ftccommon.SoundPlayer;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.io.File;

@Autonomous
public class Auto_BLUE extends LinearOpMode{
    ShootingController shootingController;
    AprilTagManager aprilTagManager;
    public static OmniDriveController driveController;
    public boolean hasShot;
    public static ElapsedTime runtime = new ElapsedTime();

    final MotorDefinition[] DRIVE_MOTOR_DEFINITIONS = RobotUtility.DEFAULT_DRIVE_MOTOR_DEFINITIONS;

    final MotorDefinition[] SHOOT_MOTOR_DEFINITIONS = RobotUtility.DEFAULT_SHOOT_MOTOR_DEFINITIONS;

    // Multi-shot cycle configuration
    private static final int SHOOT_CYCLES = 3;
    private static final long SHOOT_FIRE_MS = 400; // ms to hold feeder in fire position
    private static final long SHOOT_REST_MS = 600; // ms to wait between shots

    private String soundPath = "/FIRST/blocks/sounds";
    private File audioFile   = new File("/sdcard" + soundPath + "/audio.wav");

    @Override
    public void runOpMode() throws InterruptedException {

        driveController = new OmniDriveController();
        RobotUtility.Hardware.Init(hardwareMap, DRIVE_MOTOR_DEFINITIONS, SHOOT_MOTOR_DEFINITIONS, this);

        // initialize shooting controller (handles servos, motor power and telemetry)
        shootingController = new ShootingController(hardwareMap);

        //Initialize April Tag Detection
        aprilTagManager = new AprilTagManager(hardwareMap, true, RobotUtility.DEFAULT_WEBCAM_NAME, 2);
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


        shootingController.update(
                true,
                false,
                true, false, true);



        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());

            boolean shootingTemp = handleAutoDrive();
            driveController.printHeader(telemetry);
            driveController.printMotorPowerInfo(telemetry);

            aprilTagManager.getDetected();
            telemetry.update();
        }
    }

    public boolean handleAutoDrive(){
        while (runtime.seconds() <= 3 && !aprilTagManager.targetFound){
            driveController.moveRobot(-0.2, 0,0);
            aprilTagManager.getDetected();

            telemetry.addLine("Moving Away from wall");
            telemetry.update();
        }

        while (!aprilTagManager.targetFound) {
            driveController.moveRobot(0,0, 0.5f);
            aprilTagManager.getDetected();

            telemetry.addLine("Scanning for april tag");
            telemetry.update();
        }

        aprilTagManager.getDetected();

        if(aprilTagManager.targetFound) {
            AprilTagDetection desiredTag = aprilTagManager.desiredTag;

            telemetry.addData("Found", "ID %d (%s)", desiredTag.id, desiredTag.metadata.name);
            telemetry.addData("Range",  "%5.1f inches", desiredTag.ftcPose.range);
            telemetry.addData("Bearing","%3.0f degrees", desiredTag.ftcPose.bearing);
            telemetry.addData("Yaw","%3.0f degrees", desiredTag.ftcPose.yaw);

            boolean reached = driveController.autoDriveToAprilTag(desiredTag, telemetry);
            if (reached) {
                // Perform multiple shooting cycles once we've arrived
                performShootingCycles(SHOOT_CYCLES, SHOOT_FIRE_MS, SHOOT_REST_MS);
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, audioFile);
                // we've handled firing here; return false so the main loop doesn't try to fire again
                return false;
            }
            return reached;
        }
        return false;
    }

    // Performs the requested number of fire/rest cycles using the ShootingController
    public void performShootingCycles(int cycles, long fireMs, long restMs) {
        for (int i = 0; i < cycles && opModeIsActive(); i++) {
            // Fire (feeder active = true)
            shootingController.updateAuto(true, false, true, true, true);
            sleep(fireMs);

            // Stop firing (feeder inactive)
            shootingController.updateAuto(true, false, true, false, true);
            sleep(restMs);
        }
    }
}
