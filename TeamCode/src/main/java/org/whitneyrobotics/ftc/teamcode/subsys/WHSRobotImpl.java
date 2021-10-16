package org.whitneyrobotics.ftc.teamcode.subsys;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.whitneyrobotics.ftc.teamcode.lib.control.PIDController;
import org.whitneyrobotics.ftc.teamcode.lib.geometry.Coordinate;
import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;
import org.whitneyrobotics.ftc.teamcode.lib.util.Functions;
import org.whitneyrobotics.ftc.teamcode.lib.util.RobotConstants;

public class WHSRobotImpl {
    public Carousel robotCarousel;
    public Drivetrain robotDrivetrain;
    public Outtake robotOuttake;
    public IMU robotIMU;

    Coordinate currentCoord;
    private double targetHeading; //field frame
    public double angleToTargetDebug;
    public double distanceToTargetDebug = 0;
    public Position vectorToTargetDebug = new Position(542, 542);
    private double lastKnownHeading = 0.1;

    private static double DEADBAND_DRIVE_TO_TARGET = RobotConstants.DEADBAND_DRIVE_TO_TARGET; //in mm
    private static double DEADBAND_ROTATE_TO_TARGET = RobotConstants.DEADBAND_ROTATE_TO_TARGET; //in degrees

    public static double DRIVE_MIN = RobotConstants.drive_min;
    public static double DRIVE_MAX = RobotConstants.drive_max;
    public static double ROTATE_MIN = RobotConstants.rotate_min;
    public static double ROTATE_MAX = RobotConstants.rotate_max;

    public PIDController rotateController = new PIDController(RobotConstants.ROTATE_CONSTANTS);
    public PIDController driveController = new PIDController(RobotConstants.DRIVE_CONSTANTS);

    private boolean firstRotateLoop = true;
    private boolean firstDriveLoop = true;
    private boolean driveBackwards;

    private int driveSwitch = 0;

    private boolean driveToTargetInProgress = false;
    private boolean rotateToTargetInProgress = false;

    private double[] encoderDeltas = {0.0, 0.0};
    private double[] encoderValues = {0.0, 0.0};
    private double robotX;
    private double robotY;
    private double distance;

    public WHSRobotImpl (HardwareMap robotMap){
        DEADBAND_DRIVE_TO_TARGET = RobotConstants.DEADBAND_DRIVE_TO_TARGET; //in mm
        DEADBAND_ROTATE_TO_TARGET = RobotConstants.DEADBAND_ROTATE_TO_TARGET; //in degrees

        robotCarousel = new Carousel(robotMap);
        robotDrivetrain = new Drivetrain(robotMap);
        robotOuttake = new Outtake(robotMap);

        DRIVE_MIN = RobotConstants.drive_min;
        DRIVE_MAX = RobotConstants.drive_max;
        ROTATE_MIN = RobotConstants.rotate_min;
        ROTATE_MAX = RobotConstants.rotate_max;


        robotDrivetrain.resetEncoders();
        robotIMU = new IMU(robotMap);
        currentCoord = new Coordinate(0.0, 0.0, 0.0);
    }

    public void driveToTarget(Position targetPos, boolean backwards) {
        Position vectorToTarget = Functions.Positions.subtract(targetPos, currentCoord.getPos()); //field frame
        vectorToTarget = Functions.field2body(vectorToTarget, currentCoord); //body frame
        vectorToTargetDebug = vectorToTarget;
        double distanceToTarget = vectorToTarget.getX()/*Functions.calculateMagnitude(vectorToTarget) * (vectorToTarget.getX() >= 0 ? 1 : -1)*/;
        distanceToTargetDebug = distanceToTarget;

        double degreesToRotate = Math.atan2(vectorToTarget.getY(), vectorToTarget.getX()); //from -pi to pi rad
        degreesToRotate = degreesToRotate * 180 / Math.PI;
        targetHeading = Functions.normalizeAngle(currentCoord.getHeading() + degreesToRotate); //-180 to 180 deg

        switch (driveSwitch) {
            case 0:
                driveToTargetInProgress = true;
                rotateToTarget(targetHeading, backwards);
                if (!rotateToTargetInProgress()) {
                    driveSwitch = 1;
                }
                break;
            case 1:

                if (firstDriveLoop) {
                    driveToTargetInProgress = true;
                    driveController.init(distanceToTarget);
                    firstDriveLoop = false;
                }

                driveController.setConstants(RobotConstants.DRIVE_CONSTANTS);
                driveController.calculate(distanceToTarget);

                double power = Functions.map(Math.abs(driveController.getOutput()), DEADBAND_DRIVE_TO_TARGET, 1500, DRIVE_MIN, DRIVE_MAX);

                // this stuff may be causing the robot to oscillate around the target position
                if (distanceToTarget < 0) {
                    power = -power;
                } else if (distanceToTarget > 0) {
                    power = Math.abs(power);
                }
                if (Math.abs(distanceToTarget) > DEADBAND_DRIVE_TO_TARGET) {
                    driveToTargetInProgress = true;
                    robotDrivetrain.operateLeft(power);
                    robotDrivetrain.operateRight(power);
                } else {
                    robotDrivetrain.operateRight(0.0);
                    robotDrivetrain.operateLeft(0.0);
                    driveToTargetInProgress = false;
                    rotateToTargetInProgress = false;
                    firstDriveLoop = true;
                    driveSwitch = 0;
                }
                // end of weird code
                break;
        }
    }

