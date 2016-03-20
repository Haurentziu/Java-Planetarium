package com.haurentziu.starchart;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import com.haurentziu.coordinates.EquatorialCoordinates;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

/**
 * 
 * @author haurentziu
 *
 */

public class Main {
	
	public static void main(String[] args){
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		GLCanvas canvas = new GLCanvas(caps);
		
		GLStarchart uaie = new GLStarchart();
		canvas.addGLEventListener(uaie);
		
		FPSAnimator animator = new FPSAnimator(canvas, 120);
		animator.start();
	
		Frame frame = new Frame("JavaChart");
		frame.setSize(1000, 1000);
		frame.add(canvas);
		frame.setVisible(true);
		
		
		
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
	}
}
