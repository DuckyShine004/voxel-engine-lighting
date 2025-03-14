package com.duckyshine.app.model;

import java.util.Arrays;

import org.joml.Vector3f;
import org.joml.Vector3i;

import com.duckyshine.app.math.Direction;
import com.duckyshine.app.physics.AABB;

public class Block {
    private final int FACES = 6;

    private final Vector3i position;

    private final Vector3i globalPosition;

    private final BlockType blockType;

    private boolean[] isFaceActive;

    public Block(Vector3i position, BlockType blockType) {
        this.position = position;

        this.globalPosition = new Vector3i();

        this.blockType = blockType;

        this.initialise();
    }

    public Block(int x, int y, int z, BlockType blockType) {
        this.position = new Vector3i(x, y, z);

        this.globalPosition = new Vector3i();

        this.blockType = blockType;

        this.initialise();
    }

    public void initialise() {
        this.isFaceActive = new boolean[this.FACES];

        Arrays.fill(this.isFaceActive, true);
    }

    public void setAllFaceStatuses(boolean status) {
        Arrays.fill(this.isFaceActive, status);
    }

    public boolean isFaceActive(Direction direction) {
        return this.isFaceActive[direction.getIndex()];
    }

    public void setFaceStatus(Direction direction, boolean status) {
        this.isFaceActive[direction.getIndex()] = status;
    }

    public Vector3i getPosition() {
        return this.position;
    }

    public BlockType getBlockType() {
        return this.blockType;
    }

    public void setGlobalPosition(int x, int y, int z) {
        this.setGlobalPosition(new Vector3i(x, y, z));
    }

    public void setGlobalPosition(Vector3i position) {
        this.globalPosition.set(position);
    }

    public Vector3i getGlobalPosition() {
        return this.globalPosition;
    }

    public boolean isTransparent() {
        return this.blockType.isTransparent();
    }

    public AABB getAABB() {
        Vector3f position = new Vector3f(this.getGlobalPosition());

        return new AABB(
                position.x,
                position.y,
                position.z,
                position.x + 1.0f,
                position.y + 1.0f,
                position.z + 1.0f);
    }
}
