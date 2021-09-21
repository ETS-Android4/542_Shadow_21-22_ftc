package org.whitneyrobotics.ftc.teamcode.autoop;

import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;
import org.whitneyrobotics.ftc.teamcode.lib.purepursuit.FollowerConstants;
import org.whitneyrobotics.ftc.teamcode.lib.purepursuit.PathGenerator;
import org.whitneyrobotics.ftc.teamcode.lib.purepursuit.swervetotarget.SwervePath;
import org.whitneyrobotics.ftc.teamcode.lib.purepursuit.swervetotarget.SwervePathGenerationConstants;

import java.util.ArrayList;

import static org.whitneyrobotics.ftc.teamcode.lib.purepursuit.PathGenerator.generateSwervePath;

public class AutoSwervePositions {

    // declare positions here put spaces between each path you are generating for and label which path it is for
    // test path
    public final static Position p1Test = new Position(1,2);
    public final static Position p2Test = new Position(3,4);
    //Start to Shotline
    public final static Position p1StartToShotline = new Position(-1500, -600);
    public final static Position p2StartToShotLine = new Position(-600, 0);
    public final static Position p3StartToShotLine = new Position(0, -300);
    //shotlineToWobble1
    public final static Position p1ShotlineToWobble1 = new Position(0, -300);
    public final static Position p2ShotlineToWobble1 = new Position(300, -1500);
    //shotlineToWobble2
    public final static Position p1ShotlineToWobble2 = new Position(0, -300);
    public final static Position p2ShotlineToWobble2 = new Position(900, -900);
    //ShotlineToWobble3
    public final static Position p1ShotlineToWobble3 = new Position(0, -300);
    public final static Position p2ShotlineToWobble3 = new Position(1500, -1500);
    //Wobble1ToParking
    public final static Position p1Wobble1ToParking = new Position(300, -1500);
    public final static Position p2Wobble1ToParking = new Position(300, -1500);
    //Wobble2ToParking
    public final static Position p1Wobble2ToParking = new Position(900, -900);
    public final static Position p2Wobble2ToParking = new Position(300, -900);
    //Wobble3ToParking
    public final static Position p1Wobble3ToParking = new Position(1500, -1500);
    public final static Position p2Wobble3ToParking = new Position(300, -1500);


    // swerve Lookahed Distances
    public final static double startToShotlineLookaheadDist = 350; // in mm
    public final static double shotlineToWobble1LookaheadDist = 350; //in mm
    public final static double shotlineToWobble2LookahadDist = 350;
    public final static double shotlineToWobble3LookaheadDist = 350;
    public final static double wobble1ToParkLookaheadDist = 350;
    public final static double wobble2ToParkLookaheadDist = 350;
    public final static double wobble3ToParkLookaheadDist = 350;

    // swerve spacing
    public final static double startToShotlineWaypointSpacing = 80; // in mm
    public final static double shotlineToWobble1Spacing = 80;
    public final static double shotlineToWobble2Spacing = 80;
    public final static double shotlineToWobble3Spacing = 80;
    public final static double wobble1ToParkSpacing = 80;
    public final static double wobble2ToParkSpacing = 80;
    public final static double wobble3ToParkSpacing = 80;

    // swerve weight smooth
    public final static double startToShotlineWeightSmooth = 0.5;
    public final static double shotlineToWobble1WeightSmooth = 0.5;
    public final static double shotlineToWobble2WeightSmooth = 0.5;
    public final static double shotlineToWobble3WeightSmooth = 0.5;
    public final static double wobble1ToParkWeightSmooth = 0.5;
    public final static double wobble2ToParkWeightSmooth = 0.5;
    public final static double wobble3ToParkWeightSmooth = 0.5;

    // swerve turn speed
    public final static double startToShotlineTurnSpeed = 3;
    public final static double shotlineToWobble1TurnSpeed = 3;
    public final static double shotlineToWobble2TurnSpeed = 3;
    public final static double shotlineToWobble3TurnSpeed = 3;
    public final static double wobble1ToParkTurnSpeed = 3;
    public final static double wobble2ToParkTurnSpeed = 3;
    public final static double wobble3ToParkTurnSpeed = 3;

