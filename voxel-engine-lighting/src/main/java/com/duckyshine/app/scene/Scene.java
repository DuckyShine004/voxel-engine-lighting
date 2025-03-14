package com.duckyshine.app.scene;

import java.util.Map;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashMap;

import org.joml.Vector3f;
import org.joml.Vector3i;

import com.duckyshine.app.physics.AABB;

import com.duckyshine.app.physics.controller.Player;
import com.duckyshine.app.physics.ray.RayResult;
import com.duckyshine.app.asset.AssetPool;

import com.duckyshine.app.camera.Camera;
import com.duckyshine.app.camera.CameraType;
import com.duckyshine.app.math.Axis;
import com.duckyshine.app.model.Block;
import com.duckyshine.app.shader.Shader;
import com.duckyshine.app.shader.ShaderType;

import com.duckyshine.app.debug.Debug;
import com.duckyshine.app.gui.Crosshair;

import static org.lwjgl.opengl.GL11.*;

public class Scene {
    private Shader shader;

    private Player player;

    private ChunkManager chunkManager;

    private Crosshair crosshair;

    public Scene() {
        this.player = new Player(0.0f, 20.0f, 0.0f);

        this.shader = AssetPool.getShader(ShaderType.WORLD.getName());

        this.chunkManager = new ChunkManager(this.player.getCamera());

        this.crosshair = new Crosshair();
    }

    public Scene(Shader shader) {
        this.player = new Player();

        this.shader = shader;

        this.chunkManager = new ChunkManager(this.player.getCamera());
    }

    public void initialise() {
        this.chunkManager.initialise();
    }

    public boolean isColliding(AABB aabb) {
        Vector3f min = aabb.getMin();
        Vector3f max = aabb.getMax();

        int minX = (int) Math.floor(min.x);
        int minY = (int) Math.floor(min.y);
        int minZ = (int) Math.floor(min.z);

        int maxX = (int) Math.floor(max.x);
        int maxY = (int) Math.floor(max.y);
        int maxZ = (int) Math.floor(max.z);

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    Vector3f position = new Vector3f(x, y, z);

                    if (this.chunkManager.isBlockActiveAtGlobalPosition(position)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public void checkCollisions(long window, float deltaTime) {
        this.player.updateVelocity(window, deltaTime);

        Vector3f position = this.player.getPosition();
        Vector3f target = this.player.getNextPosition(deltaTime);

        if (this.player.isGravityOn()) {
            target.x = this.getAxisCollision(position.x, target.x, Axis.X);
            target.y = this.getAxisCollision(position.y, target.y, Axis.Y);
            target.z = this.getAxisCollision(position.z, target.z, Axis.Z);
        }

        this.player.setPosition(target);
    }

    // Refactor to controller for other dynamic entities
    private void setControllerGravity(Player player, Axis axis, float position, float target, boolean isColliding) {

    }

    public float getAxisCollision(float position, float target, Axis axis) {
        AABB aabb = this.player.getAABB();

        AABB offsetAABB = aabb.getOffset(target - position, axis);

        boolean isColliding = this.isColliding(offsetAABB);

        if (axis == Axis.Y) {
            if (isColliding) {
                if (target <= position) {
                    this.player.setIsGrounded(true);
                } else {
                    this.player.resetVerticalVelocity();
                }
            } else {
                this.player.setIsGrounded(false);
            }
        }

        return isColliding ? position : target;
    }

    public void update(long window, float deltaTime) {
        this.checkCollisions(window, deltaTime);

        this.player.update(window, this);

        this.chunkManager.update(this.player);
    }

    public void setShader(ShaderType shaderType, CameraType cameraType) {
        Camera camera = this.player.getCamera();

        this.shader = AssetPool.getShader(shaderType.getName());

        this.shader.use();

        if (cameraType == CameraType.PERSPECTIVE) {
            this.shader.setMatrix4f("projectionViewMatrix", camera.getProjectionView());
        } else {
            this.shader.setMatrix4f("orthographicMatrix", camera.getOrthographic());
        }
    }

    public void setFog() {
        this.shader.setFloat("fogEnd", 50.0f);
        this.shader.setFloat("fogStart", 20.0f);

        this.shader.setVector3f("fogColour", new Vector3f(0.5f, 0.7f, 1.0f));
    }

    public void render() {
        this.setShader(ShaderType.WORLD, CameraType.PERSPECTIVE);

        this.setFog();
        this.shader.setVector3f("cameraPosition", this.getCamera().getPosition());

        this.chunkManager.render();

        Block block = this.player.getCurrentBlock();

        // AABB aabb = this.player.getAABB();

        // aabb.loadBuffer();
        // this.setShader(ShaderType.AABB);
        // aabb.render();
        if (block != null) {
            AABB aabb = block.getAABB();

            this.setShader(ShaderType.AABB, CameraType.PERSPECTIVE);

            aabb.loadBuffer();
            aabb.render();
        }

        glDisable(GL_DEPTH_TEST);
        this.setShader(ShaderType.CROSSHAIR, CameraType.ORTHOGRAPHIC);

        this.crosshair.loadBuffer();
        this.crosshair.render();
        glEnable(GL_DEPTH_TEST);
    }

    public void cleanup() {
        this.chunkManager.cleanup();
    }

    public Camera getCamera() {
        return this.player.getCamera();
    }

    public ChunkManager getChunkManager() {
        return this.chunkManager;
    }

    public Player getPlayer() {
        return this.player;
    }
}
