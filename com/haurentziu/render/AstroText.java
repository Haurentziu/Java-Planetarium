package com.haurentziu.render;

import com.haurentziu.starchart.Observer;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.util.ArrayList;

/**
 * Created by haurentziu on 21.05.2016.
 */

public class AstroText extends Renderer{
    private Texture texture;

    public AstroText(String vertShader, String geomShader, String fragShader){
        super(vertShader, geomShader, fragShader);
    }

    @Override
    public void initialize(GL3 gl){
        shader.init(gl);
        texture = loadTexture(gl, "./res/textures/planets_name.png");
    }

    @Override
    public void delete(GL3 gl){
        shader.deleteProgram(gl);
        texture.destroy(gl);
    }

    public void render(GL3 gl, Observer observer, int start, int size){
        shader.useShader(gl);
        setObserver(gl, observer);
        gl.glDrawArrays(GL3.GL_POINTS, start, size);
    }

}
