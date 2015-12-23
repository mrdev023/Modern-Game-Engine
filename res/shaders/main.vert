#version 330
#include "light.vert"
layout (location = 0) in vec3 in_position;
layout (location = 1) in vec4 in_color;
layout (location = 2) in vec2 in_coord_texture;
layout (location = 3) in vec3 in_normal;

uniform mat4 transform;
uniform mat4 projection;
uniform mat4 camera;


out vec4 color;
out vec3 normal;
out vec2 out_coord_texture;

void main(){
	color = in_color;
	out_coord_texture = in_coord_texture;
	normal = mat3(transform) * in_normal;
	normal = normalize(normal);
	gl_Position = projection * camera * transform * vec4(in_position,1.0f);
}
