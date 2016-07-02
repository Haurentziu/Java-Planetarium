/* GEOMETRY SHADER */

#version 330


layout(points) in;
layout(triangle_strip, max_vertices = 4) out;

uniform float width;
uniform float height;
uniform float zoom;

in vec3 geom_color[];
in float draw[];

out vec3 frag_color;
out vec2 fragUV;

bool isInBounds(vec4 p){
    return p.x >= -width && p.x <= width && p.y >= -height && p.y <= height;
}

void main(){

    vec4 center = gl_in[0].gl_Position;
    float radius = 18 * pow(1.4, -center.z) / 950;
    float zoomed_radius = (zoom/2 + 1) * radius / 2;

    float sprite_width = zoomed_radius / width;
    float sprite_height = zoomed_radius / height;

  /*  float sprite_width = 0.1155 ;
    float sprite_height = 0.1155 ;*/

    if(isInBounds(center) && draw[0] > 0.5){
     /*  for(float i =  0; i <= 6.5; i += 0.5){
            float x = center.x + zoomed_radius * cos(i) / width;
            float y = center.y + zoomed_radius * sin(i) / height;

            gl_Position = vec4(x, y, 0, 1);
            frag_color = geom_color[0];
            EmitVertex();

            gl_Position = vec4(center.xy, 0, 1);
            frag_color = geom_color[0];
            EmitVertex();
        }*/

        gl_Position = vec4(center.x - sprite_width, center.y - sprite_height , 0, center.w);
        fragUV = vec2(0, 0);
        frag_color = geom_color[0];
        EmitVertex();

        gl_Position = vec4(center.x - sprite_width, center.y + sprite_height , 0, center.w);
        fragUV = vec2(0, 1);
        frag_color = geom_color[0];
        EmitVertex();

        gl_Position = vec4(center.x + sprite_width, center.y - sprite_height , 0, center.w);
        fragUV = vec2(1, 0);
        frag_color = geom_color[0];
        EmitVertex();

        gl_Position = vec4(center.x + sprite_width, center.y + sprite_height , 0, center.w);
        fragUV = vec2(1, 1);
        frag_color = geom_color[0];
        EmitVertex();

        EndPrimitive();

    }
}