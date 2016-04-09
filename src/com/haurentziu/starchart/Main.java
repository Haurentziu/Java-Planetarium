package com.haurentziu.starchart;

import java.awt.Frame;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.haurentziu.gui.ChartMenuBar;
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
	static int width = 1200, height = 950;
	public static void main(String[] args){
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp);
		StarchartCanvas canvas = new StarchartCanvas(caps);
		canvas.setLocation(0, 0);

		FPSAnimator animator = new FPSAnimator(canvas, 60);
		animator.start();
	
		JFrame frame = new JFrame("Java Ephemerides");


		ChartMenuBar mb = new ChartMenuBar();
		TimeMenu tm = new TimeMenu();
		tm.setOpaque(true);
	//	frame.setJMenuBar(mb);
		frame.setSize(width, height);

	//	frame.add(tm);
		frame.add(canvas);
		frame.setVisible(true);
		
		
		
		
		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				System.exit(0);
			}
		});
	}
}
