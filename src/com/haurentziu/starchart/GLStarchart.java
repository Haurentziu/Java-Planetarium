package com.haurentziu.starchart;

import java.awt.Font;

import java.awt.geom.Point2D;

import com.haurentziu.coordinates.EquatorialCoordinates;
import com.haurentziu.coordinates.HorizontalCoordinates;
import com.haurentziu.coordinates.SphericalCoordinates;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.util.awt.TextRenderer;


/**
 * 
 * @author haurentziu
 *
 */

public class GLStarchart implements GLEventListener{

	final Star stars[];
	private final Constellation constellations[];
	

	double localSideralTime = 12; //Local Sideral Time
	float latitude = (float) Math.toRadians(51 + 28.0/60.0);
	float longitude = 0;
	float altitudeAngle = (float) Math.toRadians(-80);
	float azimuthAngle = (float) Math.toRadians(180);

	boolean showUnderHorizon = false;
	boolean showGrid = true;
	boolean showConstellationLines = true;
	boolean showCardinalPoints = true;
	
	byte projection = SphericalCoordinates.STEREOGRAPHIC_PROJECTION;
	float timeWarp = 1;

	int height, width;
	double ortoHeight, ortoWidth;
	private Timer t = new Timer();

	float zoom = 1;
	
	Star selectedStar = new Star(0, 0, 0, 0);
	boolean isSelected = false;
	
	GLStarchart(){
		double julianDate = System.currentTimeMillis()/86400000.0 + 2440587.5;
		double T = (julianDate - 2451545.0)/36525.0;
		double LST0 = 280.46061837 + 360.98564736629 * (julianDate - 2451545.0) + 0.000387933*T*T - T*T*T/38710000.0;
		while(LST0 > 360)
			LST0 -= 360;
		
		localSideralTime = (float) Math.toRadians(LST0);
		DataLoader loader = new DataLoader();
		stars = loader.loadStars();
		constellations = loader.loadConstellations();
	}
	
	
	@Override
	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
	
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
			
		float fps = drawable.getAnimator().getLastFPS();
		System.out.println(fps);
		if(altitudeAngle > -Math.PI/2) {
			drawSky(gl);
			renderCelestialObjects(gl);
		}
		else{
			drawGround(gl);
		}


		if(altitudeAngle > -Math.PI/2)
			drawGround(gl);
		else {
			drawSky(gl);
			renderCelestialObjects(gl);

		}
		if(showCardinalPoints)
			drawCardinalPoints();

		if(isSelected)
			renderInfoText(gl);

