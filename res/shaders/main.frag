#version 330

in vec4 color;
in vec3 normal;
in vec2 out_coord_texture;
uniform sampler2D myTexture;

uniform vec3 light_direction;
uniform vec4 diffuse_light_color;
uniform vec4 ambient_light;


void main(){
	vec3 lightDir;
	float lightIntensity;
	
	// Set the default output color to the ambient light value for all pixels.
	vec4 color1 = color * ambient_light;
	
	// Invert the light direction for calculations.
	lightDir = -light_direction;

	// Calculate the amount of light on this pixel.
	lightIntensity = clamp(dot(normal, lightDir), 0.0f, 1.0f);
	
	if(lightIntensity > 0.0f)
	{
		// Determine the final diffuse color based on the diffuse color and the amount of light intensity.
		color1 += (diffuse_light_color * lightIntensity);
	}

	// Clamp the final light color.
	color1 = clamp(color1, 0.0f, 1.0f);
	
	gl_FragColor = color1 * texture2D(myTexture,out_coord_texture);
}