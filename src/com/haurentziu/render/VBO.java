package com.haurentziu.render;

import com.haurentziu.utils.Utils;
import com.jogamp.opengl.GL3;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;

/**
 * Created by haurentziu on 21.05.2016.
 */

public class VBO {

    private IntBuffer vertexArray = IntBuffer.allocate(1);
    private IntBuffer buffers = IntBuffer.allocate(3);
    private int vertexArraySize;


    public void delete(GL3 gl){
        gl.glDeleteVertexArrays(vertexArraySize, vertexArray);
    //    gl.glDeleteBuffers(texArraySize, texArray);
        gl.glDeleteBuffers(3, buffers);
    }

    public IntBuffer getBuffers(){
        return buffers;
    }

    public void init(GL3 gl, ArrayList<Float> vertList, ArrayList<Float> colorList){
        float verts[] = Utils.floatArrayList2FloatArray(vertList);
        vertexArraySize = verts.length;

        FloatBuffer vertexFB = FloatBuffer.wrap(verts);
        vertexFB.rewind();

        gl.glGenBuffers(3, buffers);
        gl.glBindVertexArray(buffers.get(0));

        gl.glGenBuffers(1, vertexArray);
        gl.glBindVertexArray(vertexArray.get(0));

        //vertex
        gl.glEnableVertexAttribArray(0);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers.get(0));
        gl.glBufferData(GL3.GL_ARRAY_BUFFER, 4 * verts.length, vertexFB, GL3.GL_DYNAMIC_DRAW);
        gl.glVertexAttribPointer(0, 3, GL3.GL_FLOAT, false, 36, 0);

        //colors
        gl.glEnableVertexAttribArray(1);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers.get(1));
        gl.glBufferData(GL3.GL_ARRAY_BUFFER, 4 * verts.length, vertexFB, GL3.GL_DYNAMIC_DRAW);
        gl.glVertexAttribPointer(1, 3, GL3.GL_FLOAT, false, 36, 12);

        //texture and misc proprieties
        gl.glEnableVertexAttribArray(2);
        gl.glBindBuffer(GL3.GL_ARRAY_BUFFER, buffers.get(2));
        gl.glBufferData(GL3.GL_ARRAY_BUFFER, 4 * verts.length, vertexFB, GL3.GL_DYNAMIC_DRAW);
        gl.glVertexAttribPointer(2, 3, GL3.GL_FLOAT, false, 36, 24);

        vertexFB.clear();
    }
}
