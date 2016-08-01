#version 330

layout(triangles) in;
layout(triangle_strip, max_vertices = 3) out;

in vec2 geomUV[];
out vec2 fragUV;

uniform float scaleX = 1;
uniform float scaleY = 1;

out vec3 fragColor;

void main() {
    vec4 scale = vec4(scaleX, scaleY, 1, 1);
    /*vec4 uaie = gl_in[0].gl_Position + gl_in[0].gl_Position * scale;
    gl_Position = uaie;
    fragUV = geomUV[0];
    EmitVertex();

    gl_Position = uaie - (gl_in[0].gl_Position -  gl_in[1].gl_Position) * scale;
    fragUV = geomUV[1];
    EmitVertex();


    gl_Position = uaie - (gl_in[0].gl_Position -  gl_in[2].gl_Position) * scale;
    fragUV = geomUV[2];
    EmitVertex();*/



    gl_Position = gl_in[0].gl_Position;
    fragUV = geomUV[0];
    EmitVertex();

    gl_Position = gl_in[1].gl_Position;
    fragUV = geomUV[1];
    EmitVertex();

    gl_Position = gl_in[2].gl_Position;
    fragUV = geomUV[2];
    EmitVertex();



}
