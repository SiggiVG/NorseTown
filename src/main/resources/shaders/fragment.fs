#version 400 core

//vao inputs
in vec2 pass_uvs;
in vec3 surfaceNormal;
in vec3 toLightVector;

struct DirectionalLight
{
    vec3 color;
    vec3 direction;
    float intensity;
};

//uniform inputs
uniform sampler2D textureSampler;
uniform vec3 ambientLight;
uniform DirectionalLight directionalLight;
uniform vec3 spotLightColor;

//output
out vec4 out_Color;

void main(void)
{
    vec4 texel = texture(textureSampler, pass_uvs);
    //exit if completely transparent
    if(texel.a == 0.0) //Only works for point filtering, change to 0.5 if Linear Filtering
        discard;

    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);

    float nDot1 = dot(unitNormal,unitLightVector);
    float brightness = max(nDot1, 0.0);
    vec3 diffuse = brightness * spotLightColor;

    out_Color = vec4(diffuse+ambientLight,1.0) * texel;
}


