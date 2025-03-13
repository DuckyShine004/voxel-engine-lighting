package com.duckyshine.app.camera;

import org.joml.Vector3f;
import com.duckyshine.app.math.Vector3;
import com.duckyshine.app.model.Chunk;

public class Frustum {
    private float tan;

    private float factorX;
    private float factorY;

    private Camera camera;

    public Frustum(Camera camera) {
        this.camera = camera;

        this.initialise();
    }

    private void initialise() {
        float angleY = this.camera.getVFOV() * 0.5f;
        float angleX = this.camera.getHFOV() * 0.5f;

        this.tan = (float) Math.tan(angleY);

        this.factorX = 1.0f / (float) Math.cos(angleX);
        this.factorY = 1.0f / (float) Math.cos(angleY);
    }

    public boolean isIntersecting(Chunk chunk) {
        Vector3f direction = Vector3.sub(chunk.getCentre(), this.camera.getPosition());

        Vector3f projection = new Vector3f(
                Vector3.dot(Vector3.mul(-1.0f, this.camera.getRight()), direction),
                Vector3.dot(this.camera.getUp(), direction),
                Vector3.dot(this.camera.getFront(), direction));

        float far = this.camera.getFar();
        float near = this.camera.getNear();

        float radius = chunk.getRadius();

        if (projection.z > far + radius || projection.z < near - radius) {
            return false;
        }

        float dY = this.factorY * radius;
        float testY = projection.z * this.tan;

        if (projection.y > testY + dY || projection.y < -testY - dY) {
            return false;
        }

        float dX = this.factorX * radius;
        float testX = testY * this.camera.getAspectRatio();

        if (projection.x > testX + dX || projection.x < -testX - dX) {
            return false;
        }

        return true;
    }

    // public boolean isIntersecting(Chunk chunk) {
    // Vector3f direction = Vector3.sub(chunk.getCentre(),
    // this.camera.getPosition());

    // float radius = chunk.getRadius();
    // float projectionZ = Vector3.dot(this.camera.getFront(), direction);

    // if (projectionZ < this.camera.getNear() - radius || projectionZ >
    // this.camera.getFar() + radius) {
    // return false;
    // }

    // float projectionY = Vector3.dot(direction, this.camera.getUp());
    // float distanceY = this.factorY * radius + projectionZ * this.tanY;

    // if (projectionY < -distanceY || projectionY > distanceY) {
    // return false;
    // }

    // float projectionX = Vector3.dot(direction, this.camera.getRight());
    // float distanceX = this.factorX * radius + projectionZ * this.tanX;

    // if (projectionX < -distanceX || projectionX > distanceX) {
    // return false;
    // }

    // return true;
    // }
}
