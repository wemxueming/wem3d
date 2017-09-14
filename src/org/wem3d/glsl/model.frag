#version 440
#define MAX_LIGHT 10
out vec4 color;

struct LightInfo
{
    vec3 color;
    float brightness;
    vec3 position;
    float cutoff;
    vec3 direction;
    float outcutoff;
    vec3 attenuation;
};

struct MaterialInfo
{
    vec3 albedo;
    float metallic;

    float roughness;
    float ao;
    vec2 wrap;

    bool isAlbedoMap;
    bool isMetallicMap;
    bool isRoughnessMap;
    bool isAoMap;

    bool isNormalMap;
    bool isHeightMap;
    float heightScale;
};

struct Material
{
    vec3 albedo;
    float metallic;
    float roughness;
    float ao;
    vec3 normal;
    vec2 coord;
    float height;
    float heightScale;
    vec2 wrap;
};

struct Ray
{
    vec3 radiance;
    vec3 direction;
    float attenuation;
};

layout(std140, binding = 2) uniform lightUBO
{
    int lightSize;
    LightInfo[MAX_LIGHT] lightInfos;
};

layout(std140, binding = 3) uniform materialUBO
{
    MaterialInfo materialInfo;
};

in VS_OUT
{
    vec2 texcoord;
    vec3 normal;
    vec3 fragPos;
    vec3 viewPos;
    mat3 tbn;
    mat3 nbt;
};

uniform sampler2D albedoMap;
uniform sampler2D metallicMap;
uniform sampler2D roughnessMap;
uniform sampler2D aoMap;
uniform sampler2D normalMap;
uniform sampler2D heightMap;

#include src/org/wem3d/glsl/model.glsl

void main()
{
    vec3 view = normalize(viewPos - fragPos);

    Material material = createMaterial(materialInfo, albedoMap, metallicMap, roughnessMap, aoMap, normalMap, heightMap, texcoord, normal, view, tbn, nbt);

    vec4 modelColor = vec4(0,0,0,1);
    for(int i = 0; i < lightSize; i ++)
    {
        Ray ray = createRay(lightInfos[i], fragPos);
        modelColor += brdf(material, ray, view, nbt);
    }



    color = modelColor;
}