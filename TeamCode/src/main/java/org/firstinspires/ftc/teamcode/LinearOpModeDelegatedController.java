package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="OmniDrive_LinearOpMode_Delegated", group="Robot")
public class LinearOpModeDelegatedController extends LinearOpMode {
    private  OmniDriveController driveController;

    final OmniDriveController.MotorDefinition[] MOTOR_DEFINITIONS = {
            new OmniDriveController.MotorDefinition("leftFrontMotor", DcMotor.Direction.FORWARD),
            new OmniDriveController.MotorDefinition("leftBackMotor", DcMotor.Direction.FORWARD),
            new OmniDriveController.MotorDefinition("rightFrontMotor", DcMotor.Direction.REVERSE),
            new OmniDriveController.MotorDefinition("rightBackMotor", DcMotor.Direction.REVERSE)
    };


    @Override
    public void runOpMode() {
        driveController = new OmniDriveController(this.hardwareMap, MOTOR_DEFINITIONS);

        /* Run until the driver presses stop */
        while (opModeIsActive()) {
            handleDrive();
        }
    }

    private void handleDrive() {
        OmniDriveController.DriveInput input = new OmniDriveController.DriveInput(
                gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x,
                gamepad1.right_bumper?
                1f :
                (
                    gamepad1.left_bumper?
                            0.25f
                            : 0.5f
                )
        );

        driveController.Update(input);
    }
}
