package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.control.PIDController;
import org.whitneyrobotics.ftc.teamcode.lib.util.Functions;
import org.whitneyrobotics.ftc.teamcode.lib.util.RobotConstants;
import org.whitneyrobotics.ftc.teamcode.lib.util.SimpleTimer;
import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class Outtake {
    public Servo gate;
    public DcMotorEx linearSlides;

    public Outtake(HardwareMap outtakeMap) {
        gate = outtakeMap.servo.get("gateServo");
        linearSlides = outtakeMap.get(DcMotorEx.class, "linearSlides");
        gate.setPosition(GatePositions.CLOSE.getPosition());
        linearSlides.setDirection(DcMotor.Direction.REVERSE);
        resetEncoder();
    }

    private Toggler servoGateTog = new Toggler(2);
    private Toggler linearSlidesTog = new Toggler(3);
    private SimpleTimer outtakeGateTimer = new SimpleTimer();
    public double errorDebug = 0;

    private enum GatePositions{
        CLOSE(1),
        OPEN(0.6);
        private double position;
        private GatePositions(double position){
            this.position = position;
        }
        public double getPosition(){return this.position;}
    }

    private enum MotorLevels{
        LEVEL1(0.0),
        LEVEL2(1296.0),
        LEVEL3(2092.0);
        private double encoderPos;
        private MotorLevels(double encoderPos){
            this.encoderPos = encoderPos;
        }
        public double getPosition(){return this.encoderPos;}

    }
    private double[] orderedPositions = {MotorLevels.LEVEL1.getPosition(), MotorLevels.LEVEL2.getPosition(),MotorLevels.LEVEL3.getPosition()};
    public boolean slidingInProgress = false;
    public PIDController slidesController = new PIDController(RobotConstants.SLIDE_CONSTANTS);
    public boolean slidesFirstLoop = true;
    public boolean dropFirstLoop = true; //for setting drop timer
    public int gateDelay = 500;
    //private boolean outtakeTimerSet = true; <<I don't know what this is used for

    //toggler based teleop
    public void operate(boolean up, boolean down) {
        if (!slidingInProgress){linearSlidesTog.changeState(up, down);}
        operateWithoutController(linearSlidesTog.currentState());
    }

    public void togglerServoGate(boolean pressed){
        servoGateTog.changeState(pressed);
        if (servoGateTog.currentState() == 0) {
            gate.setPosition(GatePositions.CLOSE.getPosition());
        } else {
            gate.setPosition(GatePositions.OPEN.getPosition());
        }
    }

    public void operateWithoutController(int levelIndex) {
        double currentTarget = orderedPositions[levelIndex];
        double error = linearSlides.getCurrentPosition()-currentTarget;
        errorDebug = error;

        if (slidesFirstLoop){
            slidingInProgress = true;
            slidesController.init(error);
            slidesFirstLoop = false;
        }

        slidesController.calculate(error);

        double power = (slidesController.getOutput() >= 0 ? 1 : -1) * (Functions.map(Math.abs(slidesController.getOutput()), RobotConstants.DEADBAND_SLIDE_TO_TARGET, 3000, RobotConstants.slide_min, RobotConstants.slide_max));

        if(Math.abs(error) <= RobotConstants.DEADBAND_SLIDE_TO_TARGET){
            linearSlides.setPower(0);
            slidingInProgress = false;
            slidesFirstLoop = true;
        }
        else {
            linearSlides.setPower(power);
            slidingInProgress = true;
        }
    }

    public boolean autoDrop() { //boolean so our autoop knows if its done
        if(dropFirstLoop) {
            gate.setPosition(GatePositions.OPEN.getPosition());
            outtakeGateTimer.set(gateDelay); /*ms to keep the flap open*/
            dropFirstLoop = false;
        }

        if(outtakeGateTimer.isExpired()){
            gate.setPosition(GatePositions.CLOSE.getPosition());
            dropFirstLoop = true;
            return true;
        }
        return false;
    }

    public void reset() {
        linearSlidesTog.setState(0);
        operateWithoutController(0);
    }

    public void resetEncoder() {
        linearSlides.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        linearSlides.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public int getTier() { return linearSlidesTog.currentState(); }
}

