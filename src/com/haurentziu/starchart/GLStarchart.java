package com.haurentziu.starchart;

import java.awt.geom.Point2D;

import com.haurentziu.coordinates.EquatorialCoordinates;
import com.haurentziu.coordinates.HorizontalCoordinates;
import com.jogamp.opengl.FPSCounter;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;

/**
 * 
 * @author haurentziu
 *
 */

public class GLStarchart implements GLEventListener{
	private long timestamp = System.currentTimeMillis();
	
	private Star stars[];
	private Constellation constellations[];
	
	private FPSCounter fps;
	
	private float lst = 12; //Local Sideral Time
	private float altitudeAngle = (float) Math.toRadians(70);
	private float azimuthAngle = (float) Math.toRadians(60);
	
	GLStarchart(){
		DataLoader loader = new DataLoader();
		stars = loader.loadStars();
		constellations = loader.loadConstellations();
	//	System.out.println(stars[118216].getHipparcos());
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		
		float time=drawable.getAnimator().getLastFPS();
		
		System.out.println(time);
		
		drawStars(gl);
		for(int i = 0; i < stars.length; i++);
		drawConstellations(gl);
		displayFPS();
	}
	
	private void drawConstellations(GL2 gl){
		for(int i = 0; i < constellations.length; i++){
			ConstellationLine[] lines = constellations[i].getLines();
			for(int j = 0; j < lines.length; j++){
				EquatorialCoordinates equatorial[] = lines[j].getPositions(stars);
				HorizontalCoordinates start = equatorial[0].toHorizontal(Math.toRadians(45), Math.toRadians(lst*15));
				HorizontalCoordinates end = equatorial[1].toHorizontal(Math.toRadians(45), Math.toRadians(lst*15));
				
				if(start.getAltitude() > 0 && end.getAltitude() > 0){
					Point2D p1 = start.toStereographicProjection(azimuthAngle, altitudeAngle);
					Point2D p2 = end.toStereographicProjection(azimuthAngle, altitudeAngle);

					gl.glBegin(GL2.GL_LINES);
					gl.glVertex2f((float)p1.getX(), (float)p1.getY());
					gl.glVertex2f((float)p2.getX(), (float)p2.getY());
					gl.glEnd();
				}
			}
		}
	}
	
	private void drawStars(GL2 gl){
		for(int i = 0; i < stars.length; i++){
			if(stars[i].getMagnitude() < 5.5){
				HorizontalCoordinates c = stars[i].toHorizontal(Math.toRadians(45), Math.toRadians(lst*15));
				if(c.getAltitude() > 0){
					Point2D p = c.toStereographicProjection(azimuthAngle, altitudeAngle);
					drawCircle((float)p.getX(), (float)p.getY(), stars[i].getRadius(), gl);
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
	
	private void displayFPS(){
		lst += 8000*(System.currentTimeMillis() - timestamp)/3.6e+6f;
		timestamp = System.currentTimeMillis();
	}
	
	@Override
	public void dispose(GLAutoDrawable arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		drawable.getAnimator().setUpdateFPSFrames(3, null);
	}

	@Override
	public void reshape(GLAutoDrawable drawable, int arg1, int arg2, int arg3, int arg4) {

		
	}
}
