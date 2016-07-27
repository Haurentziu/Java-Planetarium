/* FRAGMENT_SHADER */
#version 330

uniform float width;
uniform float height;

out vec4 outputColor;

in float start;
in vec2 fragUV;
uniform sampler2D messierTex;

void main(){
     outputColor = vec4(0, 0.4, 0, texture(messierTex, fragUV).a);
}