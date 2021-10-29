#version 330

uniform sampler2D sampler;

varying vec2 tex_coords;

void main()
{
      gl_FragColor = texture(sampler, tex_coords);
   // gl_FragColor = vec4(1*tex_coords.y ,1*tex_coords.x ,1 ,1 );
}