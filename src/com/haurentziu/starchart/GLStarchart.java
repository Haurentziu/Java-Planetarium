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

public class GLStarchart implements GLEventListener{

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
    private boolean showEqGrid = false;

    private int starNo;
    private int linesNo;
    private ArrayList<Integer> gridNo;
    private int groundNo;
    private int circleNo;
    private int totalGridNo;
    private IntBuffer vbo;
    private int bufferSize;

    private boolean isSelected = false;

    private Star selectedStar = new Star(0, 0, 0, 0, 0);

    private Shader starShader;
    private Shader constellationShader;
    private Shader markingsShader;

    private int currentWarp = 8;
    private int timeWarpLevels[] = {-10000, -5000, -3000, -1000, -100, -10, -1, 0, 1, 10, 100, 1000, 3000, 5000, 10000};

    public GLStarchart(){
        observer = new Observer();

        sky = new Sky();
        markings = new Markings();
        text = new TextInfo();

        ortoBounds = new Rectangle2D.Double();
        windowBounds = new Rectangle2D.Double();
    }


    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL3 gl = glAutoDrawable.getGL().getGL3();

        starShader = new Shader();
        starShader.loadAllShaders("./shader/vertex.glsl", "./shader/stars_geom.glsl", "./shader/star_frag.glsl");
        starShader.init(gl);

        constellationShader = new Shader();
        constellationShader.loadAllShaders("./shader/vertex.glsl", "./shader/const_geom.glsl", "./shader/const_frag.glsl");
        constellationShader.init(gl);

        ground = new Ground(gl);

        markingsShader = new Shader();
        markingsShader.loadAllShaders("./shader/vertex.glsl", "./shader/marking_geom.glsl", "./shader/marking_frag.glsl");
        markingsShader.init(gl);

        ArrayList<Float> vertsArray = new ArrayList<>();

        sky.loadStarsVerts(vertsArray);
        starNo = vertsArray.size()/3;

        sky.loadConstellationVerts(vertsArray);
        linesNo = vertsArray.size()/3 - starNo;

        ground.loadGroundVerts(vertsArray);
        groundNo = vertsArray.size()/3 - starNo - linesNo;

        gridNo = markings.loadAzGridVerts(vertsArray);
        totalGridNo = vertsArray.size()/3 - starNo - groundNo - linesNo;
        circleNo = markings.renderGreatCircle(vertsArray);

        bufferSize = vertsArray.size();

        float[] verts = Utils.floatArrayList2FloatArray(vertsArray);
    //    System.out.println(verts.length);
        sendVerts(gl, verts, "pos");

