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
    public boolean hasFinishedShooting;
    public boolean hasFinishedRetreating;
    public static ElapsedTime runtime = new ElapsedTime();
    public ElapsedTime adjustmentTime = null;

    final MotorDefinition[] DRIVE_MOTOR_DEFINITIONS = RobotUtility.DEFAULT_DRIVE_MOTOR_DEFINITIONS;

    final MotorDefinition[] SHOOT_MOTOR_DEFINITIONS = RobotUtility.DEFAULT_SHOOT_MOTOR_DEFINITIONS;

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

        hasFinishedShooting = false;
        hasFinishedRetreating = false;
        adjustmentTime = null;

        RobotUtility.Hardware.Init(hardwareMap, DRIVE_MOTOR_DEFINITIONS, SHOOT_MOTOR_DEFINITIONS, this);

        aprilTagManager.setAutoExposure();

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

            if(hasFinishedShooting == false)
            {
                telemetry.addLine("Shooting State");
                telemetry.addLine("");

                boolean reached = handleAutoDrive();

                if(reached)
                    hasFinishedShooting = true;
            }



            aprilTagManager.getDetected();

            if(hasFinishedShooting && hasFinishedRetreating == false){

                telemetry.addLine("Retreating State");
                telemetry.addLine("");

                //Runs after robot has finished Auto Shooting
                aprilTagManager.getDetected();
                aprilTagManager.tagDetectionFilter = 3; //Obelisk Only
                boolean reached = PreformRetreat();

                if(reached)
                    hasFinishedRetreating = true;
            }
            /*if(hasFinishedShooting && hasFinishedRetreating){
                if(adjustmentTime == null){
                    adjustmentTime = new ElapsedTime();
                    adjustmentTime.reset();
                }

                while (adjustmentTime != null && adjustmentTime.seconds() < 1){
                    driveController.moveRobot(0, 1, 0);
                    telemetry.addLine("Adjustment State");
                    telemetry.addLine("");

                    driveController.printHeader(telemetry);
                    driveController.printMotorPowerInfo(telemetry);
                    telemetry.update();
                }

                telemetry.addLine("Final State");
                telemetry.addLine("");

                driveController.moveRobot(0,0,0);
            }*/

            driveController.printHeader(telemetry);
            driveController.printMotorPowerInfo(telemetry);
            telemetry.update();
        }
    }

    public boolean handleAutoDrive(){
        while (runtime.seconds() <= 3 && !aprilTagManager.targetFound){
            driveController.moveRobot(-0.3, 0,0);
            aprilTagManager.getDetected();

            telemetry.addLine("Moving Away from wall");
            telemetry.update();
        }

        while (!aprilTagManager.targetFound) {
            driveController.moveRobot(0,0, 0.25f);
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
                shootingController.performShootingCycles(RobotUtility.SHOOT_CYCLES, RobotUtility.SHOOT_FIRE_MS, RobotUtility.SHOOT_REST_MS, this);
                SoundPlayer.getInstance().startPlaying(hardwareMap.appContext, audioFile);
                // we've handled firing here; return false so the main loop doesn't try to fire again
                return false;
            }
            return false;
        }
        return false;
    }
    public boolean PreformRetreat()
    {
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

            return driveController.autoDriveToAprilTag(desiredTag, telemetry,
                    1, RobotUtility.DEFAULT_DESIRED_DISTANCE * 2.1);
        }
        return false;
    }
}
