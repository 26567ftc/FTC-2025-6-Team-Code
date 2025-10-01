package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="OmniDrive_LinearOpMode_Delegated", group="Robot")
public class LinearOpModeDelegatedController extends LinearOpMode {
    private  OmniDriveController driveController;
    private ElapsedTime runtime = new ElapsedTime();

    final OmniDriveController.MotorDefinition[] MOTOR_DEFINITIONS = {
            new OmniDriveController.MotorDefinition("frontLeftMotor", DcMotor.Direction.REVERSE),
            new OmniDriveController.MotorDefinition("backLeftMotor", DcMotor.Direction.REVERSE),
            new OmniDriveController.MotorDefinition("frontRightMotor", DcMotor.Direction.FORWARD),
            new OmniDriveController.MotorDefinition("backRightMotor", DcMotor.Direction.FORWARD)
    };


    @Override
    public void runOpMode() {
        driveController = new OmniDriveController(this.hardwareMap, MOTOR_DEFINITIONS);

        telemetry.addLine("Robot Ready.");
        telemetry.update();

        /* Wait for the game driver to press play */
        waitForStart();
        runtime.reset();

        /* Run until the driver presses stop */
        while (opModeIsActive()) {
            handleDrive();
        }
    }

    private void handleDrive() {
        float speedCoef = 0.5f; // default matches previous behavior which divided by 2
        if (gamepad2.left_bumper) {
            speedCoef = 0.25f; // quarter speed
        } else if (gamepad2.right_bumper) {
            speedCoef = 1.0f; // full speed (fixes empty branch in original OpMode)
        }

        OmniDriveController.DriveInput input = new OmniDriveController.DriveInput(
                gamepad2.left_stick_y,
                -gamepad2.left_stick_x,
                gamepad2.right_stick_x,
                speedCoef
        );

        driveController.Update(input);
    }
}
