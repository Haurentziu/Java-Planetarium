package com.haurentziu.starchart;

import com.haurentziu.astro_objects.Star;
import com.haurentziu.coordinates.EquatorialCoordinates;
import com.haurentziu.coordinates.HorizontalCoordinates;
import com.haurentziu.utils.Utils;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.util.awt.TextRenderer;
import com.jogamp.opengl.util.gl2.GLUT;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by haurentziu on 28.04.2016.
 */
public class TextInfo {
    public TextInfo(){

    }

    public void renderObserverInfo(Observer obs, double fps, double warp, Rectangle2D bounds, GL2 gl){
        GLUT glut = new GLUT();
        Date date = new Date(obs.getUnixTime());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        sdf.setTimeZone(TimeZone.getDefault());
        String formatedDate = sdf.format(date);

        gl.glColor3f(1f, 1f, 1f);
        String fpsString = String.format("FPS: %.2f", fps);
        gl.glRasterPos2d(bounds.getX(), bounds.getY() + 0.52);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, fpsString);

        String fovString = String.format("FOV: %.2f", Math.toDegrees(2 * obs.getFOV()));
        gl.glRasterPos2d(bounds.getX(), bounds.getY() + 0.42);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, fovString);

        gl.glRasterPos2d(bounds.getX(), bounds.getY() + 0.32);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Time Warp: " + warp + "x");

        gl.glRasterPos2d(bounds.getX(), bounds.getY() + 0.22);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Time: " + formatedDate);

        gl.glRasterPos2d(bounds.getX(), bounds.getY() + 0.12);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Observer's Altitude: " + Utils.rad2String(obs.getLatitude(), false, false));

        gl.glRasterPos2d(bounds.getX(), bounds.getY() + 0.02);
        glut.glutBitmapString(GLUT.BITMAP_HELVETICA_18, "Observer's Longitude: " + Utils.rad2String(obs.getLongitude(), false, false));
    }

    public void renderStarInfo(Star selectedStar, Observer obs, Rectangle2D window, GL2 gl){
        TextRenderer titleRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 26));
        titleRenderer.setColor(0.051f, 0.596f, 0.729f, 1f);
        titleRenderer.beginRendering((int)window.getWidth(), (int)window.getHeight());
        titleRenderer.draw("HIP " + selectedStar.getHipparcos(), 0, (int)window.getHeight() - 30);
        titleRenderer.endRendering();

        TextRenderer infoRenderer = new TextRenderer(new Font("SansSerif", Font.BOLD, 15));
        infoRenderer.setColor(0.141f, 0.784f, 0.941f, 1f);
        infoRenderer.beginRendering((int)window.getWidth(), (int)window.getHeight());
        infoRenderer.draw("Magnitude: " + selectedStar.getMagnitude(), 0, (int)window.getHeight() - 55);

        String bvString = String.format("B-V Color Index %.2f", selectedStar.getBVMagnitude());
        infoRenderer.draw(bvString, 0, (int) (window.getHeight() - 75));

        EquatorialCoordinates eq = selectedStar.getEquatorialCoordinates();
        String raString = Utils.rad2String(eq.getRightAscension(), false, true);
        String decString = Utils.rad2String(eq.getDeclination(), false, false);
        infoRenderer.draw("RA/Dec(J2000): "  + raString + "/" + decString, 0, (int)window.getHeight() - 95);

        HorizontalCoordinates az = eq.toHorizontal(obs.getLongitude(), obs.getLatitude(), obs.getSideralTime());
        String azString = Utils.rad2String(az.getAzimuth() - Math.PI, true, false);
        String altString = Utils.rad2String(az.getAltitude(), false, false);
        infoRenderer.draw("Az/Alt: " + azString + " / " + altString, 0, (int)window.getHeight() - 115);
        infoRenderer.endRendering();
    }
}
