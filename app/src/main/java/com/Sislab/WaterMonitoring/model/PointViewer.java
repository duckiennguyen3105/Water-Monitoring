package com.Sislab.WaterMonitoring.model;

public class PointViewer {
    private float fx;
    private float fy;
    private float fz;
    private long timestamp;

    public PointViewer() {
        super();
    }

    public PointViewer(float fx, float fy, float fz, long timestamp) {
        this.fx = fx;
        this.fy = fy;
        this.fz = fz;
        this.timestamp = timestamp;
    }

    public float getFx() {
        return fx;
    }
    public float getFy() {
        return fy;
    }
    public float getFz() {
        return fz;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
