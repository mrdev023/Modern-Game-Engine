#version 330
layout (location = 0) in vec3 in_position;
layout (location = 1) in vec4 in_color;
layout (location = 2) in vec2 in_coord_texture;

uniform mat4 projection;

out vec4 color;
out vec2 out_coord_texture;

void main(){
	color = in_color;
	out_coord_texture = in_coord_texture;
	gl_Position = projection * vec4(in_position,1.0f);
}
