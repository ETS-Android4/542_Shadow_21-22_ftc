package org.whitneyrobotics.ftc.teamcode.ShadowAuto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.whitneyrobotics.ftc.teamcode.subsys.Carousel;
import org.whitneyrobotics.ftc.teamcode.subsys.WHSRobotImpl;
import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;

@Autonomous(name = "TFAutoOP")
public class TFAuto extends OpMode {

    public WHSRobotImpl Robot;

    private static final String VUFORIA_KEY = "AWYX8QX/////AAABmQ8w8KJJuEsNlO9fxNmHDg1BoH/L5lzniFIDqLd+XlCF9gXWlYeddle27IIm9DH8mtLY2CLX9LW3uAzD8IH5Stmf+NoLjfm+m4jnj7KmR+v+xGuUEgP3Aj8sez5uhtsKarKiv94URMVnf39sjHW3xhiUBI30M762Ee6bEy69ZHQSOHLNxMwm9lnETo0O13vhmtZvI44HtEjIvXbW71p/+jdZw/33i6q//G4O3h5Ej+MQ3UCgUe9ERfh9L/v/lgLmekgYdFNaUZi8C1z+O4Jb/8MbHmpJ4Hu9XtA8pI2MLZMRNgOrnFwgXTukIhyHhJ2/2wVi6gwfyxzkJMU27jyGY/gLX9NtjwqhL4NaMWG6t/6m";
    private static final String TFOD_MODEL_ASSET = "FreightFrenzy_BCDM.tflite";
    private static final String[] LABELS = {
            "Ball",
            "Cube",
            "Duck",
            "Marker"
    };

    static final int RED = 0;
    static final int BLUE = 1;
    static final int TOP = 0;
    static final int BOT = 1;

    static int SELF_TEAM = RED; // Change both on initialize
    static int SELF_SIDE = BOT;

    public int CurrentState = 0;
    static final int INIT = 0;
    static final int SHIPPINGHUB = 1;
    static final int CAROUSEL = 2;
    static final int WAREHOUSE = 3;

    public float CAMERA_LEFT;
    public float CAMERA_CENTER;
    public float CAMERA_RIGHT;
    public float CAMERA_PIXEL_OFFSET;
    public final float ScreenWidth = 500; // test value idk real one

    Position[][] ShippingHub = new Position[2][3];
    Position[] WarehousePark = new Position[2];
    Position[] CarosuelApp = new Position[2];

    public boolean ScanningBarcode = false;
    public int BarcodeLevel = 0;
    public final int BarcodePixelOffset = 200;

    private VuforiaLocalizer Vuforia;
    private TFObjectDetector TFOD;

    private void initVuforia() {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        Vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    private void initTfod() {
        int tfodMonitorViewId = hardwareMap.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(tfodMonitorViewId);
        tfodParameters.minResultConfidence = 0.8f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 320;
        TFOD = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, Vuforia);
        TFOD.loadModelFromAsset(TFOD_MODEL_ASSET, LABELS);
    }

    @Override
    public void init() {

        Robot = new WHSRobotImpl(hardwareMap);
        initVuforia();
        initTfod();
        if (TFOD != null) {
            TFOD.activate();
            TFOD.setZoom(2.5, 16.0 / 9.0);
        }

        // Set positions after testing


    }

    public int UpdateBarcode() {
        if (TFOD != null) {
            List<Recognition> UpdatedRecognitions = TFOD.getUpdatedRecognitions();
            if (UpdatedRecognitions != null) {
                for (Recognition recognition : UpdatedRecognitions) {
                    if (recognition.getLabel().equals("Duck") || recognition.getLabel().equals("Cube")) {

                        CAMERA_LEFT = recognition.getLeft();
                        CAMERA_RIGHT = recognition.getRight();
                        CAMERA_CENTER = (CAMERA_LEFT + CAMERA_RIGHT) / 2;
                        CAMERA_PIXEL_OFFSET = CAMERA_CENTER - ScreenWidth / 2;

                        // Robot must be centered and in front of barcodes for precision
                        if (CAMERA_PIXEL_OFFSET < -BarcodePixelOffset) {
                            BarcodeLevel = 1;
                        } else if (CAMERA_PIXEL_OFFSET > -BarcodePixelOffset && CAMERA_PIXEL_OFFSET < BarcodePixelOffset) {
                            BarcodeLevel = 2;
                        } else {
                            BarcodeLevel = 3;
                        }
                    }
                }
            }
        }
        if (BarcodeLevel == 0 && ScanningBarcode) { // if couldn't find game object then recurse
            BarcodeLevel = UpdateBarcode();
        }
        return BarcodeLevel;
    }

    @Override
    public void loop() {
        switch(CurrentState) {
            case INIT:
                ScanningBarcode = true;
                UpdateBarcode();
                CurrentState++;
                break;
            case SHIPPINGHUB:
                Robot.driveToTarget(ShippingHub[SELF_TEAM][SELF_SIDE], false);
                if (!Robot.driveToTargetInProgress()) {
                    Robot.outtake.operateWithoutGamepad(BarcodeLevel);
                    if (Robot.outtake.autoDrop()) {
                        CurrentState++;
                    }
                }
                break;
            case CAROUSEL: // idk if this works or if its even right. im probably doing something wrong
                Robot.driveToTarget(CarosuelApp[SELF_TEAM], false); // Possibly change to swerveToTarget
                if (!Robot.driveToTargetInProgress()) {
                    Robot.carousel.operateAuto(SELF_TEAM == BLUE);
                }
                CurrentState++;
                break;
            case WAREHOUSE:

                break;
            default:

        }
    }

}
