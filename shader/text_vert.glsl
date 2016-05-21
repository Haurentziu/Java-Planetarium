/*VERTEX SHADER*/
#version 330

layout (location = 0) in vec3 position;
layout (location = 2) in vec2 vertUV;

out vec2 geomUV;

void main(void){
    gl_Position = vec4(position, 1);
    geomUV = vertUV;
}
