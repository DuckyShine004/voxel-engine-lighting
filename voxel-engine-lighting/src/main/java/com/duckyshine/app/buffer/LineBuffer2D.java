package com.duckyshine.app.buffer;

import static org.lwjgl.opengl.GL30.*;

public class LineBuffer2D extends Buffer {
    public LineBuffer2D() {
        super();

        this.initialise();
    }

    private void initialise() {
        super.setupBuffers();

        this.bindVertexArray();

        glBindBuffer(GL_ARRAY_BUFFER, this.vertexBufferId);
        glBufferData(GL_ARRAY_BUFFER, 0, GL_STATIC_DRAW);
        this.setVertexAttributePointer(0, 2, GL_FLOAT, 2 * Float.BYTES, 0);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.indexBufferId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, 0, GL_STATIC_DRAW);

        this.detachVertexArray();
    }

    public void setup(BufferData bufferData) {
        this.bindVertexArray();

        glBindBuffer(GL_ARRAY_BUFFER, this.vertexBufferId);
        glBufferData(GL_ARRAY_BUFFER, bufferData.getVertices(), GL_STATIC_DRAW);

        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, this.indexBufferId);
        glBufferData(GL_ELEMENT_ARRAY_BUFFER, bufferData.getIndices(), GL_STATIC_DRAW);
    }
}
