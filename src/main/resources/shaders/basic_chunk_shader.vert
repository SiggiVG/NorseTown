#version 400 core
//vao inputs
in vec3 position;
in vec2 uvs;
//uniform inputs
uniform mat4 transformationMatrix;
uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
//output
out vec2 pass_uvs;

void main(void)
{
    gl_Position = projectionMatrix * viewMatrix * transformationMatrix * vec4(position, 1.0);
    pass_uvs = uvs;

}
