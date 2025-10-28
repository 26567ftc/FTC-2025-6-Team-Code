package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="OmniDrive_LinearOpMode_Delegated", group="Robot")
public class LinearOpModeDelegatedController extends LinearOpMode {

    public static final float DEFAULT_SPEED_COEF = 0.5f;
    public static final float SLOW_SPEED_COEF = 0.25f;
    public static final float FAST_SPEED_COEF = 1.0f;
    public static final double FEEDER_REST_ANGLE = 0;
    public static final double FEEDER_ACTIVE_ANGLE = 0.5;

    public static final double INTAKE_REST_ANGLE = 0;
    public static final double INTAKE_ACTIVE_ANGLE = 0.5;

    boolean enableShooter = true;
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
        shooterFeeder.setPosition(FEEDER_REST_ANGLE);

        intakeFeeder = hardwareMap.get(Servo.class, "intakeFeeder");
        intakeFeeder.setDirection(Servo.Direction.FORWARD);
        intakeFeeder.setPosition(INTAKE_REST_ANGLE);

        telemetry.addLine("Robot Ready.");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());

            handleDrive();
            driveController.printHeader(telemetry);
            driveController.printMotorPowerInfo(telemetry);

            shootPower = 0.75f;

            if(RobotUtility.Hardware.shootGamepad.dpadDownWasReleased())
                shootPower -= 0.05f;
            else if(RobotUtility.Hardware.shootGamepad.dpadUpWasReleased())
                shootPower += 0.05f;

            if(shootPower > 1f)
                shootPower = 1f;
            else if (shootPower < 0f)
                shootPower = 0f;

            //Shooter Enabling/Disabling
                if(RobotUtility.Hardware.shootGamepad.yWasReleased()) enableShooter = !enableShooter;
                if(!enableShooter) shootPower = 0;

                RobotUtility.Hardware.shootRightMotor.setPower(shootPower);
                RobotUtility.Hardware.shootLeftMotor.setPower(shootPower);

            //Shooter Feeder Control
                if(!RobotUtility.Hardware.shootGamepad.a) shooterFeeder.setPosition(FEEDER_ACTIVE_ANGLE);
                else shooterFeeder.setPosition(FEEDER_REST_ANGLE);

            //Intake Feeder Control
                if(!RobotUtility.Hardware.shootGamepad.x) intakeFeeder.setPosition(INTAKE_ACTIVE_ANGLE);
                else intakeFeeder.setPosition(INTAKE_REST_ANGLE);

            //Telemetry------------------------------------------------------------------------------------

            //Shooter Data
                telemetry.addLine("");
                telemetry.addLine("Shooter Info:");
                telemetry.addLine("Shoot Enabled: " + enableShooter);
                if(!enableShooter) telemetry.addLine("Shoot Disabled. Enable it by pressing: Y");
                telemetry.addLine("Shoot Power: " +  shootPower);
                telemetry.addLine("");

            //Shooter Feeder Data
                telemetry.addLine("Feeder Info:");
                telemetry.addData("Servo Position", shooterFeeder.getPosition());
                telemetry.addData("Active Servo Position", FEEDER_ACTIVE_ANGLE);
                telemetry.addData("Rest Servo Position", FEEDER_ACTIVE_ANGLE);
                telemetry.addLine("");

            //Intake Data
                telemetry.addLine("Intake Info:");
                telemetry.addData("Servo Position", intakeFeeder.getPosition());
                telemetry.addData("Active Servo Position", INTAKE_ACTIVE_ANGLE);
                telemetry.addData("Rest Servo Position", INTAKE_REST_ANGLE);
                telemetry.addLine("");

            telemetry.addLine("Fire by pressing & holding 'A'");
            
            telemetry.update();
        }
    }

    private void handleDrive() {

        float speedCoef = DEFAULT_SPEED_COEF;

        if (RobotUtility.Hardware.DriveGamepad.left_bumper)
            speedCoef = SLOW_SPEED_COEF;
        else if (RobotUtility.Hardware.DriveGamepad.right_bumper)
            speedCoef = FAST_SPEED_COEF;


        OmniDriveController.DriveInput input = new OmniDriveController.DriveInput(
                RobotUtility.Hardware.DriveGamepad.left_stick_y,
                -RobotUtility.Hardware.DriveGamepad.left_stick_x,
                -RobotUtility.Hardware.DriveGamepad.right_stick_x,
                speedCoef
        );

        driveController.driveFromInput(input);
    }
}
