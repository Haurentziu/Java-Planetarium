package com.haurentziu.starchart;

import com.haurentziu.coordinates.*;
import com.haurentziu.planets.Earth;
import com.jogamp.opengl.GL3;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class SolarSystem {
	private int arraySize;
	private IntBuffer vertexArray = IntBuffer.allocate(1);
	private IntBuffer buffers = IntBuffer.allocate(1);
	boolean initialized = false;

	public SolarSystem(){
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

	void updateSystem(GL3 gl, double jde){

		Earth earth = new Earth();
		RectangularCoordinates earthRect = earth.computeEarthCoordinates(jde);
		earthRect.changeOrigin(0, 0, 0);
		EclipticCoordinates earthEcliptical = earthRect.toEclipticCoordinates();

		float[] vertices = {
				(float)earthEcliptical.getLongitude(), (float)earthEcliptical.getLatitude(), -4f,
		};

		arraySize = vertices.length;
		FloatBuffer systemFB = FloatBuffer.wrap(vertices);
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers.get(0));
		gl.glBufferData(GL3.GL_ARRAY_BUFFER, 4 * vertices.length, systemFB, GL3.GL_STREAM_DRAW);

		gl.glGenVertexArrays(1, vertexArray);
		gl.glBindVertexArray(vertexArray.get(0));

		gl.glGenVertexArrays(1, vertexArray);
		gl.glBindVertexArray(vertexArray.get(0));

		gl.glEnableVertexAttribArray(0);
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers.get(0));
		gl.glVertexAttribPointer(0, 3, GL3.GL_FLOAT, false, 0, 0);

		if(initialized) {
			gl.glDeleteBuffers(3, vertexArray);
		}
		else {
			initialized = true;
		}

	}
	
	static double computeObliquityOfTheEcliptic(double jde){
		double obliquity = 0.4093197552;
		return obliquity;
	}
}
