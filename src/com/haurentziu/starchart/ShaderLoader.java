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

    String[] vertexShader;
    String[] fragmentShader;
    String[] geometryShader;

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

    private void attachShader(GL3 gl){
        vertexShaderProgram = gl.glCreateShader(GL3.GL_VERTEX_SHADER);
        fragmentShaderProgram = gl.glCreateShader(GL3.GL_FRAGMENT_SHADER);
        geometryShaderProgram = gl.glCreateShader(GL3.GL_GEOMETRY_SHADER);

        gl.glShaderSource(vertexShaderProgram, 1, vertexShader, null, 0);
        gl.glCompileShader(vertexShaderProgram);

        gl.glShaderSource(fragmentShaderProgram, 1, fragmentShader, null, 0);
        gl.glCompileShader(fragmentShaderProgram);

        gl.glShaderSource(geometryShaderProgram, 1, geometryShader, null, 0);
        gl.glCompileShader(geometryShaderProgram);

        shaderProgram = gl.glCreateProgram();

        gl.glAttachShader(shaderProgram, vertexShaderProgram);
        gl.glAttachShader(shaderProgram, fragmentShaderProgram);
        gl.glAttachShader(shaderProgram, geometryShaderProgram);

        gl.glLinkProgram(shaderProgram);
        gl.glValidateProgram(shaderProgram);


        IntBuffer intBuffer = IntBuffer.allocate(1);
        gl.glGetProgramiv(shaderProgram, GL3.GL_LINK_STATUS, intBuffer);

        if (intBuffer.get(0) != 1){

            gl.glGetProgramiv(shaderProgram, GL3.GL_INFO_LOG_LENGTH, intBuffer);
            int size = intBuffer.get(0);
            System.err.println("Program link error: ");
            if (size > 0){

                ByteBuffer byteBuffer = ByteBuffer.allocate(size);
                gl.glGetProgramInfoLog(shaderProgram, size, intBuffer, byteBuffer);
                for (byte b : byteBuffer.array()){
                    System.err.print((char) b);
                }
            }

            else{
                System.out.println("Unknown");
            }
            System.out.println();
            System.exit(0);
        }

    }

    public void setVariable(GL3 gl, String name, float value){
        int location = gl.glGetUniformLocation(shaderProgram, name);
        gl.glUniform1f(location, value);
    }

    public void setVariable(GL3 gl, String name, int value){
        int location = gl.glGetUniformLocation(shaderProgram, name);
        gl.glUniform1f(location, value);
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
