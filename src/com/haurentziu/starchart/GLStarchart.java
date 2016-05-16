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
    private SolarSystem system;
    private TextInfo text;

    private boolean showGround = true;
    private boolean showCardinalPoints = true;
    private boolean showConstellations = true;
    private boolean showAzGrid = false;
    private boolean showEcliptic = true;
    private boolean showCelestialEq = true;
    private boolean showStarNames = true;
    private boolean showEqGrid = false;
    private boolean showDSO = true;
    private boolean showMilkyWay = true;

    private int starNo;
    private int linesNo;
    private ArrayList<Integer> gridNo;
    private ArrayList<Integer> milkyWayNo;
    private int totalMW;
    private int groundNo;
    private int circleNo;
    private int totalGridNo;
    private int messierNo;


    private boolean isSelected = false;

    private Star selectedStar = new Star(0, 0, 0, 0, 0);

    private Shader starShader;
    private Shader constellationShader;
    private Shader markingsShader;
    private Shader messierShader;

    private IntBuffer vertexArray = IntBuffer.allocate(1);
    private IntBuffer colorArray = IntBuffer.allocate(1);
    private IntBuffer buffers = IntBuffer.allocate(2);
    private int vertexArraySize;
    private int colorArraySize;

    private int currentWarp = 8;
    private int timeWarpLevels[] = {-10000, -5000, -3000, -1000, -100, -10, -1, 0, 1, 10, 100, 1000, 3000, 5000, 10000};

    GLStarchart(){
        observer = new Observer();

        sky = new Sky();
        markings = new Markings();
        text = new TextInfo();
        system = new SolarSystem();

        ortoBounds = new Rectangle2D.Double();
        windowBounds = new Rectangle2D.Double();
    }


    @Override
    public void init(GLAutoDrawable glAutoDrawable) {
        GL3 gl = glAutoDrawable.getGL().getGL3();



        starShader = new Shader();
        starShader.loadAllShaders("./shader/vertex.glsl", "./shader/stars_geom.glsl", "./shader/star_frag.glsl");
        starShader.init(gl);

        messierShader = new Shader();
        messierShader.loadAllShaders("./shader/vertex.glsl", "./shader/messier_geom.glsl", "./shader/messier_frag.glsl");
        messierShader.init(gl);

        constellationShader = new Shader();
        constellationShader.loadAllShaders("./shader/vertex.glsl", "./shader/const_geom.glsl", "./shader/const_frag.glsl");
        constellationShader.init(gl);

        ground = new Ground(gl);

        markingsShader = new Shader();
        markingsShader.loadAllShaders("./shader/vertex.glsl", "./shader/marking_geom.glsl", "./shader/marking_frag.glsl");
        markingsShader.init(gl);

        ArrayList<Float> vertsList = new ArrayList<>();

        vertsList.add(0f);
        vertsList.add(0f);
        vertsList.add(0.4f);

        sky.loadStarsVerts(vertsList);
        starNo = vertsList.size()/3;

        sky.loadConstellationVerts(vertsList);
        linesNo = vertsList.size()/3 - starNo;

        ground.loadGroundVerts(vertsList);
        groundNo = vertsList.size()/3 - starNo - linesNo;

        gridNo = markings.loadAzGridVerts(vertsList);
        totalGridNo = vertsList.size()/3 - starNo - groundNo - linesNo;
        circleNo = markings.renderGreatCircle(vertsList);
        messierNo = sky.loadMessier(vertsList);

        milkyWayNo = sky.loadMilkyWayVerts(vertsList);

        float[] verts = Utils.floatArrayList2FloatArray(vertsList);
        vertexArraySize = verts.length;

        FloatBuffer vertexFB = FloatBuffer.wrap(verts);
        vertexFB.rewind();

        ArrayList<Float> colorList = new ArrayList<>();
        colorList.add(1f);
        colorList.add(0.965f);
        colorList.add(0f);

        sky.loadStarColors(colorList);

        float colors[] = Utils.floatArrayList2FloatArray(colorList);
        FloatBuffer colorFB = FloatBuffer.wrap(colors);
        colorFB.rewind();
        colorArraySize = colors.length;

        gl.glGenBuffers(2, buffers);
        gl.glBindVertexArray(buffers.get(0));

        gl.glGenVertexArrays(1, vertexArray);
        gl.glBindVertexArray(vertexArray.get(0));

        //vertex
        gl.glEnableVertexAttribArray(0);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers.get(0));
        gl.glBufferData(GL3.GL_ARRAY_BUFFER, 4 * verts.length, vertexFB, GL3.GL_DYNAMIC_DRAW);
        gl.glVertexAttribPointer(0, 3, GL3.GL_FLOAT, false, 0, 0L);

        //colors
        gl.glEnableVertexAttribArray(1);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers.get(1));
        gl.glBufferData(GL3.GL_ARRAY_BUFFER, 4 * colors.length, colorFB, GL3.GL_STREAM_DRAW);
        gl.glVertexAttribPointer(1, 3, GL3.GL_FLOAT, false, 0, 0L);

        colorFB.clear();
        vertexFB.clear();

        glAutoDrawable.getAnimator().setUpdateFPSFrames(20, null);
    }

    @Override
    public void dispose(GLAutoDrawable glAutoDrawable) {
        System.err.println("Closing application...");
        GL3 gl = glAutoDrawable.getGL().getGL3();
        starShader.deleteProgram(gl);
        ground.getShader().deleteProgram(gl);
        constellationShader.deleteProgram(gl);
        gl.glDeleteVertexArrays(vertexArraySize, vertexArray);
        gl.glDeleteVertexArrays(colorArraySize, colorArray);
        gl.glDeleteBuffers(2, buffers);
    }

    @Override
    public void display(GLAutoDrawable glAutoDrawable) {
        GL3 gl = glAutoDrawable.getGL().getGL3();

        gl.glClearColor(0f, 0.075f, 0.125f, 1f);
        gl.glClear(GL3.GL_COLOR_BUFFER_BIT | GL3.GL_DEPTH_BUFFER_BIT);
        observer.updateTime(timeWarpLevels[currentWarp]);

    //    System.out.println(glAutoDrawable.getAnimator().getLastFPS());
    //    System.out.println("uaie 6");
        int sentVerts = 0;
        if(showAzGrid || showEqGrid || showCelestialEq || showEcliptic || showMilkyWay) {
            markingsShader.useShader(gl);
            gl.glBindVertexArray(vertexArray.get(0));
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

        if(showMilkyWay){
            setUniformVariables(gl, markingsShader, 1);
            markingsShader.setVariable(gl, "color", 0f, 0.31f, 0.533f, 1f);
            sentVerts = starNo + linesNo + groundNo + totalGridNo + circleNo + messierNo;
            for(int i = 0; i < milkyWayNo.size(); i++){
                gl.glDrawArrays(GL3.GL_LINE_STRIP, sentVerts, milkyWayNo.get(i));
                sentVerts += milkyWayNo.get(i);
            }
        }

        if(showConstellations) {
            constellationShader.useShader(gl);
            gl.glBindVertexArray(vertexArray.get(0));
            setUniformVariables(gl, constellationShader, 1);
            gl.glDrawArrays(GL3.GL_LINES, starNo, linesNo);
        }

        if(showDSO){
            messierShader.useShader(gl);
            gl.glBindVertexArray(vertexArray.get(0));
            setUniformVariables(gl, messierShader, 1);
            gl.glDrawArrays(GL3.GL_POINTS, starNo + linesNo + groundNo + totalGridNo + circleNo, messierNo);
        }

        starShader.useShader(gl);
        gl.glBindVertexArray(vertexArray.get(0));
        setUniformVariables(gl, starShader, 1);
        gl.glDrawArrays(GL3.GL_POINTS, 1, starNo);

    /*    system.updateSystem(gl, buffers, observer.getJDE());
        setUniformVariables(gl, starShader, 2);
        gl.glDrawArrays(GL3.GL_POINTS, 0, 1);
*/
        if(showGround) {
            ground.getShader().useShader(gl);
            gl.glBindVertexArray(vertexArray.get(0));
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
        setSize(gl, messierShader, aspectRatio, 1f);

        ortoBounds.setRect(-aspectRatio, - 2, 2 * aspectRatio, 2);
        windowBounds.setRect(0, 0, i2, i3);
        observer.updateZoom(ortoBounds);
    }

    private void setSize(GL3 gl, Shader shader, float width, float height){
        shader.useShader(gl);
        shader.setVariable(gl, "width", width);
        shader.setVariable(gl, "height", height);
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
        showMilkyWay = !showMilkyWay;
    }

    void toogleDSO(){
        showDSO = !showDSO;
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
