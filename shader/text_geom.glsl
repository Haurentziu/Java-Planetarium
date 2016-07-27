/* GEOMETRY SHADER */

#version 330


layout(points) in;
layout(triangle_strip, max_vertices = 4) out;

uniform float width;
uniform float height;
uniform float zoom;

const float aspect_ratio = 9.0 * 111 / 180;
const float size = 0.0175;
const float offset = 0.016;

in vec3 geom_color[];
out float start;
out vec3 frag_color;
out vec2 fragUV;

in float draw[];

bool isInBounds(vec4 p){
    return p.x >= -width && p.x <= width && p.y >= -height && p.y <= height;
}

void main(){
    if(draw[0] > 0.5){
        float normalised_zoom = zoom / 2 + 1;

        vec4 center = gl_in[0].gl_Position + vec4(normalised_zoom * offset / width, 0, 0, 0);

        float sprite_width = size * aspect_ratio / width;
        float sprite_height = size / height;

        gl_Position = vec4(center.x, center.y - sprite_height , center.z, center.w);
        fragUV = vec2(0, 0);
        frag_color = geom_color[0];
        start = center.z;
        EmitVertex();

        gl_Position = vec4(center.x, center.y + sprite_height , center.z, center.w);
        fragUV = vec2(0, 1);
        frag_color = geom_color[0];
        start = center.z;
        EmitVertex();

        gl_Position = vec4(center.x + sprite_width, center.y - sprite_height , center.z, center.w);
        fragUV = vec2(1, 0);
        frag_color = geom_color[0];
        start = center.z;
        EmitVertex();

        gl_Position = vec4(center.x + sprite_width, center.y + sprite_height , center.z, center.w);
        fragUV = vec2(1, 1);
        frag_color = geom_color[0];
        start = center.z;
        EmitVertex();

        EndPrimitive();
    }
}