package com.haurentziu.render;

import com.haurentziu.coordinates.*;
import com.haurentziu.planets.Planet;
import com.haurentziu.planets.VSOPLoader;
import com.jogamp.opengl.GL3;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class SolarSystem {
	private int arraySize;
	private Planet earth;
	private Planet[] planets;
	private float mags[] = {0f, 0f, 0f, 0f, 0f, 0f, 0f};

	public SolarSystem(){
		VSOPLoader loader = new VSOPLoader();
		earth = loader.readVSOP("./res/vsop87/VSOP87C.ear");
		planets = new Planet[7];
		planets[0] = loader.readVSOP("./res/vsop87/VSOP87C.mer");
		planets[1] = loader.readVSOP("./res/vsop87/VSOP87C.ven");
		planets[2] = loader.readVSOP("./res/vsop87/VSOP87C.mar");
		planets[3] = loader.readVSOP("./res/vsop87/VSOP87C.jup");
		planets[4] = loader.readVSOP("./res/vsop87/VSOP87C.sat");
		planets[5] = loader.readVSOP("./res/vsop87/VSOP87C.ura");
		planets[6] = loader.readVSOP("./res/vsop87/VSOP87C.nep");

	}

	public void render(GL3 gl, Shader shader, IntBuffer buffer, double jde){
		updateSystem(gl, buffer, jde);
		shader.setVariable(gl, "vertex_type", 0);
		shader.setVariable(gl, "transform_type", 2);
		gl.glDrawArrays(GL3.GL_POINTS, 0, 8);
	}

	public void loadVertices(ArrayList<Float> verts){
		for(int i = 0; i < 8; i++){
			verts.add(0f);
			verts.add(0f);
			verts.add(0f);
		}
	}

	public void loadColor(ArrayList<Float> colors){
		colors.add(1f);
		colors.add(0.965f);
		colors.add(0f);

		colors.add(1f);
		colors.add(0f);
		colors.add(0f);

		colors.add(0f);
		colors.add(1f);
		colors.add(0f);

		colors.add(0f);
		colors.add(0f);
		colors.add(1f);

		colors.add(0f);
		colors.add(1f);
		colors.add(1f);

		colors.add(1f);
		colors.add(0f);
		colors.add(1f);

		colors.add(0.5f);
		colors.add(1f);
		colors.add(0.5f);

	/*	colors.add(0.8f);
		colors.add(0.7f);
		colors.add(0.2f);*/
	}


	private void updateSystem(GL3 gl, IntBuffer buffers, double jde){
		double tau = (jde - 2451545) / 365250;
		RectangularCoordinates earthRect = earth.getRectangularCoordinates(tau);
		earthRect.invert();
		EclipticCoordinates earthEcliptical = earthRect.toEclipticCoordinates();

		float vertices[] = new float[planets.length * 3 + 3];
		vertices[0] = (float)earthEcliptical.getLongitude();
		vertices[1] = (float)earthEcliptical.getLatitude();
		vertices[2] = -3f;
		for(int i = 0; i < planets.length; i++) {
			RectangularCoordinates rect = planets[i].getRectangularCoordinates(tau);
			rect.addCoordinates(earthRect);
			EclipticCoordinates ecliptical = rect.toEclipticCoordinates();

			vertices[3*i + 3] = (float)ecliptical.getLongitude();
			vertices[3*i + 4] = (float)ecliptical.getLatitude();
			vertices[3*i + 5] = mags[i];
		}

	//	System.out.println(Math.toDegrees(vertices[3 * 7]));

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
