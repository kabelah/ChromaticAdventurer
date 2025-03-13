package org.example.phase3.phase1algo.algorithms;

public class TimerCheck {
    private final long timeoutMillis;
    private long startTime;
    private volatile boolean timerExpired;

    public TimerCheck(long timeoutMillis) {
        this.timeoutMillis = timeoutMillis;
        this.timerExpired = false;
    }

    public void start() {
        startTime = System.currentTimeMillis();
        new Thread(() -> {
            while (!timerExpired && System.currentTimeMillis() - startTime < timeoutMillis) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            timerExpired = true;
        }).start();
    }

    public void stop() {
        timerExpired = true;
    }

    public boolean hasTimerExpired() {
        return timerExpired;
    }
}
