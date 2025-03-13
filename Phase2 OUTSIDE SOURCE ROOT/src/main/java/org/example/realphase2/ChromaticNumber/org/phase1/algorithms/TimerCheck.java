package org.example.realphase2.ChromaticNumber.org.phase1.algorithms;

/**
 * The Timer Class, used to make sure processing doesn't take too long
 */
public class TimerCheck {
    private int time; // Amount of time allowed for processing
    private volatile boolean timerExpired = false; // Lets all threads know if the timer is finished
    private volatile boolean timerStarted = false; // Indicates if the timer has already started
    private Thread timerThread; // Reference to the timer thread

    /**
     * Initializes a Timer object
     *
     * @param time Time allowed for processing
     */
    public TimerCheck(int time) {
        this.time = time;
    }

    /**
     * Starts the timer.
     * It creates a new thread so that the processing of the graphs can continue in the meantime.
     */
    public void start() {
        if (!timerStarted) {
            timerStarted = true; // Set timer as started
            timerThread = new Thread(() -> {
                try {
                    Thread.sleep(time);
                    timerExpired = true;
                } catch (InterruptedException e) {

                }
            });
            timerThread.setDaemon(true);
            timerThread.start();
        }
    }

    /**
     * Stops the timer if it's still running
     */
    public void stop() {
        if (timerThread != null && timerThread.isAlive()) {
            timerThread.interrupt();
        }
        timerExpired = false;
    }

    /**
     * Used to check whether the timer has expired
     *
     * @return boolean value determining whether the timer has expired
     */
    public boolean hasTimerExpired() {
        return timerExpired;
    }
}
