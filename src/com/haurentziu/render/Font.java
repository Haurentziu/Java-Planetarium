package com.haurentziu.render;

import com.haurentziu.bitmap_fonts.Character;
import com.jogamp.opengl.util.texture.Texture;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by haurentziu on 03.08.2016.
 */
public class Font {
    ArrayList<Character> characters;
    Texture fontTex;

    public Font(ArrayList<Character> characters, Texture fontTex){
        this.characters = characters;
    }





}
