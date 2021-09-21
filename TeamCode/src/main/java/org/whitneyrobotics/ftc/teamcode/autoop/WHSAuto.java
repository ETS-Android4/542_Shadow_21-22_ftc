package org.whitneyrobotics.ftc.teamcode.autoop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whitneyrobotics.ftc.teamcode.lib.geometry.Coordinate;
import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;
import org.whitneyrobotics.ftc.teamcode.lib.purepursuit.FollowerConstants;
import org.whitneyrobotics.ftc.teamcode.lib.purepursuit.PathGenerator;
import org.whitneyrobotics.ftc.teamcode.lib.purepursuit.swervetotarget.SwervePath;
import org.whitneyrobotics.ftc.teamcode.lib.purepursuit.swervetotarget.SwervePathGenerationConstants;
import org.whitneyrobotics.ftc.teamcode.lib.util.SimpleTimer;
import org.whitneyrobotics.ftc.teamcode.subsys.Intake;
import org.whitneyrobotics.ftc.teamcode.subsys.WHSRobotImpl;
import org.whitneyrobotics.ftc.teamcode.subsys.Wobble;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.ArrayList;
import java.util.List;

//import static org.whitneyrobotics.ftc.teamcode.subsys.Outtake.Off;

@Autonomous(name = "WHS Auto", group = "Auto")
public class WHSAuto extends OpMode {
    WHSRobotImpl robot;

    // Starting color and inside/outside array values
    static final int RED = 0;
    //static final int BLUE = 1;
    static final int INSIDE = 0;
    static final int OUTSIDE = 1;

    // Starting information
    //static final int STARTING_POSITION = INSIDE;
    public static final int STARTING_ALLIANCE = RED;
    static final double STARTING_COORDINATE_X = 1571;
    static final double STARTING_COORDINATE_Y = -1200; // may or may not be -600
    //static final boolean PARTNER_MOVED_WOBBLE = false;

    // ?? Anyone who knows... uncomment this?
    /*
    static final double LEFT_MAX = 80;
    static final double CENTER_MAX = 165;
     */

    /*static final int LEFT = 0;
    static final int CENTER = 1;
    static final int RIGHT = 2;*/

    public int wobblePosition = 0; //placeholder

    //public Position launchPoint = new Position(300, -285.75);// optimize during testing
    public final Position powershot1 = new Position(1800, -95.25); // from right to left fix later
    public final Position powershot2 = new Position(1800, -285.75);
    public final Position powershot3 = new Position(1800, -476.25);

    Coordinate[] startingCoordinateArray = new Coordinate[2];//starting coordinate

    Position[] shootingPositionArray = new Position[2];// points whrere robot sits to shoot powershots
    Position[] ringPosition = new Position[2];//ring stack postions

    Position[][] scanningDistanceArray = new Position[2][2];//scanning diatances
    Position[][] wobblePositionArray = new Position[2][3];// wobble boxes
    Position[][] parkingPositionArray = new Position[2][3];//parking spots

    /*
    SwerveToTarget driveToShotLineSwerve;
    SwerveToTarget driveToWobblePositionOneSwerve;
    SwerveToTarget driveToWobblePositionTwoSwerve;
    SwerveToTarget driveToWobblePositionThreeSwerve;
    SwerveToTarget driveToLaunchLineFromWobbleOneSwerve;
    SwerveToTarget driveToLaunchLineFromWobbleTwoSwerve;
    SwerveToTarget driveToLaunchLineFromWobbleThreeSwerve;
     */

    /*
    SwervePath startToShotline;
    SwervePath shotLineToWobbleOne;
    SwervePath shotlineToWobbleTwo;
    SwervePath shotlineToWobbleThree;
    SwervePath wobbleOneToParkline;
    SwervePath wobbleTwoToParkline;
    SwervePath wobbleThreeToParkline;
    */

