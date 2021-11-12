package org.whitneyrobotics.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whitneyrobotics.ftc.teamcode.subsys.Drivetrain;
import org.whitneyrobotics.ftc.teamcode.subsys.Intake;
import org.whitneyrobotics.ftc.teamcode.subsys.Outtake;
import org.whitneyrobotics.ftc.teamcode.subsys.WHSRobotImpl;

@TeleOp(name = "WHS TeleOp", group = "TeleOp")
public class WHSTeleOp extends OpMode {
    WHSRobotImpl robot;
    @Override
    public void init() {robot = new WHSRobotImpl(hardwareMap);}

    // Driver 1 (Gamepad 1): Drive train, Carousel
    // Driver 2 (Gamepad 2): Intake, Outtake
    @Override
    public void loop() {

        // DRIVER 1 CONTROLS
        // Drivetrain
        // robot.estimateHeading();
        robot.drivetrain.switchFieldCentric(gamepad1.b);
        if (gamepad1.left_bumper) {
            robot.drivetrain.operateMecanumDriveScaled(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, robot.getCoordinate().getHeading());
        }
        else {
            robot.drivetrain.operateMecanumDrive(gamepad1.left_stick_x / 2.54, gamepad1.left_stick_y / 2.54, gamepad1.right_stick_x / 2.54, robot.getCoordinate().getHeading());
        }
        // Carousel
        robot.robotCarousel.operate(gamepad1.a);
        robot.robotCarousel.togglerOperate(gamepad1.b);

        // DRIVER 2 CONTROLS
        // Intake
        // - Extend arm/unextend arm
        robot.robotIntake.operate(gamepad2.right_bumper,gamepad2.right_bumper, gamepad2.y,gamepad2.left_bumper);

        // Outtake
        // - Adjust levels
        robot.robotOuttake.togglerOuttake(gamepad2.dpad_right, gamepad2.dpad_left);

        if(gamepad2.a) { robot.robotOuttake.reset(); }

        // - Hatch | Servo gate
        robot.robotOuttake.togglerServoGate(gamepad2.x);

    }
}
