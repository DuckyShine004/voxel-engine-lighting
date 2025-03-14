package com.duckyshine.app.camera;

import java.applet.Applet;

import org.joml.Matrix4f;
import org.joml.Vector2f;
import org.joml.Vector3f;

import com.duckyshine.app.math.Matrix4;
import com.duckyshine.app.math.Vector3;

import com.duckyshine.app.display.Display;

import com.duckyshine.app.debug.Debug;

public class Camera {
    private final float YAW = 90.0f;
    private final float PITCH = 0.0f;
    private final float PITCH_LIMIT = 89.0f;

    private final float FAR = 100.0f;
    private final float NEAR = 0.1f;
    private final float FIELD_OF_VIEW = 45.0f;

    private final float SENSITIVITY = 0.05f;

    private float yaw;
    private float pitch;

    private float aspectRatio;

    private Vector2f lastMousePosition;

    private Vector3f position;
    private Vector3f direction;

    private Vector3f up;
    private Vector3f front;

    private Matrix4f view;
    private Matrix4f projection;
    private Matrix4f projectionView;

    private Matrix4f orthographic;

    private Frustum frustum;

    public Camera() {
        this.position = new Vector3f();

        this.initialise();
    }

    public Camera(Vector3f position) {
        this.position = position;

        this.initialise();
    }

    public Camera(float x, float y, float z) {
        this.position = new Vector3f(x, y, z);

        this.initialise();
    }

    public void initialise() {
        this.yaw = -this.YAW;
        this.pitch = this.PITCH;

        this.lastMousePosition = null;

        this.direction = new Vector3f();

        this.up = new Vector3f(0.0f, 1.0f, 0.0f);
        this.front = new Vector3f(0.0f, 0.0f, -1.0f);

        this.view = new Matrix4f();
        this.projection = new Matrix4f();
        this.projectionView = new Matrix4f();

        this.orthographic = new Matrix4f();

        this.frustum = new Frustum(this);

        this.initialiseAspectRatio();
    }

    public void setPosition(Vector3f position) {
        this.position = position;
    }

    private void initialiseAspectRatio() {
        Display display = Display.get();

        float width = (float) display.getWidth();
        float height = (float) display.getHeight();

        this.aspectRatio = display.getAspectRatio();

        this.orthographic.identity().ortho2D(0.0f, width, 0.0f, height);
    }

    public void updateAspectRatio(int width, int height) {
        this.aspectRatio = (float) width / height;

        this.orthographic.identity().ortho2D(0.0f, width, 0.0f, height);
    }

    public void updateMatrices() {
        this.view.identity().lookAt(this.position, Vector3.add(this.position, this.front), this.up);

        this.projection.identity().perspective(this.getVFOV(), this.aspectRatio, this.NEAR, this.FAR);

        this.projectionView = Matrix4.mul(this.projection, this.view);
    }

    public void rotate(double mouseX, double mouseY) {
        float floatMouseX = (float) mouseX;
        float floatMouseY = (float) mouseY;

        float theta;
        float omega;

        if (this.lastMousePosition == null) {
            this.lastMousePosition = new Vector2f((float) mouseX, (float) mouseY);
        }

        float offsetX = (floatMouseX - lastMousePosition.x) * this.SENSITIVITY;
        float offsetY = (lastMousePosition.y - floatMouseY) * this.SENSITIVITY;

        this.lastMousePosition.x = floatMouseX;
        this.lastMousePosition.y = floatMouseY;

        this.yaw += offsetX;
        this.pitch += offsetY;

        if (this.pitch > this.PITCH_LIMIT) {
            this.pitch = this.PITCH_LIMIT;
        }

        if (this.pitch < -this.PITCH_LIMIT) {
            this.pitch = -this.PITCH_LIMIT;
        }

        theta = (float) Math.toRadians(this.yaw);
        omega = (float) Math.toRadians(this.pitch);

        this.direction.zero();

        this.direction.x = (float) (Math.cos(theta) * Math.cos(omega));
        this.direction.y = (float) Math.sin(omega);
        this.direction.z = (float) (Math.sin(theta) * Math.cos(omega));

        this.front = this.direction.normalize();
    }

    public Vector3f getUp() {
        return this.up;
    }

    public Vector3f getFront() {
        return this.front;
    }

    public Vector3f getRight() {
        Vector3f right = this.front.cross(this.up, new Vector3f());

        return right.normalize();
    }

    public Vector3f getPosition() {
        return this.position;
    }

    public Matrix4f getProjectionView() {
        return this.projectionView;
    }

    public Matrix4f getOrthographic() {
        return this.orthographic;
    }

    public float getNear() {
        return this.NEAR;
    }

    public float getFar() {
        return this.FAR;
    }

    public float getFOV() {
        return this.FIELD_OF_VIEW;
    }

    public float getVFOV() {
        return (float) Math.toRadians(this.getFOV());
    }

    public float getHFOV() {
        float VFOV = this.getVFOV();

        return 2.0f * ((float) Math.atan(this.aspectRatio * Math.tan(VFOV * 0.5f)));
    }

    public Frustum getFrustum() {
        return this.frustum;
    }

    public float getAspectRatio() {
        return this.aspectRatio;
    }
}
