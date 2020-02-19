package com.Sislab.MagicSpoon.model;

public class PointViewer {
    private float value;
    private long timestamp;

    public PointViewer() {
        super();
    }

    public PointViewer(float value, long timestamp) {
        this.value = value;
        this.timestamp = timestamp;
    }

    public float getValue() {
        return value;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
