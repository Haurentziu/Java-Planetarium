package com.haurentziu.starchart;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

import com.haurentziu.coordinates.EquatorialCoordinates;
import com.haurentziu.coordinates.HorizontalCoordinates;
import com.jogamp.opengl.FPSCounter;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;

/**
 * 
 * @author haurentziu
 *
 */

public class GLStarchart implements GLEventListener, MouseMotionListener, MouseListener, MouseWheelListener{
	private long timestamp = System.currentTimeMillis();
	
	private Star stars[];
	private Constellation constellations[];
	
	private FPSCounter fps;
	
	private float localSideralTime = 12; //Local Sideral Time
	private float altitudeAngle = (float) Math.toRadians(70);
	private float azimuthAngle = (float) Math.toRadians(60);
	
	private int initX, initY;
	
	private byte projection = 1; //0 -> stereographic 1 -> ortographic
	
	private float zoom = 1;
	
	GLStarchart(GLCanvas c){
		DataLoader loader = new DataLoader();
		stars = loader.loadStars();
		constellations = loader.loadConstellations();
		c.addMouseMotionListener(this);
		c.addMouseListener(this);
		c.addMouseWheelListener(this);
	}
	
	@Override
	public void display(GLAutoDrawable drawable) {
		final GL2 gl = drawable.getGL().getGL2();
	
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
		
		float fps = drawable.getAnimator().getLastFPS();
		
	//	System.out.println(fps);
		
		drawConstellations(gl);
		drawStars(gl);
		updateTime();
	}
	
	@Override
	public void dispose(GLAutoDrawable arg0) {
		
	}

	@Override
	public void init(GLAutoDrawable drawable) {
		drawable.getAnimator().setUpdateFPSFrames(3, null);
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
	}
	
	private void drawConstellations(GL2 gl){
		gl.glColor3f(0.4f, 0.4f, 0.4f);
		for(int i = 0; i < constellations.length; i++){
			ConstellationLine[] lines = constellations[i].getLines();
			for(int j = 0; j < lines.length; j++){
				
				EquatorialCoordinates equatorial[] = lines[j].getPositions(stars);
				HorizontalCoordinates start = equatorial[0].toHorizontal(Math.toRadians(45), Math.toRadians(localSideralTime*15));
				HorizontalCoordinates end = equatorial[1].toHorizontal(Math.toRadians(45), Math.toRadians(localSideralTime*15));
				
				if(start.getAltitude() > 0 && end.getAltitude() > 0){
					Point2D p1, p2;
					if(projection == 0){
						p1 = start.toStereographicProjection(azimuthAngle, altitudeAngle);
						p2 = end.toStereographicProjection(azimuthAngle, altitudeAngle);
					}
					
					else{
						p1 = start.toOrtohraphicProjection(azimuthAngle, altitudeAngle);
						p2 = end.toOrtohraphicProjection(azimuthAngle, altitudeAngle);
					}
					
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
			if(stars[i].getMagnitude() < 5.5 + 0.1*zoom){
				HorizontalCoordinates c = stars[i].toHorizontal(Math.toRadians(45), Math.toRadians(localSideralTime*15));
				if(c.getAltitude() > 0){
					Point2D p;
					if(projection == 0)
						p= c.toStereographicProjection(azimuthAngle, altitudeAngle);
					else
						p = c.toOrtohraphicProjection(azimuthAngle, altitudeAngle);
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
		timestamp = System.currentTimeMillis();
	}
	


	@Override
	public void mouseDragged(MouseEvent e) {
		int distanceX = (e.getX() - initX);
		int distanceY = (e.getY() - initY);
		
		initX = e.getX();
		initY = e.getY();
		
		azimuthAngle += (float)Math.PI*distanceX/(2*Main.width*zoom);
		altitudeAngle -= (float)Math.PI*distanceY/(2*Main.height*zoom);
		
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		initX = e.getX();
		initY = e.getY();
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int moves = e.getWheelRotation();
		if(moves > 0){
			zoom /= 1.1;
		}
		else{
			zoom *= 1.1;
		}
	}
}