        glAutoDrawable.getAnimator().setUpdateFPSFrames(20, null);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        System.out.println("Closing...");
        GL3 gl = glAutoDrawable.getGL().getGL3();
        starShader.deleteProgram(gl);
        ground.getShader().deleteProgram(gl);
        constellationShader.deleteProgram(gl);
        gl.glDeleteBuffers(bufferSize, vbo);
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL3 gl = glAutoDrawable.getGL().getGL3();
        gl.glClearColor(0f, 0.075f, 0.125f, 1f);
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT);
        observer.updateTime(timeWarpLevels[currentWarp]);

        System.out.println(glAutoDrawable.getAnimator().getLastFPS());
        int sentVerts = 0;
        if(showAzGrid || showEqGrid || showCelestialEq || showEcliptic) {
            markingsShader.useShader(gl);

        }

        if(showAzGrid){
            setUniformVariables(gl, markingsShader, 0);
            markingsShader.setVariable(gl, "color", 0.404f, 0.302f, 0f, 1f);
            for (int i = 0; i < gridNo.size(); i++) {
                gl.glDrawArrays(GL3.GL_LINE_STRIP, starNo + linesNo + groundNo + sentVerts, gridNo.get(i));
                sentVerts += gridNo.get(i);
            }
        }

        sentVerts = 0;
        if(showEqGrid){
            setUniformVariables(gl, markingsShader, 1);
            markingsShader.setVariable(gl, "color", 0.365f, 0.541f, 659f, 1f);
            for (int i = 0; i < gridNo.size(); i++) {
                gl.glDrawArrays(GL3.GL_LINE_STRIP, starNo + linesNo + groundNo + sentVerts, gridNo.get(i));
                sentVerts += gridNo.get(i);
            }
        }

        if(showCelestialEq){
            setUniformVariables(gl, markingsShader, 1);
            markingsShader.setVariable(gl, "color", 1f, 0f, 0f, 1f);
            gl.glDrawArrays(GL3.GL_LINE_STRIP, starNo + linesNo + groundNo + totalGridNo, circleNo);
        }

        if(showEcliptic){
            setUniformVariables(gl, markingsShader, 2);
            markingsShader.setVariable(gl, "color", 0.741f, 0.718f, 0.42f, 1f);
            gl.glDrawArrays(GL3.GL_LINE_STRIP, starNo + linesNo + groundNo + totalGridNo, circleNo);
        }

        if(showConstellations) {
            constellationShader.useShader(gl);
            setUniformVariables(gl, constellationShader, 1);
            gl.glDrawArrays(GL3.GL_LINES, starNo, linesNo);
        }

        starShader.useShader(gl);
        setUniformVariables(gl, starShader, 1);
        gl.glDrawArrays(GL3.GL_POINTS, 0, starNo);

        if(showGround) {
            ground.getShader().useShader(gl);
            setUniformVariables(gl, ground.getShader(), 0);
            ground.render(gl, 0);
        }

    }

    @Override
    public void reshape(GLAutoDrawable glAutoDrawable, int i, int i1, int i2, int i3) {
        final GL3 gl = glAutoDrawable.getGL().getGL3();
        float aspectRatio = (float)i2/i3;

        setSize(gl, starShader, aspectRatio, 1f);
        setSize(gl, constellationShader, aspectRatio, 1f);
        setSize(gl, ground.getShader(), aspectRatio, 1f);
        setSize(gl, markingsShader, aspectRatio, 1f);

        ortoBounds.setRect(-aspectRatio, - 2, 2 * aspectRatio, 2);
        windowBounds.setRect(0, 0, i2, i3);
        observer.updateZoom(ortoBounds);
    }

    void setSize(GL3 gl, Shader shader, float width, float height){
        shader.useShader(gl);
        shader.setVariable(gl, "width", width);
        shader.setVariable(gl, "height", height);
    }


   void sendVerts(GL3 gl, float[] verts, String name){
       IntBuffer vao = Buffers.newDirectIntBuffer(1);
       gl.glGenVertexArrays(1, vao);
       gl.glBindVertexArray(vao.get(0));

       vbo = Buffers.newDirectIntBuffer(1);
       FloatBuffer vertBuffer = Buffers.newDirectFloatBuffer(verts);

       gl.glGenBuffers(1, vbo);

       gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, vbo.get(0));
       gl.glBufferData(GL3.GL_ARRAY_BUFFER, vertBuffer.limit() * Buffers.SIZEOF_FLOAT, vertBuffer, GL3.GL_STATIC_DRAW);


       int posAttribute = gl.glGetAttribLocation(starShader.getShaderProgram(), name);
       gl.glEnableVertexAttribArray(posAttribute);
       gl.glVertexAttribPointer(posAttribute, 3, GL3.GL_FLOAT, false, 0, 0L);
       vertBuffer.clear();

   }

    private void setUniformVariables(GL3 gl, Shader shader, int transformation){
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

    void toogleAzGrid(){
        showAzGrid = !showAzGrid;
    }

    void toogleEcliptic(){
        showEcliptic = !showEcliptic;
    }

    void toogleEqGrid(){
        showEqGrid = !showEqGrid;
    }

    void toogleStarNames(){
        showStarNames = !showStarNames;
    }

    void toogleCelestialEq(){
        showCelestialEq = !showCelestialEq;
    }

    void toogleMilkyWay(){
      //  showMilkyWay = !showMilkyWay;
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
