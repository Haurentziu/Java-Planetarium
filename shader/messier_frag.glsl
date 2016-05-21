/* FRAGMENT_SHADER */
#version 330

uniform float width;
uniform float height;

out vec4 outputColor;

void main(){
    gl_FragColor = vec4(0, 0.296, 0, 1.0);
 //    outputColor = texture(messierTex, fragmentUV).rgba;
}