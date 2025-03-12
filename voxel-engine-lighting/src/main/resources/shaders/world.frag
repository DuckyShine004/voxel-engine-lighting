#version 330 core

in vec2 outTextureCoordinate;

in vec3 outNormal;
in vec3 outVertexPosition;

in vec4 outVertexColour;

out vec4 fragmentColour;

flat in int outTextureIndex;

uniform float fogEnd;
uniform float fogStart;

uniform vec3 fogColour;
uniform vec3 cameraPosition;

uniform sampler2DArray textureArray;

float getFogFactor() {
    float fogDistance = length(outVertexPosition - cameraPosition);

    return clamp((fogStart - fogDistance) / (fogEnd - fogStart), 0.0, 1.0);
}

void main() {
    vec4 textureColour = texture(textureArray, vec3(outTextureCoordinate, outTextureIndex));

    if (textureColour.a < 0.1f) {
        discard;
    }

    // textureColour.rgb *= ambient;
    float ambientIntensity = 0.1f;
    vec3 ambient = ambientIntensity * vec3(1.0f, 1.0f, 1.0f);

    vec3 litColour = ambient * textureColour.rgb;

    vec3 finalColour = mix(fogColour, litColour, getFogFactor());

    fragmentColour = vec4(finalColour, textureColour.a);
};
