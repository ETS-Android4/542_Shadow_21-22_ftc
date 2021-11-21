package org.whitneyrobotics.ftc.teamcode.lib.util;

//import com.acmerobotics.dashboard.config.Config;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import org.whitneyrobotics.ftc.teamcode.lib.control.ControlConstants;
@Config
public class RobotConstants {
    //Drivetrain
    public final static double DEADBAND_DRIVE_TO_TARGET = 5;
    public final static double DEADBAND_ROTATE_TO_TARGET = 15;
    public final static double drive_min = .1;//.1245;
    public final static double drive_max = 0.6;//.6;
    public final static double rotate_min = 0;
    public final static double rotate_max = 0.6;

    public static double DRIVE_KP = 0.6;
    public static double DRIVE_KI = 0.007;
    public static double DRIVE_KD = 0.08;

    public final static ControlConstants DRIVE_CONSTANTS = new ControlConstants(DRIVE_KP,DRIVE_KI,DRIVE_KD);

    public static double ROTATE_KP = 1.2;
    public static double ROTATE_KI = 0.0015;
    public static double ROTATE_KD = 0.85;
    public final static ControlConstants ROTATE_CONSTANTS = new ControlConstants(ROTATE_KP,ROTATE_KI,ROTATE_KD);

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
