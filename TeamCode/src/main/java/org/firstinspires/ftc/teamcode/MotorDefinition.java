package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MotorDefinition{
    public String key;
    public DcMotor.Direction direction;

    public DcMotor motor;

    public DcMotor setHardware(HardwareMap hardwareMap){
        DcMotor hardware = hardwareMap.get(DcMotor.class, key);
        hardware.setDirection(direction);
        motor = hardware;
        return hardware;
    }

    public MotorDefinition(String key, DcMotor.Direction direction){
        this.key = key;
        this.direction = direction;
    }
}