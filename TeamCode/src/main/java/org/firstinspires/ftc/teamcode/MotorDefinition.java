package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class MotorDefinition{
    public String key;
    public DcMotorEx.Direction direction;
    public boolean setMode;
    public DcMotorEx motor;

    public DcMotorEx setHardware(HardwareMap hardwareMap){
        DcMotorEx hardware = hardwareMap.get(DcMotorEx.class, key);
        hardware.setDirection(direction);

        if(setMode)
            hardware.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        motor = hardware;
        return hardware;
    }

    public MotorDefinition(String key, DcMotorEx.Direction direction, boolean setMode){
        this.key = key;
        this.direction = direction;
        this.setMode = setMode;
    }
}