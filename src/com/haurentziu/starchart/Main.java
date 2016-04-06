package com.haurentziu.starchart;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.awt.GLCanvas;
import com.jogamp.opengl.util.FPSAnimator;

/**
 * 
 * @author haurentziu
 *
 */

//FIXME everything
public class Main {
	static int width = 1200, height = 950;
	public static void main(String[] args){
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		StarchartCanvas canvas = new StarchartCanvas(caps);
		
		//GLStarchart starchart = new GLStarchart(canvas);
	//	canvas.addGLEventListener(starchart);

		FPSAnimator animator = new FPSAnimator(canvas, 60);
		animator.start();
	
		Frame frame = new Frame("Java Ephemerides");
		
		frame.setSize(width, height);
		frame.add(canvas);
		frame.setVisible(true);
		
		
		
		
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
	}
}
