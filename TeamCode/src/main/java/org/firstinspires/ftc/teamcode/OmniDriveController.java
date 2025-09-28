package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

//This is a class containing all logic for handling robot-relative Omni-Drive, decoupled from hardware and OpMode(s)
//Adapted from OmniDrive_LinearOoMode
public class OmniDriveController {
    public static class MotorDefinition{
        public String key;
        public DcMotor.Direction direction;

        public  MotorDefinition(String key, DcMotor.Direction direction){
            this.key = key;
            this.direction = direction;
        }
    }

    public static class  DriveInput{
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
    private HardwareMap hardwareMap;

    private MotorDefinition[] motors;

    private DcMotor //Motors
            leftFrontMotor, rightFrontMotor,
               //                 //
               //                //
            leftBackMotor, rightBackMotor;



    public OmniDriveController(HardwareMap hardwareMap, MotorDefinition[] motorConfigs){
        this.hardwareMap = hardwareMap;
        this.motors = motorConfigs;

        setMotors();
    }

    private void setMotors()
    {
        leftFrontMotor = setMotor(motors[0]);
        leftBackMotor = setMotor(motors[1]);
        rightFrontMotor = setMotor(motors[2]);
        rightBackMotor = setMotor(motors[3]);
    }

    private DcMotor setMotor(MotorDefinition motor){
        DcMotor hardware = hardwareMap.get(DcMotor.class, motor.key);
        hardware.setDirection(motor.direction);
        return hardware;
    }

    public void Update(DriveInput input) {
        double leftFrontPower  = input.driveInput + input.strafeInput + input.turnInput;
        double rightFrontPower = input.driveInput - input.strafeInput - input.turnInput;
        double leftBackPower   = input.driveInput - input.strafeInput + input.turnInput;
        double rightBackPower  = input.driveInput + input.strafeInput - input.turnInput;

        // Normalize the values so no wheel power exceeds 100%
        // This ensures that the robot maintains the desired motion.
        double max = Math.max(Math.abs(
                leftFrontPower), Math.abs(rightFrontPower));

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
        leftFrontMotor.setPower(leftFrontPower * input.speedCoefficient);
        rightFrontMotor.setPower(rightFrontPower * input.speedCoefficient);
        leftBackMotor.setPower(leftBackPower * input.speedCoefficient);
        rightBackMotor.setPower(rightBackPower * input.speedCoefficient);
    }
}
