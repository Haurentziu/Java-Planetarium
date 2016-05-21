/*FRAGMENT SHADER*/
#version 330

out vec4 outputColor;

in vec2 fragUV;
uniform sampler2D myTexture;

void main(){
    outputColor = texture(myTexture, fragUV).rgba;
//    outputColor = vec4(1, 1, 1, 1);
}