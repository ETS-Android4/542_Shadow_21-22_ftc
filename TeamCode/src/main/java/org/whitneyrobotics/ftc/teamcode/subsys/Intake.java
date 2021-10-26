package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.SimpleTimer;
import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class Intake {

    private DcMotor roller;
    private Servo arm;
    public SimpleTimer armTimer = new SimpleTimer();
    public double armTimerDelay = 1; //change based on testing
    int state = 0;
    public boolean intakeAutoDone = false;

    public double[] armPositions = {0, 0.5}; //change numbers later

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
    //testing
    public void setIntakePower(double power) { roller.setPower(power);}

    public void setArm(double position) { arm.setPosition(position); }

    public void autoDropIntake(){
        switch(state){
            case 0:
                roller.setPower(1);
                intakeAutoDone = false;
                armTimer.set(armTimerDelay);
                state++;
                break;
            case 1:
                if(armTimer.isExpired()){
                    setArm(0);
                    roller.setPower(0);
                    intakeAutoDone = true;
                    state = 0;
                }else{
                    setArm(0.5);
                }
                break;
        }

    }
}
