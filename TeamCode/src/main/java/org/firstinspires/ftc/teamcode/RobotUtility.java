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
    public static final double DEFAULT_DESIRED_DISTANCE = 12.0 * 6.6; //in inches (value is 4 ft)

    public static final double SPEED_GAIN  =  0.6 / 25; // Ramp up to 50% power at a 25 inch error.   (0.50 / 25.0)
    public static final double STRAFE_GAIN =  0.475 / 25.0;
    public static final double TURN_GAIN   =  0.35 / 25.0;
    public static final double DESTINATION_ERROR_BUFFER = 2; //Auto drive is consider complete when distance/drive error >= to this

    public static final double MAX_AUTO_SPEED = 1;
    public static final double MAX_AUTO_STRAFE= 1;
    public static final double MAX_AUTO_TURN  = 1;

    public static final float DEFAULT_SHOOT_POWER = 0.58f;
    public static final float FAR_SHOOT_POWER = 0.68f;

    public static final float FEEDER_ACTIVE_ANGLE = 0.415f;
    public static final float FEEDER_REST_ANGLE = 0f;

    //Auto Multi-Shoot Configuration
    public static final int SHOOT_CYCLES = 3;
    public static final long SHOOT_FIRE_MS = 400; // ms to hold feeder in fire position
    public static final long SHOOT_REST_MS = 3000; // ms to wait between shots

    public final static int BLUE_GOAL_TAG_ID = 20;
    public final static int RED_GOAL_TAG_ID = 24;

    public final static int OBELISK_1_TAG_ID = 21;
    public final static int OBELISK_2_TAG_ID = 22;
    public final static int OBELISK_3_TAG_ID = 23;

    public static final float DEFAULT_SPEED_COEF = 0.5f;
    public static final float SLOW_SPEED_COEF = 0.25f;
    public static final float FAST_SPEED_COEF = 1.0f;

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
