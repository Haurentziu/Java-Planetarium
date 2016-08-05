package com.haurentziu.bitmap_fonts;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.Buffer;
import java.util.ArrayList;


/**
 * Created by haurentziu on 31.07.2016.
 */
public final class FontLoader {

    private FontLoader(){

    }

    public static ArrayList<Character> loadFonts(String filePath, String imagePath){
        ArrayList<Character> font = new ArrayList<>();

        try {
            File input = new File(filePath);
            BufferedImage image = ImageIO.read(new File(imagePath));
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(input);
            doc.getDocumentElement().normalize();
            NodeList nodes = doc.getElementsByTagName("char");
            for(int i = 0; i < nodes.getLength(); i++) {
                Element element = (Element) nodes.item(i);
                int id = Integer.parseInt(element.getAttribute("id"));
                float x = (float)Integer.parseInt(element.getAttribute("x")) / image.getWidth();
                float y = 1 - (float)Integer.parseInt(element.getAttribute("y")) / image.getHeight();

                float width = (float)Integer.parseInt(element.getAttribute("width")) / image.getWidth();
                float height = (float)Integer.parseInt(element.getAttribute("height")) / image.getHeight();

                float xOffset = (float)Integer.parseInt(element.getAttribute("xoffset")) / image.getWidth();
                float yOffset = (float)Integer.parseInt(element.getAttribute("yoffset")) / image.getHeight();
                float xAdvance = (float)Integer.parseInt(element.getAttribute("xadvance")) / image.getWidth();

                Character character = new Character(id, x, y - height, width, height, xOffset, yOffset, xAdvance);
                font.add(character);
            }

        }
        catch (Exception ex){
            ex.printStackTrace();
        }

        return font;
    }
}
