package com.duckyshine.app.math.noise;

import com.duckyshine.app.math.RandomNumber;

public class Noise {
    private static final long SEED = RandomNumber.getRandomLong();

    private static final int OCTAVES = 3;

    private static final double PERSISTENCE = 0.5f;

    private static final double LACUNARITY = 2.0f;

    private static final double FREQUENCY = 0.01f;

    private static final double SCALE = 48.0f;

    public static int getNoise2d(int x, int z) {
        return Noise.getNoise2d((double) x, (double) z);
    }

    public static double getRawNoise2d(double x, double z) {
        double totalNoise = 0.0d;
        double totalAmplitude = 0.0d;

        double amplitude = 1.0d;
        double frequency = Noise.FREQUENCY;

        for (int i = 0; i < Noise.OCTAVES; i++) {
            double noise = Noise.getSimplexNoise2d(x * frequency, z * frequency);

            totalNoise += noise * amplitude;

            totalAmplitude += amplitude;

            amplitude *= Noise.PERSISTENCE;

            frequency *= Noise.LACUNARITY;
        }

        double normalisedNoise = totalNoise / totalAmplitude;

        return (normalisedNoise + 1.0f) * 0.5f * SCALE;
    }

    public static int getNoise2d(double x, double z) {
        return (int) Math.ceil(Noise.getRawNoise2d(x, z));
    }

    public static double getRawNoise3d(double x, double y, double z) {
        return Noise.getSimplexNoise3d(x * 0.09, y * 0.09, z * 0.09);
    }

    public static int getNoise3d(double x, double y, double z) {
        return (int) Math.ceil(Noise.getRawNoise3d(x, y, z));
    }

    // Second model
    // public static int getNoise2d(double x, double z) {
    // double amplitude = 32.0d;

    // double frequency = 0.005d;

    // double totalNoise = 0.0d;

    // for (int i = 1; i <= 3; i++) {
    // int offset = (1 << i);

    // double currentAmplitude = amplitude * (1 / offset);
    // double currentFrequency = frequency * offset;

    // double noise = amplitude * Noise.getSimplexNoise2d(x * frequency, z *
    // frequency);

    // if ((i & 1) == 1) {
    // noise += currentAmplitude;
    // } else {
    // noise -= currentAmplitude;
    // }

    // totalNoise += noise;
    // }

    // return (int) totalNoise;
    // }

    private static double getSimplexNoise2d(double x, double z) {
        return SimplexNoise.noise2(Noise.SEED, x, z);
    }

    // Performance improves slightly, at the cost of worse terrain generation
    private static double getFastSimplexNoise2d(double x, double z) {
        return FastSimplexNoise.noise2(Noise.SEED, x, z);
    }

    private static double getSimplexNoise3d(double x, double y, double z) {
        return SimplexNoise.noise3_Fallback(Noise.SEED, x, y, z);
    }

    public static long getSeed() {
        return Noise.SEED;
    }
}
