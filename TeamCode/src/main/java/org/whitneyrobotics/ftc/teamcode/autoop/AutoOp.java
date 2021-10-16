package org.whitneyrobotics.ftc.teamcode.autoop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;
import org.whitneyrobotics.ftc.teamcode.subsys.WHSRobotImpl;

public class AutoOp extends OpMode {

    public WHSRobotImpl robot;

    static final int RED = 0;
    static final int BLUE = 1;
    static final int BOTTOM = 0;
    static final int TOP = 1;

    final int STARTING_ALLIANCE = RED;
    final int STARTING_SIDE = BOTTOM;

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

    public void advanceState(){
        if (stateEnabled[state + 1]){
            subState = 0;
            state++;
        } else {
            state++;
            advanceState();
        }
    }

    public void init() {
        robot = new WHSRobotImpl(hardwareMap);
        robot.robotDrivetrain.resetEncoders();
        // add outtake reset
        defineStatesEnabled();

        // figure out actual values for this
        startingPositions[RED][BOTTOM] = new Position(0,0);
        startingPositions[RED][TOP] = new Position(1,1);
        startingPositions[BLUE][BOTTOM] = new Position(2,2);
        startingPositions[BLUE][TOP] = new Position(3,3);

        shippingHubPosition[RED] = new Position(4,4);
        shippingHubPosition[BLUE] = new Position(5,5);

        sharedShippingHub[RED] = new Position(6, 6);
        sharedShippingHub[BLUE] = new Position(7, 7);

        finalParkingPosition[RED][TOP] = new Position(8,8);
        finalParkingPosition[RED][BOTTOM] = new Position(9,9);
        finalParkingPosition[BLUE][TOP] = new Position(10,10);
        finalParkingPosition[BLUE][BOTTOM] = new Position(11, 11);

        carouselPositions[RED] = new Position(12,12);
        carouselPositions[BLUE] = new Position(13, 13);

    }

    public void loop() {
        switch (state){
            case INIT:
                switch (subState){
                    case 0:
                        //add camera code later
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
                            subState++;
                            advanceState();
                        }
                        break;
                }
            case SHIPPING_HUB:
            case WAREHOUSE:
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
    }
}
