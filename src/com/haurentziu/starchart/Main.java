package com.haurentziu.starchart;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.haurentziu.gui.*;
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
	public static StarchartCanvas canvas;
	public static TimeMenu timeMenu;
	public static LocationMenu locationMenu;
	public static SatelliteMenu satelliteMenu;

	public static void main(String[] args){
		GraphicsDevice d = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice();

		boolean fullScreen = args.length > 0 && args[0].equals("-fullscreen");
		Observer observer = new Observer();

		SplashDialog splash = new SplashDialog();
		splash.setVisible(true);

		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp.get(GLProfile.GL3));
		caps.setAlphaBits(8);
		canvas = new StarchartCanvas(caps, observer);

		FPSAnimator animator = new FPSAnimator(canvas, 120);
		ToolBar bar = new ToolBar(observer);

		final JFrame frame = new JFrame("Java Planetarium");
		frame.setLayout(new BorderLayout());

		JPanel panel = new JPanel(new BorderLayout());
		panel.add(canvas);

		timeMenu = new TimeMenu(frame, observer);
		timeMenu.setVisible(false);

		locationMenu = new LocationMenu(frame, observer);
		locationMenu.setVisible(false);

		satelliteMenu = new SatelliteMenu();
		satelliteMenu.setVisible(false);

		final Toolkit toolkit = Toolkit.getDefaultToolkit();
		final Dimension screenSize = toolkit.getScreenSize();
		final int x = (screenSize.width - width) / 2;
		final int y = (screenSize.height - height) / 2;
		frame.setLocation(x, y);

		if(fullScreen){
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setUndecorated(true);
		/*	if(d.isFullScreenSupported()){
				d.setFullScreenWindow(frame);
			}*/

		}

		else{
			frame.setSize(width, height);
		}

		panel.add(bar, BorderLayout.SOUTH);
		frame.setContentPane(panel);

		animator.start();
		frame.setVisible(true);
		splash.close();

		frame.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				exit();
			}
		});
	}

	public static void showTimeMenu(){
		timeMenu.setSpinnerValues();
		timeMenu.setVisible(true);
		timeMenu.toFront();
	}

	public static void showLocationMenu(){
		locationMenu.setVisible(true);
		locationMenu.toFront();
	}

	public static void showSatelliteMenu(){
		satelliteMenu.setVisible(true);
		satelliteMenu.toFront();
	}


	public static void exit(){
		canvas.destroy();
		System.exit(0);
	}
}