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
}
