#version 330 core

layout(location = 0) in vec3 vertexPosition;

uniform mat4 projectionViewMatrix;

void main() {
    gl_Position = projectionViewMatrix * vec4(vertexPosition, 1.0f);
}