    /*
    // Initialize FollowerConstants
    FollowerConstants startToShotlineFollowerConstants = new FollowerConstants(AutoSwervePositions.startToShotlineLookaheadDist, false);
    FollowerConstants shotlineToWobbleOneFollowerConstants = new FollowerConstants(AutoSwervePositions.shotlineToWobble1LookaheadDist, false);
    FollowerConstants shotlineToWobbleTwoFollowerConstants = new FollowerConstants(AutoSwervePositions.shotlineToWobble2LookahadDist, false);
    FollowerConstants shotlineToWobbleThreeFollowerConstants = new FollowerConstants(AutoSwervePositions.shotlineToWobble3LookaheadDist, false);
    FollowerConstants wobbleOneToParkFollowerConstants = new FollowerConstants(AutoSwervePositions.wobble1ToParkLookaheadDist, true);
    FollowerConstants wobbleTwoToParkFollowerConstants = new FollowerConstants(AutoSwervePositions.wobble2ToParkLookaheadDist, true);
    FollowerConstants wobbleThreeToParkFollowerConstants = new FollowerConstants(AutoSwervePositions.wobble3ToParkLookaheadDist, true);

    // Initialize SwervePathGenerationConstants
    SwervePathGenerationConstants startToShotlinePathGenConstants = new SwervePathGenerationConstants(AutoSwervePositions.startToShotlineWaypointSpacing, AutoSwervePositions.startToShotlineWeightSmooth, AutoSwervePositions.startToShotlineTurnSpeed, AutoSwervePositions.startToShotlineMaxVelocity);
    SwervePathGenerationConstants shotlineToWobbleOneGenerationConstants = new SwervePathGenerationConstants(AutoSwervePositions.shotlineToWobble1Spacing, AutoSwervePositions.shotlineToWobble1WeightSmooth, AutoSwervePositions.shotlineToWobble1TurnSpeed, AutoSwervePositions.shotlineToWobble1MaxVelocity);
    SwervePathGenerationConstants shotlineToWobbleTwoGenerationConstants = new SwervePathGenerationConstants(AutoSwervePositions.shotlineToWobble2Spacing, AutoSwervePositions.shotlineToWobble2WeightSmooth, AutoSwervePositions.shotlineToWobble2TurnSpeed, AutoSwervePositions.shotlineToWobble2MaxVelocity);
    SwervePathGenerationConstants shotlineToWobbleThreeGenerationConstants = new SwervePathGenerationConstants(AutoSwervePositions.shotlineToWobble3Spacing, AutoSwervePositions.shotlineToWobble3WeightSmooth, AutoSwervePositions.shotlineToWobble3TurnSpeed, AutoSwervePositions.shotlineToWobble3MaxVelocity);
    SwervePathGenerationConstants wobbleOneToParkGenerationConstants = new SwervePathGenerationConstants(AutoSwervePositions.wobble1ToParkSpacing, AutoSwervePositions.wobble1ToParkWeightSmooth, AutoSwervePositions.wobble1ToParkTurnSpeed, AutoSwervePositions.wobble1ToParkMaxVelocity);
    SwervePathGenerationConstants wobbleTwoToParkGenerationConstants = new SwervePathGenerationConstants(AutoSwervePositions.wobble2ToParkSpacing, AutoSwervePositions.wobble2ToParkWeightSmooth, AutoSwervePositions.wobble2ToParkTurnSpeed, AutoSwervePositions.wobble2ToParkMaxVelocity);
    SwervePathGenerationConstants wobbleThreeToParkGenerationConstants = new SwervePathGenerationConstants(AutoSwervePositions.wobble3ToParkSpacing, AutoSwervePositions.wobble3ToParkWeightSmooth, AutoSwervePositions.wobble3ToParkTurnSpeed, AutoSwervePositions.wobble3ToParkMaxVelocity);
    */

