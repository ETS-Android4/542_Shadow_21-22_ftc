package org.whitneyrobotics.ftc.teamcode.autoop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.sun.tools.javac.util.List;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;
import org.whitneyrobotics.ftc.teamcode.subsys.WHSRobotImpl;

@Autonomous (name="WHS Freight Frenzy Auto")
public class AutoOp extends OpMode {

    public WHSRobotImpl robot;

    static final int RED = 0;
    static final int BLUE = 1;
    static final int BOTTOM = 0;
    static final int TOP = 1;

    // values for our array positions
    static final int TESTED_LEFT = 0;
    static final int TESTED_TOP = 1;
    static final int TESTED_RIGHT = 2;
    static final int TESTED_BOTTOM = 3;

    public float CAMERA_LEFT;
    public float CAMERA_TOP;
    public float CAMERA_RIGHT;
    public float CAMERA_BOTTOM;

    public final float ERROR_MARGIN = 15;

    final int STARTING_ALLIANCE = RED;
    final int STARTING_SIDE = BOTTOM;

    private int scanLevel = 3;

    Position[][] startingPositions = new Position[2][2];
    Position[] carouselPositions = new Position[2];
    Position[] shippingHubPosition = new Position[2];
    Position[] sharedShippingHub = new Position[2];
    Position[] warehouse = new Position[2];
    Position[][] finalParkingPosition = new Position[2][2];

    static final int INIT = 0;
    static final int ROTATE_CAROUSEL = 1;
    static final int SHIPPING_HUB = 2;
    static final int WAREHOUSE = 3;
    static final int PARK = 4;

    static final int NUMBER_OF_STATES = 5;

    int state = INIT;
    int subState = 0;

    boolean[] stateEnabled = new boolean[NUMBER_OF_STATES];

    public void defineStatesEnabled(){
        stateEnabled[INIT] = true;
        stateEnabled[ROTATE_CAROUSEL] = true;
        stateEnabled[SHIPPING_HUB] = false;
        stateEnabled[WAREHOUSE] = false;
        stateEnabled[PARK] = true;
    }

    public String[] stateNames = {"Init", "Rotate Carousel", "Shipping Hub", "Warehouse", "Park"};

    public float[][] barcodeLocation;

    public void advanceState(){
        if (stateEnabled[state + 1]){
            subState = 0;
            state++;
        } else {
            state++;
            advanceState();
        }
    }

    public long lastRecordedTime = System.nanoTime();

    // Camera INIT Methods
    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";

    private static final String[] LABELS = {
            "Ball",
            "Cube",
            "Duck",
            "Marker"
    };

    private static final String VUFORIA_KEY =
            "AWYX8QX/////AAABmQ8w8KJJuEsNlO9fxNmHDg1BoH/L5lzniFIDqLd+XlCF9gXWlYeddle27IIm9DH8mtLY2CLX9LW3uAzD8IH5Stmf+NoLjfm+m4jnj7KmR+v+xGuUEgP3Aj8sez5uhtsKarKiv94URMVnf39sjHW3xhiUBI30M762Ee6bEy69ZHQSOHLNxMwm9lnETo0O13vhmtZvI44HtEjIvXbW71p/+jdZw/33i6q//G4O3h5Ej+MQ3UCgUe9ERfh9L/v/lgLmekgYdFNaUZi8C1z+O4Jb/8MbHmpJ4Hu9XtA8pI2MLZMRNgOrnFwgXTukIhyHhJ2/2wVi6gwfyxzkJMU27jyGY/gLX9NtjwqhL4NaMWG6t/6m";

    private VuforiaLocalizer vuforia;

