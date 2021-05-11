package com.Sislab.WaterMonitoring.model;

public class TempViewer {
    private float Basement;
    private float Room1;
    private float Room2;
    private long timestamp;

    public TempViewer(float basement, float room1, float room2, long timestamp) {
        Basement = basement;
        Room1 = room1;
        Room2 = room2;
        this.timestamp = timestamp;
    }

    public TempViewer() {
        super();
    }

    public float getBasement() {
        return Basement;
    }

    public void setBasement(float basement) {
        Basement = basement;
    }

    public float getRoom1() {
        return Room1;
    }

    public void setRoom1(float room1) {
        Room1 = room1;
    }

    public float getRoom2() {
        return Room2;
    }

    public void setRoom2(float room2) {
        Room2 = room2;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
