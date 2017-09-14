#version 440
layout(location = 0) in vec2 vPosition;
layout(location = 1) in vec2 vTexcoord;

out vec2 texcoord;

void main()
{
    gl_Position = vec4(vPosition.x, vPosition.y, 0.0f, 1.0f);
    texcoord = vTexcoord;
}