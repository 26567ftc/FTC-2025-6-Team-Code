package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="OmniDrive_LinearOpMode_Delegated", group="Robot")
public class LinearOpModeDelegatedController extends LinearOpMode {

    public static final float DEFAULT_SPEED_COEF = 0.5f;
    public static final float SLOW_SPEED_COEF = 0.25f;
    public static final float FAST_SPEED_COEF = 1.0f;



    public static OmniDriveController driveController;
    public static ElapsedTime runtime = new ElapsedTime();

    final MotorDefinition[] MOTOR_DEFINITIONS = {
            new MotorDefinition("frontLeftMotor", DcMotor.Direction.REVERSE),
            new MotorDefinition("backLeftMotor", DcMotor.Direction.REVERSE),
            new MotorDefinition("frontRightMotor", DcMotor.Direction.FORWARD),
            new MotorDefinition("backRightMotor", DcMotor.Direction.FORWARD)
    };


    @Override
    public void runOpMode() {
        driveController = new OmniDriveController();
        RobotUtility.Hardware.Init(hardwareMap, MOTOR_DEFINITIONS, this);

        telemetry.addLine("Robot Ready.");
        telemetry.update();

        waitForStart();
        runtime.reset();

        while (opModeIsActive()) {
            telemetry.addData("Status", "Run Time: " + runtime.toString());

            handleDrive();
            driveController.printHeader();
            driveController.printMotorPowerInfo();

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
                RobotUtility.Hardware.DriveGamepad.right_stick_x,
                speedCoef
        );

        driveController.driveFromInput(input);
    }
}
