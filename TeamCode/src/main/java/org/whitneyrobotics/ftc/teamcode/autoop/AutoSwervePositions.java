package org.whitneyrobotics.ftc.teamcode.autoop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;
import org.whitneyrobotics.ftc.teamcode.lib.purepursuit.FollowerConstants;

import java.util.ArrayList;

public class AutoSwervePositions {
//Start to Carousel
 public final  static Position startToCarousel1 = new Position(-900, -1800);
 public final  static Position startToCarousel2 = new Position(-1800, -1800);
 //Carousel to Shipping Hub
 public final  static Position carouselToShippingHub1 = new Position(-1800, -1800);
 public final  static Position carouselToShippingHub2 = new Position(-300, -900);
 //Shipping Hub to Warehouse
 public final  static Position  shippingHubToWarehouse1 = new Position(-300, -900);
 public final  static Position  shippingHubToWarehouse2 = new Position(600, -1500);
//Warehouse to Shipping Hub
 public final  static Position  warehouseToShippingHub1= new Position(600, -1500);
 public final  static Position  warehouseToShippingHub2 = new Position(-300, -900);
 //Shipping Hub to Warehouse (Park)
 public final  static Position shippingHubToWarehousePark1 = new Position(-300, -900);
 public final  static Position shippingHubToWarehousePark2 = new Position(900, -900);
 //Shipping Hub to Storage Unit
 public final  static Position shippingHubToStorageUnit1 = new Position(-300, -900);
 public final  static Position shippingHubToStorageUnit2 = new Position(-1500, -900);

 //swerve lookabead distances
 public static double startToCarouselLookaheadDistance = 350;
 public static double carouselToShippingHubLookaheadDistance = 350;
 public static double shippingHubToWarehouseLookaheadDistance = 350;
 public static double warehouseToShippingHubLookaheadDistance = 350;
 public static double shippingHubToWarehouseParkLookaheadDistance = 350;
 public static double shippingHubToStorageUnitLookaheadDistance = 350;

 //swerve spacing
 public static double startToCarouselSwerveSpacing =80;
 public static double  carouselToShippingHubSwerveSpacing =80;
 public static double shippingHubToWarehouseSwerveSpacing =80;
 public static double warehouseToShippingHubSwerveSpacing =80;
 public static double shippingHubToWarehouseParkSwerveSpacing =80;
 public static double shippingHubToStorageUnitSwerveSpacing =80;

 //Weight Smooth
 public static double startToCarouselWeightSmooth = 0.5;
 public static double carouselToShippingHubWeightSmooth = 0.5;
 public static double shippingHubToWarehouseWeightSmooth = 0.5;
 public static double warehouseToShippingHubWeightSmooth = 0.5;
 public static double shippingHubToWarehouseParkWeightSmooth = 0.5;
 public static double shippingHubToStorageUnitWeightSmooth = 0.5;

 //swerve turn speed
 public static double startToCarouselTurnSpeed = 3;
 public static double carouselToShippingHubTurnSpeed = 3;
 public static double shippingHubToWarehouseTurnSpeed = 3;
 public static double warehouseToShippingHubTurnSpeed = 3;
 public static double shippingHubToWarehouseParkTurnSpeed = 3;
 public static double shippingHubToStorageUnitTurnSpeed = 3;

 //swerve max velocity
 public static double startToCarouselMaxVelocity = 750;
 public static double carouselToShippingHubMaxVelocity = 750;
 public static double shippingHubToWarehouseMaxVelocity = 750;
 public static double warehouseToShippingHubMaxVelocity = 750;
 public static double shippingHubToWarehouseParkMaxVelocity = 750;
 public static double shippingHubToStorageUnitMaxVelocity = 750;

 //ArrayLists to call in getPath
 public static ArrayList<Position> startToCarouselPath = new ArrayList<Position>();
 public static ArrayList<Position> carouselToShippingHubPath = new ArrayList<Position>();
 public static ArrayList<Position> shippingHubToWarehousePath = new ArrayList<Position>();
 public static ArrayList<Position> warehouseToShippingHubPath = new ArrayList<Position>();
 public static ArrayList<Position> shippingHubToWarehouseParkPath = new ArrayList<Position>();
 public static ArrayList<Position> shippingHubToStorageUnitPath = new ArrayList<Position>();

 //Initialioze Follower Constants
 public static FollowerConstants startToCarouselFollowerConstants = new FollowerConstants(startToCarouselLookaheadDistance, false);
 public static FollowerConstants carouselToShippingHubFollowerConstants = new FollowerConstants(carouselToShippingHubLookaheadDistance, false);
 public static FollowerConstants shippingHubToWarehouseFollowerConstants = new FollowerConstants(shippingHubToWarehouseLookaheadDistance, false);
 public static FollowerConstants warehouseToShippingHubFollowerConstants = new FollowerConstants(warehouseToShippingHubLookaheadDistance, false);
 public static FollowerConstants shippingHubToWarehouseParkFollowerConstants = new FollowerConstants(shippingHubToWarehouseParkLookaheadDistance, false);
 public static FollowerConstants shippingHubToStorageUnitFollowerConstants = new FollowerConstants(shippingHubToStorageUnitLookaheadDistance, false);
}


