package org.whitneyrobotics.ftc.teamcode.ShadowAuto;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.whitneyrobotics.ftc.teamcode.subsys.WHSRobotImpl;

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

    public Dictionary StateContainer = new Hashtable();
    public int CurrentState = 0;

    public float CAMERA_LEFT;
    public float CAMERA_CENTER;
    public float CAMERA_RIGHT;
    public float CAMERA_PIXEL_OFFSET;

    public final float ScreenWidth = 500; // test value idk real one

    public int BarcodeLevel;
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

    public void initStates() {
        StateContainer.put(0, "Init");
        StateContainer.put(1, "Carousel");
        StateContainer.put(2, "Shipping Hub");
        StateContainer.put(3, "Freight Deliver");
        StateContainer.put(4, "Park");
    }

    @Override
    public void init() {
        Robot = new WHSRobotImpl(hardwareMap);
        //Robot.robotDrivetrain.resetEncoders();
        initStates();
        initVuforia();
        initTfod();
        if (TFOD != null) {
            TFOD.activate();
            TFOD.setZoom(2.5, 16.0 / 9.0);
        }
    }

    public void init_loop() {
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
    }

    @Override
    public void loop() {
        switch(CurrentState) {
            case 0:

                break;

            case 1:

                break;

            case 2:

                break;

            case 3:

                break;

            case 4:

                break;

            default:
        }
    }

}
