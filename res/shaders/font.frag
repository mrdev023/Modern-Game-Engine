#version 330

in vec4 color;
in vec2 out_coord_texture;
uniform sampler2D myTexture;


out vec4 frag_color;

void main(){
	frag_color = color * texture2D(myTexture,out_coord_texture);
}