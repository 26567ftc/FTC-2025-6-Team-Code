package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp(name="MotorTest_LinearOpMode", group="Robot")
public class MotorTest extends LinearOpMode {

    private DcMotor shootMotorLeft = null;
    private DcMotor shootMotorRight = null;

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
                shootMotorLeft.setPower(gamepad1.right_trigger);
                shootMotorRight.setPower(gamepad1.right_trigger);
        }
    }

    private void setMotorDirections() {
        shootMotorLeft.setDirection(
                DcMotor.Direction.REVERSE);
        shootMotorRight.setDirection(DcMotor.Direction.FORWARD);
    }

    private void initMotorHardware() {
        // Initialize the hardware variables. Note that the strings used here must correspond
        shootMotorLeft = hardwareMap.get(DcMotor.class, "shootMotorLeft");
        shootMotorRight = hardwareMap.get(DcMotor.class, "shootMotorRight");
    }
}
