package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="MotorTest_LinearOpMode", group="Robot")
public class MotorTest extends LinearOpMode {

    private DcMotor leftFrontMotor = null;
    private DcMotor leftBackMotor = null;
    private DcMotor rightFrontMotor = null;
    private DcMotor rightBackMotor = null;

    @Override
    public void runOpMode() throws InterruptedException {

        initMotorHardware();
        setMotorDirections();

        telemetry.addLine("Robot Ready.");
        telemetry.update();

        /* Wait for the game driver to press play */
        waitForStart();

        /* Run until the driver presses stop */
        while (opModeIsActive()) {
            double power = gamepad1.left_stick_y;

            if(gamepad1.left_bumper)
                leftFrontMotor.setPower(power);
            if(gamepad1.right_bumper)
                rightFrontMotor.setPower(power);
            if(gamepad1.left_trigger > 0.5)
                leftFrontMotor.setPower(power);
            if(gamepad1.right_trigger > 0.5)
                leftFrontMotor.setPower(power);
        }
    }

    private void setMotorDirections() {
        leftFrontMotor.setDirection(
                DcMotor.Direction.REVERSE);
        leftBackMotor.setDirection(DcMotor.Direction.REVERSE);
        rightFrontMotor.setDirection(DcMotor.Direction.FORWARD);
        rightBackMotor.setDirection(DcMotor.Direction.FORWARD);
    }

    private void initMotorHardware() {
        // Initialize the hardware variables. Note that the strings used here must correspond
        leftFrontMotor = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        leftBackMotor = hardwareMap.get(DcMotor.class, "backLeftMotor");
        rightFrontMotor = hardwareMap.get(DcMotor.class, "frontRightMotor");
        rightBackMotor = hardwareMap.get(DcMotor.class, "backRightMotor");
    }
}
