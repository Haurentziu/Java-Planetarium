#version 330

out vec4 outColor;
in vec2 fragUV;
in vec3 fragColor;

uniform sampler2D tex;

void main() {
    outColor = vec4(1, 1, 1, texture(tex, fragUV).a);
    //outColor = vec4(fragColor, 1);

}
