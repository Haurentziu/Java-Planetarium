package com.haurentziu.render;

import com.haurentziu.bitmap_fonts.Character;
import com.haurentziu.bitmap_fonts.FontLoader;
import com.haurentziu.starchart.Observer;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;

import java.util.ArrayList;

/**
 * Created by haurentziu on 03.08.2016.
 */
public class TextRenderer extends Renderer{

    private Texture fontTex;
    private ObserverText observerText;
    private BodyText bodyText;
    private ArrayList<Character> font;


    public TextRenderer(String vertShader, String geomShader, String fragShader){
        super(vertShader, geomShader, fragShader);
        font = FontLoader.loadFonts("./res/fonts/font.fnt", "./res/fonts/font.png");
        observerText = new ObserverText(font);
        bodyText = new BodyText(font);

    }

    public void renderObserverText(GL3 gl, VBO vbo, Observer observer){
        observerText.updateText(gl, vbo, observer);
        observerText.render(gl, shader);

   }

    public void renderCelestialBodyText(GL3 gl){
        bodyText.render(gl, shader);
    }

    public void updateCelestialBodyText(GL3 gl, VBO vbo, Observer observer){
        bodyText.updateText(gl, vbo, observer);
    }

    public void loadVertices(ArrayList<Float> verts){
        observerText.loadVertices(verts);
        bodyText.loadVertices(verts);
    }


    @Override
    public void initialize(GL3 gl){
        shader.init(gl);
        fontTex = loadTexture(gl, "./res/fonts/font.png");
        observerText.setTexture(fontTex);
        bodyText.setTexture(fontTex);
    }

    @Override
    public void delete(GL3 gl){
        shader.deleteProgram(gl);
        fontTex.destroy(gl);
    }


    public void setAllAspectRatios(float aspectRatio){
        observerText.setAspectRatio(aspectRatio);
        bodyText.setAspectRatio(aspectRatio);
    }
}
