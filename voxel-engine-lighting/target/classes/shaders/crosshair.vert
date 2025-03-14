#version 330 core

layout(location = 0) in vec2 vertexPosition;

uniform mat4 orthographicMatrix;

void main() {
    gl_Position = orthographicMatrix * vec4(vertexPosition, 0.0f, 1.0f);
}
