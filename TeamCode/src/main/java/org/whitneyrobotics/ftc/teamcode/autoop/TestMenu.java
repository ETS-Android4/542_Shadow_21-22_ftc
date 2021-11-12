package org.whitneyrobotics.ftc.teamcode.autoop;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.whitneyrobotics.ftc.teamcode.lib.util.SelectionMenu;

import java.util.Arrays;

@Autonomous(group = "Tests",name="Menu Selector Test")
public class TestMenu extends OpMode {
    private SelectionMenu configureAuto;

    @Override
    public void init() {
        SelectionMenu.Selection<Integer> RED = new SelectionMenu.Selection<Integer>("Red",0);
        SelectionMenu.Selection<Integer> BLUE = new SelectionMenu.Selection<Integer>("Blue",1);
        SelectionMenu.Prompt color = new SelectionMenu.Prompt("Select alliance color: ", RED, BLUE);
        SelectionMenu.Prompt startingPosition = new SelectionMenu.Prompt("Select starting position: ")
                .addSelection("Top",0)
                .addSelection("Bottom",1);
        SelectionMenu.Prompt rotateCarousel = new SelectionMenu.Prompt("Rotate Carousel State: ")
                .addSelection("On",false)
                .addSelection("Off", true);
        SelectionMenu.Prompt doShippingHub = new SelectionMenu.Prompt("Shipping Hub State: ")
                .addSelection("On", true)
                .addSelection("Off",false);
        SelectionMenu.Prompt doWarehouse = new SelectionMenu.Prompt("Warehouse State: ")
                .addSelection("On",true)
                .addSelection("Off",false);
        SelectionMenu.Prompt park = new SelectionMenu.Prompt("Park State: ")
                .addSelection("On", true)
                .addSelection("Off", false);
        SelectionMenu configureAuto = new SelectionMenu("Autonomous Configuration", color, startingPosition, rotateCarousel, doShippingHub, doWarehouse)
                .addPrompt(park); //just for fun ;)
        configureAuto.init();
    }

    @Override
    public void init_loop() {
        configureAuto.run(gamepad1.dpad_right,gamepad1.dpad_left,gamepad1.dpad_down,gamepad1.dpad_up);
        telemetry.addLine(configureAuto.formatDisplay());
        telemetry.update();
    }

    @Override
    public void loop() {
        telemetry.addData("Selection output", Arrays.toString(configureAuto.getOutputs()));
        telemetry.addData("Value of color",configureAuto.getPrompts().get(0).getValueOfActive());
    }
}
