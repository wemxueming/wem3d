#version 440
layout(location = 0) in vec3 vPosition;
layout(location = 1) in vec2 vTexcoord;
layout(location = 2) in vec3 vNormal;
layout(location = 3) in vec3 vTangent;

layout(std140, binding = 0) uniform cameraUBO
{
    mat4 projection;
    mat4 view;
};

layout(std140, binding = 1) uniform transformUBO
{
    mat4 model;
};

out VS_OUT
{
    vec2 texcoord;
    vec3 normal;
    vec3 fragPos;
    vec3 viewPos;
    mat3 tbn;
    mat3 nbt;
};

uniform vec3 cameraPos;

void main()
{
    vec4 worldPos = model * vec4(vPosition, 1);
    gl_Position = projection * view * worldPos;

    normal = normalize(vec3(model * vec4(vNormal, 0.0)));
    vec3 tangent = normalize(vec3(model * vec4(vTangent, 0.0)));
    tangent = normalize(tangent -  dot(tangent, normal) * normal);
    vec3 bitangent = cross(tangent, normal);
    tbn = mat3(tangent, bitangent, normal);
    nbt = transpose(tbn);

    texcoord = vTexcoord;
    fragPos = worldPos.xyz;
    viewPos = cameraPos;
}