    // swerve max velocity
    public final static double startToShotlineMaxVelocity = 750; // 1 - 5
    public final static double shotlineToWobble1MaxVelocity = 750;
    public final static double shotlineToWobble2MaxVelocity = 750;
    public final static double shotlineToWobble3MaxVelocity = 750;
    public final static double wobble1ToParkMaxVelocity = 750;
    public final static double wobble2ToParkMaxVelocity = 750;
    public final static double wobble3ToParkMaxVelocity = 750;

    //make  ArrayLists here Call these in getPath
    public static ArrayList<Position> testPath = new ArrayList<Position>();
    public static ArrayList <Position> startToShotlinePath = new ArrayList<Position>();
    public static ArrayList <Position> shotlineToWobble1Path = new ArrayList<Position>();
    public static ArrayList <Position> shotlineToWobble2Path = new ArrayList<Position>();
    public static ArrayList <Position> shotlineToWobble3Path = new ArrayList<Position>();
    public static ArrayList <Position> wobble1ToParkPath = new ArrayList<Position>();
    public static ArrayList <Position> wobble2ToParkPath = new ArrayList<Position>();
    public static ArrayList <Position> wobble3ToParkPath = new ArrayList<Position>();

    // Initialize FollowerConstants
    static FollowerConstants startToShotlineFollowerConstants = new FollowerConstants(AutoSwervePositions.startToShotlineLookaheadDist, false);
    static FollowerConstants shotlineToWobbleOneFollowerConstants = new FollowerConstants(AutoSwervePositions.shotlineToWobble1LookaheadDist, false);
    static FollowerConstants shotlineToWobbleTwoFollowerConstants = new FollowerConstants(AutoSwervePositions.shotlineToWobble2LookahadDist, false);
    static FollowerConstants shotlineToWobbleThreeFollowerConstants = new FollowerConstants(AutoSwervePositions.shotlineToWobble3LookaheadDist, false);
    static FollowerConstants wobbleOneToParkFollowerConstants = new FollowerConstants(AutoSwervePositions.wobble1ToParkLookaheadDist, true);
    static FollowerConstants wobbleTwoToParkFollowerConstants = new FollowerConstants(AutoSwervePositions.wobble2ToParkLookaheadDist, true);
    static FollowerConstants wobbleThreeToParkFollowerConstants = new FollowerConstants(AutoSwervePositions.wobble3ToParkLookaheadDist, true);

    // Initialize SwervePathGenerationConstants
    static SwervePathGenerationConstants startToShotlinePathGenConstants = new SwervePathGenerationConstants(AutoSwervePositions.startToShotlineWaypointSpacing, AutoSwervePositions.startToShotlineWeightSmooth, AutoSwervePositions.startToShotlineTurnSpeed, AutoSwervePositions.startToShotlineMaxVelocity);
    static SwervePathGenerationConstants shotlineToWobbleOneGenerationConstants = new SwervePathGenerationConstants(AutoSwervePositions.shotlineToWobble1Spacing, AutoSwervePositions.shotlineToWobble1WeightSmooth, AutoSwervePositions.shotlineToWobble1TurnSpeed, AutoSwervePositions.shotlineToWobble1MaxVelocity);
    static SwervePathGenerationConstants shotlineToWobbleTwoGenerationConstants = new SwervePathGenerationConstants(AutoSwervePositions.shotlineToWobble2Spacing, AutoSwervePositions.shotlineToWobble2WeightSmooth, AutoSwervePositions.shotlineToWobble2TurnSpeed, AutoSwervePositions.shotlineToWobble2MaxVelocity);
    static SwervePathGenerationConstants shotlineToWobbleThreeGenerationConstants = new SwervePathGenerationConstants(AutoSwervePositions.shotlineToWobble3Spacing, AutoSwervePositions.shotlineToWobble3WeightSmooth, AutoSwervePositions.shotlineToWobble3TurnSpeed, AutoSwervePositions.shotlineToWobble3MaxVelocity);
    static SwervePathGenerationConstants wobbleOneToParkGenerationConstants = new SwervePathGenerationConstants(AutoSwervePositions.wobble1ToParkSpacing, AutoSwervePositions.wobble1ToParkWeightSmooth, AutoSwervePositions.wobble1ToParkTurnSpeed, AutoSwervePositions.wobble1ToParkMaxVelocity);
    static SwervePathGenerationConstants wobbleTwoToParkGenerationConstants = new SwervePathGenerationConstants(AutoSwervePositions.wobble2ToParkSpacing, AutoSwervePositions.wobble2ToParkWeightSmooth, AutoSwervePositions.wobble2ToParkTurnSpeed, AutoSwervePositions.wobble2ToParkMaxVelocity);
    static SwervePathGenerationConstants wobbleThreeToParkGenerationConstants = new SwervePathGenerationConstants(AutoSwervePositions.wobble3ToParkSpacing, AutoSwervePositions.wobble3ToParkWeightSmooth, AutoSwervePositions.wobble3ToParkTurnSpeed, AutoSwervePositions.wobble3ToParkMaxVelocity);

