package com.haurentziu.starchart;

import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;

import com.haurentziu.coordinates.EquatorialCoordinates;
import com.haurentziu.coordinates.HorizontalCoordinates;
import com.haurentziu.coordinates.SphericalCoordinates;
import com.jogamp.opengl.FPSCounter;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.awt.TextRenderer;

/**
 * 
 * @author haurentziu
 *
 */

public class GLStarchart implements GLEventListener, MouseMotionListener, MouseListener, MouseWheelListener, KeyListener{
	private long timestamp = System.currentTimeMillis();
	
	private final Star stars[];
	private final Constellation constellations[];
	
	private FPSCounter fps;
	
	private float localSideralTime = 12; //Local Sideral Time
	private float latitude = (float) Math.toRadians(51 + 28.0/60.0);
	private float longitude = 0;
	private float altitudeAngle = (float) Math.toRadians(-80); //+90 = alt
	private float azimuthAngle = (float) Math.toRadians(180); //+90 = az
	
	private int initX, initY;
	
	private boolean showUnderHorizon = false;
	private boolean showGrid = true;
	private boolean showConstellationLines = true;
	private boolean addTime = false;
	private boolean showCardinalPoints = true;
	
	private byte projection = SphericalCoordinates.STEREOGRAPHIC_PROJECTION;
	
	private int height, width;
	
	private float zoom = 2;
	
	private Star selectedStar = new Star(0, 0, 0, 0);
	private boolean isSelected = false;
	
	GLStarchart(GLCanvas c){
		double julianDate = System.currentTimeMillis()/86400000.0 + 2440587.5;
		double T = (julianDate - 2451545.0)/36525.0;
		double LST0 = 280.46061837 + 360.98564736629 * (julianDate - 2451545.0) + 0.000387933*T*T - T*T*T/38710000.0;
		while(LST0 > 360)
			LST0 -= 360;
		
		localSideralTime = (float) Math.toRadians(LST0);
		
		DataLoader loader = new DataLoader();
		stars = loader.loadStars();
		constellations = loader.loadConstellations();
		
		
		c.setFocusable(true);
		c.addKeyListener(this);
		c.addMouseMotionListener(this);
		c.addMouseListener(this);
		c.addMouseWheelListener(this);
	}
	
	
	@Override
	public void display(GLAutoDrawable drawable) {
//		System.out.println(rad2String(latitude, false, false));
		final GL2 gl = drawable.getGL().getGL2();
	
		gl.glClear(GL2.GL_COLOR_BUFFER_BIT);
			
		float fps = drawable.getAnimator().getLastFPS();
		
		System.out.println(fps);
		drawHorizon(gl);
		

		if(showGrid)
			drawGrid(gl);
		
		if(showConstellationLines)
			drawConstellations(gl);
		drawStars(gl);
		
		if(showCardinalPoints)
			drawCardinalPoints();

		if(isSelected)
			renderInfoText();
		
		if(addTime)
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
		
		this.width = width;
		this.height = height;
	}
	
	private void renderInfoText(){
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
		
		String azString = rad2String(Math.PI - selectedStar.getHorizontalCoordinates().getAzimuth(), true, false);
		String altString = rad2String(selectedStar.getHorizontalCoordinates().getAltitude(), false, false);
		System.out.println(Math.toDegrees(selectedStar.getHorizontalCoordinates().getAltitude()));
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
		for(float i = 0.1f; i < 2*Math.PI; i+= 10f*Math.PI/180f){
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
	
private void drawHorizon(GL2 gl){
		
		if(altitudeAngle > -Math.PI/2 || projection == SphericalCoordinates.ORTOGRAPHIC_PROJECTION)
			gl.glClearColor(0f, 0.075f, 0.125f, 1f); //prussian blue
		else
			gl.glClearColor(0.28f, 0.21f, 0.16f, 1f); //brown
		
		if(altitudeAngle > -Math.PI/2)
			gl.glColor3f(0.28f, 0.21f, 0.16f); //brown
		else
			gl.glColor3f(0f, 0.075f, 0.125f); //prussian blue

		gl.glBegin(GL2.GL_POLYGON);
		for(float i = 0; i < 2*Math.PI + 0.25; i+=0.15){
			
			HorizontalCoordinates h = new HorizontalCoordinates(i, 0);
			Point2D p = h.toProjection(azimuthAngle, altitudeAngle, projection);
			
			gl.glVertex2f((float)(zoom*p.getX()), (float)(zoom*p.getY()));
			
		}
		gl.glEnd();
	}
	
	private void drawConstellations(GL2 gl){
		gl.glColor3f(0.4f, 0.4f, 0.4f);
		for(int i = 0; i < constellations.length; i++){
			ConstellationLine[] lines = constellations[i].getLines();
			for(int j = 0; j < lines.length; j++){
				
				EquatorialCoordinates equatorial[] = lines[j].getPositions(stars);
				HorizontalCoordinates start = equatorial[0].toHorizontal(longitude, latitude, Math.toRadians(localSideralTime*15));
				HorizontalCoordinates end = equatorial[1].toHorizontal(longitude, latitude, Math.toRadians(localSideralTime*15));
				
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
				HorizontalCoordinates c = stars[i].toHorizontal(longitude, latitude, Math.toRadians(localSideralTime*15));
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
		localSideralTime += 0.01;
	}
	


	@Override
	public void mouseDragged(MouseEvent e) {
		int distanceX = (e.getX() - initX);
		int distanceY = (e.getY() - initY);
		
		initX = e.getX();
		initY = e.getY();
		
		azimuthAngle += (float)Math.PI*distanceX/(width*zoom);
		altitudeAngle -= (float)Math.PI*distanceY/(height*zoom);
	}

	@Override
	public void mouseMoved(MouseEvent arg0) {
		// TODO Auto-generated method stub
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1){
			int x = e.getX();
			int y = e.getY();
			float ortoWidth = (float)(4.0 * width/height);
			float ortoX = (-width / 2f + x) * ortoWidth/width;
			float ortoY = (height / 2f - y) * 4f / height;
			for(int i = 0; i < stars.length; i++){
				if(stars[i].getMagnitude() < 5.5){
					Point2D projection = stars[i].getProjection();
					if(Point2D.distance(ortoX, ortoY, zoom*projection.getX(), zoom*projection.getY()) < stars[i].getRadius()){
						selectedStar = stars[i];
						isSelected = true;
						break;
					}
				}
			}
		}
		else{
			isSelected = false;
		}

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

//	private get
	
	
	@Override
	public void mouseWheelMoved(MouseWheelEvent e) {
		int moves = e.getWheelRotation();
		if(moves > 0 && zoom > 1){
			zoom /= 1.1;
		}
		else{
			zoom *= 1.1;
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		int k = e.getKeyCode();
		
		switch (k){
		case KeyEvent.VK_1: projection = SphericalCoordinates.STEREOGRAPHIC_PROJECTION;
							break;
							
		case KeyEvent.VK_2: projection = SphericalCoordinates.ORTOGRAPHIC_PROJECTION;
							break;
		
		case KeyEvent.VK_A: showGrid = !showGrid;
							break;
							
		case KeyEvent.VK_C: showConstellationLines = !showConstellationLines;
							break;
							
		case KeyEvent.VK_SPACE: addTime = !addTime;
								break;
								
		case KeyEvent.VK_P:	showCardinalPoints = !showCardinalPoints;
							break;
		}
		
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
