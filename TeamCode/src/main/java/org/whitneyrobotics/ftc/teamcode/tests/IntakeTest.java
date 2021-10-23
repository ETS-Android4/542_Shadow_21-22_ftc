package org.whitneyrobotics.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;
import org.whitneyrobotics.ftc.teamcode.subsys.Intake;

public class IntakeTest {
    @TeleOp(name = "Intake Test", group = "Tests")
    public class Intake extends OpMode {
        public Intake testIntake;
        public Toggler intakeState;
        public Toggler rollerState;
        int i;
        double power = 0;
        double position =0;

        @Override
        public void init() {
            testIntake = new Intake();
            i = 0;
        }

        @Override
        public void loop() {
            i++;
            if(i%10 == 0){
                if(gamepad1.a && power < 1){
                    power += 0.01;
                }else if(gamepad1.b && power > -1)
                {
                    power -= 0.01;
                }

                if(gamepad1.x && position < 1){
                    position += 0.01;
                }else if(gamepad1.y && position > 0)
                {
                    position -= 0.01;
                }
            }
            //testIntake.setIntakePower(power);
            telemetry.addData("Roller Power: ", power);
            telemetry.addData("Arm Position", position);

        }

    }

}

