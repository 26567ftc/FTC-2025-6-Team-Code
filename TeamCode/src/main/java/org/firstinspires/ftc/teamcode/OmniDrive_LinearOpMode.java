package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;


@TeleOp(name="OmniDrive_LinearOpMode", group="Robot")
public class OmniDrive_LinearOpMode extends LinearOpMode {
    private ElapsedTime runtime = new ElapsedTime();

    private DcMotor leftFrontMotor = null;
    private DcMotor leftBackMotor = null;
    private DcMotor rightFrontMotor = null;
    private DcMotor rightBackMotor = null;


    @Override
    public void runOpMode() {

        initMotorHardware(); //Fetches the motor hardware by name, and sets their digital counterparts.

        setMotorDirections(); //Sets the digital motor directions, make sure these match real-world motor directions!!


        /* Send telemetry message to signify robot waiting */
        telemetry.addLine("Robot Ready.");
        telemetry.update();

        /* Wait for the game driver to press play */
        waitForStart();
        runtime.reset();

        /* Run until the driver presses stop */
        while (opModeIsActive()) {

            double max;

            // POV Mode uses left joystick to go forward & strafe, and right joystick to rotate.
            double leftStickY =  gamepad2.left_stick_y;  // Note: pushing stick forward gives negative value
            double leftStickX = -gamepad2.left_stick_x;

            double rightStickX     =  gamepad2.right_stick_x;


            // Combine the joystick requests for each axis-motion to determine each wheel's power.
            // Set up a variable for each drive wheel to save the power level for telemetry.
            double leftFrontPower  = leftStickY + leftStickX + rightStickX;
            double rightFrontPower = leftStickY - leftStickX - rightStickX;
            double leftBackPower   = leftStickY - leftStickX + rightStickX;
            double rightBackPower  = leftStickY + leftStickX - rightStickX;

            // Normalize the values so no wheel power exceeds 100%
            // This ensures that the robot maintains the desired motion.
            max = Math.max(Math.abs(
                    leftFrontPower), Math.abs(rightFrontPower));

            max = Math.max(max, Math.abs(leftBackPower));
            max = Math.max(max, Math.abs(rightBackPower));

            //TODO Is there a way of refactoring this to avoid the reassignment of local vars? We should try to stay Functional...
            if (max > 1.0) {
                leftFrontPower  /= max;
                rightFrontPower /= max;
                leftBackPower   /= max;
                rightBackPower  /= max;
            }

            // Send calculated power to wheels

            if (gamepad2.left_bumper){
                leftFrontMotor.setPower(
                        leftFrontPower/4);
                rightFrontMotor.setPower(rightFrontPower/4);
                leftBackMotor.setPower(leftBackPower/4);
                rightBackMotor.setPower(rightBackPower/4);
            }
            else if(gamepad2.right_bumper){

            }
            else {
                leftFrontMotor.setPower(leftFrontPower / 2);
                rightFrontMotor.setPower(rightFrontPower / 2);
                leftBackMotor.setPower(leftBackPower / 2);
                rightBackMotor.setPower(rightBackPower / 2);
            }




            telemetry.addData("Status", "Run Time: " + runtime.toString());
            // Show the elapsed game time and wheel power.
            telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
            telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
            telemetry.update();
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
/*
 * This OpMode is an example driver-controlled (TeleOp) mode for the goBILDA 2024-2025 FTC
 * Into The Deep Starter Robot
 * The code is structured as a LinearOpMode
 *
 * This robot has a two-motor differential-steered (sometimes called tank or skid steer) drivetrain.
 * With a left and right drive motor.
 * The drive on this robot is controlled in an "Arcade" style, with the left stick Y axis
 * controlling the forward movement and the right stick X axis controlling rotation.
 * This allows easy transition to a standard "First Person" control of a
 * mecanum or omnidirectional chassis.
 *
 * The drive wheels are 96mm diameter traction (Rhino) or omni wheels.
 * They are driven by 2x 5203-2402-0019 312RPM Yellow Jacket Planetary Gearmotors.
 *
 * This robot's main scoring mechanism includes an arm powered by a motor, a "wrist" driven
 * by a servo, and an intake driven by a continuous rotation servo.
 *
 * The arm is powered by a 5203-2402-0051 (50.9:1 Yellow Jacket Planetary Gearmotor) with an
 * external 5:1 reduction. This creates a total ~254.47:1 reduction.
 * This OpMode uses the motor's encoder and the RunToPosition method to drive the arm to
 * specific setpoints. These are defined as a number of degrees of rotation away from the arm's
 * starting position.
 *
 * Make super sure that the arm is reset into the robot, and the wrist is folded in before
 * you run start the OpMode. The motor's encoder is "relative" and will move the number of degrees
 * you request it to based on the starting position. So if it starts too high, all the motor
 * setpoints will be wrong.
 *
 * The wrist is powered by a goBILDA Torque Servo (2000-0025-0002).
 *
 * The intake wheels are powered by a goBILDA Speed Servo (2000-0025-0003) in Continuous Rotation mode.
 */