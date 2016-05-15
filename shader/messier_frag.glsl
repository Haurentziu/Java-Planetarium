/* FRAGMENT_SHADER */
#version 330

uniform float width;
uniform float height;

in vec3 frag_color;
out vec4 outColor;


void main(){
    gl_FragColor = vec4(0, 0.296, 0, 1.0);
}