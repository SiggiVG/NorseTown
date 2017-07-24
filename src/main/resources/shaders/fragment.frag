#version 400 core

//vao inputs
in vec2 pass_uvs;
in vec3 surfaceNormal;
in vec3 toLightVector; //the position of the spotlight relative to worldposition
in vec3 toCameraVector;

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

//uniform inputs
uniform sampler2D textureSampler; //
uniform vec3 ambientLight;
uniform DirectionalLight directionalLight;
uniform SpotLight spotLight;
uniform float shineDamper;
uniform float reflectivity;

//output
out vec4 out_Color;

void main(void)
{
    //determine the pixel on the texture
    vec4 texel = texture(textureSampler, pass_uvs);
    //exit if completely transparent
    if(texel.a == 0.0) //Only works for point filtering, change to 0.5 if Linear Filtering
        discard;

    //Normalize vectors
    vec3 unitNormal = normalize(surfaceNormal);
    vec3 unitLightVector = normalize(toLightVector); //this works for spotlights
    vec3 unitVectorToCamera = normalize(toCameraVector);

    //diffuse lighting
    float nDot1 = dot(unitNormal,unitLightVector);
    float brightness = max(nDot1, 0.0); //rather than use an ambient light vector, I can set the second parameter higher
    vec3 diffuse = brightness * spotLightColor;

    //specular lighting
    vec3 specular = vec3(0.0,0.0,0.0);
    if(reflectivity > 0.0) //check if object is reflective at all
    {

        vec3 lightDirection = -unitLightVector;
        vec3 reflrectedLightDirection = reflect(lightDirection, unitNormal);
        float specularFactor = dot(reflrectedLightDirection, unitVectorToCamera);
        specularFactor = max(specularFactor,0.0);
        float dampedFactor = pow(specularFactor, shineDamper);
        specular = dampedFactor * reflectivity * spotLightColor;
    }

    out_Color = vec4(diffuse+ambientLight+specular,1.0) * texel;//) + vec4(finalSpecular, 1.0);
}
