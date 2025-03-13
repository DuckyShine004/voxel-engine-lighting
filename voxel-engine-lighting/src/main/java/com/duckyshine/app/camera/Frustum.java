package com.duckyshine.app.camera;

import org.joml.Vector3f;

import com.duckyshine.app.math.Vector3;
import com.duckyshine.app.model.Chunk;

public class Frustum {
    private float tanX;
    private float tanY;

    private float factorX;
    private float factorY;

    private Camera camera;

    public Frustum(Camera camera) {
        this.camera = camera;

        this.initialise();
    }

    private void initialise() {
        float FOV = (float) Math.toRadians(this.camera.getFOV());

        float hFOV = 2.0f * ((float) Math.atan(Math.atan(FOV * 0.5f) * this.camera.getAspectRatio()));

        float halfFOVX = hFOV * 0.5f;
        float halfFOVY = FOV * 0.5f;

        this.tanX = (float) Math.tan(halfFOVX);
        this.tanY = (float) Math.tan(halfFOVY);

        this.factorX = 1.0f / (float) (Math.cos(halfFOVX));
        this.factorY = 1.0f / (float) (Math.cos(halfFOVY));
    }

    public boolean isIntersecting(Chunk chunk) {
        Vector3f direction = Vector3.sub(chunk.getCentre(), this.camera.getPosition());

        float radius = chunk.getRadius();
        float projectionZ = Vector3.dot(this.camera.getFront(), direction);

        if (projectionZ < this.camera.getNear() - radius || projectionZ > this.camera.getFar() + radius) {
            return false;
        }

        float projectionY = Vector3.dot(direction, this.camera.getUp());
        float distanceY = this.factorY * radius + projectionZ * this.tanY;

        if (projectionY < -distanceY || projectionY > distanceY) {
            return false;
        }

        float projectionX = Vector3.dot(direction, this.camera.getRight());
        float distanceX = this.factorX * radius + projectionZ * this.tanX;

        if (projectionX < -distanceX || projectionX > distanceX) {
            return false;
        }

        return true;
    }
}
