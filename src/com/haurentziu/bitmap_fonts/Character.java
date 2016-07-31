package com.haurentziu.bitmap_fonts;

/**
 * Created by haurentziu on 31.07.2016.
 */
public class Character {
    private int id;
    private float x;
    private float y;
    private float height;
    private float width;
    private float xOffset;
    private float yOffset;
    private float xAdvance;

    public Character(int id, float x, float y, float width, float height, float xOffset, float yOffset, float xAdvance){
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
        this.xAdvance = xAdvance;
    }

    public float getX(){
        return x;
    }

    public float getY(){
        return y;
    }

    public float getHeight(){
        return height;
    }

    public float getWidth(){
        return width;
    }

    public float getMaxX(){
        return x + width;
    }

    public float getMaxY(){
        return y + height;
    }

    public float getXAdvance(){
        return xAdvance;
    }

    public float getYOffset(){
        return yOffset;
    }

    public float getXOffset(){
        return xOffset;
    }

    public int getID(){
        return id;
    }
}
