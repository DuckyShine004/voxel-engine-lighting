#version 330 core

layout(location = 0) in vec3 vertexPosition;
layout(location = 1) in vec2 textureCoordinate;
layout(location = 2) in int textureId;
layout(location = 3) in int normalId;

out vec2 outTextureCoordinate;

out vec3 outNormal;
out vec3 outVertexPosition;

out vec4 outVertexColour;

flat out int outTextureIndex;

uniform mat4 projectionViewMatrix;

const vec3 NORMALS[6] = vec3[6](
        vec3(0, 1, 0),
        vec3(0, -1, 0),
        vec3(-1, 0, 0),
        vec3(1, 0, 0),
        vec3(0, 0, 1),
        vec3(0, 0, -1)
    );

void main() {
    gl_Position = projectionViewMatrix * vec4(vertexPosition, 1.0);

    outVertexPosition = vertexPosition;

    outNormal = NORMALS[normalId];

    outTextureIndex = textureId;

    outTextureCoordinate = textureCoordinate;

    outVertexColour = vec4(1.0, 1.0, 1.0, 1.0);
};