   /* private void instantiateSwerveToTargets() {
        Position[] driveToShotLineSwervePositions = {scanningDistanceArray[STARTING_ALLIANCE][STARTING_POSITION], shootingPositionArray[STARTING_ALLIANCE]};
        Position[] driveToWobblePositionOneSwervePositions = {shootingPositionArray[STARTING_ALLIANCE], wobblePositionArray[STARTING_ALLIANCE][0]};
        Position[] driveToWobblePositionTwoSwervePositions = {shootingPositionArray[STARTING_ALLIANCE], wobblePositionArray[STARTING_ALLIANCE][1]};
        Position[] driveToWobblePositionThreeSwervePositions = {shootingPositionArray[STARTING_ALLIANCE], wobblePositionArray[STARTING_ALLIANCE][2]};
        Position[] driveToLaunchLineFromWobbleOneSwervePositions = {wobblePositionArray[STARTING_ALLIANCE][0], parkingPositionArray[STARTING_ALLIANCE][wobblePosition]};
        Position[] driveToLaunchLineFromWobbleTwoSwervePositions = {wobblePositionArray[STARTING_ALLIANCE][1], parkingPositionArray[STARTING_ALLIANCE][wobblePosition]};
        Position[] driveToLaunchLineFromWobbleThreeSwervePositions = {wobblePositionArray[STARTING_ALLIANCE][2], parkingPositionArray[STARTING_ALLIANCE][wobblePosition]};
    }*/

    //insert Swerve to Target Here
    //final double STRAFE_TO_RING_LAUNCH_POWER = 0.7542;

    //State definitions
    static final int INIT = 0;
    static final int SCAN_STACK = 1;
    static final int LAUNCH_PARTICLES = 2;
    static final int DROP_OFF_WOBBLE_GOAL = 3;
    static final int PARK_ON_STARTING_LINE = 4;
    static final int END = 5;

    static final int NUMBER_OF_STATES = 6;

    boolean[] stateEnabled = new boolean[NUMBER_OF_STATES];

    int state = INIT;
    int subState = 0;

    /*int wobblePickupState = 0;
    String outtakeState = "hover";*/

    public void advanceState() {
        if (stateEnabled[(state + 1)] || state  >= 4) {
            state++;
            subState = 0;
        } else {
            state++;
            advanceState();
        }
    }

    public void defineStateEnabledStatus() {
        stateEnabled[INIT] = true;
        stateEnabled[SCAN_STACK] = true;
        stateEnabled[LAUNCH_PARTICLES] = true;
        stateEnabled[DROP_OFF_WOBBLE_GOAL] = true;
        stateEnabled[PARK_ON_STARTING_LINE] = true;
        stateEnabled[END] = true;
    }

    //timers
    //SimpleTimer scannerTimer = new SimpleTimer(); // implement in SCAN STACK code uncomment when needed
    SimpleTimer wobbleExtendTimer = new SimpleTimer();
    SimpleTimer wobblePickupArmDownTimer = new SimpleTimer();
    SimpleTimer wobblePickupClawCloseTimer = new SimpleTimer();
    SimpleTimer dropDownTimer = new SimpleTimer();
    SimpleTimer leftPowershotAimTimer = new SimpleTimer();
    SimpleTimer centerPowershotAimTimer = new SimpleTimer();
    SimpleTimer rightPowershotAimTimer = new SimpleTimer();
    SimpleTimer putDownWobble = new SimpleTimer();
    SimpleTimer wobbleFoldTimer = new SimpleTimer();
    SimpleTimer resetDropdownTimer = new SimpleTimer();

    //test all of these
    private final double WOBBLE_EXTEND_DELAY = 1000.0;// optimize in testing
    private final double WOBBLE_PICKUP_CLAW_CLOSE_DELAY = 1000.0; // optimize in testing
    private final double DROPDOWN_DELAY = 1000.0; //test
    private final double POWERSHOT_AIM_DELAY = 1000.0;
    private final double PUT_DOWN_WOBBLE_DELAY = 1000.0; // optimize in testing
    private final double WOBBLE_FOLD_DELAY = 1000.0;
    private final double RESET_DROPDOWN_DELAY = 1000.0;

    //camera objects, assets, init methods
    private static final String TFOD_MODEL_ASSET = "UltimateGoal.tflite";
    private static final String LABEL_FIRST_ELEMENT = "Quad";
    private static final String LABEL_SECOND_ELEMENT = "Single";

