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

    public static ElapsedTime runtime = new ElapsedTime();
    final MotorDefinition[] DRIVE_MOTOR_DEFINITIONS = RobotUtility.DEFAULT_DRIVE_MOTOR_DEFINITIONS;

    final MotorDefinition SHOOT_MOTOR_DEFINITION = RobotUtility.SHOOT_MOTOR_DEFINITION;

    @Override
    public void runOpMode() throws InterruptedException {
        RobotUtility.tagDetectionFilter = 2;

        driveController = new OmniDriveController();
        RobotUtility.Hardware.Init(hardwareMap, DRIVE_MOTOR_DEFINITIONS, SHOOT_MOTOR_DEFINITION, this);

        // initialize shooting controller (handles servos, motor power and telemetry)
        shootingController = new ShootingController(hardwareMap);

        //Initialize April Tag Detection
        aprilTagManager = new AprilTagManager(hardwareMap, true, RobotUtility.DEFAULT_WEBCAM_NAME);
        aprilTagManager.init();

        telemetry.addLine("Robot Ready.");
        telemetry.update();

        waitForStart();
        runtime.reset();


        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());

            driveController.printHeader(telemetry);
            //driveController.printMotorPowerInfo(telemetry); //Perhaps with out motors attached this is causing issues?

            handleAutoDrive();


            boolean foundTAG = aprilTagManager.getDetected();
                shootingController.updateAuto(
                        true,
                        false,
                        foundTAG, foundTAG);

            telemetry.update();
        }
    }

    public void handleAutoDrive(){
        /*while (runtime.seconds() <= 1 && !aprilTagManager.getDetected()){
            driveController.moveRobot(0.2, 0,0);
            telemetry.addLine("Driving forward to look for April Tag");
            telemetry.update();

        }*/

        while (!aprilTagManager.targetFound) {
            driveController.moveRobot(0,0, 0.5f);
            aprilTagManager.getDetected();

            telemetry.addLine("Scanning for April Tag");
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.update();
        }

        aprilTagManager.getDetected();

        AprilTagDetection desiredTag = aprilTagManager.desiredTag;



        driveController.printHeader(telemetry);
        driveController.printMotorPowerInfo(telemetry);


        telemetry.addData("Found", "ID %d (%s)", desiredTag.id, desiredTag.metadata.name);
        telemetry.addData("Range",  "%5.1f inches", desiredTag.ftcPose.range);
        telemetry.addData("Bearing","%3.0f degrees", desiredTag.ftcPose.bearing);
        telemetry.addData("Yaw","%3.0f degrees", desiredTag.ftcPose.yaw);

        driveController.autoDriveToAprilTag(desiredTag, telemetry);

        telemetry.update();
    }
}
