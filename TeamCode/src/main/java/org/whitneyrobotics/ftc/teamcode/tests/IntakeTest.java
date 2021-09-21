package org.whitneyrobotics.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;
import org.whitneyrobotics.ftc.teamcode.subsys.Intake;

@TeleOp(name = "Intake Test", group = "Tests")
public class IntakeTest extends OpMode {
    public Intake testIntake;
    public Toggler powerTog;
    public Toggler dropdownTog;
    int i;
    double power = 0;
    double position =0;

    @Override
    public void init() {
        testIntake = new Intake(hardwareMap);
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
        testIntake.autoDropIntake();
        telemetry.addData("Wheel Power: ", power);
        telemetry.addData("Dropdown Position", position);

    }
}
