#version 440
layout(location = 0) in vec3 vPosition;


layout(std140, binding = 0) uniform cameraUBO
{
    mat4 projection;
    mat4 view;
};

layout(std140, binding = 1) uniform transformUBO
{
    mat4 model;
};

void main()
{
    mat4 scaleModel = model;
    scaleModel[0][0] += 0.02f;
    scaleModel[1][1] += 0.02f;
    scaleModel[2][2] += 0.02f;

    gl_Position = projection * view * scaleModel * vec4(vPosition, 1.0f);
}