#version 330

layout(location = 0) in vec3 pos;
layout(location = 1) in vec3 color;
layout(location = 2) in vec3 uv;

out vec2 geomUV;
out vec3 geomColor;

void main() {
    gl_Position = vec4(pos, 1);
    geomColor = color;
    geomUV = uv.xy;
}
