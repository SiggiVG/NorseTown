#version 400 core
//vao inputs
in vec2 pass_uvs;
//uniform inputs
uniform sampler2D textureSampler;
//output
out vec4 out_Color;

void main(void)
{
    out_Color = texture(textureSampler, pass_uvs);
}