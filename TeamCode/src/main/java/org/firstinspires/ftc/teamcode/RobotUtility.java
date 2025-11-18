package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;

public class RobotUtility {
    public static class Hardware{
        public static DcMotor leftFrontMotor, rightFrontMotor,
                leftBackMotor, rightBackMotor,
                shootLeftMotor, shootRightMotor
                ;

        public static HardwareMap hardwareMap;
        public static Gamepad DriveGamepad;
        public static Gamepad shootGamepad;
        static LinearOpMode mainController;

        public static void Init(HardwareMap hardwareMap, MotorDefinition[] driveMotorDefinitions,
                                MotorDefinition[] shootMotorDefinitions, LinearOpMode mainController) {
            Hardware.hardwareMap = hardwareMap;

            for (int i = 0; i < 4; i++)
                driveMotorDefinitions[i].setHardware(hardwareMap);

            leftFrontMotor = driveMotorDefinitions[0].motor;
            leftBackMotor = driveMotorDefinitions[1].motor;
            rightFrontMotor = driveMotorDefinitions[2].motor;
            rightBackMotor = driveMotorDefinitions[3].motor;

            shootLeftMotor = shootMotorDefinitions[0].setHardware(hardwareMap);
            shootRightMotor = shootMotorDefinitions[1].setHardware(hardwareMap);


            DriveGamepad = mainController.gamepad1;
            shootGamepad = mainController.gamepad2;

            Hardware.mainController = mainController;
        }

        public static Quaternion getOrientation() throws Exception {
            //TODO use IMU to compute robot orientation as a Quaternion.
            throw new Exception("This method is not yet implemented");
        }
    }

    // Default motor and camera constants shared across opmodes
    public static final String FRONT_LEFT_MOTOR = "frontLeftMotor";
    public static final String BACK_LEFT_MOTOR = "backLeftMotor";
    public static final String FRONT_RIGHT_MOTOR = "frontRightMotor";
    public static final String BACK_RIGHT_MOTOR = "backRightMotor";

    public static final String SHOOT_LEFT_MOTOR = "shootMotorLeft";
    public static final String SHOOT_RIGHT_MOTOR = "shootMotorRight";

    public static final String DEFAULT_WEBCAM_NAME = "Webcam 1";

    // Auto-drive / AprilTag related defaults (moved from OpMode)
    public static final double DEFAULT_DESIRED_DISTANCE = 12.0 * 4; // inches

    public static final double SPEED_GAIN  =  0.1/25;
    public static final double STRAFE_GAIN =  0.1/25;
    public static final double TURN_GAIN   =  0.1/25;

    public static final double MAX_AUTO_SPEED = 1;
    public static final double MAX_AUTO_STRAFE= 1;
    public static final double MAX_AUTO_TURN  = 0.75;

    public static final float DEFAULT_SHOOT_POWER = 0.6f;
    public static final float FAR_SHOOT_POWER = 0.7f;

    public static final float FEEDER_ACTIVE_ANGLE = 0.5f;
    public static final float FEEDER_REST_ANGLE = 0f;

    public static final int DESIRED_TAG_ID = -1;

    // Default MotorDefinition arrays for convenience
    public static final MotorDefinition[] DEFAULT_DRIVE_MOTOR_DEFINITIONS = new MotorDefinition[] {
            new MotorDefinition(FRONT_LEFT_MOTOR, DcMotor.Direction.FORWARD),
            new MotorDefinition(BACK_LEFT_MOTOR, DcMotor.Direction.FORWARD),
            new MotorDefinition(FRONT_RIGHT_MOTOR, DcMotor.Direction.REVERSE),
            new MotorDefinition(BACK_RIGHT_MOTOR, DcMotor.Direction.REVERSE)
    };

    public static final MotorDefinition[] DEFAULT_SHOOT_MOTOR_DEFINITIONS = new MotorDefinition[] {
            new MotorDefinition(SHOOT_LEFT_MOTOR, DcMotor.Direction.REVERSE),
            new MotorDefinition(SHOOT_RIGHT_MOTOR, DcMotor.Direction.FORWARD)
    };
}