    private static final String VUFORIA_KEY =
            "AWYX8QX/////AAABmQ8w8KJJuEsNlO9fxNmHDg1BoH/L5lzniFIDqLd+XlCF9gXWlYeddle27IIm9DH8mtLY2CLX9LW3uAzD8IH5Stmf+NoLjfm+m4jnj7KmR+v+xGuUEgP3Aj8sez5uhtsKarKiv94URMVnf39sjHW3xhiUBI30M762Ee6bEy69ZHQSOHLNxMwm9lnETo0O13vhmtZvI44HtEjIvXbW71p/+jdZw/33i6q//G4O3h5Ej+MQ3UCgUe9ERfh9L/v/lgLmekgYdFNaUZi8C1z+O4Jb/8MbHmpJ4Hu9XtA8pI2MLZMRNgOrnFwgXTukIhyHhJ2/2wVi6gwfyxzkJMU27jyGY/gLX9NtjwqhL4NaMWG6t/6m";

    public String stackLabel;

    private VuforiaLocalizer vuforia;

    private TFObjectDetector tfod;

    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = hardwareMap.get(WebcamName.class, "Webcam 1");

        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABEL_FIRST_ELEMENT, LABEL_SECOND_ELEMENT);
    }

    //double[] motorPowers = {0.0, 0.0};

    FollowerConstants startToLaunchLineFollowerConstants = new FollowerConstants(400, false);
    SwervePathGenerationConstants startToLaunchLinePathGenerationConstants = new SwervePathGenerationConstants(12, 0.7, 0.7, 230);
    SwervePath startToLaunchLinePath;

    FollowerConstants launchLineToWobbleZeroFollowerConstants = new FollowerConstants(400, true);
    SwervePathGenerationConstants launchLineToWobbleZeroPathGenerationConstants = new SwervePathGenerationConstants(12, 0.7, 0.7, 230);
    SwervePath launchLineToWobbleZeroPath;

    FollowerConstants launchLineToWobbleOneFollowerConstants = new FollowerConstants(400, true);
    SwervePathGenerationConstants launchLineToWobbleOnePathGenerationConstants = new SwervePathGenerationConstants(12, 0.7, 0.7, 230);
    SwervePath launchLineToWobbleOnePath;

    FollowerConstants launchLineToWobbleFourFollowerConstants = new FollowerConstants(400, true);
    SwervePathGenerationConstants launchLineToWobbleFourPathGenerationConstants = new SwervePathGenerationConstants(12, 0.7, 0.7, 230);
    SwervePath launchLineToWobbleFourPath;

    FollowerConstants wobbleFourToParkFollowerConstants = new FollowerConstants(400, true);
    SwervePathGenerationConstants wobbleFourToParkPathGenerationConstants = new SwervePathGenerationConstants(12, 0.7, 0.7, 230);
    SwervePath wobbleFourToParkPath;


    @Override
    public void init() {
        robot = new WHSRobotImpl(hardwareMap);
        robot.wobble.setClawPosition(Wobble.ClawPositions.CLOSE);
        robot.drivetrain.resetEncoders();
        defineStateEnabledStatus();

        startingCoordinateArray[RED] = new Coordinate(STARTING_COORDINATE_X, STARTING_COORDINATE_Y, 0);

        //all coordinates here are placeholders, change later
        scanningDistanceArray[RED][INSIDE] = new Position(1, -2);
        shootingPositionArray[RED] = new Position(STARTING_COORDINATE_X - 1425, STARTING_COORDINATE_Y);
        wobblePositionArray[STARTING_ALLIANCE][0] = new Position(-100, STARTING_COORDINATE_Y);
        wobblePositionArray[STARTING_ALLIANCE][1] = new Position(-400, STARTING_COORDINATE_Y);
        wobblePositionArray[STARTING_ALLIANCE][2] = new Position(-1200, STARTING_COORDINATE_Y);
        parkingPositionArray[RED][2] = new Position(-300, STARTING_COORDINATE_Y);
        ringPosition[RED] = new Position(13, -14);

        ArrayList<Position> startToLaunchLinePositions = new ArrayList<>();
        startToLaunchLinePositions.add(startingCoordinateArray[STARTING_ALLIANCE]);
        startToLaunchLinePositions.add(shootingPositionArray[STARTING_ALLIANCE]);
        startToLaunchLinePath = PathGenerator.generateSwervePath(startToLaunchLinePositions, startToLaunchLineFollowerConstants, startToLaunchLinePathGenerationConstants);

        ArrayList<Position> launchLineToWobbleZero = new ArrayList<>();
        launchLineToWobbleZero.add(shootingPositionArray[STARTING_ALLIANCE]);
        launchLineToWobbleZero.add(wobblePositionArray[STARTING_ALLIANCE][0]);
        launchLineToWobbleZeroPath = PathGenerator.generateSwervePath(launchLineToWobbleZero, launchLineToWobbleZeroFollowerConstants, launchLineToWobbleZeroPathGenerationConstants);

        ArrayList<Position> launchLineToWobbleOne = new ArrayList<>();
        launchLineToWobbleOne.add(shootingPositionArray[STARTING_ALLIANCE]);
        launchLineToWobbleOne.add(wobblePositionArray[STARTING_ALLIANCE][1]);
        launchLineToWobbleOnePath = PathGenerator.generateSwervePath(launchLineToWobbleOne, launchLineToWobbleOneFollowerConstants, launchLineToWobbleOnePathGenerationConstants);

        ArrayList<Position> launchLineToWobbleFour = new ArrayList<>();
        launchLineToWobbleFour.add(shootingPositionArray[STARTING_ALLIANCE]);
        launchLineToWobbleFour.add(wobblePositionArray[STARTING_ALLIANCE][2]);
        launchLineToWobbleFourPath = PathGenerator.generateSwervePath(launchLineToWobbleFour, launchLineToWobbleFourFollowerConstants, launchLineToWobbleFourPathGenerationConstants);

        ArrayList<Position> wobbleFourToPark = new ArrayList<>();
        wobbleFourToPark.add(wobblePositionArray[STARTING_ALLIANCE][2]);
        wobbleFourToPark.add(parkingPositionArray[STARTING_ALLIANCE][2]);
        wobbleFourToParkPath = PathGenerator.generateSwervePath(wobbleFourToPark, wobbleFourToParkFollowerConstants, wobbleFourToParkPathGenerationConstants);


        //instantiateSwerveToTargets();
        robot.setInitialCoordinate(startingCoordinateArray[STARTING_ALLIANCE]);

        /*
        // intit swerveToTargets
        startToShotline = PathGenerator.generateSwervePath(AutoSwervePositions.getPath(AutoSwervePositions.startToShotlinePath), startToShotlineFollowerConstants, startToShotlinePathGenConstants);
        shotLineToWobbleOne = PathGenerator.generateSwervePath(AutoSwervePositions.getPath(AutoSwervePositions.shotlineToWobble1Path), shotlineToWobbleOneFollowerConstants, shotlineToWobbleOneGenerationConstants);
        shotlineToWobbleTwo = PathGenerator.generateSwervePath(AutoSwervePositions.getPath(AutoSwervePositions.shotlineToWobble2Path), shotlineToWobbleTwoFollowerConstants, shotlineToWobbleTwoGenerationConstants);
        shotlineToWobbleThree = PathGenerator.generateSwervePath(AutoSwervePositions.getPath(AutoSwervePositions.shotlineToWobble3Path), shotlineToWobbleThreeFollowerConstants, shotlineToWobbleThreeGenerationConstants);
        wobbleOneToParkline = PathGenerator.generateSwervePath(AutoSwervePositions.getPath(AutoSwervePositions.wobble1ToParkPath), wobbleOneToParkFollowerConstants, wobbleOneToParkGenerationConstants);
        wobbleTwoToParkline = PathGenerator.generateSwervePath(AutoSwervePositions.getPath(AutoSwervePositions.wobble2ToParkPath), wobbleTwoToParkFollowerConstants, wobbleTwoToParkGenerationConstants);
        wobbleThreeToParkline = PathGenerator.generateSwervePath(AutoSwervePositions.getPath(AutoSwervePositions.wobble3ToParkPath), wobbleThreeToParkFollowerConstants, wobbleThreeToParkGenerationConstants);
        */


        initVuforia();
        initTfod();

        if (tfod != null) {
            tfod.activate();

            tfod.setZoom(2.0, 16.0 / 9.0);
        }
    }

    @Override
    public void init_loop() {
        FtcDashboard.getInstance().startCameraStream(vuforia, 0);
        robot.wobble.setClawPosition(Wobble.ClawPositions.CLOSE);
        List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
        if (updatedRecognitions != null) {
            stackLabel = "Lelouch Vi Britannia";
                                /*telemetry.addData("# Object Detected", updatedRecognitions.size());
                                int i = 0;*/
            for (Recognition recognition : updatedRecognitions) {
                                    /*telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                                    telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                            recognition.getLeft(), recognition.getTop());
                                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                            recognition.getRight(), recognition.getBottom());
                                    telemetry.update();*/
                stackLabel = recognition.getLabel();
            }
        }
        telemetry.addData("Height", stackLabel);
      /*  if (robot.wobbleHeightDetector.getScreenPosition().x < LEFT_MAX) {
            autoOpRingPosition = 2;
        } else if (robot.wobbleHeightDetector.getScreenPosition().x < CENTER_MAX) {
            autoOpRingPosition = 1;
        } else {
            autoOpRingPosition = 0;
        }*/
    }

    public String stateDesc = "";
    public String subStateDesc = "";

    @Override
    public void loop() {
        robot.estimateHeading();
        robot.estimatePosition();

       /* switch (newOuttakeState) {
            case "hover":
                robot.wobble.setArmPosition(Wobble.ArmPositions.UP);
                robot.wobble.setClawPosition(Wobble.ClawPositions.OPEN);
                break;
            case "grab":
                robot.wobble.setArmPosition(Wobble.ArmPositions.OVER);
                robot.wobble.setClawPosition(Wobble.ClawPositions.CLOSE);
                robot.wobble.setArmPosition(Wobble.ArmPositions.UP);
                break;
            case "outtake1":
                //robot.rotateToTarget(robot.outtake.calculateLaunchHeading(robot.outtake.powershot1, robot.getCoordinate()), false);
                robot.newouttake.setFlapServoPositions(Outtake.GoalPositions.POWER_SHOT_TARGET_ONE);
                //robot.outtake.autoOuttake(1);
                break;
            case "outtake2":
                //robot.outtake.autoOuttake(2);
                break;
            default:
                break;
        }*/

        switch (state) {
            case INIT:
                stateDesc = "Starting auto";
                advanceState();
                break;
            case SCAN_STACK:
                stateDesc = "Scan Stack + Drop Intake";
                robot.wobble.setClawPosition(Wobble.ClawPositions.CLOSE);
                robot.wobble.setArmRotatorPositions(Wobble.ArmRotatorPositions.IN);
                switch (subState) {
                    case 0:
                        subStateDesc = "Scan Stack";
                        /*if (tfod != null) {
                            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
                            if (updatedRecognitions != null) {
                                stackLabel = "Zero";
                                *//*telemetry.addData("# Object Detected", updatedRecognitions.size());
                                int i = 0;*//*
                                for (Recognition recognition : updatedRecognitions) {
                                    *//*telemetry.addData(String.format("label (%d)", i), recognition.getLabel());
                                    telemetry.addData(String.format("  left,top (%d)", i), "%.03f , %.03f",
                                            recognition.getLeft(), recognition.getTop());
                                    telemetry.addData(String.format("  right,bottom (%d)", i), "%.03f , %.03f",
                                            recognition.getRight(), recognition.getBottom());
                                    telemetry.update();*//*
                                    stackLabel = recognition.getLabel();
                                }
                            }
                        }*/
                        robot.updatePath(startToLaunchLinePath);
                        subState++;
                        break;
                    case 1:
                        subStateDesc = "Driving to Launch Point";
                        robot.swerveToTarget();
                        if (!robot.swerveInProgress()) {
                            subState++;
                        }
                        break;
                    case 2:
                        subStateDesc = "Rotating to Launch Heading";
                        robot.rotateToTarget(9, true);
                        robot.intake.autoDropIntake();
                        if (!robot.rotateToTargetInProgress()) {
                            robot.intake.setDropdown(Intake.DropdownPositions.UP);
                            advanceState();
                        }
                        break;
                    default:
                        break;
                }
                break;
            case LAUNCH_PARTICLES:
                stateDesc = "Ready to Launch";
                robot.intake.setDropdown(Intake.DropdownPositions.UP);
                robot.wobble.setClawPosition(Wobble.ClawPositions.CLOSE);
                robot.wobble.setArmRotatorPositions(Wobble.ArmRotatorPositions.IN);
                robot.autoShootHighGoal();
                if (!robot.shootingInProgress()) {
                    advanceState();
                }
                break;
            case DROP_OFF_WOBBLE_GOAL:
                stateDesc = "Wobble goal";
                switch (subState) {
                    case 0: // Check to make sure ring amount and swerve paths match
                        robot.wobble.setClawPosition(Wobble.ClawPositions.CLOSE);
                        robot.wobble.setArmRotatorPositions(Wobble.ArmRotatorPositions.IN);
                        subStateDesc = "Move to Wobble Box";
                        tfod.getRecognitions();
                        if (stackLabel == "Quad") {
                            robot.updatePath(launchLineToWobbleFourPath);
                            robot.swerveToTarget();
                        } else if (stackLabel == "Single") {
                            robot.updatePath(launchLineToWobbleOnePath);
                            robot.swerveToTarget();
                        } else {
                            robot.updatePath(launchLineToWobbleZeroPath);
                            robot.swerveToTarget();
                        }
                        if (!robot.swerveInProgress() && !robot.rotateToTargetInProgress() && !robot.driveToTargetInProgress()) {
                            subState++;
                        }
                        break;
                    case 1:
                        robot.wobble.setClawPosition(Wobble.ClawPositions.CLOSE);
                        robot.wobble.setArmRotatorPositions(Wobble.ArmRotatorPositions.IN);
                        subStateDesc = "Rotate to Wobble Box (Part 2)";
                        if (stackLabel == "Single") {
                            robot.rotateToTarget(-100, true);
                        } else if (stackLabel == "Quad") {
                            robot.rotateToTarget(-30, false);
                        } else {
                            robot.rotateToTarget(145, true);
                        }
                        if (!robot.swerveInProgress() && !robot.rotateToTargetInProgress() && !robot.driveToTargetInProgress()) {
                            subState++;
                        }
                        break;
                    case 2:
                        robot.wobble.autoDropWobble();
                        if(robot.wobble.getDroppedState()){
                            advanceState();
                        }
                   /* case 1:
                        subStateDesc = "Set Wobble Put Down Timer";
                        putDownWobble.set(PUT_DOWN_WOBBLE_DELAY);
                    case 2:
                        subStateDesc = "Lower Arm and Release";
                        if (!putDownWobble.isExpired()) {
                            robot.wobble.setArmRotatorPositions(Wobble.ArmRotatorPositions.OUT);
                            robot.wobble.setClawPosition(Wobble.ClawPositions.OPEN);
                        }
                        subState++;
                        break;
                    case 3:
                        subStateDesc = "Set Wobble Fold Timer";
                        wobbleFoldTimer.set(WOBBLE_FOLD_DELAY);
                        subState++;
                        break;
                    case 4:
                        subStateDesc = "Fold Wobble";
                        if (!wobbleFoldTimer.isExpired()) {
                            robot.wobble.setArmRotatorPositions(Wobble.ArmRotatorPositions.IN);
                            robot.wobble.setClawPosition(Wobble.ClawPositions.CLOSE);
                            robot.wobble.setLinearSlidePosition(Wobble.LinearSlidePositions.DOWN);
                        }
                        subState++;
                        break;*/
                    default:
                        break;
                        /*case "score":
                        robot.wobble.setArmPosition(Wobble.ArmPositions.OVER);
                        robot.wobble.setClawPosition(Wobble.ClawPositions.OPEN);
                        break;*/
                }
                break;

            case PARK_ON_STARTING_LINE:
                stateDesc = "Park";
                if (stackLabel == "Quad") {
                    robot.updatePath(wobbleFourToParkPath);
                } else {
                    advanceState();
                }
                robot.swerveToTarget();
                // make swerve to target
               /* stateDesc = "Driving to foundation";
                switch (subState) {
                    case 0:
                        subStateDesc = "Entry";
                        subState++;
                        break;
                    case 1:
                        subStateDesc = "Driving to foundation";
                        if (stateEnabled[SECONDARY_MOVE_FOUNDATION]) {
                            motorPowers = skystoneToUnmovedFoundationSwerve.calculateMotorPowers(robot.getCoordinate(), robot.drivetrain.getWheelVelocities(), true);
                        } else {
                            motorPowers = skystoneToMovedFoundationSwerve.calculateMotorPowers(robot.getCoordinate(), robot.drivetrain.getWheelVelocities(), STARTING_ALLIANCE == BLUE);
                        }
                        if (robot.getCoordinate().getX() > 300) {
                            outtakeState = "outtake1";
                        }
                        if (!skystoneToUnmovedFoundationSwerve.inProgress() && !skystoneToMovedFoundationSwerve.inProgress()) {
                            subState++;
                        }
                        break;
                    case 2:
                        subStateDesc = "Exit";
                        advanceState();
                        break;
                }
                break;
            case OUTTAKE_SKYSTONE:
                stateDesc = "Outtaking skystone";
                switch (subState) {
                    case 0:
                        subStateDesc = "Entry";
                        foundationPullerUpToDownTimer.set(GRAB_FOUNDATION_DELAY);
                        subState++;
                        break;
                    case 1:
                        subStateDesc = "Moving foundation pullers";
                        robot.foundationPuller.setFoundationPullerPosition(FoundationPuller.PullerPosition.DOWN);
                        operatingFoundationPullers = true;
                        if (foundationPullerUpToDownTimer.isExpired()) {
                            subState++;
                        }
                        break;
                    case 2:
                        subStateDesc = "Outtaking skystone and pulling foundation";
                        robot.intake.setVelocity(-Intake.INTAKE_VELOCITY);
                        motorPowers = foundationToWallSwerve.calculateMotorPowers(robot.getCoordinate(), robot.drivetrain.getWheelVelocities(), false);
                        if (!foundationToWallSwerve.inProgress()) {
                            robot.intake.setVelocity(0);
                            subState++;
                        }
                        break;
                    case 3:
                        subStateDesc = "rotating";
                        if (STARTING_ALLIANCE == BLUE){
                            robot.rotateToTarget(-160,false);
                        }else if (STARTING_ALLIANCE == RED){
                            robot.rotateToTarget(160, false);
                        }
                        if (!robot.rotateToTargetInProgress()){
                            robot.foundationPuller.setFoundationPullerPosition(FoundationPuller.PullerPosition.UP);
                        }
                        if (!robot.rotateToTargetInProgress()){
                            subState++;
                        }
                        break;
                    case 4:
                        subStateDesc = "Exit";
                        advanceState();
                        break;
                }*/
                if (!robot.swerveInProgress()) {
                }
                break;
            case END:
                break;

            default:
                break;
        }
        telemetry.addData("State: ", stateDesc);
        telemetry.addData("Substate: ", subStateDesc);
        telemetry.addData("IMU", robot.imu.getHeading());

        telemetry.addData("Stack Height", stackLabel);

        //telemetry.addData("Stone Sensed?", robot.intake.stoneSensed());
        telemetry.addData("X", robot.getCoordinate().getX());
        telemetry.addData("Y", robot.getCoordinate().getY());
        telemetry.addData("Heading", robot.getCoordinate().getHeading());
        /*Telemetry.Item stone_position_x = telemetry.addData("Stone Position X", robot.skystoneDetector.getScreenPosition().x);
        telemetry.addData("Stone Position Y", robot.skystoneDetector.getScreenPosition().y);
        telemetry.addData("Frame Count", robot.webcam.getFrameCount());
        telemetry.addData("FPS", String.format(Locale.US, "%.2f", robot.webcam.getFps()));
        telemetry.addData("Total frame time ms", robot.webcam.getTotalFrameTimeMs());
        telemetry.addData("Pipeline time ms", robot.webcam.getPipelineTimeMs());
        telemetry.addData("Overhead time ms", robot.webcam.getOverheadTimeMs());
        telemetry.addData("Theoretical max FPS", robot.webcam.getCurrentPipelineMaxFps());
        telemetry.addData("Skystone Position", skystonePosition);
        telemetry.addData("Auto Ring Position: ", autoOpRingPosition);*/
    }

    @Override
    public void stop() {
        //robot.intake.setIntakePusherPosition(Intake.IntakePusherPosition.DOWN);
    }
}

