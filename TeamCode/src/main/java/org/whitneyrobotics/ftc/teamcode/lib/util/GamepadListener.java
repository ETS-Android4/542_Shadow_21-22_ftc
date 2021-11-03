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
        } else if (button && !longPressFired) {
            if((System.nanoTime()-longPressStartTime)/1E6 >= minThresholdms){
                longPressFired = true;
                return true;
            }
        }
        if (!button) {
          firstLongPress = true;
          longPressFired = false;
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
            if((endTime-shortPressStartTime)/1E6 <= maxThresholdms){
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

    private boolean firstPress = true;
    private boolean expired = false;
    private double expireTime;
    private double firstPressEndTime;

    public boolean doublePress(boolean button, double maxPressIntervalMs){
        if(button && firstPress ) {
            firstPress = false;
            expired = false;
            expireTime = (System.nanoTime()/1E6) + 500 + maxPressIntervalMs; //you wil have the maximum press interval plus half a second to perform the double press
        } else if(!button && !firstPress && !expired){ //first unpress
            firstPressEndTime = System.nanoTime()/1E6;
        } else if(button && !firstPress && !expired){
            if(System.nanoTime()/1E6 < firstPressEndTime+maxPressIntervalMs) {
                firstPress = true;
                return true;
            }
        }

        if(System.nanoTime()/1E6 > expireTime){
            expired = true;
            firstPress = true;
        }
        return false;
    }

    private boolean doublePress(boolean button){
        return doublePress(button, 250);
    }

}
