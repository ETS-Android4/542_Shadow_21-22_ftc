package org.whitneyrobotics.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whitneyrobotics.ftc.teamcode.lib.geometry.Coordinate;
import org.whitneyrobotics.ftc.teamcode.lib.util.DataToolsLite;
import org.whitneyrobotics.ftc.teamcode.lib.util.GamepadListener;
import org.whitneyrobotics.ftc.teamcode.subsys.Outtake;
import org.whitneyrobotics.ftc.teamcode.subsys.WHSRobotImpl;

@TeleOp(name = "WHS TeleOp", group = "TeleOp")
public class WHSTeleOp extends OpMode {
    WHSRobotImpl robot;
    private GamepadListener gamepadListener1 = new GamepadListener();
    private int autoDropState = 1;
    private Outtake outtake;
    private int autoDropStateTele = 1;
    String[] outtakeLabels = new String[]{"Level 1","Level 1.5","Level 2", "Level 3"};

    private long lastRecordedTime;

    @Override
    public void init() {
        telemetry.setAutoClear(false);
        robot = new WHSRobotImpl(hardwareMap);
        Object[] data = DataToolsLite.decode("autoConfig.txt");
        try {
            robot.carousel.setAlliance((int)data[0]);
            telemetry.addLine("Auto set alliance to " + robot.carousel.getAlliance());
        } catch (Exception e){
            System.out.println("sussy");
        }
        lastRecordedTime=lastRecordedTime = System.nanoTime();
    }

    // Driver 1 (Gamepad 1): Drivetrain, Intake
    // Driver 2 (Gamepad 2): Intake Reverse, Carousel, Outtake
    @Override
    public void loop() {
        telemetry.setAutoClear(true);

        // DRIVER 1 CONTROLS
        // Drivetrain
        robot.estimateHeading();
        if(gamepad1.back){
            robot.setInitialCoordinate(new Coordinate(0,0,0));
        }
        robot.drivetrain.switchFieldCentric(gamepad1.back);
        if (gamepad1.left_bumper) {
            robot.drivetrain.operateMecanumDrive(-gamepad1.left_stick_x / 3, gamepad1.left_stick_y / 3, -gamepad1.right_stick_x / 3, robot.getCoordinate().getHeading());
        }
        else {
            robot.drivetrain.operateMecanumDriveScaled(-gamepad1.left_stick_x, gamepad1.left_stick_y, -gamepad1.right_stick_x, robot.getCoordinate().getHeading());
        }
        // Intake
        robot.intake.operate(gamepad1.right_bumper,gamepad1.right_trigger>0.05 || gamepad2.right_trigger>0.05); //just so player 2 can reverse

        // DRIVER 2 CONTROLS
        // Intake
        // - Extend arm/unextend arm
        robot.outtakeInStates(gamepad2.a,gamepad2.b,gamepad2.dpad_up,gamepad2.dpad_down,gamepad2.x,-gamepad2.left_stick_y,gamepad2.dpad_left, gamepad2.b);
        // Outtake
        // - Adjust levels

        //robot.robotOuttake.togglerOuttake(gamepad2.dpad_right, gamepad2.dpad_left);
        //robot.outtake.operate(gamepad2.dpad_up,gamepad2.dpad_down);

        //In case line outtake levels don't work properly
        /*if(gamepad1.dpad_up){
            robot.robotOuttake.linearSlides.setPower(-0.2);
        } else if (gamepad1.dpad_down){
            robot.robotOuttake.linearSlides.setPower(0.2);
        } else {
            robot.robotOuttake.linearSlides.setPower(0);
        }*/

        //if(gamepad2.a) { robot.robotOuttake.reset(); }
        robot.carousel.operate(gamepad2.left_bumper,gamepad2.left_trigger>0.01, gamepad2.y);
        // - Hatch | Servo gate
        /*if(gamepad2.b){
            autoDropState = 1;
        }*/


            /*switch(autoDropStateTele){
                case 0:
                    if(robot.outtake.autoDrop()){
                        autoDropStateTele++;
                    }
                    break;
                case 1:
                    if (gamepad2.y && !robot.outtake.isGateBusy()){
                        autoDropStateTele = 0;
                        break;
                    }
            }*/
            robot.outtake.updateGateOverride(gamepad2.dpad_left);

        if(gamepadListener1.longPress(gamepad2.back,500)){
            throw new RuntimeException("UnknownException - TeamCode terminated with a non-zero exit code.");
        }
        telemetry.addData("Drive mode",robot.drivetrain.getFieldCentric());
        telemetry.addData("Selected Outtake Level",robot.outtakeLevel());
        telemetry.addData("Sliding in Progress",robot.outtake.slidingInProgress);
        telemetry.addData("Outtake state",robot.stateDesc);
        telemetry.addData("Carousel Alliance",robot.carousel.getAlliance());
        telemetry.addData("Outtake encoder position",robot.outtake.getSlidesPosition());
        telemetry.addData("Outtake Level",outtakeLabels[robot.outtake.getTier()]);
        telemetry.addLine();
        telemetry.addData("Current processing latency: ", (Math.ceil(System.nanoTime()-lastRecordedTime)/1E6) + "ms");
        lastRecordedTime = System.nanoTime();

    }
}
