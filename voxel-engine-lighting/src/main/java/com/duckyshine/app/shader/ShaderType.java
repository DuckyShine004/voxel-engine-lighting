package com.duckyshine.app.shader;

public enum ShaderType {
    WORLD,
    AABB,
    CROSSHAIR;

    public String getName() {
        return this.name().toLowerCase();
    }
}
