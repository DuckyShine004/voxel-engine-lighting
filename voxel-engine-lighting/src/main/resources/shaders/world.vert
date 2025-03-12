#version 330 core

layout(location = 0) in vec3 vertexPosition;
layout(location = 1) in vec2 textureCoordinate;
layout(location = 2) in int textureId;

out vec2 outTextureCoordinate;

out vec4 outVertexColour;

out vec3 outVertexPosition;

flat out int outTextureIndex;

uniform mat4 projectionViewMatrix;

void main() {
    gl_Position = projectionViewMatrix * vec4(vertexPosition, 1.0);

    outVertexPosition = vertexPosition;

    outTextureIndex = textureId;

    outTextureCoordinate = textureCoordinate;

    outVertexColour = vec4(1.0, 1.0, 1.0, 1.0);
};
