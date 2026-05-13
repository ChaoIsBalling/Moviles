package com.example.gamelogic;

import org.json.JSONObject;
import org.json.JSONException;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidFont;
/**
 * Clase que representa un texto
 */
public class Text {
    //Font del texto
    String font;
    //Posición
    float x;
    float y;
    //tamaño
    float size;
    //Indican si es en negrita o italica
    boolean bold;
    boolean italic;
    //color del texto
    String color = "#FF000000";

    //el texto despues de hacerle un Split, tiene todo el contenido en string del texto
    String[] myArray;

    /**
     * Constructora del texto con su contenido, posición, tamaño y si es en negrito e italica
     */
    public Text(String font, String text, float x, float y,float size,boolean bold, boolean italic){
        this.font=font;
        this.myArray=text.split("\n");
        this.x=x;
        this.y=y;
        this.size=size;
        this.bold=bold;
        this.italic=italic;
    }
    public Text(String font, String text, float x, float y){
        this.font=font;
        this.myArray=text.split("\n");
        this.x=x;
        this.y=y;
        this.size=12;
        this.bold=false;
        this.italic=false;
    }
    public Text(String font, String text, float x, float y,float size){
        this.font=font;
        this.myArray=text.split("\n");
        this.x=x;
        this.y=y;
        this.size=size;
        this.bold=false;
        this.italic=false;
    }
    public Text(String font, String text, float x, float y,float size,boolean bold){
        this.font=font;
        this.myArray=text.split("\n");
        this.x=x;
        this.y=y;
        this.size=size;
        this.bold=bold;
        this.italic=false;
    }
    public Text(JSONObject json)
    {
        try{
            String text= json.getString("texto");
            this.myArray=text.split("\n");
            this.font=json.getString("font");
            this.x=json.getInt("x");
            this.y=json.getInt("y");
            this.size=json.getInt("size");
            this.bold= json.getBoolean("bold");
            this.italic =json.getBoolean("italic");
            this.color=json.getString("color");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Setters
     */
    public void setColor(String color){
        this.color = color;
    }
    public void setText(String text){
        this.myArray=text.split("\n");
    }
    public void setY(float y){this.y=y;}
    public void setX(float x){this.x=x;}

    public String getText(){
        return String.join("\n", this.myArray);
    }

    /**
     * Renderiza el texto (con y sin centrado) con la fuente y color deseados
     * @param gr
     */
    public void Render(AndroidGraphics gr){
        if(this.y>=-size) {
            AndroidFont fuente = gr.newFont(this.font, this.size, this.bold, this.italic);
            gr.setColor(this.color);
            gr.setFont(fuente);
            for (int i = 0; i < myArray.length; i++)
                gr.pintarTextoCentrado(myArray[i], this.x, this.y + i * size);
        }
    }
    public void RenderCentrado(AndroidGraphics gr,float x, float y){
        AndroidFont fuente = gr.newFont(this.font,this.size,this.bold,this.italic);
        gr.setColor(this.color);
        gr.setFont(fuente);
        for(int i=0;i<myArray.length;i++)
            gr.pintarTextoCentrado(myArray[i], x+this.x,y+this.y+i*size);
    }
    /**
     * Getters
     */
    public float getX(){return this.x;}
    public float getY(){return this.y;}
}
