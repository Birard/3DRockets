#version 330

in  vec2 outTexCoord;
out vec4 fragColor;

uniform sampler2D texture_sampler;

void main()
{
      //gl_FragColor = vec4(1*outTexCoord.y ,1*outTexCoord.x ,1 ,0 );
    gl_FragColor = texture(texture_sampler, outTexCoord);
}