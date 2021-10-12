package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whitneyrobotics.ftc.teamcode.lib.util.SimpleTimer;
import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

public class Carousel {
    private CRServo spinner;

    public Carousel(HardwareMap hardwareMap) {
        spinner = hardwareMap.crservo.get("spinnerServo");
    }

    public final double WHEEL_CIRCUMFERENCE = 6.2831853072;
    public final double CAROUSEL_CIRCUMFERENCE = 47.1238898038;
    public final double SERVO_RPM = 250;
    private double rotations = CAROUSEL_CIRCUMFERENCE/WHEEL_CIRCUMFERENCE;
    private double seconds = SERVO_RPM/rotations*60 + 0.5;

    private boolean rotateInProgress = false;
    private boolean firstLoop = true;

    private Toggler onOff= new Toggler(2);
    private SimpleTimer timer = new SimpleTimer();
    //toggler based teleop
    public void togglerOperate(boolean on){
        onOff.changeState(on);
        if (onOff.currentState() == 1) {
            spinner.setPower(1);
            rotateInProgress = true;
        } else {
            spinner.setPower(0);
            rotateInProgress = false;
        }
    }
    //tele-op
    public void operate(boolean buttonInput) {
        telemetry.addData("Spin ducky: ", rotateInProgress);
        if (!rotateInProgress){
            if(buttonInput){
                rotateInProgress = true;
            }
        }  else {
            if(firstLoop){
                timer.set(seconds);
                spinner.setPower(1);
                firstLoop = false;
            } else if (timer.isExpired()){
                spinner.setPower(0);
                firstLoop = true;
                rotateInProgress = false;
            }

        }

    }

    //autonomous
    public void operate() {
        telemetry.addData("Spin ducky: ", rotateInProgress);
        if(firstLoop){
            rotateInProgress = true;
            timer.set(seconds);
            spinner.setPower(1);
            firstLoop = false;
        } else if (timer.isExpired()){
            spinner.setPower(0);
            firstLoop = true;
            rotateInProgress = false;
        }
    }

    public double getRotations() { return rotations; }
    public double getSeconds() { return seconds; }
    public boolean rotateInProgress() { return rotateInProgress; }
    public boolean firstLoop() { return firstLoop; }

}
