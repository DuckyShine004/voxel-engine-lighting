#version 330 core

in vec2 outTextureCoordinate;

in vec3 outVertexPosition;

in vec4 outVertexColour;

in vec3 outNormal;

out vec4 fragmentColour;

flat in int outTextureIndex;

uniform float fogEnd;
uniform float fogStart;

uniform vec3 fogColour;
uniform vec3 cameraPosition;

uniform sampler2DArray textureArray;

float getExponentialFogFactor() {
    float fogDistance = length(outVertexPosition - cameraPosition);

    float fogFactor = exp2(-0.0005 * fogDistance * fogDistance);

    return clamp(fogFactor, 0.0f, 1.0f);
}

float getLinearFogFactor() {
    float fogDistance = length(outVertexPosition - cameraPosition);

    float fogFactor = (fogEnd - fogDistance) / (fogEnd - fogStart);

    return clamp(fogFactor, 0.0f, 1.0f);
}

void main() {
    vec4 textureColour = texture(textureArray, vec3(outTextureCoordinate, outTextureIndex));

    if (textureColour.a < 0.1f) {
        discard;
    }

    float ambientIntensity = 0.5f;
    vec3 ambient = ambientIntensity * vec3(1.0f);

    vec3 lightDirection = vec3(0.8f, 1.0f, 0.7f);
    lightDirection = normalize(-lightDirection);

    float diffuseIntensity = max(dot(outNormal, lightDirection), 0.0f);
    vec3 diffuse = diffuseIntensity * vec3(1.0f);

    vec3 lightColour = (ambient + diffuse) * textureColour.rgb;

    vec3 finalColour = mix(fogColour, lightColour, getExponentialFogFactor());

    fragmentColour = vec4(finalColour, textureColour.a);
};
