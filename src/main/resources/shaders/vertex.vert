#version 400 core

//vao inputs
layout (location = 0) in vec3 position;
layout (location = 1) in vec2 uvs;
layout (location = 2) in vec3 normals;

struct SpotLight
{
    vec3 color;
    vec3 position;
    float intensity;
};

//uniform inputs
uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix; // inverse of camera's position
uniform SpotLight spotLight;

//output
out vec2 pass_uvs;
out vec3 surfaceNormal;
out vec3 toLightVector;
out vec3 unitCameraVector;

void main(void)
{
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPosition;
    pass_uvs = uvs;

    surfaceNormal = (transformationMatrix * vec4(normals,0.0)).xyz;
    toLightVector = spotLight.position - worldPosition.xyz;
    unitCameraVector = normalize((inverse(viewMatrix) * vec4(0.0,0.0,0.0,1.0)).xyz - worldPosition.xyz);
}