/* GEOMETRY SHADER */

#version 330


layout(points) in;
layout(triangle_strip, max_vertices = 4) out;

uniform float width;
uniform float height;
uniform float zoom;
uniform float aspect_ratio;
uniform int zoomable;

in vec3 geom_color[];
in float draw[];
in float tex_start[];

out vec3 frag_color;
out vec2 fragUV;

bool isInBounds(vec4 p){
    return p.x >= -width && p.x <= width && p.y >= -height && p.y <= height;
}

void main(){

    vec4 center = gl_in[0].gl_Position;
    float radius = 18 * pow(1.4, -center.z) / 950;
    float zoomed_radius;

    if(zoomable == 1){
        zoomed_radius = (zoom/2 + 1) * radius / 2;
    }
    else{
        zoomed_radius = radius;
    }

    float sprite_width = zoomed_radius * aspect_ratio / width;
    float sprite_height = zoomed_radius / height;


    if(isInBounds(center) && draw[0] > 0.5){

        gl_Position = vec4(center.x - sprite_width, center.y - sprite_height , 0, center.w);
        fragUV = vec2(tex_start[0], 0);
        frag_color = geom_color[0];
        EmitVertex();

        gl_Position = vec4(center.x - sprite_width, center.y + sprite_height , 0, center.w);
        fragUV = vec2(tex_start[0], 1);
        frag_color = geom_color[0];
        EmitVertex();

        gl_Position = vec4(center.x + sprite_width, center.y - sprite_height , 0, center.w);
        fragUV = vec2(tex_start[0] + 1.0 / 8.0, 0);
        frag_color = geom_color[0];
        EmitVertex();

        gl_Position = vec4(center.x + sprite_width, center.y + sprite_height , 0, center.w);
        fragUV = vec2(tex_start[0] + 1.0 / 8.0, 1);
        frag_color = geom_color[0];
        EmitVertex();

        EndPrimitive();
    }

    else{
        EndPrimitive();
    }


}