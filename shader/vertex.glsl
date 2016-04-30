/* VERTEX SHADER */
#version 330
uniform float altitude_rotation;
uniform float azimuth_rotation;
uniform float zoom;

uniform float sideral_time;
uniform float observer_latitude;
uniform float observer_longitude;

uniform float width;
uniform float height;

uniform int transform_type; //0: horizontal -> projection, 1: equatorial -> projection

in vec4 pos;

void main(void){
    vec4 coord = pos;

 /*   if(transform_type == 1){
        float hour_angle = sideral_time - coord.x - observer_longitude;
        float altitude = asin(sin(coord.y) * sin(observer_latitude) + cos(coord.y) * cos(observer_latitude) * cos(hour_angle));
        float azimuth = atan(sin(hour_angle), cos(hour_angle) * sin(observer_latitude) - tan(coord.y) * cos(observer_latitude));
        coord = vec4(azimuth, altitude, 0, 1);
    }

    float rotated_longitude = atan(sin(coord.x + azimuth_rotation)*cos(altitude_rotation) - tan(coord.y)*sin(altitude_rotation), cos(coord.x + azimuth_rotation));
    float rotated_latitude = asin(sin(coord.y) * cos(altitude_rotation) + cos(coord.y)*sin(altitude_rotation)*sin(coord.x + azimuth_rotation));

    float r = 1.0 / tan((3.1415/2.0 - rotated_latitude) / 2.0);

    float x = - zoom * r *cos(rotated_longitude);
    float y = zoom * r* sin(rotated_longitude);

    vec4 projection = vec4(x / width, y / height, 0, 1);
*/
    gl_Position = vec4(coord.x / width, coord.y / height, 0, 1);

}  
