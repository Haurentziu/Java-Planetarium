package com.haurentziu.render;

import com.haurentziu.coordinates.*;
import com.haurentziu.planets.Moon;
import com.haurentziu.planets.Planet;
import com.haurentziu.planets.VSOPLoader;
import com.haurentziu.starchart.Observer;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

public class SolarSystem extends Renderer{
	private int arraySize;
	private Planet earth;
	private Planet[] planets;
	private final float magnitudes[] = {0f, 0f, 0f, 0f, 0f, 0f, 0f};
	private final float colors[][] = {
		{0.5f, 0.5f, 0.5f},
		{1f, 0.965f, 0f},
		{1f, 0f, 0f},
		{0f, 1f, 0f},
		{0f, 0.5f, 1f},
		{0f, 1f, 1f},
		{1f, 0f, 1f},
		{0.5f, 1f, 0.5f},
		{0.8f, 0.7f, 0.2f}
	};

	private Moon moon;

	private int textStart;
	private int textSize;
	private Texture texture;

	public SolarSystem(String vertShader, String geomShader, String fragShader){
		super(vertShader, geomShader, fragShader);
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
		moon = new Moon();

	}


    @Override
	public void initialize(GL3 gl){
		shader.init(gl);
		texture = loadTexture(gl, "./res/textures/planets_name.png");
	}

	@Override
	public void delete(GL3 gl){
		shader.deleteProgram(gl);
		texture.destroy(gl);

	}

	public void renderPlanets(GL3 gl, Shader starShader, Observer observer, IntBuffer buffer){
		updateSystem(gl, buffer, observer.getJDE());
		starShader.setVariable(gl, "vertex_type", 0);
		starShader.setVariable(gl, "transform_type", 2);
		gl.glDrawArrays(GL3.GL_POINTS, 0, 9);

	}

	public void renderText(GL3 gl, Observer observer){
		shader.useShader(gl);
		gl.glEnable(GL3.GL_TEXTURE_2D);
		gl.glActiveTexture(GL3.GL_TEXTURE0);
		texture.enable(gl);
		texture.bind(gl);
		super.setObserver(gl, observer);
		shader.setVariable(gl, "texTexture", 0);
		shader.setVariable(gl, "transform_type", 2);
		shader.setVariable(gl, "vertex_type", 0);
		gl.glDrawArrays(GL3.GL_POINTS, 9, 9);
		texture.disable(gl);

	}


	//the initial vertices
	public void loadVertices(ArrayList<Float> verts){
		for(int i = 0; i < 9; i++){
			verts.add(0f);
			verts.add(0f);
			verts.add(0f);

			verts.add(colors[i][0]);
			verts.add(colors[i][1]);
			verts.add(colors[i][2]);

			verts.add(0f);
			verts.add(0f);
			verts.add(0f);
		}

		for(int i = 0; i < 9; i++){
			verts.add(0f);
			verts.add(0f);
			verts.add(0f);

			verts.add(colors[i][0]);
			verts.add(colors[i][1]);
			verts.add(colors[i][2]);

			verts.add(0f);
			verts.add(0f);
			verts.add(0f);
		}
	}

	private void updateSystem(GL3 gl, IntBuffer buffers, double jde){
		double tau = (jde - 2451545) / 365250; //julian millenia
		RectangularCoordinates earthRect = earth.getRectangularCoordinates(tau);
		earthRect.invert();
		EclipticCoordinates earthEcliptical = earthRect.toEclipticCoordinates();
		float vertices[] = new float[(planets.length + 2) * 18];

		EclipticCoordinates moonCoord = moon.computeMoonEquatorial(10 * tau); //julian centuries, not millenia
		vertices[0] = (float)moonCoord.getLongitude();
		vertices[1] = (float)moonCoord.getLatitude();
		vertices[2] = -2.4f;

		//3, 4, 5  6, 7, 8
		vertices[9] = (float)earthEcliptical.getLongitude();
		vertices[10] = (float)earthEcliptical.getLatitude();
		vertices[11] = -2.5f;

		//12, 13, 14, 15, 16, 17,

		for(int i = 0; i < planets.length; i++) {
			RectangularCoordinates rect = planets[i].getRectangularCoordinates(tau);
			rect.addCoordinates(earthRect);
			EclipticCoordinates ecliptical = rect.toEclipticCoordinates();

			vertices[9*i + 18] = (float)ecliptical.getLongitude();
			vertices[9*i + 19] = (float)ecliptical.getLatitude();
			vertices[9*i + 20] = magnitudes[i];
		}

		for(int i = 0; i < planets.length + 2; i++){
			vertices[9 * i + 3] = colors[i][0];
			vertices[9 * i + 4] = colors[i][1];
			vertices[9 * i + 5] = colors[i][2];

			vertices[9 * i + 6] = 0f;
			vertices[9 * i + 7] = 0f;
			vertices[9 * i + 8] = 0f;
		}

		for(int  i = 0; i < planets.length + 2; i++){
			vertices[9 * i + 81] = vertices[9 * i];
			vertices[9 * i + 82] = vertices[9 * i + 1];
			vertices[9 * i + 83] = (i + 0f) / 9f;

			vertices[9 * i + 84] = colors[i][0];
			vertices[9 * i + 85] = colors[i][1];
			vertices[9 * i + 86] = colors[i][2];

			vertices[9 * i + 87] = 0;
			vertices[9 * i + 88] = 0;
			vertices[9 * i + 89] = 0;
		}

		arraySize = vertices.length / 9;
		FloatBuffer systemFB = FloatBuffer.wrap(vertices);
		gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers.get(0));
		gl.glBufferSubData(GL3.GL_ARRAY_BUFFER, 0, 4 * vertices.length - 4, systemFB);

	}
	
	static double computeObliquityOfTheEcliptic(double jde){
		double obliquity = 0.4093197552;
		return obliquity;
	}
}
