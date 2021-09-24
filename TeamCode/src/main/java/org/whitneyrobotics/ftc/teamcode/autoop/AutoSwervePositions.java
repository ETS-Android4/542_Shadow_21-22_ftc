package org.whitneyrobotics.ftc.teamcode.autoop;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whitneyrobotics.ftc.teamcode.lib.geometry.Position;

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
 public final  static Position   warehouseToShippingHub2 = new Position(-300, -900);
 //Shipping Hub to Warehouse (Park)
 public final  static Position shippingHubToWarehousePark1 = new Position(-300, -900);
 public final  static Position shippingHubToWarehousePark2 = new Position(900, -900);
 //Shipping Hub to Storage Unit
 public final  static Position shippingHubToStorageUnit1 = new Position(-300, -900);
 public final  static Position shippingHubToStorageUnit2 = new Position(-1500, -900);
}
