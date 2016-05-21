package com.haurentziu.starchart;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.haurentziu.gui.TimeMenu;
import com.jogamp.opengl.GLCapabilities;
import com.jogamp.opengl.GLProfile;
import com.jogamp.opengl.util.FPSAnimator;

import javax.swing.*;

/**
 * 
 * @author haurentziu
 *
 */

//FIXME everything

public class Main {
	static int width = 950, height = 950;
	public static void main(String[] args){

		boolean fullScreen = args.length > 0 && args[0].equals("-fullscreen");
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp.get(GLProfile.GL3));
		caps.setAlphaBits(8);
		final StarchartCanvas canvas = new StarchartCanvas(caps);
		canvas.setLocation(0, 0);
		FPSAnimator animator = new FPSAnimator(canvas, 120);
		animator.start();
	
		final Frame frame = new JFrame("Java Planetarium");

		TimeMenu tm = new TimeMenu();
		tm.setVisible(false);

		if(fullScreen){
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setUndecorated(true);
		}

		frame.add(canvas);
		frame.pack();
		frame.setSize(width, height);
		frame.setVisible(true);
		

		
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				canvas.destroy();
				System.exit(0);
			}
		});
	}
}
