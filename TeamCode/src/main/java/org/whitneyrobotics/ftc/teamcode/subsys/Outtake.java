package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.SimpleTimer;
import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class Outtake {
    private Servo gate;
    private DcMotor linearSlides;

    public Outtake(HardwareMap outtakeMap) {
        gate = outtakeMap.servo.get("gateServo");
        linearSlides = outtakeMap.get(DcMotorEx.class, "linearSlides");
    }
    private int level1 = 1;
    private int level2 = 2;
    private int level3 = 3;
    private Toggler servoGateTog = new Toggler(2);
    private Toggler linearSlidesTog = new Toggler (3);
    private SimpleTimer outtakeTimer = new SimpleTimer();

    public boolean slidingInProgress = false;
    private boolean outtakeTimerSet = true;

    //toggler based teleop
    public void togglerOuttake(boolean up,boolean down){
        linearSlidesTog.changeState(up,down);
        if (linearSlidesTog.currentState() == 0) {
            if(linearSlides.getCurrentPosition()>level1){
                linearSlides.setPower(-1);
            } else if (linearSlides.getCurrentPosition()<level1){
                linearSlides.setPower(1);
            } else {
                linearSlides.setPower(0);
            }
            slidingInProgress = true;
            if (linearSlides.getCurrentPosition() == level1) {
                slidingInProgress = false;
            }
        } else if (linearSlidesTog.currentState() == 1) {
            if(linearSlides.getCurrentPosition()>level2){
                linearSlides.setPower(-1);
            } else if (linearSlides.getCurrentPosition()<level2){
                linearSlides.setPower(1);
            } else {
                linearSlides.setPower(0);
            }
            slidingInProgress = true;
            if (linearSlides.getCurrentPosition() == level2) {
                slidingInProgress = false;
            }
        } else if (linearSlidesTog.currentState() == 2) {
            if(linearSlides.getCurrentPosition()>level3){
                linearSlides.setPower(-1);
            } else if (linearSlides.getCurrentPosition()<level3){
                linearSlides.setPower(1);
            } else {
                linearSlides.setPower(0);
            }
            slidingInProgress = true;
            if (linearSlides.getCurrentPosition() == level3) {
                slidingInProgress = false;
            }
        }
    }
    public void togglerServoGate(boolean open){

    }
}
