#version 400 core
//vao inputs
in vec2 pass_uvs;
//uniform inputs
uniform sampler2D textureSampler;
//output
out vec4 out_Color;

void main(void)
{
     vec4 texel = texture(textureSampler, pass_uvs);
     if(texel.a == 0.0) //Only works with Point Filtering, Change to 0.5 if Linear Filtering
        discard;
     out_Color = texel;
}