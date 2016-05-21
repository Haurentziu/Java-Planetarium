package com.haurentziu.render;

import com.haurentziu.coordinates.*;
import com.haurentziu.planets.Earth;
import com.haurentziu.starchart.Observer;
import com.jogamp.opengl.GL3;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class SolarSystem {
	private int arraySize;

	public void render(GL3 gl, Shader shader, IntBuffer buffer, double jde){
		updateSystem(gl, buffer, jde);
		shader.setVariable(gl, "vertex_type", 0);
		shader.setVariable(gl, "transform_type", 2);
		gl.glDrawArrays(GL3.GL_POINTS, 0, 1);
	}


	private EquatorialCoordinates computeSunEquatorial(double jde){
		Earth earth = new Earth();
		RectangularCoordinates earthRect = earth.computeEarthCoordinates(jde);
		earthRect.changeOrigin(0, 0, 0);
		EclipticCoordinates earthEcliptical = earthRect.toEclipticCoordinates();
		double obliquity = computeObliquityOfTheEcliptic(jde);
		EquatorialCoordinates earthEquatorial = earthEcliptical.toEquatorialCoordinates(obliquity);
		return earthEquatorial;
	}

	private void updateSystem(GL3 gl, IntBuffer buffers, double jde){
		Earth earth = new Earth();
		RectangularCoordinates earthRect = earth.computeEarthCoordinates(jde);
		earthRect.changeOrigin(0, 0, 0);
		EclipticCoordinates earthEcliptical = earthRect.toEclipticCoordinates();
		float[] vertices = new float[]{
				(float)earthEcliptical.getLongitude(), (float)earthEcliptical.getLatitude(), -3f,
		};

		arraySize = vertices.length / 3;
		FloatBuffer systemFB = FloatBuffer.wrap(vertices);
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers.get(0));
		gl.glBufferSubData(GL3.GL_ARRAY_BUFFER, 0, 4 * vertices.length, systemFB);
	}
	
	static double computeObliquityOfTheEcliptic(double jde){
		double obliquity = 0.4093197552;
		return obliquity;
	}
}
