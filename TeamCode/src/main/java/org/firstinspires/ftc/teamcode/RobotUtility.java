package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;

public class RobotUtility {
    public static class Hardware{
        public static DcMotor leftFrontMotor, rightFrontMotor,
                leftBackMotor, rightBackMotor, shootMotor;

        public static HardwareMap hardwareMap;
        public static Gamepad DriveGamepad;
        public static Gamepad shootGamepad;
        static LinearOpMode mainController;

        public static void Init(HardwareMap hardwareMap, MotorDefinition[] driveMotorDefinitions,
                                MotorDefinition shootMotorDefinition, LinearOpMode mainController) {
            Hardware.hardwareMap = hardwareMap;

            for (int i = 0; i < 4; i++)
                driveMotorDefinitions[i].setHardware(hardwareMap);

            leftFrontMotor = driveMotorDefinitions[0].motor;
            leftBackMotor = driveMotorDefinitions[1].motor;
            rightFrontMotor = driveMotorDefinitions[2].motor;
            rightBackMotor = driveMotorDefinitions[3].motor;

            shootMotor = shootMotorDefinition.setHardware(hardwareMap);


            DriveGamepad = mainController.gamepad1;
            shootGamepad = mainController.gamepad2;

            Hardware.mainController = mainController;
        }

        public static Quaternion getOrientation() throws Exception {
            //TODO use internal IMU to compute robot orientation as a Quaternion.
            throw new Exception("This method is not yet implemented");
        }
    }

    // Default motor and camera constants shared across opmodes
    public static final String FRONT_LEFT_MOTOR = "frontLeftMotor";
    public static final String BACK_LEFT_MOTOR = "backLeftMotor";
    public static final String FRONT_RIGHT_MOTOR = "frontRightMotor";
    public static final String BACK_RIGHT_MOTOR = "backRightMotor";
    public static final String SHOOT_RIGHT_MOTOR = "shootMotor";

    public static final String DEFAULT_WEBCAM_NAME = "Webcam 1";

    // Auto-drive / AprilTag related defaults (moved from OpMode)
    public static final double DEFAULT_DESIRED_DISTANCE = 12.0 * 4; // inches

    public static final double SPEED_GAIN  =  0.07/25;
    public static final double STRAFE_GAIN =  0.07/25;
    public static final double TURN_GAIN   =  0.07/25;

    public static final double MAX_AUTO_SPEED = 1;
    public static final double MAX_AUTO_STRAFE= 1;
    public static final double MAX_AUTO_TURN  = 1;

    public static final float DEFAULT_SHOOT_POWER = 0.6f;
    public static final float FAR_SHOOT_POWER = 0.7f;

    public final static int BLUE_GOAL_TAG_ID = 23;
    public final static int RED_GOAL_TAG_ID = 24;

    public static int[] GLOBAL_DESIRED_TAG_IDS = new int[] {
            BLUE_GOAL_TAG_ID, RED_GOAL_TAG_ID
    };

    /// 0 = Use GLOBAL_DESIRED_TAG_IDS
    /// 1 = RED Only
    /// 2 = BLUE Only
    public static int tagDetectionFilter = 0;

    public static boolean FilterTagID(int tagID){
        if(tagDetectionFilter == 1){
            return tagID == RED_GOAL_TAG_ID;
        }
        else if(tagDetectionFilter == 2){
            return  tagID == BLUE_GOAL_TAG_ID;
        }
        else return true;
    }

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

    public static final MotorDefinition SHOOT_MOTOR_DEFINITION = new MotorDefinition(SHOOT_RIGHT_MOTOR, DcMotor.Direction.FORWARD);

}
