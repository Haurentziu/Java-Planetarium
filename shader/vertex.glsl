/* VERTEX SHADER */
#version 330

#define obliquity 0.4084071

uniform float altitude_rotation;
uniform float azimuth_rotation;
uniform float zoom;

uniform float sideral_time;
uniform float observer_latitude;
uniform float observer_longitude;

uniform float width;
uniform float height;

uniform float max_mag;
uniform int vertex_type;
uniform int transform_type = 1; //0: horizontal -> projection, 1: equatorial -> projection

/*
number  |   transformation
--------|----------------------------
0       |horizontal -> projection
1       |equatorial -> projection
2       |ecliptical -> projection
*/

uniform int projection_type = 0;

/*
number  |   type
--------|-----------------
0       |   stereographic
1       |   gnomonic
*/

layout(location = 0) in vec3 pos;
layout(location = 1) in vec3 color;

out vec3 geom_color;
out float draw;


void computeStereographic(vec4 coord, out vec4 projection){
    float k = 2 / (1 + sin(altitude_rotation) * sin(coord.y) + cos(altitude_rotation) * cos(coord.y) * cos(coord.x - azimuth_rotation));
    float x = k * cos(coord.y) * sin(coord.x - azimuth_rotation);
    float y = k * (cos(altitude_rotation) * sin(coord.y) - sin(altitude_rotation) * cos(coord.y) * cos(coord.x - azimuth_rotation));
    projection = vec4(x, y, coord.z, coord.w);
}

void computeGnomoic(vec4 coord, out vec4 projection){
    float c = sin(altitude_rotation) * sin(coord.y) + cos(coord.y) * cos(altitude_rotation) * cos(coord.x - azimuth_rotation);
    float x = cos(coord.y) * sin(coord.x - azimuth_rotation) / c;
    float y = (cos(altitude_rotation) * sin(coord.y) - sin(altitude_rotation) * cos(coord.y) * cos(coord.x - azimuth_rotation))/c;
    projection = vec4(x, y, coord.z, coord.w);
}

void computeLambertAEA(vec4 coord, out vec4 projection){
    float k = sqrt(2 / (1 + sin(altitude_rotation) * sin(coord.y) + cos(altitude_rotation) * cos(coord.y) * cos(coord.x - azimuth_rotation)));
    float x = k * cos(coord.y) * sin(coord.x - azimuth_rotation);
    float y = k * (cos(altitude_rotation) * sin(coord.y) - sin(altitude_rotation) * cos(coord.y) * cos(coord.x - azimuth_rotation));
    projection = vec4(x, y, coord.z, coord.w);
}

void main(void){
    if(vertex_type != 1 || pos.z < max_mag){
        geom_color = color;
        vec4 coord = vec4(pos, 1);
        if(transform_type == 2){
            float declination = asin(sin(coord.y) * cos(obliquity) + cos(coord.y) * sin(obliquity) * sin(coord.x));
            float right_ascension = atan(sin(coord.x) * cos(obliquity) - tan(coord.y) * sin(obliquity), cos(coord.x));
            coord = vec4(right_ascension, declination, coord.z, 1);
        }

        if(transform_type > 0){
            float hour_angle = sideral_time - coord.x - observer_longitude;
            float altitude = asin(sin(coord.y) * sin(observer_latitude) + cos(coord.y) * cos(observer_latitude) * cos(hour_angle));
            float azimuth = atan(sin(hour_angle), cos(hour_angle) * sin(observer_latitude) - tan(coord.y) * cos(observer_latitude));
            coord = vec4(azimuth, altitude, coord.z, 1);
        }

        vec4 projection;
        computeStereographic(coord, projection);
        draw = 1.0;
        gl_Position = vec4(zoom * projection.x / width, zoom * projection.y / height, projection.z, projection.w);
    }

    else{
        geom_color = vec3(0, 0, 0);
        draw  = 0.0;
        gl_Position = vec4(0, 0, 0, 0);
    }

}  

