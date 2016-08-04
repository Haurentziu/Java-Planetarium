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
	private static final int DEFAULT_WIDTH = 950;
	private static final int DEFAULT_HEIGHT = 950;

	public static StarchartCanvas canvas;
	public static TimeMenu timeMenu;
	public static LocationMenu locationMenu;
	public static SatelliteMenu satelliteMenu;
	public static JFrame frame = new JFrame("Java Planetarium");

	public static boolean fullScreen;
	public static void main(String[] args){
		try {
			UIManager.setLookAndFeel("com.jtattoo.plaf.hifi.HiFiLookAndFeel");
			//UIManager.getLookAndFeelDefaults().put("defaultFont", new Font("Arial", Font.BOLD, 14));

			//UIManager.setLookAndFeel("com.jtattoo.plaf.noire.NoireLookAndFeel");
		}
		catch (Exception ex){
			ex.printStackTrace();
		}

		fullScreen = args.length > 0 && args[0].equals("-fullscreen");
		Observer observer = new Observer();

		SplashDialog splash = new SplashDialog();
		splash.setVisible(true);

		GLProfile glp = GLProfile.getDefault();
		GLCapabilities caps = new GLCapabilities(glp.get(GLProfile.GL3));
		caps.setAlphaBits(8);
		canvas = new StarchartCanvas(caps, observer);

		FPSAnimator animator = new FPSAnimator(canvas, 120);
		ToolBar bar = new ToolBar(observer);

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
		final int x = (screenSize.width - DEFAULT_WIDTH) / 2;
		final int y = (screenSize.height - DEFAULT_HEIGHT) / 2;
		frame.setLocation(x, y);

		setSize();

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

	public static void toogleFullScreen(){
		fullScreen = !fullScreen;
		setSize();
	}

	public static void setSize(){
		if(fullScreen){
			frame.dispose();
			frame.setUndecorated(true);
			frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
			frame.setVisible(true);
		}
		else{
			frame.dispose();
			frame.setUndecorated(false);
			frame.setExtendedState(JFrame.NORMAL);
			frame.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
			frame.setVisible(true);

		}

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