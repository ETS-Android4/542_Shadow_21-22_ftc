package org.whitneyrobotics.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whitneyrobotics.ftc.teamcode.lib.util.GamepadListener;
import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;

@TeleOp(name = "ControllerDemo", group = "Tests")
public class ControllerDemo extends OpMode {
    private GamepadListener gamepadListener= new GamepadListener();
    private Toggler buttonLongPressToggler = new Toggler(2);
    private Toggler buttonShortPressToggler = new Toggler(2);

    @Override
    public void init() {

    }

    @Override
    public void loop() {
        buttonLongPressToggler.changeState(gamepadListener.longPress(gamepad1.a,750));
        buttonShortPressToggler.changeState(gamepadListener.shortPress(gamepad1.b,500));


        telemetry.addData("gamepad1LeftStickX: ", gamepad1.left_stick_x);
        telemetry.addData("gamepad1LeftStickY: ", gamepad1.left_stick_y);
        telemetry.addData("gamepad1RightStickX: ", gamepad1.right_stick_x);
        telemetry.addData("gamepad1RightStickY: ", gamepad1.right_stick_y);
        telemetry.addData("Long Press A: ", buttonLongPressToggler.currentState());
        telemetry.addData("Short Press B", buttonShortPressToggler.currentState());
        telemetry.addData("Gamepad X: ", gamepad1.x);
        telemetry.addData("Right Trigger", gamepad1.right_trigger);

    }
}
