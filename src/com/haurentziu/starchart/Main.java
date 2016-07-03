package com.haurentziu.starchart;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.haurentziu.gui.LocationMenu;
import com.haurentziu.gui.TimeMenu;
import com.haurentziu.gui.ToolBar;
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
	static int width = 950, height = 850;  //934 x 911/876
	public static StarchartCanvas canvas;
	public static TimeMenu timeMenu;
	public static LocationMenu locationMenu;

	public static void main(String[] args){

		boolean fullScreen = args.length > 0 && args[0].equals("-fullscreen");
		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp.get(GLProfile.GL3));
		caps.setAlphaBits(8);
		canvas = new StarchartCanvas(caps);
		FPSAnimator animator = new FPSAnimator(canvas, 120);

		ToolBar bar = new ToolBar();

		final JFrame frame = new JFrame("Java Planetarium");
		frame.setLayout(new BorderLayout());

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(canvas);

		timeMenu = new TimeMenu();
		timeMenu.setVisible(false);

		locationMenu = new LocationMenu();
		locationMenu.setVisible(false);

		if(fullScreen){
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setUndecorated(true);
		}

		frame.setContentPane(panel);
		frame.add(bar, BorderLayout.SOUTH);

		frame.setSize(width, height);
		frame.setVisible(true);
		animator.start();




		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				exit();
			}
		});
	}

	public static void showTimeMenu(){
		timeMenu.setSpinnerValues(GLStarchart.observer.getUnixTime());
		timeMenu.setVisible(true);
		timeMenu.toFront();
	}

	public static void exit(){
		canvas.destroy();
		System.exit(0);
	}
}