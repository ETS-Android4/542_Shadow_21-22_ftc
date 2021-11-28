package org.whitneyrobotics.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whitneyrobotics.ftc.teamcode.lib.util.GamepadListener;
import org.whitneyrobotics.ftc.teamcode.subsys.Outtake;
import org.whitneyrobotics.ftc.teamcode.subsys.WHSRobotImpl;

@TeleOp(name = "WHS TeleOp Outtake Presets Disabled", group = "TeleOp")
public class WHSTeleOp extends OpMode {
    WHSRobotImpl robot;
    private GamepadListener rbHold;
    private GamepadListener lbHold;
    private int autoDropState = 1;
    private Outtake outtake;

    @Override
    public void init() {
        robot = new WHSRobotImpl(hardwareMap);
    }

    // Driver 1 (Gamepad 1): Drive train, Carousel
    // Driver 2 (Gamepad 2): Intake, Outtake
    @Override
    public void loop() {

        // DRIVER 1 CONTROLS
        // Drivetrain
        // robot.estimateHeading();
        //robot.drivetrain.switchFieldCentric(gamepad1.b);
        if (gamepad1.left_bumper) {
            robot.drivetrain.operateMecanumDrive(-gamepad1.left_stick_x / 2.54, gamepad1.left_stick_y / 2.54, gamepad1.right_stick_x / 2.54, robot.getCoordinate().getHeading());
        }
        else {
            robot.drivetrain.operateMecanumDriveScaled(-gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, robot.getCoordinate().getHeading());
        }
        // Carousel
        //robot.robotCarousel.operate(gamepad1.right_bumper);
        //robot.robotCarousel.togglerOperate(gamepad1.right_trigger>0.05,gamepad1.y);

        // DRIVER 2 CONTROLS
        // Intake
        // - Extend arm/unextend arm
        //robot.robotIntake.operate(gamepad2.right_bumper,gamepad2.left_bumper, gamepad2.right_trigger>0,gamepad2.left_trigger>0);

        // Outtake
        // - Adjust levels

        //robot.robotOuttake.togglerOuttake(gamepad2.dpad_right, gamepad2.dpad_left);
        robot.outtake.operate(gamepad2.dpad_up,gamepad2.dpad_down);

        //In case line outtake levels don't work properly
        /*if(gamepad1.dpad_up){
            robot.robotOuttake.linearSlides.setPower(-0.2);
        } else if (gamepad1.dpad_down){
            robot.robotOuttake.linearSlides.setPower(0.2);
        } else {
            robot.robotOuttake.linearSlides.setPower(0);
        }*/

        //if(gamepad2.a) { robot.robotOuttake.reset(); }

        // - Hatch | Servo gate
        if(gamepad2.b){
            autoDropState = 1;
        }

        switch(autoDropState){
            case 0:
                if(robot.outtake.autoDrop()){
                    autoDropState++;
                }
                break;
            case 1:
                if(gamepad2.b){autoDropState = 0;}
        }

    }
}
