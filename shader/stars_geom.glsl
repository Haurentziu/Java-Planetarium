/* GEOMETRY SHADER */

#version 330


layout(points) in;
layout(triangle_strip, max_vertices = 28) out;

uniform float width;
uniform float height;
uniform float zoom;

in vec3 geom_color[];
in float draw[];

out vec3 frag_color;

bool isInBounds(vec4 p){
    return p.x >= -width && p.x <= width && p.y >= -height && p.y <= height;
}

void main(){

    vec4 center = gl_in[0].gl_Position;
    float radius = 18 * pow(1.4, -center.z) / 950;
    float zoomed_radius = (zoom/2 + 1) * radius / 2;

    if(isInBounds(center) && draw[0] > 0.5){
       for(float i =  0; i <= 6.5; i += 0.5){
            float x = center.x + zoomed_radius * cos(i) / width;
            float y = center.y + zoomed_radius * sin(i) / height;

            gl_Position = vec4(x, y, 0, 1);
            frag_color = geom_color[0];
            EmitVertex();

            gl_Position = vec4(center.xy, 0, 1);
            frag_color = geom_color[0];
            EmitVertex();
        }

    }
    EndPrimitive();
}