    private TFObjectDetector tfod;

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier(
                "tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);
        tfod.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }

    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    @Override
    public void init() {
        robot = new WHSRobotImpl(hardwareMap);
        robot.robotDrivetrain.resetEncoders();
        // add outtake reset
        defineStatesEnabled();

        // figure out actual values for this
        startingPositions[RED][BOTTOM] = new Position(-1647.6,900);
        startingPositions[RED][TOP] = new Position(-1647.6,-150);
        startingPositions[BLUE][BOTTOM] = new Position(-1647.6,-900);
        startingPositions[BLUE][TOP] = new Position(-1647.6,150);

        shippingHubPosition[RED] = new Position(-752.4,452.4);
        shippingHubPosition[BLUE] = new Position(-752.4,-452.4);

        sharedShippingHub[RED] = new Position(-152.4, -1200);
        sharedShippingHub[BLUE] = new Position(-152.4, 1200);

        warehouse[RED] = new Position(-1500,-1122.6);
        warehouse[BLUE] = new Position(-1500,1122.6);

        finalParkingPosition[RED][TOP] = new Position(-1500,-1122.6);
        finalParkingPosition[RED][BOTTOM] = new Position(-900,1500);
        finalParkingPosition[BLUE][TOP] = new Position(-1500,1122.6);
        finalParkingPosition[BLUE][BOTTOM] = new Position(-900, -1500);

        carouselPositions[RED] = new Position(-1400,1400);
        carouselPositions[BLUE] = new Position(-1400, -1400);

        // INIT Camera
        initVuforia();
        initTfod();
        if (tfod != null) {
            tfod.activate();
            tfod.setZoom(2.5, 16.0/9.0);
        }

        // Camera Barcode Object Locations (Based on Red) TEST FOR THESE
        // SCAN LEVEL 0
        barcodeLocation[0][TESTED_LEFT] = 0; // LEFT
        barcodeLocation[0][TESTED_TOP] = 1; // TOP
        barcodeLocation[0][TESTED_RIGHT] = 2; // RIGHT
        barcodeLocation[0][TESTED_BOTTOM] = 3; // BOTTOM

        // SCAN LEVEL 1
        barcodeLocation[1][TESTED_LEFT] = 0; // LEFT
        barcodeLocation[1][TESTED_TOP] = 1; // TOP
        barcodeLocation[1][TESTED_RIGHT] = 2; // RIGHT
        barcodeLocation[1][TESTED_BOTTOM] = 3; // BOTTOM

        // SCAN LEVEL 2
        barcodeLocation[2][TESTED_LEFT] = 0; // LEFT
        barcodeLocation[2][TESTED_TOP] = 1; // TOP
        barcodeLocation[2][TESTED_RIGHT] = 2; // RIGHT
        barcodeLocation[2][TESTED_BOTTOM] = 3; // BOTTOM
    }

    @Override
    public void init_loop() {
        if (tfod != null) {
            List<Recognition> updatedRecognitions = tfod.getUpdatedRecognitions();
            if (updatedRecognitions != null) {
                telemetry.addData("# Object Detected", updatedRecognitions.size());
                int i = 0;
                for (Recognition recognition : updatedRecognitions) {
                    if (recognition.getLabel() == "Cube"){
                        CAMERA_BOTTOM = recognition.getBottom();
                        CAMERA_LEFT = recognition.getLeft();
                        CAMERA_TOP = recognition.getTop();
                        CAMERA_RIGHT = recognition.getRight();
                    }
                    i++;
                }
                telemetry.update();
            }
        }
    }

