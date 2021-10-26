package org.whitneyrobotics.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whitneyrobotics.ftc.teamcode.subsys.Drivetrain;
import org.whitneyrobotics.ftc.teamcode.subsys.Intake;
import org.whitneyrobotics.ftc.teamcode.subsys.Outtake;
import org.whitneyrobotics.ftc.teamcode.subsys.WHSRobotImpl;

@TeleOp(name = "WHS TeleOp", group = "TeleOp")
public class WHSTeleOP extends OpMode {
    WHSRobotImpl robot;
    @Override
    public void init() {
        robot = new WHSRobotImpl(hardwareMap);
    }

    // Driver 1 (Gamepad 1): Drive train, Carousel
    // Driver 2 (Gamepad 2): Intake, Outtake
    @Override
    public void loop() {
        //Drivetrain
        //robot.estimateHeading();
        robot.drivetrain.switchFieldCentric(gamepad1.b);
        if (gamepad1.left_bumper) {
           // robot.drivetrain.operateMecanumDrive(gamepad1.left_stick_x / 2.54, gamepad1.left_stick_y / 2.54, gamepad1.right_stick_x / 2.54, robot.getCoordinate().getHeading());
        } else{
            //robot.drivetrain.operateMecanumDriveScaled(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, robot.getCoordinate().getHeading());
        }
        // Carousel
        if (gamepad1.y) {
            robot.robotCarousel.operate(gamepad1.y);
        }

        // Intake
        // - Extend arm/unextend arm
        if (gamepad2.right_bumper) {
            robot.intake.operate(true, false);
        }

        // - Start/off roller
        if (gamepad2.left_bumper) {
            robot.intake.operate(false, true);
        }

        // Outtake
        // - Adjust levels
        if (gamepad2.dpad_right || gamepad2.dpad_left) {
            robot.robotOuttake.togglerOuttake(gamepad2.dpad_right, gamepad2.dpad_left);
        }
        
        // - Hatch
        if (gamepad2.x) {
            robot.robotOuttake.togglerServoGate(true);
        }

    }
}
