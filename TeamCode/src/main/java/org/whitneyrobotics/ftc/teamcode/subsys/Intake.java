package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class Intake {

    private DcMotor roller;
    private Servo arm;

    private Toggler intakeState = new Toggler(2);
    private Toggler rollerState = new Toggler(2);

    public Intake(HardwareMap map) {
        roller = map.dcMotor.get("intakeMotor");
        arm = map.servo.get("armServo");
    }

    public void operate(boolean armState, boolean roll, boolean reverse) {
        intakeState.changeState(armState);
        rollerState.changeState(roll);

        if (intakeState.currentState() == 0) {
            arm.setPosition(0);
        } else {
            arm.setPosition(0.5);
        }

        if (rollerState.currentState() == 0) {
            roller.setPower(0);
        } else {
            roller.setPower(1 * (reverse ? -1 : 1));
        }
    }
}
