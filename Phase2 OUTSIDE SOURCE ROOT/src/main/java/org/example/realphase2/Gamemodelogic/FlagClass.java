package org.example.realphase2.Gamemodelogic;

import javafx.beans.property.SimpleStringProperty;

// This class contains global flags used for managing the state of the game logic.
public class FlagClass {
    // If a vertex cannot be colored, this will become true
    // Otherwise, it is false
    public static boolean illegalColor = false;
    public static boolean illegalVertex = false;
    public static boolean illegalUncoloring = false;
    public static boolean isTimerStarted = false;
    public static SimpleStringProperty timer = new SimpleStringProperty("00:00");
    public static int timeTaken;
    public static SimpleStringProperty score = new SimpleStringProperty("0.0");
    public static int chromaticNumber;
}
