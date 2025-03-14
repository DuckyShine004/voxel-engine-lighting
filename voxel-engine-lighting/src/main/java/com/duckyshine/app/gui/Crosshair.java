package com.duckyshine.app.gui;

import org.joml.Vector2f;

import com.duckyshine.app.buffer.Buffer;
import com.duckyshine.app.buffer.BufferData;
import com.duckyshine.app.buffer.LineBuffer2D;
import com.duckyshine.app.debug.Debug;
import com.duckyshine.app.display.Display;

import static org.lwjgl.opengl.GL11.*;

public class Crosshair {
    private final int[] INDICES = { 0, 1, 2, 3 };
    private final float RADIUS = 10.0f;

    private final Vector2f position;

    private Buffer buffer;

    public Crosshair() {
        this.position = Display.get().getCentre();

        this.buffer = new LineBuffer2D();
    }

    public float[] getVertices() {
        return new float[] {
                this.position.x - this.RADIUS, this.position.y,
                this.position.x + this.RADIUS, this.position.y,
                this.position.x, this.position.y - this.RADIUS,
                this.position.x, this.position.y + this.RADIUS
        };
    }

    public void loadBuffer() {
        BufferData bufferData = new BufferData(this.getVertices(), this.INDICES);

        this.buffer.setup(bufferData);
    }

    public void render() {
        glDrawElements(GL_LINES, this.INDICES.length, GL_UNSIGNED_INT, 0);

        this.buffer.detachVertexArray();
    }
}
