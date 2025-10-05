package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.ServoController;

import org.firstinspires.ftc.robotcore.external.navigation.Quaternion;

public class RobotUtility {
    public static class Hardware{
        public static DcMotor leftFrontMotor, rightFrontMotor,
                leftBackMotor, rightBackMotor;

        public static HardwareMap hardwareMap;
        public static Gamepad DriveGamepad;
        public static Gamepad ScoreGamepad;

        static LinearOpMode mainController;

        public static void Init(HardwareMap hardwareMap, MotorDefinition[] motorDefinitions, LinearOpMode mainController) {
            Hardware.hardwareMap = hardwareMap;

            for (int i = 0; i < 4; i++)
                motorDefinitions[i].setHardware(hardwareMap);

            leftFrontMotor = motorDefinitions[0].motor;
            leftBackMotor = motorDefinitions[1].motor;
            rightFrontMotor = motorDefinitions[2].motor;
            rightBackMotor = motorDefinitions[3].motor;

            DriveGamepad = mainController.gamepad1;
            ScoreGamepad = mainController.gamepad2;

            Hardware.mainController = mainController;
        }

        public static Quaternion getOrientation() throws Exception {
            //TODO use IMU to compute robot orientation as a Quaternion.
            throw new Exception("This method is not yet implemented");
        }
    }
}
