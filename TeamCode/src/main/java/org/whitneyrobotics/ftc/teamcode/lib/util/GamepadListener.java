package org.whitneyrobotics.ftc.teamcode.lib.util;

public class GamepadListener {

    public GamepadListener() {}

    private boolean firstLongPress = true; //Starts the timer ONCE
    private long longPressStartTime; //Lets you measure when button is starting to be held
    //Stops long press from continuously firing if button is held
    private boolean longPressFired = false;

    public boolean longPress(boolean button, double minThresholdms){
        if(firstLongPress && button){
            longPressStartTime = System.nanoTime();
            firstLongPress = false;
            longPressFired = true;
        } else if (button && !longPressFired) {
            if((System.nanoTime()-longPressStartTime)*1000 >= minThresholdms){
                longPressFired = true;
                return true;
            }
        }
        if (!button) {
          firstLongPress = true;
        }
        return false;
    }

    public boolean longPress(boolean button){
        return longPress(button, 1000);
    }

    private boolean firstShortPress = true;
    private long shortPressStartTime;

    public boolean shortPress(boolean button, double maxThresholdms) {
        if(firstShortPress && button){
            shortPressStartTime = System.nanoTime();
            firstShortPress = false;
        } else if (!button) {
            long endTime = System.nanoTime();
            if((endTime-shortPressStartTime)*1000 <= maxThresholdms){
                endTime += maxThresholdms; // to get it to only fire once
                return true;
            }
            firstShortPress = true;
        }
        return false;
    }

    public boolean shortPress(boolean button){
        return shortPress(button, 250);
    }

}
