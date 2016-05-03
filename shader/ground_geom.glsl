/* GEOMETRY SHADER */

#version 330

layout(triangles) in;
layout(triangle_strip, max_vertices = 3) out;

uniform float width;
uniform float height;

bool isInBounds(vec4 p){
    return p.x >= -width && p.x <= width && p.y >= -height && p.y <= height;
}

bool intesectsCircle(vec4 p1, vec4 p2, float r){

    float m = (p1.y - p2.y)/(p1.x - p2.x);
    float n = p2.y - m * p2.x;
    float distance = abs(n)/sqrt(m * m + 1);
    return distance <= r;
}

void main(){
    float radius = sqrt(width * width + height * height);
    bool is_visible = intesectsCircle(gl_in[0].gl_Position, gl_in[1].gl_Position, radius) &&
        intesectsCircle(gl_in[1].gl_Position, gl_in[2].gl_Position, radius) &&
            intesectsCircle(gl_in[0].gl_Position, gl_in[2].gl_Position, radius);

    if(is_visible){
        for(int i = 0; i < gl_in.length(); i++){
             gl_Position = gl_in[i].gl_Position;
             EmitVertex();
        }
    }

    EndPrimitive();
}