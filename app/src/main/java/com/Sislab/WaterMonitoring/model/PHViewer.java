package com.Sislab.WaterMonitoring.model;

public class PHViewer {
    private float Basement;
    private long timestamp;

    public PHViewer() {
        super();
    }

    public PHViewer(float basement, long timestamp) {
        Basement = basement;
        this.timestamp = timestamp;
    }

    public float getBasement() {
        return Basement;
    }

    public void setBasement(float basement) {
        Basement = basement;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
