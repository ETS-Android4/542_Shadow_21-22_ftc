package org.whitneyrobotics.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.checkerframework.checker.units.qual.C;
import org.whitneyrobotics.ftc.teamcode.subsys.Carousel;

@TeleOp(name = "CarouselTest")
public class CarouselTest extends OpMode {

    private Carousel carousel;

    @Override
    public void init() {
        carousel = new Carousel(hardwareMap);
    }

    @Override
    public void loop() {
        telemetry.addData("Rotations", carousel.getRotations());
        telemetry.addData("Rotate in progress", carousel.rotateInProgress());

        rotate(gamepad1.x);
    }
    private void rotate(boolean button) {
        carousel.operate(button);
    }
}
