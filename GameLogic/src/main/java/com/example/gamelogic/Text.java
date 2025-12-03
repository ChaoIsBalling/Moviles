package com.example.gamelogic;

import com.example.engine.Graphics;
import com.example.engine.IFont;
import org.json.JSONObject;
import org.json.JSONException;
import org.json.JSONArray;
/**
 * Clase que representa un texto
 */
public class Text {
    //Contenido y font del texto
    String text;
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
    int color = 0xFF000000;

    /**
     * Constructora del texto con su contenido, posición, tamaño y si es en negrito e italica
     */
    public Text(String font, String text, float x, float y,float size,boolean bold, boolean italic){
        this.font=font;
        this.text=text;
        this.x=x;
        this.y=y;
        this.size=size;
        this.bold=bold;
        this.italic=italic;
    }
    public Text(String font, String text, float x, float y){
        this.font=font;
        this.text=text;
        this.x=x;
        this.y=y;
        this.size=12;
        this.bold=false;
        this.italic=false;
    }
    public Text(String font, String text, float x, float y,float size){
        this.font=font;
        this.text=text;
        this.x=x;
        this.y=y;
        this.size=size;
        this.bold=false;
        this.italic=false;
    }
    public Text(String font, String text, float x, float y,float size,boolean bold){
        this.font=font;
        this.text=text;
        this.x=x;
        this.y=y;
        this.size=size;
        this.bold=bold;
        this.italic=false;
    }
    public Text(JSONObject json)
    {
        this.text= json.getString("texto");
        this.font=json.getString("font");
        this.x=json.getInt("x");
        this.y=json.getInt("y");
        this.size=json.getInt("size");
        this.bold= json.getBoolean("bold");
        this.italic =json.getBoolean("italic");
        this.color=json.getInt("color");
    }
    /**
     * Setters
     */
    public void setColor(int color){
        this.color = color;
    }
    public void setText(String text){
        this.text = text;
    }

    /**
     * Renderiza el texto (con y sin centrado) con la fuente y color deseados
     * @param gr
     */
    public void Render(Graphics gr){
        IFont fuente = gr.newFont(this.font,this.size,this.bold,this.italic);
        gr.setColor(this.color);
        gr.setFont(fuente);
        gr.pintarTextoCentrado(this.text, this.x,this.y);
    }
    public void RenderCentrado(Graphics gr,float x, float y){
        IFont fuente = gr.newFont(this.font,this.size,this.bold,this.italic);
        gr.setColor(this.color);
        gr.setFont(fuente);
        gr.pintarTextoCentrado(this.text, x+this.x,y+this.y);
    }
}
