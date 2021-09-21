package org.whitneyrobotics.ftc.teamcode.lib.util;

//import com.acmerobotics.dashboard.config.Config;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import org.whitneyrobotics.ftc.teamcode.lib.control.ControlConstants;
@Config
public class RobotConstants {
    //Drivetrain
    public final static double DEADBAND_DRIVE_TO_TARGET = 24.5;
    public final static double DEADBAND_ROTATE_TO_TARGET = 1.0;
    public final static double drive_min = .2;//.1245;
    public final static double drive_max = 1.0;//.6;
    public final static double rotate_min = 0.2;
    public final static double rotate_max = 1.0;

    public final static ControlConstants DRIVE_CONSTANTS = new ControlConstants(1.7, 0.7, 0.8);
    public final static ControlConstants ROTATE_CONSTANTS = new ControlConstants(1.5, 0.085, 0.3);

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