		updateTime();
		
	}


	@Override
	public void dispose(GLAutoDrawable arg0) {
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		drawable.getAnimator().setUpdateFPSFrames(20, null);
	}
	

	@Override
	public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glMatrixMode(GL2.GL_PROJECTION);
		gl.glLoadIdentity();
		
		double aspectRatio = (double)width/height;
		gl.glOrtho(-2*aspectRatio, 2*aspectRatio, -2, 2, -1, 1);
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL2.GL_MODELVIEW);
		gl.glLoadIdentity();
		ortoWidth = aspectRatio*2;
		ortoHeight = 2;
		this.width = width;
		this.height = height;
	}

	private void renderCelestialObjects(GL2 gl){
		renderBodies(gl);
		if(showGrid)
			drawGrid(gl);

		if(showConstellationLines)
			drawConstellations(gl);

		drawStars(gl);
	}
	
	private void renderBodies(GL2 gl){
		double julianDate = System.currentTimeMillis()/86400000.0 + 2440587.5;
		SolarSystem system = new SolarSystem();
		EquatorialCoordinates sunEquatorial = system.computeSunEquatorial(julianDate);
	//	System.out.printf("RA: %s  DE: %s\n", rad2String(sunEquatorial.getRightAscension(), true, true), rad2String(sunEquatorial.getDeclination(), true, false));
	}


	private void renderInfoText(GL2 gl){

		TextRenderer titleRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 26));
		titleRenderer.setColor(0.051f, 0.596f, 0.729f, 1f);
		titleRenderer.beginRendering(width, height);
		titleRenderer.draw("HIP " + selectedStar.getHipparcos(), 0, height - 30);
		titleRenderer.endRendering();
		
		TextRenderer infoRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 15));
		infoRenderer.setColor(0.141f, 0.784f, 0.941f, 1f);
		infoRenderer.beginRendering(width, height);
		infoRenderer.draw("Magnitude: " + selectedStar.getMagnitude(), 0, height - 55);

		String raString = rad2String(selectedStar.getRightAscension(), false, true);
		String decString = rad2String(selectedStar.getDeclination(), false, false);
		infoRenderer.draw("RA/Dec(J2000): "  + raString + "/" + decString, 0, height - 75);
		
		String azString = rad2String(selectedStar.getHorizontalCoordinates().getAzimuth() - Math.PI, true, false);
		String altString = rad2String(selectedStar.getHorizontalCoordinates().getAltitude(), false, false);
		infoRenderer.draw("Az/Alt: " + azString + " / " + altString, 0, height - 95);
		infoRenderer.endRendering();
	}
	
	private String rad2String(double d, boolean normalise, boolean inHours){
		double deg = Math.toDegrees(d);
		if(normalise){
			while(deg > 360)
				deg -= 360;
			while(deg < 0)
				deg +=360;
		}
		if(inHours)
			deg /= 15.0;
		int degrees = (int)deg;
		int minutes = (int)((deg - (int)deg)*60);
		float seconds = (float)(deg - degrees - minutes/60.0)*3600f;
		
		String s;
		if(inHours)
			s = String.format("%dh %02dm %.2fs", degrees, minutes, seconds);
		else
			s = String.format("%d\u00b0 %02d\u2032 %.2f\u2033", degrees, minutes, seconds);

		return s;
	}
	
	private void drawCardinalPoints(){
		TextRenderer renderer = new TextRenderer(new Font("SansSerif", Font.PLAIN, 36));
		renderer.beginRendering(width, height);
		renderer.setColor(0.075f, 0.306f, 0.075f, 1f); //orange
		String[] cardinalPoints = {"S", "W", "N", "E"};
		for(int i = 0; i < 4; i++){
			HorizontalCoordinates hc = new HorizontalCoordinates(i*Math.PI/2, 0);
			Point2D p = hc.toProjection(azimuthAngle, altitudeAngle, projection);

			renderer.setColor(1f, 0.647f, 0f, 1f);
			float ortoWidth = (float)(4.0 * width / height);
			int x = (int) ((zoom * p.getX() + ortoWidth) * width / ortoWidth - width /2.0);
			int y = (int) ((zoom * p.getY() + 4) * height / 4.0 - height/2.0);

			renderer.draw(cardinalPoints[i], x, y);
		}

		renderer.endRendering();
	}
	
		
	private void drawGrid(GL2 gl){
		gl.glColor3f(0.192f, 0.325f, 0f);
		//altitude lines
		for(float i = 0.1f; i < 2*Math.PI; i+= Math.PI/18f){
			gl.glBegin(GL2.GL_LINE_STRIP);
			for(float j = 0; j < Math.PI/2; j+= 0.1){
				HorizontalCoordinates h = new HorizontalCoordinates(i, j);
				Point2D p = h.toProjection(azimuthAngle, altitudeAngle, projection);
				gl.glVertex2f((float)(zoom*p.getX()), (float)(zoom*p.getY()));
			}
			gl.glEnd();
		}
		
		for(float i = 0; i < Math.PI/2; i += 0.15){
			gl.glBegin(GL2.GL_LINE_STRIP);
			for(float j = 0; j < 2*Math.PI + 0.2; j += 0.1){
				HorizontalCoordinates h = new HorizontalCoordinates(j, i);
				Point2D p = h.toProjection(azimuthAngle, altitudeAngle, projection);
				gl.glVertex2f((float)(zoom*p.getX()), (float)(zoom*p.getY()));
			}
			gl.glEnd();
		}
		
	}

	private void drawSky(GL2 gl){
		gl.glColor3f(0f, 0.075f, 0.125f);
		for(double i = 0; i <= 2*Math.PI + Math.toRadians(235/2); i += Math.PI/20.0){

			drawPieceofSky(gl, i, i + Math.PI/19.8, Math.PI/45.0);

		}
	}

	private void drawGround(GL2 gl){
		gl.glColor3f(0.28f, 0.21f, 0.16f);
		for(double i = 0; i <= 2*Math.PI; i += Math.PI/20.0){
			drawPieceofGround(gl, i, i + Math.PI/19.8, Math.PI/45.0);

		}
	}


	private boolean isInBounds(Point2D p){
		double x = p.getX();
		double y = p.getY();
		return (x < ortoWidth && x > -ortoWidth && y < ortoHeight && y > -ortoHeight);
	}



	private void drawPieceofSky(GL2 gl, double azStart, double azEnd, double step){
		gl.glBegin(GL2.GL_POLYGON);


		for(double i = azStart; i <= azEnd; i += step){
			HorizontalCoordinates c = new HorizontalCoordinates(i, 0);
			Point2D p = c.toProjection(azimuthAngle, altitudeAngle, projection);
			gl.glVertex2d(zoom*p.getX(), zoom*p.getY());
		}

		for(double i = 0; i < Math.PI/2; i += step){
			HorizontalCoordinates c = new HorizontalCoordinates(azEnd, i);
			Point2D p = c.toProjection(azimuthAngle, altitudeAngle, projection);
			gl.glVertex2d(zoom*p.getX(), zoom*p.getY());
		}


		for(double i = Math.PI/2; i > 0; i -= step){
			HorizontalCoordinates c = new HorizontalCoordinates(azStart, i);
			Point2D p = c.toProjection(azimuthAngle, altitudeAngle, projection);
			gl.glVertex2d(zoom*p.getX(), zoom*p.getY());
		}
		gl.glEnd();
	}

	private void drawPieceofGround(GL2 gl, double azStart, double azEnd, double step){
		gl.glBegin(GL2.GL_POLYGON);

		for(double i = azStart; i <= azEnd; i += step){
			HorizontalCoordinates c = new HorizontalCoordinates(i, 0);
			Point2D p = c.toProjection(azimuthAngle, altitudeAngle, projection);
			gl.glVertex2d(zoom*p.getX(), zoom*p.getY());
		}

		for(double i = 0; i > -Math.PI/2; i -= step){
			HorizontalCoordinates c = new HorizontalCoordinates(azEnd, i);
			Point2D p = c.toProjection(azimuthAngle, altitudeAngle, projection);
			gl.glVertex2d(zoom*p.getX(), zoom*p.getY());
		}


		for(double i = -Math.PI/2; i <= 0; i += step){
			HorizontalCoordinates c = new HorizontalCoordinates(azStart, i);
			Point2D p = c.toProjection(azimuthAngle, altitudeAngle, projection);
			gl.glVertex2d(zoom*p.getX(), zoom*p.getY());
		}

		gl.glEnd();
	}

	private void drawConstellations(GL2 gl){
		gl.glColor3f(0.4f, 0.4f, 0.4f);
		for(int i = 0; i < constellations.length; i++){
			ConstellationLine[] lines = constellations[i].getLines();
			for(int j = 0; j < lines.length; j++){
				
				EquatorialCoordinates equatorial[] = lines[j].getPositions(stars);
				HorizontalCoordinates start = equatorial[0].toHorizontal(longitude, latitude, localSideralTime);
				HorizontalCoordinates end = equatorial[1].toHorizontal(longitude, latitude, localSideralTime);
				
				if(start.getAltitude() > 0 && end.getAltitude() > 0 || showUnderHorizon){
					Point2D p1, p2;
					
					p1 = start.toProjection(azimuthAngle, altitudeAngle, projection);
					p2 = end.toProjection(azimuthAngle, altitudeAngle, projection);
					
					gl.glBegin(GL2.GL_LINES);
					gl.glVertex2f((float)(zoom*p1.getX()), (float)(zoom*p1.getY()));
					gl.glVertex2f((float)(zoom*p2.getX()), (float)(zoom*p2.getY()));
					gl.glEnd();
				}
			}
		}
	}
	
	private void drawStars(GL2 gl){
		gl.glColor3f(1f, 1f, 1f);
		for(int i = 0; i < stars.length; i++){
			if(stars[i].getMagnitude() < 5.5){
				HorizontalCoordinates c = stars[i].toHorizontal(longitude, latitude, localSideralTime);
				stars[i].setHorizontalCoordinates(c);
				if(c.getAltitude() > 0 || showUnderHorizon){
					Point2D p;
					p = c.toProjection(azimuthAngle, altitudeAngle, projection);
					stars[i].setProjection(p);
					drawCircle((float)(zoom*p.getX()), (float)(zoom*p.getY()), stars[i].getRadius(), gl);
				}
			}
		}
	}

	private void drawCircle(float centerX, float centerY , float radius, GL2 gl){
		gl.glBegin(GL2.GL_POLYGON);
		for(float angle = 0; angle < 2*Math.PI; angle += 0.5){
			float x = (float)(centerX + radius * Math.cos(angle));
			float y = (float)(centerY + radius * Math.sin(angle));
			gl.glVertex2f(x, y);
		}

		gl.glEnd();
	}
	
	private void updateTime(){
		localSideralTime += 4.84813681e-9*timeWarp*t.getDeltaTime(); //dt*pi/(180*3600000) ms(time) -> rad
	}

}
