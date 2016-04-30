/* GEOMETRY SHADER */

#version 330


layout(points) in;
layout(triangle_strip, max_vertices = 28) out;

uniform float width;
uniform float height;
uniform float zoom;

bool isInBounds(vec4 p){
    return p.x >= -width && p.x <= width && p.y >= -height && p.y <= height;
}

void main(){

    vec4 center = gl_in[0].gl_Position;
    float radius = center.z / 2;

    if(isInBounds(center)){
        float i;

       for(i =  0; i <= 6.5; i += 0.5){
            float x = center.x + 0.5*log2(2 * zoom) * radius * cos(i) / width;
            float y = center.y + 0.5*log2(2 * zoom) * radius * sin(i) / height;

            gl_Position = vec4(x, y, 0, 1);
            EmitVertex();

            gl_Position = center;
            EmitVertex();
        }
    }

    EndPrimitive();
}
