package com.haurentziu.starchart;

import com.jogamp.opengl.GL;
import com.jogamp.opengl.GL2;
import com.jogamp.opengl.GL3;
import com.jogamp.opengl.GL4;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

/**
 * Created by haurentziu on 29.04.2016.
 */
public class ShaderLoader {
    private int vertexShaderProgram;
    private int fragmentShaderProgram;
    private int geometryShaderProgram;

    private int shaderProgram;

    private String[] vertexShader;
    private String[] fragmentShader;
    private String[] geometryShader;

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

    public void init(GL3 gl){
        try{
            attachShader(gl);
        }

        catch (Exception e){
            e.printStackTrace();
        }
    }

    public void loadVertexShader(String file){
        vertexShader = loadShader(file);
    }

    public void loadFragmentShader(String file){
        fragmentShader = loadShader(file);
    }

    public void loadGeometryShader(String file){
        geometryShader = loadShader(file);
    }

    public void loadAllShaders(String vertexFile, String geometryFile, String fragmentFile){
        loadVertexShader(vertexFile);
        loadGeometryShader(geometryFile);
        loadFragmentShader(fragmentFile);
    }


    private void attachShader(GL3 gl){
        vertexShaderProgram = gl.glCreateShader(GL3.GL_VERTEX_SHADER);
        geometryShaderProgram = gl.glCreateShader(GL3.GL_GEOMETRY_SHADER);
        fragmentShaderProgram = gl.glCreateShader(GL3.GL_FRAGMENT_SHADER);

        gl.glShaderSource(vertexShaderProgram, 1, vertexShader, null, 0);
        gl.glCompileShader(vertexShaderProgram);

        gl.glShaderSource(geometryShaderProgram, 1, geometryShader, null, 0);
        gl.glCompileShader(geometryShaderProgram);

        gl.glShaderSource(fragmentShaderProgram, 1, fragmentShader, null, 0);
        gl.glCompileShader(fragmentShaderProgram);

        shaderProgram = gl.glCreateProgram();

        gl.glAttachShader(shaderProgram, vertexShaderProgram);
        gl.glAttachShader(shaderProgram, geometryShaderProgram);
        gl.glAttachShader(shaderProgram, fragmentShaderProgram);

        gl.glLinkProgram(shaderProgram);
        gl.glValidateProgram(shaderProgram);


        IntBuffer intBuffer = IntBuffer.allocate(1);
        gl.glGetProgramiv(shaderProgram, GL3.GL_LINK_STATUS, intBuffer);

        if (intBuffer.get(0) != 1){
            System.out.println("Something is fucked up!");
            System.exit(0);
        }

    }

    public void setVariable(GL3 gl, String name, float value){
        int location = gl.glGetUniformLocation(shaderProgram, name);
        gl.glUniform1f(location, value);
    }

    public void setVariable(GL3 gl, String name, float value1, float value2, float value3, float value4){
        int location = gl.glGetUniformLocation(shaderProgram, name);
        gl.glUniform4f(location, value1, value2, value3, value4);
    }


    public void setVariable(GL3 gl, String name, int value){
        int location = gl.glGetUniformLocation(shaderProgram, name);
        gl.glUniform1i(location, value);
    }

    public void deleteProgram(GL3 gl){
        gl.glDeleteProgram(shaderProgram);
    }

    public void useShader(GL3 gl){
        gl.glUseProgram(shaderProgram);
    }

    public void disableShader(GL3 gl){
        gl.glUseProgram(0);
    }

    public int getShaderProgram(){
        return shaderProgram;
    }

}
