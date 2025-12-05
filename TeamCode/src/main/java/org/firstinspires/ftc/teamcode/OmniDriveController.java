package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;


//This is a class containing all logic for handling robot-relative Omni-Drive, decoupled from hardware and OpMode(s)
//Adapted from OmniDrive_LinearOoMode
public class OmniDriveController {
    public static class DriveInput{
        double driveInput, strafeInput, turnInput;
        float speedCoefficient;

        public DriveInput(double driveInput, double strafeInput, double turnInput,
                          float speedCoefficient){
            this.driveInput = driveInput;
            this.strafeInput = strafeInput;
            this.turnInput = turnInput;
            this.speedCoefficient = speedCoefficient;
        }
    }

    /**
     * Compute auto-drive commands to approach an AprilTag and apply them to the robot.
     * Uses constants from RobotUtility.
     */
    public boolean autoDriveToAprilTag(AprilTagDetection desiredTag, Telemetry telemetry) {
        if (desiredTag == null) return false;

        double rangeError = (desiredTag.ftcPose.range - RobotUtility.DEFAULT_DESIRED_DISTANCE);
        double headingError = desiredTag.ftcPose.bearing;
        double yawError = desiredTag.ftcPose.yaw;

        double drive = Range.clip(rangeError * RobotUtility.SPEED_GAIN, -RobotUtility.MAX_AUTO_SPEED, RobotUtility.MAX_AUTO_SPEED);
        double turn = Range.clip(headingError * RobotUtility.TURN_GAIN, -RobotUtility.MAX_AUTO_TURN, RobotUtility.MAX_AUTO_TURN);
        double strafe = Range.clip(-yawError * RobotUtility.STRAFE_GAIN, -RobotUtility.MAX_AUTO_STRAFE, RobotUtility.MAX_AUTO_STRAFE);

        moveRobot(-drive, strafe, turn);
        if (telemetry != null) telemetry.addData("Auto","Drive %5.2f, Strafe %5.2f, Turn %5.2f ", drive, strafe, turn);

        return Math.abs(drive) <= RobotUtility.DESTINATION_ERROR_BUFFER;
    }

    double leftFrontPower;
    double rightFrontPower;
    double leftBackPower;
    double rightBackPower;

    public OmniDriveController(){

    }

    public void moveRobot(double drive, double strafe, double turn, float speedMult){
        DriveInput input = new DriveInput(drive, strafe, turn, speedMult);
        moveRobot(input);
    }
    public void moveRobot(double drive, double strafe, double turn) {
        moveRobot(drive, strafe, turn, 1f);
    }
    public void moveRobot(DriveInput input) {
        leftFrontPower  = input.driveInput + input.strafeInput + input.turnInput;
        rightFrontPower = input.driveInput - input.strafeInput - input.turnInput;
        leftBackPower   = input.driveInput - input.strafeInput + input.turnInput;
        rightBackPower  = input.driveInput + input.strafeInput - input.turnInput;

        // Normalize the values so no wheel power exceeds 100%
        // This ensures that the robot maintains the desired motion.
        double max;

        max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));

        if (max > 1.0) {
            leftFrontPower  /= max;
            rightFrontPower /= max;
            leftBackPower   /= max;
            rightBackPower  /= max;
        }

        //Ensure speedCoefficient is between 0 & 1
        input.speedCoefficient = Math.min(1, input.speedCoefficient);
        input.speedCoefficient = Math.max(0, input.speedCoefficient);

        // Send calculated power to wheels
        RobotUtility.Hardware.leftFrontMotor.setPower(leftFrontPower * input.speedCoefficient);
        RobotUtility.Hardware.rightFrontMotor.setPower(rightFrontPower * input.speedCoefficient);
        RobotUtility.Hardware.leftBackMotor.setPower(leftBackPower * input.speedCoefficient);
        RobotUtility.Hardware.rightBackMotor.setPower(rightBackPower * input.speedCoefficient);
    }

    public void printHeader(Telemetry telemetry) {
        telemetry.addLine(" ");
        telemetry.addLine("===================================");
        telemetry.addLine("OmniDrive Info:");
    }

    public void printMotorPowerInfo(Telemetry telemetry) {
        telemetry.addData("Front left/Right", "%4.2f, %4.2f", leftFrontPower, rightFrontPower);
        telemetry.addData("Back  left/Right", "%4.2f, %4.2f", leftBackPower, rightBackPower);
    }
}

