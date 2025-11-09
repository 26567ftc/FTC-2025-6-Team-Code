package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous
public class LinearOpMode_AUTO_CLOSE extends LinearOpMode {

    public static final double FEEDER_REST_ANGLE = 0;
    public static final double FEEDER_ACTIVE_ANGLE = 0.5;

    public static final float FAR_SHOOT_POWER = 0.65f;


    boolean enableShooter = false;
    float shootPower = 0;
    Servo shooterFeeder;
    Servo intakeFeeder;

    public static OmniDriveController driveController;
    public static ElapsedTime runtime = new ElapsedTime();

    final MotorDefinition[] DRIVE_MOTOR_DEFINITIONS = {
            new MotorDefinition("frontLeftMotor", DcMotor.Direction.FORWARD),
            new MotorDefinition("backLeftMotor", DcMotor.Direction.FORWARD),
            new MotorDefinition("frontRightMotor", DcMotor.Direction.REVERSE),
            new MotorDefinition("backRightMotor", DcMotor.Direction.REVERSE)
    };

    final MotorDefinition[] SHOOT_MOTOR_DEFINITIONS = {
            new MotorDefinition("shootMotorLeft", DcMotor.Direction.REVERSE),
            new MotorDefinition("shootMotorRight", DcMotor.Direction.FORWARD),
    };


    @Override
    public void runOpMode() {
        driveController = new OmniDriveController();
        RobotUtility.Hardware.Init(hardwareMap, DRIVE_MOTOR_DEFINITIONS, SHOOT_MOTOR_DEFINITIONS, this);

        shooterFeeder = hardwareMap.get(Servo.class, "shootFeeder");
        shooterFeeder.setDirection(Servo.Direction.FORWARD);
        shooterFeeder.setPosition(FEEDER_ACTIVE_ANGLE);

        telemetry.addLine("Robot Ready.");
        telemetry.update();

        waitForStart();
        runtime.reset();


        OmniDriveController.DriveInput input = new OmniDriveController.DriveInput(
                1,
                0,
                0,
                0.2f

        );

        driveController.driveFromInput(input);
        while(opModeIsActive() && runtime.seconds() <= 4.75){
            telemetry.addLine("Drive Backward");
            telemetry.addData("seconds remaining", runtime.seconds());
            telemetry.update();
        }
        input = new OmniDriveController.DriveInput(
                0,
                0,
                0,
                0f
        );
        driveController.driveFromInput(input);

        runtime.reset();

        input = new OmniDriveController.DriveInput(
                0,
                0,
                0,
                0f
        );
        driveController.driveFromInput(input);

        runtime.reset();

        shootPower = FAR_SHOOT_POWER;
        RobotUtility.Hardware.shootRightMotor.setPower(shootPower);
        RobotUtility.Hardware.shootLeftMotor.setPower(shootPower);

        while(opModeIsActive() && runtime.seconds() <= 1.5){
            telemetry.addData("Waiting for shooter", runtime.seconds());
            telemetry.update();
        }

        shooterFeeder.setPosition(FEEDER_REST_ANGLE);

        runtime.reset();

        while(opModeIsActive() && runtime.seconds() <= 0.5f){
            telemetry.addData("Waiting to stop", runtime.seconds());
        }

        RobotUtility.Hardware.shootRightMotor.setPower(0);
        RobotUtility.Hardware.shootLeftMotor.setPower(0);
        shooterFeeder.setPosition(FEEDER_ACTIVE_ANGLE);

        runtime.reset();

        while(opModeIsActive()){
            telemetry.addLine("Finished!");
            telemetry.addData("Runtime:", runtime.seconds());
        }
    }
}
