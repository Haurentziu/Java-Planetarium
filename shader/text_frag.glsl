/* FRAGMENT_SHADER */
#version 330

uniform float width;
uniform float height;

out vec4 outputColor;

in float start;
in vec2 fragUV;
in vec3 frag_color;
uniform sampler2D textTex;


void main(){
     vec2 real_tex_coord = vec2(0, start) + (fragUV * vec2(1, 1.0/9.0));
     outputColor = vec4(frag_color, texture(textTex, real_tex_coord).a);
}