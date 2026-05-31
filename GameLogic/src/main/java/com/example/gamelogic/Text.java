package com.example.gamelogic;

import org.json.JSONObject;
import org.json.JSONException;
import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.AndroidFont;
/**
 * Clase que representa un texto
 */
public class Text extends UIElement {
    //Font del texto
    String font;
    //tamaño
    float size;
    //Indican si es en negrita o italica
    boolean bold;
    boolean italic;
    private AndroidGraphics gr;

    private AndroidFont fuente;

    //Booleano que determina si se puede imprimir el texto o no
    boolean visible;
    //color del texto
    String color = "#FF000000";

    //el texto despues de hacerle un Split, tiene todo el contenido en string del texto
    String[] myArray;
    public Text(JSONObject json, AndroidGraphics gr)
    {
        super(json);
        try{
            this.gr=gr;
            String text= json.getString("texto");
            this.myArray=text.split("\n");
            this.font=json.getString("font");
            this.size=json.getInt("size");
            this.bold= json.getBoolean("bold");
            this.italic =json.getBoolean("italic");
            this.color=json.getString("color");
            this.visible = true;
            this.fuente = this.gr.newFont(this.font,this.size,this.bold,this.italic);
            this.gr.setColor(this.color);
            this.gr.setFont(fuente);
            this.h=gr.getTextBounds(text).height();
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

    public void setVisible(boolean c){ this.visible = c; }
    public String getText(){
        return String.join("\n", this.myArray);
    }

    /**
     * Renderiza el texto (con y sin centrado) con la fuente y color deseados
     * @param gr
     */
    @Override
    public void Render(AndroidGraphics gr){
        if(this.visible){
            if(this.y>=-size) {
                gr.setColor(this.color);
                gr.setFont(fuente);
                for (int i = 0; i < myArray.length; i++)
                    gr.pintarTextoCentrado(myArray[i], this.x, this.y + i * size);
            }
        }
    }
    public void RenderCentrado(AndroidGraphics gr,float x, float y){
        if(this.visible){
            gr.setColor(this.color);
            gr.setFont(fuente);
            for(int i=0;i<myArray.length;i++)
                gr.pintarTextoCentrado(myArray[i], x+this.x,y+this.y+i*size);
        }
    }
}
