package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.SimpleTimer;
import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class Intake {

    private DcMotor roller;
    private Servo arm;
    private Servo pusher;
    public SimpleTimer armTimer = new SimpleTimer();
    public double armTimerDelay = 1; //change based on testing
    int state = 0;
    public boolean intakeAutoDone = false;

    public double[] armPositions = {0, 0.5}; //change numbers later
    public enum ArmPositions {
        DOWN, UP
    }

    public double[] pusherPositions = {0.2, 0.8, 1};
    public enum PusherPositions {
        IN, OUT, FULLY_OUT
    }

    private Toggler intakeState = new Toggler(2);
    private Toggler rollerState = new Toggler(2);
    private Toggler ejectState = new Toggler(2);

    public Intake(HardwareMap map) {
        roller = map.dcMotor.get("intakeMotor");
        arm = map.servo.get("armServo");
        pusher = map.servo.get("pusherServo");
    }

    public void operate(boolean armState, boolean roll, boolean reverse, boolean reject) {
        intakeState.changeState(armState);
        rollerState.changeState(roll);

        if (reject){
            roller.setPower(-1);
            pusher.setPosition(pusherPositions[PusherPositions.OUT.ordinal()]);
        }
        // if not reject
        else {
            if (intakeState.currentState() == 0) {
                arm.setPosition(armPositions[ArmPositions.DOWN.ordinal()]);
            } else {
                arm.setPosition(armPositions[ArmPositions.UP.ordinal()]);
                if (arm.getPosition() > 0.3) {
                    ejectState.changeState(reject);
                } else if (ejectState.currentState() != 0){
                    ejectState.changeState(false);
                    ejectState.changeState(true);
                }
            }

            if (rollerState.currentState() == 0) {
                if(ejectState.currentState() == 1){
                    pusher.setPosition(armPositions[PusherPositions.FULLY_OUT.ordinal()]);
                    roller.setPower(-1);
                } else {
                    roller.setPower(0);
                    pusher.setPosition(armPositions[PusherPositions.IN.ordinal()]);
                }
            } else {
                pusher.setPosition(armPositions[PusherPositions.IN.ordinal()]);
                roller.setPower(1 * (reverse ? -1 : 1));
            }
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
