package com.haurentziu.starchart;

import com.haurentziu.utils.Utils;
import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;

import java.awt.geom.Rectangle2D;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

/**
 * Created by haurentziu on 27.04.2016.
 */

public class Starchart implements GLEventListener{

    private Rectangle2D ortoBounds;
    private Rectangle2D windowBounds;
    private Observer observer;

    private Sky sky;
    private Ground ground;
    private Markings markings;
    private TextInfo text;

    private boolean showGround = true;
    private boolean showCardinalPoints = true;
    private boolean showConstellations = true;
    private boolean showAzGrid = false;
    private boolean showEcliptic = true;
    private boolean showCelestialEq = true;
    private boolean showStarNames = true;
    private boolean showMilkyWay = true;

    private int starNo;

    private int programLoc;
    private int linesNo;

    private boolean isSelected = false;
    private Star selectedStar = new Star(0, 0, 0, 0, 0);

    public ShaderLoader starShader;
    public ShaderLoader constellationShader;

    private int currentWarp = 8;
    private int timeWarpLevels[] = {-10000, -5000, -3000, -1000, -100, -10, -1, 0, 1, 10, 100, 1000, 3000, 5000, 10000};

    public Starchart(){
        observer = new Observer();

        sky = new Sky();
        ground = new Ground();
        markings = new Markings();
        text = new TextInfo();

        ortoBounds = new Rectangle2D.Double();
        windowBounds = new Rectangle2D.Double();
    }


    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL3 gl = glAutoDrawable.getGL().getGL3();

        starShader = new ShaderLoader();
        starShader.loadAllShaders("./shader/vertex.glsl", "./shader/stars_geom.glsl", "./shader/star_frag.glsl");
        starShader.init(gl);

        constellationShader = new ShaderLoader();
        constellationShader.loadAllShaders("./shader/vertex.glsl", "./shader/const_geom.glsl", "./shader/const_frag.glsl");
        constellationShader.init(gl);

        ArrayList<Float> vertsArray = new ArrayList<>();

        sky.loadStarsVerts(gl, vertsArray);
        starNo = vertsArray.size()/3;

        sky.loadConstellationVerts(gl, vertsArray);
        linesNo = vertsArray.size()/3 - starNo;

        float[] verts = Utils.floatArrayList2FloatArray(vertsArray);
        sendVerts(gl, verts);

        glAutoDrawable.getAnimator().setUpdateFPSFrames(20, null);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {

    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL3 gl = glAutoDrawable.getGL().getGL3();
        gl.glClearColor(0f, 0.075f, 0.125f, 1f);
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT);
        observer.updateTime(timeWarpLevels[currentWarp]);

        System.out.println(glAutoDrawable.getAnimator().getLastFPS());

        if(showConstellations) {
            constellationShader.useShader(gl);
            setUniformVariables(gl, constellationShader, 1f);
            gl.glDrawArrays(GL3.GL_LINES, starNo, starNo + 1 + linesNo);
        }

        starShader.useShader(gl);
        setUniformVariables(gl, starShader, 1f);
        gl.glDrawArrays(GL3.GL_POINTS, 0, starNo);



    }



    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
        final GL3 gl = glAutoDrawable.getGL().getGL3();
        float aspectRatio = (float)i2/i3;

        starShader.useShader(gl);
        starShader.setVariable(gl, "width", aspectRatio);
        starShader.setVariable(gl, "height", 1);

        constellationShader.useShader(gl);
        constellationShader.setVariable(gl, "width", aspectRatio);
        constellationShader.setVariable(gl, "height", 1);

        ortoBounds.setRect(-aspectRatio, - 2, 2 * aspectRatio, 2);
        windowBounds.setRect(0, 0, i2, i3);
        observer.updateZoom(ortoBounds);
    }


   void sendVerts(GL3 gl, float[] verts){
       IntBuffer vao = Buffers.newDirectIntBuffer(1);
       gl.glGenVertexArrays(1, vao);
       gl.glBindVertexArray(vao.get(0));

       IntBuffer vbo = Buffers.newDirectIntBuffer(1);
       FloatBuffer vertBuffer = Buffers.newDirectFloatBuffer(verts);

       gl.glGenBuffers(1, vbo);

       gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo.get(0));
       gl.glBufferData(GL3.GL_ARRAY_BUFFER, vertBuffer.limit() * Buffers.SIZEOF_FLOAT, vertBuffer, GL3.GL_STATIC_DRAW);


       int posAttribute = gl.glGetAttribLocation(starShader.getShaderProgram(), "pos");
       gl.glEnableVertexAttribArray(posAttribute);
       gl.glVertexAttribPointer(posAttribute, 3, GL3.GL_FLOAT, false, 0, 0L);



   }

    private void setUniformVariables(GL3 gl, ShaderLoader shader, float transformation){
        shader.setVariable(gl, "zoom", (float)observer.getZoom());
        shader.setVariable(gl, "azimuth_rotation", (float)observer.getAzRotation());
        shader.setVariable(gl, "altitude_rotation", (float)observer.getAltRotation());
        shader.setVariable(gl, "sideral_time", (float)observer.getSideralTime());

        shader.setVariable(gl, "transform_type", transformation);

        shader.setVariable(gl, "observer_latitude", (float)observer.getLatitude());
        shader.setVariable(gl, "observer_longitude", (float)(observer.getLongitude()));

    }

    Observer getObserver(){
        return observer;
    }

    Rectangle2D getBounds(){
        return ortoBounds;
    }

    Rectangle2D getWindowBounds(){
        return windowBounds;
    }

    void toogleGround(){
        showGround = !showGround;
    }

    void tooglePoints(){
        showCardinalPoints = !showCardinalPoints;
    }

    void toogleConstellations(){
        showConstellations = !showConstellations;
    }

    void changeWarp(int amount){
        int newWarp = amount + currentWarp;
        if(newWarp >= 0 && newWarp < timeWarpLevels.length){
            currentWarp = newWarp;
            System.out.println(timeWarpLevels[currentWarp]);
        }
    }

    void toogleGrid(){
        showAzGrid = !showAzGrid;
    }

    void toogleEcliptic(){
        showEcliptic = !showEcliptic;
    }

    void toogleCelestialEq(){
        showCelestialEq = !showCelestialEq;
    }

    void toogleStarNames(){
        showStarNames = !showStarNames;
    }

    void toogleMilkyWay(){
        showMilkyWay = !showMilkyWay;
    }

    void setSelected(boolean isSelected){
        this.isSelected = isSelected;
    }

    void setSelectedStar(Star star){
        this.selectedStar = star;
    }

        ArrayList<Star> getStars(){
        return sky.getStars();
    }


}
