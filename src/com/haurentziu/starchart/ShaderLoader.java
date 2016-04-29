package com.haurentziu.starchart;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.IntBuffer;

/**
 * Created by haurentziu on 29.04.2016.
 */
public class ShaderLoader {
    private int vertexShaderProgram;
    private int fragmentShaderProgram;

    private int shaderProgram;

    String[] vertexShader;
    String[] fragmentShader;

    String[] loadShader(String file) {
        StringBuilder sb = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String l;

            while ((l = br.readLine()) != null) {
                sb.append(l);
                sb.append("\n");
            }
            br.close();
        }

        catch (Exception ex) {
            ex.printStackTrace();
        }

        return new String[]{sb.toString()};
    }

    public void init(GL2 gl){
        try{
            attachShader(gl);
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void attachShader(GL2 gl){
        vertexShaderProgram = gl.glCreateShader(GL2.GL_VERTEX_SHADER);
        fragmentShaderProgram = gl.glCreateShader(GL2.GL_FRAGMENT_SHADER);
        gl.glShaderSource(vertexShaderProgram, 1, vertexShader, null, 0);
        gl.glCompileShader(vertexShaderProgram);
        gl.glShaderSource(fragmentShaderProgram, 1, fragmentShader, null, 0);
        gl.glCompileShader(fragmentShaderProgram);
        shaderProgram = gl.glCreateProgram();

        gl.glAttachShader(shaderProgram, vertexShaderProgram);
        gl.glAttachShader(shaderProgram, fragmentShaderProgram);
        gl.glLinkProgram(shaderProgram);
        gl.glValidateProgram(shaderProgram);
        IntBuffer buffer = IntBuffer.allocate(1);
        gl.glGetProgramiv(shaderProgram, GL2.GL_LINK_STATUS, buffer);

        if(buffer.get(0) != 1){
            gl.glGetProgramiv(shaderProgram, GL2.GL_INFO_LOG_LENGTH, buffer);
            System.err.println("I did it of sheep!");
            System.exit(0);
        }

    }

    public void useShader(GL2 gl){
        gl.glUseProgram(shaderProgram);
    }

    public void stopShader(GL2 gl){
        gl.glUseProgram(0);
    }

}
