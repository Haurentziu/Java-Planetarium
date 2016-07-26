/* GEOMETRY SHADER */

#version 330

layout(triangles) in;
layout(triangle_strip, max_vertices = 3) out;

uniform float width;
uniform float height;

in float draw[];


bool intesectsCircle(vec4 p1, vec4 p2, float r){

    float m = (p1.y - p2.y)/(p1.x - p2.x);
    float n = p2.y - m * p2.x;
    float distance = abs(n)/sqrt(m * m + 1);
    return distance < r;
}

void main(){
    float radius = sqrt(width * width + height * height);
    bool is_visible = draw[0] > 0.5 && draw[1] > 0.5 && draw[2] > 0.5;

    if(is_visible){
        for(int i = 0; i < gl_in.length(); i++){
             gl_Position = gl_in[i].gl_Position;
             EmitVertex();
        }
    }

    EndPrimitive();
}