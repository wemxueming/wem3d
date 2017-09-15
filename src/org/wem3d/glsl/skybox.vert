#version 440
layout(location = 0) in vec3 vPosition;


layout(std140, binding = 0) uniform cameraUBO
{
    mat4 projection;
    mat4 view;
};

out vec3 texcoord;

void main()
{
    gl_Position = projection * view * vec4(vPosition, 1.0f);
    texcoord = vPosition;
}