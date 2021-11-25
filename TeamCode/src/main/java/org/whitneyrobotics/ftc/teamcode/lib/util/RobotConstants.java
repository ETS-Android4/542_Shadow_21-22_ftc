package org.whitneyrobotics.ftc.teamcode.lib.util;

//import com.acmerobotics.dashboard.config.Config;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import org.whitneyrobotics.ftc.teamcode.lib.control.ControlConstants;
@Config
public class RobotConstants {
    //Drivetrain
    public final static double DEADBAND_DRIVE_TO_TARGET = 5;
    public final static double DEADBAND_ROTATE_TO_TARGET = 2.5;
    public final static double drive_min = .15;//.1245;
    public final static double drive_max = 1;//.6;
    public final static double rotate_min = 0.15;
    public final static double rotate_max = 1;

    public static double DRIVE_KP = 5.8;
    public static double DRIVE_KI = 0.000005;
    public static double DRIVE_KD = 1.5;

    public final static ControlConstants DRIVE_CONSTANTS = new ControlConstants(DRIVE_KP,DRIVE_KI,DRIVE_KD);

    public static double ROTATE_KP = 0.60;
    public static double ROTATE_KI = 0.0024;
    public static double ROTATE_KD = 0.060;
    public final static ControlConstants ROTATE_CONSTANTS = new ControlConstants(ROTATE_KP,ROTATE_KI,ROTATE_KD);

    public final static double DEADBAND_SLIDE_TO_TARGET = 50;
    public final static double slide_min = 0.2;
    public final static double slide_max = 0.4;

    public static double SLIDE_KP = 0.1;
    public static double SLIDE_KI = 0;
    public static double SLIDE_KD = 0;
    public final static ControlConstants SLIDE_CONSTANTS = new ControlConstants(SLIDE_KP,SLIDE_KI,SLIDE_KD);

    //Outtake
    public final static double OUTTAKE_MAX_VELOCITY = 2120;
    public final static ControlConstants.FeedforwardFunction flywheelKF = (double currentPosition, double currentVelocity) -> 1/OUTTAKE_MAX_VELOCITY;
    public static double FLYWHEEL_KP = 8.6;
    public static double FLYWHEEL_KI = 0.00091;
    public static double FLYWHEEL_KD = 0.86;
    public final static ControlConstants FLYWHEEL_CONSTANTS = new ControlConstants(FLYWHEEL_KP,FLYWHEEL_KI,FLYWHEEL_KD, flywheelKF);

    public final static double rotateTestAngle = 180;
    public final static boolean rotateOrientation = true;


}
