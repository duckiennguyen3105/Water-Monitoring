package com.Sislab.WaterMonitoring.model;

public class TremorTest {
    private float time;
    private float xAxis;
    private float yAxis;
    private float zAxis;

    public TremorTest() {
        super();
    }

    public TremorTest(float time, float xAxis, float yAxis, float zAxis) {
        this.time = time;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.zAxis = zAxis;
    }

    public float getTime() {
        return time;
    }

    public void setTime(float time) {
        this.time = time;
    }

    public float getxAxis() {
        return xAxis;
    }

    public void setxAxis(float xAxis) {
        this.xAxis = xAxis;
    }

    public float getyAxis() {
        return yAxis;
    }

    public void setyAxis(float yAxis) {
        this.yAxis = yAxis;
    }

    public float getzAxis() {
        return zAxis;
    }

    public void setzAxis(float zAxis) {
        this.zAxis = zAxis;
    }
}
