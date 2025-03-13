package com.duckyshine.app.scene;

import org.joml.Vector3i;

import com.duckyshine.app.debug.Debug;
import com.duckyshine.app.math.noise.Noise;
import com.duckyshine.app.math.noise.SimplexNoise;

public class HeightMap {
    int width;
    int depth;

    int[][] heights;

    public HeightMap(int width, int depth) {
        this.width = width;
        this.depth = depth;

        this.heights = new int[depth][width];
    }

    public void generate(Vector3i chunkPosition) {
        for (int dz = 0; dz < this.depth; dz++) {
            for (int dx = 0; dx < this.width; dx++) {
                double offsetX = (double) (chunkPosition.x + dx);
                double offsetZ = (double) (chunkPosition.z + dz);

                this.heights[dz][dx] = Noise.getNoise2d(offsetX, offsetZ);
            }
        }
    }

    public int[][] getHeights() {
        return this.heights;
    }
}
