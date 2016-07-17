/* FRAGMENT_SHADER */
#version 330

uniform float width;
uniform float height;

out vec4 outputColor;

in float start;
in vec2 fragUV;
uniform sampler2D messierTex;

void main(){
     vec2 real_tex_coord = vec2(start, 0) + (fragUV * vec2(1.0/6.0, 1));
     outputColor = vec4(0, 0.4, 0, texture(messierTex, real_tex_coord).a);
}