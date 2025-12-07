package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

@Autonomous
public class Auto_BLUE extends LinearOpMode{
    ShootingController shootingController;
    AprilTagManager aprilTagManager;
    public static OmniDriveController driveController;

    public boolean shooting;
    public boolean hasToggledShooter = true;

    public static ElapsedTime runtime = new ElapsedTime();
    final MotorDefinition[] DRIVE_MOTOR_DEFINITIONS = RobotUtility.DEFAULT_DRIVE_MOTOR_DEFINITIONS;

    final MotorDefinition[] SHOOT_MOTOR_DEFINITIONS = RobotUtility.DEFAULT_SHOOT_MOTOR_DEFINITIONS;

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


        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());

            boolean shootingTemp = handleAutoDrive();
            if(!shooting) shooting = shootingTemp;
            driveController.printHeader(telemetry);
            driveController.printMotorPowerInfo(telemetry);

            aprilTagManager.getDetected();

            if(shooting){
                hasToggledShooter = false;
            }

            shootingController.updateAuto(
                    true,
                    false,
                    !hasToggledShooter, true, !hasToggledShooter);
            if(!hasToggledShooter){
                wait(3000);
                hasToggledShooter = true;
            }



            telemetry.update();
        }
    }

    public boolean handleAutoDrive(){
        while (runtime.seconds() <= 1 && !aprilTagManager.targetFound){
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

            return driveController.autoDriveToAprilTag(desiredTag, telemetry);
        }
        return false;
    }
}
