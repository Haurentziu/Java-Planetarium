package com.haurentziu.render;

import com.jogamp.opengl.GL3;
import com.jogamp.opengl.util.texture.Texture;
import java.util.ArrayList;

/**
 * Created by haurentziu on 21.05.2016.
 */

public class Text{
    Shader shader;
    Texture texture;
    int start;

    public Text(String vertShader, String geomShader, String fragShader){
        shader = new Shader();
        shader.loadAllShaders(vertShader, geomShader, fragShader);

    }

    public void initialize(GL3 gl){
        shader.init(gl);
     //   loadTexture(gl);
    }

    public void loadVertices(ArrayList<Float> verts){
        start = verts.size() / 3;
        verts.add(0.5f);
        verts.add(0f);
        verts.add(0f);

        verts.add(0.2f);
        verts.add(0.3f);
        verts.add(0f);

    }

    public void render(GL3 gl){
        shader.useShader(gl);
        gl.glEnable(GL3.GL_TEXTURE_2D);
        gl.glActiveTexture(GL3.GL_TEXTURE0);
        texture.enable(gl);
        texture.bind(gl);
        shader.setVariable(gl, "myTexture", 0);
        gl.glDrawArrays(GL3.GL_POINTS, start, 3);
        texture.disable(gl);
    }


    public void loadTexCoord(ArrayList<Float> verts){
        verts.add(0f);
        verts.add(0f);

        verts.add(2f);
        verts.add(4f);

        verts.add(4f);
        verts.add(0f);
    }

}
