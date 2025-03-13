package org.example.realphase2.scoring;

import java.util.Timer;
import java.util.TimerTask;

import org.example.realphase2.Gamemodelogic.FlagClass;

import javafx.application.Platform;


/**
 * The GameTimer class provides a simple timer for tracking time elapsed during gameplay.
 * It uses a Java Timer to update the game clock every second and updates a shared timer property 
 * in the FlagClass to display the current time.
 */
public class GameTimer {
    int secondsPassed = 0;
    int minutesPassed = 0;
    
    Timer timer = new Timer();
    TimerTask task = new TimerTask() {
    public void run() {
        secondsPassed++;
        
        if (secondsPassed % 60 == 0) {
            minutesPassed++;
            secondsPassed = 0;

        }
        String minutesPassedString;
        String secondsPassedString;
        if (minutesPassed < 10) {
            minutesPassedString = "0" + minutesPassed;
        }
        else {
            minutesPassedString = "" + minutesPassed;
        }
        if (secondsPassed < 10) {
            secondsPassedString = "0" + secondsPassed;
        }
        else {
            secondsPassedString = "" + secondsPassed;
        }
        String timerClock = minutesPassedString + ":" + secondsPassedString;
        Platform.runLater(() -> {
            FlagClass.timer.set(timerClock);
        });


    }
};

    /**
     * Starts the timer, scheduling the `TimerTask` to run at a fixed rate of 1 second.
     */
    public void start() {
        timer.scheduleAtFixedRate(task, 0, 1000);
    }
    /**
     * Retrieves the total elapsed time in seconds.
     *
     * @return The total time elapsed since the timer started, in seconds.
     */
    public int getTime() {
        return (secondsPassed + (minutesPassed * 60));
    }

    /**
     * Stops the timer and cancels the scheduled task.
     */
    public void stop() {
        timer.cancel();
    }

    /**
     * Stops the timer completely and records the total time taken in the FlagClass.
     * The recorded time is in seconds and includes both minutes and seconds passed.
     */
    public void stopComplete() {
        FlagClass.timeTaken = secondsPassed + minutesPassed * 60;

        timer.cancel();
    }
}