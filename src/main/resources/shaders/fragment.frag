#version 400 core

//vao inputs
in vec2 pass_uvs;
in vec3 surfaceNormal;
in vec3 toLightVector;
in vec3 unitCameraVector;

struct DirectionalLight
{
    vec3 color;
    vec3 direction;
    float intensity;
};

struct SpotLight
{
    vec3 color;
    vec3 position;
    float intensity;
};

struct MeshSpecular
{
    float shineDamper;
    float reflectivity;
};

//uniform inputs
uniform sampler2D textureSampler;
uniform MeshSpecular meshSpecularProps;
uniform vec3 ambientLight;
uniform DirectionalLight directionalLight;
uniform SpotLight spotLight;

//output
out vec4 out_Color;

vec3 calcDiffuse(vec3 unitNormal, vec3 unitLightVector, vec3 color)
{
    float nDot1 = dot(unitNormal,unitLightVector);
    float brightness = max(nDot1, 0.0);
    return brightness * color;
}

vec3 calcSpecular(vec3 lightDirection, vec3 unitNormal, vec3 color)
{
    vec3 reflectedLightDirection = reflect(lightDirection,  unitNormal);
    float specularFactor = dot(reflectedLightDirection, unitCameraVector);
    specularFactor = clamp(specularFactor, 0.0, 1.0);
    float dampedFactor = pow(specularFactor, meshSpecularProps.shineDamper);
    return dampedFactor * meshSpecularProps.reflectivity * color;
}

void main(void)
{
    vec4 texel = texture(textureSampler, pass_uvs);
    //exit if completely transparent
    if(texel.a == 0.0) //Only works for point filtering, change to 0.5 if Linear Filtering
        discard;

    /*vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector);

    vec3 diffuse = calcDiffuse(unitNormal, unitLightVector, spotLight.color) + calcDiffuse(unitNormal, -normalize(directionalLight.direction), directionalLight.color);

    vec3 specular = vec3(0.0);
    if(meshSpecularProps.reflectivity > 0.0)
    {
        specular = calcSpecular(-unitLightVector, unitNormal, spotLight.color) + calcSpecular(unitNormal, -normalize(directionalLight.direction), directionalLight.color);
    }*/

    out_Color = /*vec4(diffuse+specular+ambientLight,1.0) * */texel;
}

