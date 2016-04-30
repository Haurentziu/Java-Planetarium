package com.haurentziu.starchart;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.haurentziu.gui.ChartMenuBar;
import com.haurentziu.gui.TimeMenu;
import com.jogamp.opengl.GL;
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
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp.get(GLProfile.GL3));
		caps.setAlphaBits(8);
		StarchartCanvas canvas = new StarchartCanvas(caps);
		canvas.setLocation(0, 0);

		FPSAnimator animator = new FPSAnimator(canvas, 60);
		animator.start();
	
		Frame frame = new Frame("Java Planetarium");


		ChartMenuBar mb = new ChartMenuBar();
		TimeMenu tm = new TimeMenu();
	//	tm.setOpaque(true);

		canvas.setBounds(0, 0,width, height);

	//	frame.add(tm);
		frame.add(canvas);

	//	frame.setJMenuBar(mb);
		frame.pack();
		frame.setSize(width, height);
		frame.setVisible(true);
		
		
		
		
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
	}
}
