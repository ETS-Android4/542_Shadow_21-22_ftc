package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

public class HDriveDrivetrain {
    public DcMotorEx left;
    public DcMotorEx right;
    public DcMotorEx lateral;

    public class EncoderConverter {
        private double encoderTicksPerMM = 0.0;

        public EncoderConverter(double wheelRadius, double encoderTicksPerRev, double gearRatio) {
            double circOfWheel = wheelRadius * 2 * Math.PI;
            encoderTicksPerMM = encoderTicksPerRev / (circOfWheel * gearRatio);
        }

        public double encToMM(double encoderTicks) {
            return encoderTicks / encoderTicksPerMM;
        }
    }

    public HDriveDrivetrain (HardwareMap hDriveMap){
        left = hDriveMap.get(DcMotorEx.class, "Left Motor");
        right = hDriveMap.get(DcMotorEx.class, "Right Motor");
        lateral = hDriveMap.get(DcMotorEx.class, "Lateral Motor");

    }

    public void operate (double leftPower, double rightPower, double lateralPower){
        left.setPower(leftPower);
        right.setPower(rightPower);
        lateral.setPower(lateralPower);
    }

    public void setTargetPosition (int position, double power){
        left.setTargetPosition(position);
        right.setTargetPosition(position);
        lateral.setTargetPosition(position);

        left.setPower(power);
        right.setPower(power);
        lateral.setPower(power);
    }

}