    @Override
    public void loop() {
        switch (state){
            case INIT:
                switch (subState){
                    case 0:
                        if ((((barcodeLocation[0][TESTED_LEFT] - ERROR_MARGIN) <= CAMERA_LEFT) && ((barcodeLocation[0][TESTED_LEFT] + ERROR_MARGIN) >= CAMERA_LEFT)) && (((barcodeLocation[0][TESTED_RIGHT] - ERROR_MARGIN) <= CAMERA_RIGHT) && ((barcodeLocation[0][TESTED_RIGHT] + ERROR_MARGIN) >= CAMERA_RIGHT))){
                            scanLevel = 1;
                        } else if ((((barcodeLocation[1][TESTED_LEFT] - ERROR_MARGIN) <= CAMERA_LEFT) && ((barcodeLocation[1][TESTED_LEFT] + ERROR_MARGIN) >= CAMERA_LEFT)) && (((barcodeLocation[1][TESTED_RIGHT] - ERROR_MARGIN) <= CAMERA_RIGHT) && ((barcodeLocation[1][TESTED_RIGHT] + ERROR_MARGIN) >= CAMERA_RIGHT))){
                            scanLevel = 2;
                        } else if ((((barcodeLocation[2][TESTED_LEFT] - ERROR_MARGIN) <= CAMERA_LEFT) && ((barcodeLocation[2][TESTED_LEFT] + ERROR_MARGIN) >= CAMERA_LEFT)) && (((barcodeLocation[2][TESTED_RIGHT] - ERROR_MARGIN) <= CAMERA_RIGHT) && ((barcodeLocation[2][TESTED_RIGHT] + ERROR_MARGIN) >= CAMERA_RIGHT))){
                            scanLevel = 3;
                        } else {
                            scanLevel = 1;
                        }
                        subState++;
                        break;
                    case 1:
                        robot.robotDrivetrain.resetEncoders();
                        advanceState();
                        break;
                }
            case ROTATE_CAROUSEL:
                switch (subState){
                    case 0:
                        robot.driveToTarget(carouselPositions[STARTING_ALLIANCE], false);
                        if (!robot.driveToTargetInProgress()){
                            subState++;
                        }
                        break;
                    case 1:
                        robot.robotCarousel.operate();
                        if (!robot.robotCarousel.rotateInProgress()){
                            advanceState();
                            subState++;
                        }
                        break;
                }
            case SHIPPING_HUB:
                switch (subState){
                    case 0:
                        robot.driveToTarget(sharedShippingHub[STARTING_ALLIANCE],true); //check if outtake is on the back
                        if(!robot.driveToTargetInProgress()){
                            subState++;
                        }
                        break;
                    case 1:
                        robot.robotOuttake.autoControl(scanLevel);
                        if(robot.robotOuttake.slidingInProgress){
                            if(robot.robotOuttake.autoDrop()){ subState++; }
                            break;
                        }
                    case 2:
                        robot.robotOuttake.reset();
                        advanceState();
                        break;
                }
            case WAREHOUSE:
                switch (subState){
                    case 0:
                        robot.driveToTarget(warehouse[STARTING_ALLIANCE],false);
                        if (!robot.driveToTargetInProgress()) { subState++; }
                        break;
                    case 1:
                        robot.robotIntake.autoDropIntake();
                        if (robot.robotIntake.intakeAutoDone){
                            subState++;
                        }
                        break;
                    case 2:
                        robot.driveToTarget(sharedShippingHub[STARTING_ALLIANCE], true);
                        if (!robot.driveToTargetInProgress()) { subState++; }
                        break;
                    case 3:
                        robot.robotOuttake.autoControl(1);
                        if (robot.robotOuttake.autoDrop()) { subState++; }
                        break;
                    case 4:
                        robot.robotOuttake.reset();
                        advanceState();
                        break;

                }
            case PARK:
                switch (subState){
                    case 0:
                        robot.driveToTarget(finalParkingPosition[STARTING_ALLIANCE][STARTING_SIDE], false);
                        if (!robot.driveToTargetInProgress()){
                            subState++;
                        }
                        break;
                }
            default:
                break;
        }
        telemetry.addData("Current state: ",stateNames[state]);
        telemetry.addData("Substate: ", subState);
        telemetry.addData("Drive to target:", robot.driveToTargetInProgress());
        telemetry.addData("Rotate to target:", robot.rotateToTargetInProgress());
        telemetry.addData("Outtake extension: ", robot.robotOuttake.slidingInProgress);
        telemetry.addData("Intaking item from warehouse: ", robot.robotIntake.intakeAutoDone);

        //lag output
        telemetry.addData("Current processing latency: ", (lastRecordedTime-System.nanoTime())*1000 + "ms");
        lastRecordedTime = System.nanoTime();
    }
}