    public void rotateToTarget(double targetHeading, boolean backwards) {

        double angleToTarget = targetHeading - currentCoord.getHeading();
        /*if (backwards && angleToTarget > 90) {
            angleToTarget = angleToTarget - 180;
            driveBackwards = true;
        }
        else if (backwards && angleToTarget < -90) {
            angleToTarget = angleToTarget + 180;
            driveBackwards = true;
        }*/
        if (backwards) {
            angleToTarget = Functions.normalizeAngle(angleToTarget + 180); //-180 to 180 deg
            driveBackwards = true;
        } else {
            angleToTarget = Functions.normalizeAngle(angleToTarget);
            driveBackwards = false;
        }

        angleToTargetDebug = angleToTarget;

        if (firstRotateLoop) {
            rotateToTargetInProgress = true;
            rotateController.init(angleToTarget);
            firstRotateLoop = false;
        }

        rotateController.setConstants(RobotConstants.ROTATE_CONSTANTS);
        rotateController.calculate(angleToTarget);

        double power = (rotateController.getOutput() >= 0 ? 1 : -1) * (Functions.map(Math.abs(rotateController.getOutput()), 0, 180, ROTATE_MIN, ROTATE_MAX));

        if (Math.abs(angleToTarget) > DEADBAND_ROTATE_TO_TARGET/* && rotateController.getDerivative() < 40*/) {
            robotDrivetrain.operateLeft(power);
            robotDrivetrain.operateRight(-power);
            rotateToTargetInProgress = true;
        } else {
            robotDrivetrain.operateLeft(0.0);
            robotDrivetrain.operateRight(0.0);
            rotateToTargetInProgress = false;
            firstRotateLoop = true;
        }
    }

    public boolean driveToTargetInProgress() {
        return driveToTargetInProgress;
    }

    public boolean rotateToTargetInProgress() {
        return rotateToTargetInProgress;
    }

    public void estimatePosition() {
        encoderDeltas = robotDrivetrain.getLRAvgEncoderDelta();
        distance = robotDrivetrain.encToMM((encoderDeltas[0] + encoderDeltas[1]) / 2);
        robotX += distance * Functions.cosd(getCoordinate().getHeading());
        robotY += distance * Functions.sind(getCoordinate().getHeading());
        currentCoord.setX(robotX);
        currentCoord.setY(robotY);
    }

    public void deadWheelEstimateCoordinate() {

        double deltaXRobot, deltaYRobot;

        encoderDeltas = robotDrivetrain.getMMDeadwheelEncoderDeltas();

        double deltaXWheels = (encoderDeltas[0] - encoderDeltas[2]) / 2;
        double deltaYWheel = encoderDeltas[1];
        double deltaTheta = (-encoderDeltas[2] - encoderDeltas[0]) / (Drivetrain.getTrackWidth());

        if (deltaTheta == 0) {
            deltaXRobot = deltaXWheels;
            deltaYRobot = deltaYWheel;
        } else {
            double movementRadius = deltaXWheels / (deltaTheta);
            double strafeRadius = deltaYWheel / (deltaTheta);

            deltaXRobot = movementRadius * Math.sin(deltaTheta) + strafeRadius * (1 - Math.cos(deltaTheta));
            deltaYRobot = strafeRadius * Math.sin(deltaTheta) - movementRadius * (1 - Math.cos(deltaTheta));
        }
        Position bodyVector = new Position(deltaXRobot, deltaYRobot);
        Position fieldVector = Functions.body2field(bodyVector, currentCoord);
        currentCoord.addX(fieldVector.getX());
        currentCoord.addY(fieldVector.getY());
        currentCoord.setHeading(Functions.normalizeAngle(currentCoord.getHeading() + Math.toDegrees(deltaTheta)));
    }




}
