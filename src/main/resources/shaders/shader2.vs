#version 330

layout (location=0) in vec3 position;
layout (location=1) in vec2 texCoord;

out vec2 outTexCoord;

uniform mat4 modelViewMatrix;
uniform mat4 projectionMatrix;


void main()
{

    gl_Position = projectionMatrix * modelViewMatrix * vec4(position, 1.0);


//   vec4 vec = projectionMatrix * worldMatrix * viewMatrix * vec4(position, 1.0);
 ////   vec4 saved = vec;

  ////  vec3 camPos12 = vec3(0,0,0);


 //   mat4 modelViewMatrix = viewMatrix * modelInstancedMatrix;
//	vec4 vs_mvVertexPos = modelViewMatrix * vec4(position, 1.0);
 //   gl_Position = projectionMatrix * vs_mvVertexPos;

    //   mat4 matr = mat4(1, 0, 0, 0, 0, cos(camRot.x), -sin(camRot.x), 0, 0, sin(camRot.x), cos(camRot.x), 0, 0, 0, 0, 0);
//    vec = saved * matr;\

  //    vec.y = saved.y *cos(camRot.x) + saved.z *sin(camRot.x);
 //     vec.z = saved.z *cos(camRot.x) - saved.y *sin(camRot.x);

//    vec.y = camPos12.y + (saved.y - camPos12.y)*cos(camRot.x)+ (saved.z - camPos12.z) *sin(camRot.x);
//    vec.z = camPos12.z + (saved.z - camPos12.z)*cos(camRot.x) - (saved.y - camPos12.y)*sin(camRot.x);

//    vec.y = camPos12.y + (vec.y - camPos12.y)*cos(camRot.x)+ (vec.z - camPos12.z) *sin(camRot.x);
//    vec.z = camPos12.z + (vec.z - camPos12.z)*cos(camRot.x) - (vec.y - camPos12.y)*sin(camRot.x);

//    y=y0 + (y - y0)*cos(L)+(z - z0) *sin(L);
//    z=z0 + -(y - y0)*sin(L)+(z - z0)*cos(L);

//    x=x0 + (x - x0)*cos(L)+(z - z0)*sin(L);
//    z=z0 + -(x - x0)*sin(L)+(z - z0)*cos(L);


//    X = x0 + (x - x0) * cos(a) - (y - y0) * sin(a);
//    Y = y0 + (y - y0) * cos(a) + (x - x0) * sin(a);

  //  gl_Position = vec;
    outTexCoord = texCoord;
}