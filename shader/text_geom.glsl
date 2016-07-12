/*GEOMTERY SHADER*/
#version 330

layout(points) in;
layout(triangle_strip, max_vertices = 4) out;

in vec2 geomUV[];
out vec2 fragUV;

void main(){
    vec4 center = gl_in[0].gl_Position;
    float size = 0.05;

    gl_Position = vec4(center.x - size, center.y - size, center.z, center.w);
    fragUV = vec2(0, 0);
    EmitVertex();

    gl_Position = vec4(center.x - size, center.y + size, center.z, center.w);
    fragUV = vec2(0, 1);
    EmitVertex();

    gl_Position = vec4(center.x + size, center.y - size, center.z, center.w);
    fragUV = vec2(1, 0);
    EmitVertex();

    gl_Position = vec4(center.x + size, center.y + size, center.z, center.w);
    fragUV = vec2(1, 1);
    EmitVertex();

    EndPrimitive();
}