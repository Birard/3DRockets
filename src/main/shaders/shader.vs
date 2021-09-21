 #version 330

 attribute vec3 vertices;
 attribute vec2 textures;

 varying vec2 tex_coords;

 uniform mat4 projection;

 void main ()
 {

    gl_Position = projection * vec4(vertices, 1.0);
    tex_coords = textures;
 }