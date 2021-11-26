package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whitneyrobotics.ftc.teamcode.lib.util.SimpleTimer;
import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class Intake {

    private DcMotorEx intakeMotor;

    private int autoIntakeState = 0;
    private final double INTAKE_MOTOR_POWER = 0.65;
    public boolean autoIntakeInProgress = false;
    public boolean isReversed;

    private SimpleTimer autoIntakeTimer = new SimpleTimer();
    private Toggler intakePowerState;

    public void resetAllEncoder(){
        intakeMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        intakeMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public Intake (HardwareMap hardwareMap){
        intakeMotor = hardwareMap.get(DcMotorEx.class, "SurgicalTubes");
        intakePowerState = new Toggler(2);

        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }

    // TeleOp
    public void operate(boolean togglePower, boolean reverse){
        intakePowerState.changeState(togglePower);
        isReversed = (reverse) ? true : false;
        if(reverse){
            intakeMotor.setPower(-INTAKE_MOTOR_POWER);
        } else if(intakePowerState.currentState() == 1){
            intakeMotor.setPower(INTAKE_MOTOR_POWER);
        } else {
            intakeMotor.setPower(0);
        }
    }

    // AutoOp
    public void autoOperate(int durationInMS, boolean reverse){

        // If reversed, set to -1 to reverse speed later
        int direction = reverse ? -1 : 1;
        isReversed = reverse ? true : false;

        // switch case for AutoOP
        switch (autoIntakeState) {

            // Beginning
            case 0:
                // Intake has begun, set power, and start timer
                autoIntakeInProgress = true;
                intakeMotor.setPower(direction * INTAKE_MOTOR_POWER);
                autoIntakeTimer.set(durationInMS);
                autoIntakeState++;
                break;
            case 1:
                // if timer has 'rung'
                if (autoIntakeTimer.isExpired()) {
                    // set motor to 0, and reset everything
                    intakeMotor.setPower(0);
                    resetAllEncoder();
                    autoIntakeInProgress = false;
                    autoIntakeState = 0;
                }
                break;

        }
    }

    // Useful for Telemetry
    public boolean isForwards(){
        return (intakePowerState.currentState() == 1 && !isReversed);
    }

    public boolean isReversed(){return isReversed;}

    public double getVelocity(){return (double) intakeMotor.getVelocity();}

}

