package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class Outtake {
    private Servo gate;
    private DcMotor linearSlides;

    public Outtake(HardwareMap outtakeMap) {
        gate = outtakeMap.servo.get("gateServo");
        linearSlides = outtakeMap.get(DcMotorEx.class, "linearSlides");
    }
    private double level1 = 1;
    private double level2 = 2;
    private double level3 = 3;
    private Toggler servoGateTog = new Toggler(2);
    private Toggler linearSlidesTog = new Toggler (3);
}
