package org.whitneyrobotics.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.whitneyrobotics.ftc.teamcode.autoop.AutoSwervePositions;
import org.whitneyrobotics.ftc.teamcode.lib.util.Toggler;
import org.whitneyrobotics.ftc.teamcode.subsys.Drivetrain;
import org.whitneyrobotics.ftc.teamcode.subsys.WHSRobotImpl;

@TeleOp(name="Drivetrain Test" , group="Tests" )
public class DrivetrainTest extends OpMode  {
    public WHSRobotImpl yeetboi;
    // yeetboi is robot
    public Toggler posTog;
    public Toggler allianceTog;
    public int posStateNum;
    public int allianceStateNum;
    @Override
    public void init() {
        yeetboi = new WHSRobotImpl(hardwareMap);
        posTog = new Toggler(5);
        allianceTog = new Toggler(2);
    }

    @Override
    public void loop() {
        posTog.changeState(gamepad1.dpad_right, gamepad1.dpad_left);
        allianceTog.changeState(gamepad1.dpad_up, gamepad1.dpad_down);

        posStateNum = posTog.currentState();
        allianceStateNum = allianceTog.currentState();

        switch (posStateNum){
            case 0:
                yeetboi.driveToTarget(AutoSwervePositions.startToCarousel2, false);
                break;
            case 1:
                yeetboi.driveToTarget(AutoSwervePositions.carouselToShippingHub2, true);
                break;
            case 2:
                yeetboi.driveToTarget(AutoSwervePositions.shippingHubToWarehouse2, false);
                break;
            case 3:
                yeetboi.driveToTarget(AutoSwervePositions.warehouseToShippingHub2, true);
                break;
            case 4:
                switch (allianceStateNum){
                    case 0:
                        yeetboi.driveToTarget(AutoSwervePositions.shippingHubToWarehousePark2, false);
                        break;
                    case 1:
                        yeetboi.driveToTarget(AutoSwervePositions.shippingHubToStorageUnit2, false);
                        break;
                }
                break;

        }
        telemetry.addData("Position State: ", posStateNum);
        telemetry.addData("Alliance State: ", allianceTog);
    }
}