    static SwervePath startToShotline;
    static SwervePath shotLineToWobbleOne;
    static SwervePath shotlineToWobbleTwo;
    static SwervePath shotlineToWobbleThree;
    static SwervePath wobbleOneToParkline;
    static SwervePath wobbleTwoToParkline;
    static SwervePath wobbleThreeToParkline;

    public static ArrayList<Position> getPath(ArrayList<Position> pathArray){
        //test path
        testPath.add(p1Test);
        testPath.add(p2Test);
        //Start to Shotline
        startToShotlinePath.add(p1StartToShotline);
        startToShotlinePath.add(p2StartToShotLine);
        startToShotlinePath.add(p3StartToShotLine);
        //Shotline to Wobble N
        //N=1
        shotlineToWobble1Path.add(p1ShotlineToWobble1);
        shotlineToWobble1Path.add(p2ShotlineToWobble1);
        //N=2
        shotlineToWobble2Path.add(p1ShotlineToWobble2);
        shotlineToWobble2Path.add(p2ShotlineToWobble2);
        //N=3
        shotlineToWobble3Path.add(p1ShotlineToWobble3);
        shotlineToWobble3Path.add(p2ShotlineToWobble3);
        //Wobble N to Park
        //N=1
        wobble1ToParkPath.add(p1Wobble1ToParking);
        wobble1ToParkPath.add(p2Wobble1ToParking);
        //N=2
        wobble2ToParkPath.add(p1Wobble2ToParking);
        wobble2ToParkPath.add(p2Wobble2ToParking);
        //N=3
        wobble3ToParkPath.add(p1Wobble3ToParking);
        wobble3ToParkPath.add(p2Wobble3ToParking);
        return pathArray;
    }

    public static SwervePath generateAutoPaths (SwervePath swerveName){
        startToShotline = generateSwervePath(AutoSwervePositions.getPath(AutoSwervePositions.startToShotlinePath), startToShotlineFollowerConstants, startToShotlinePathGenConstants);
        shotLineToWobbleOne = generateSwervePath(AutoSwervePositions.getPath(AutoSwervePositions.shotlineToWobble1Path), shotlineToWobbleOneFollowerConstants, shotlineToWobbleOneGenerationConstants);
        shotlineToWobbleTwo = generateSwervePath(AutoSwervePositions.getPath(AutoSwervePositions.shotlineToWobble2Path), shotlineToWobbleTwoFollowerConstants, shotlineToWobbleTwoGenerationConstants);
        shotlineToWobbleThree = generateSwervePath(AutoSwervePositions.getPath(AutoSwervePositions.shotlineToWobble3Path), shotlineToWobbleThreeFollowerConstants, shotlineToWobbleThreeGenerationConstants);
        wobbleOneToParkline = generateSwervePath(AutoSwervePositions.getPath(AutoSwervePositions.wobble1ToParkPath), wobbleOneToParkFollowerConstants, wobbleOneToParkGenerationConstants);
        wobbleTwoToParkline = generateSwervePath(AutoSwervePositions.getPath(AutoSwervePositions.wobble2ToParkPath), wobbleTwoToParkFollowerConstants, wobbleTwoToParkGenerationConstants);
        wobbleThreeToParkline = generateSwervePath(AutoSwervePositions.getPath(AutoSwervePositions.wobble3ToParkPath), wobbleThreeToParkFollowerConstants, wobbleThreeToParkGenerationConstants);
        return swerveName;
    }

}
