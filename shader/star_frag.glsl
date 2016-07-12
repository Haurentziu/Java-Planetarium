/* FRAGMENT_SHADER */
#version 330

uniform float width;
uniform float height;

in vec3 frag_color;
out vec4 outColor;
in vec2 fragUV;

uniform sampler2D starTex;

float dist_sq(vec2 p1, vec2 p2){
    return (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y);
}

void main(){
    gl_FragColor = vec4(frag_color, texture(starTex, fragUV).a);
}