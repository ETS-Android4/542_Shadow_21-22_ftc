package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.SimpleTimer;
import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class Intake {

    private DcMotor surgicalTubes;
    private DcMotor arm;

    // 5281.1 ticks per revolution

    private Servo eject;
    public SimpleTimer armTimer = new SimpleTimer();
    public double armTimerDelay = 1; //change based on testing
    int state = 0;
    public boolean intakeAutoDone = false;

    public int[] armPositions = {500, 0}; //change numbers later
    public enum ArmPositions {
        DOWN, UP
    }

    public double[] pusherPositions = {1, 0.6, 0.5};
    public enum PusherPositions {
        IN, OUT, FULLY_OUT
    }

    private Toggler intakeStateTog = new Toggler(2);
    private Toggler armPositionTog = new Toggler(2);
    private Toggler ejectStateTog = new Toggler(2);

    private Toggler armPositionTestingTogTuning = new Toggler(52);
    private Toggler armPositionTestingTogFineTune = new Toggler(70);

    public Intake(HardwareMap map) {
        surgicalTubes = map.dcMotor.get("intakeMotor");
        arm = map.dcMotor.get("armMotor");
        eject = map.servo.get("ejectServo");
        arm.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    /*public void operate(boolean armState, boolean roll, boolean reverse, boolean reject) {
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
    }*/

    public void resetAllEncoder(){
        surgicalTubes.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        arm.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        surgicalTubes.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void operate(boolean armState, boolean togOnOff, boolean reverse, boolean deposit){
        if(deposit){
            eject.setPosition(pusherPositions[PusherPositions.FULLY_OUT.ordinal()]);
        } else {
            eject.setPosition(pusherPositions[PusherPositions.IN.ordinal()]);
        }
        armPositionTog.changeState(armState);
        intakeStateTog.changeState(togOnOff);
        if (reverse) {
            surgicalTubes.setPower(-1);
        } else if (intakeStateTog.currentState() == 0) {
            if(armPositionTog.currentState() != 0){
                surgicalTubes.setPower(1);
                //eject.setPosition(pusherPositions[PusherPositions.OUT.ordinal()]);
            } else {
                surgicalTubes.setPower(0);
            }
        } else if (deposit && armPositionTog.currentState() != 0) {
            surgicalTubes.setPower(1);
        } else {
            surgicalTubes.setPower(0);
        }

        if(armPositionTog.currentState() == 0){
            arm.setTargetPosition(armPositions[ArmPositions.UP.ordinal()]);
        } else {
            arm.setTargetPosition(armPositions[ArmPositions.DOWN.ordinal()]);
        }
    }

    public void setIntakePower(double power) { surgicalTubes.setPower(power);}

    public void armTest(boolean hundreds, boolean ones) {
        resetAllEncoder();
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        armPositionTestingTogTuning.changeState(hundreds);
        armPositionTestingTogFineTune.changeState(ones);
        arm.setTargetPosition(armPositionTestingTogFineTune.currentState() + (armPositionTestingTogTuning.currentState()*100));
        arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    }

    public void autoDropIntake(){
        switch(state){
            case 0:
                intakeAutoDone = false;
                armTimer.set(armTimerDelay);
                state++;
                break;
            case 1:
                if(armTimer.isExpired()){
                    armPositionTog.changeState(true);
                    surgicalTubes.setPower(0);
                    resetAllEncoder();
                    arm.setTargetPosition(0);
                    arm.setMode(DcMotor.RunMode.RUN_TO_POSITION);
                    intakeAutoDone = true;
                    state = 0;
                }
                break;
        }

    }
}
