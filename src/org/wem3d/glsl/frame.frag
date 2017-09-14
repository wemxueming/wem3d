#version 440
out vec4 color;
in vec2 texcoord;
uniform sampler2D screenTexture;
void main()
{
    color = texture(screenTexture, texcoord);
}

//void main()
//{
//    //颜色反转
//    color = vec4(vec3(1.0 - texture(screenTexture, texcoord)), 1.0);
//}

//void main()
//{
//    //黑白色调
//    color = texture(screenTexture, texcoord);
//    float average = 0.2126 * color.r + 0.7152 * color.g + 0.0722 * color.b;
//    color = vec4(average, average, average, 1.0);
//}