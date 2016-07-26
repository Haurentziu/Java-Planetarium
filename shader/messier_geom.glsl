/* GEOMETRY SHADER */

#version 330


layout(points) in;
layout(triangle_strip, max_vertices = 4) out;

uniform float width;
uniform float height;
uniform float zoom;
uniform float radius = 0.005;

out float start;
out vec2 fragUV;
out vec3 frag_color;

in float draw[];

bool isInBounds(vec4 p){
    return p.x >= -width && p.x <= width && p.y >= -height && p.y <= height;
}

void main(){
    if(draw[0] > 0.5){
        vec4 center = gl_in[0].gl_Position;
        float sprite_width = 0.0155 / width;
        float sprite_height = 0.0155 / height;

        gl_Position = vec4(center.x - sprite_width, center.y - sprite_height , center.z, center.w);
        fragUV = vec2(0, 0);
        start = center.z;
        EmitVertex();

        gl_Position = vec4(center.x - sprite_width, center.y + sprite_height , center.z, center.w);
        fragUV = vec2(0, 1);
        start = center.z;
        EmitVertex();

        gl_Position = vec4(center.x + sprite_width, center.y - sprite_height , center.z, center.w);
        fragUV = vec2(1, 0);
        start = center.z;
        EmitVertex();

        gl_Position = vec4(center.x + sprite_width, center.y + sprite_height , center.z, center.w);
        fragUV = vec2(1, 1);
        start = center.z;
        EmitVertex();

        EndPrimitive();
    }
}