package com.duckyshine.app.display;

import org.joml.Vector2f;

public class Display {
    private static final Display DISPLAY = new Display();

    private DisplayType displayType;

    private Vector2f centre;
    private Vector2f resolution;

    private Display() {
        this.displayType = DisplayType.DEFAULT;

        this.resolution = new Vector2f(this.displayType.getWidth(), this.displayType.getHeight());
        this.centre = this.resolution.mul(0.5f, new Vector2f());
    }

    public static Display get() {
        return Display.DISPLAY;
    }

    public void setDisplayType(DisplayType displayType) {
        this.displayType = displayType;
    }

    public DisplayType getDisplayType() {
        return this.displayType;
    }

    public void setResolution(int width, int height) {
        this.resolution.x = width;
        this.resolution.y = height;

        this.centre.x = 0.5f * width;
        this.centre.y = 0.5f * height;
    }

    public Vector2f getResolution() {
        return this.resolution;
    }

    public Vector2f getCentre() {
        return this.centre;
    }

    public int getInitialWidth() {
        return this.displayType.getWidth();
    }

    public int getInitialHeight() {
        return this.displayType.getHeight();
    }

    public float getWidth() {
        return this.resolution.x;
    }

    public float getHeight() {
        return this.resolution.y;
    }

    public float getAspectRatio() {
        return this.resolution.x / this.resolution.y;
    }
}
