#version 400 core

//vao inputs
in vec3 position;
in vec2 uvs;
in vec3 normals;

//uniform inputs
uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform vec3 lightPosition;

//output
out vec2 pass_uvs;
out vec3 surfaceNormal;
out vec3 toLightVector;

void main(void)
{
    vec4 worldPosition = transformationMatrix * vec4(position, 1.0);
    gl_Position = projectionMatrix * viewMatrix * worldPosition;
    pass_uvs = uvs;

    surfaceNormal = (transformationMatrix * vec4(normals,0.0)).xyz;
    toLightVector = lightPosition - worldPosition.xyz;
}
