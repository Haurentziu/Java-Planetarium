#version 330

layout(location = 0) in vec3 pos;
layout(location = 2) in vec3 uv;

out vec2 geomUV;

void main() {
    gl_Position = vec4(pos, 1);
    geomUV = uv.xy;
}
