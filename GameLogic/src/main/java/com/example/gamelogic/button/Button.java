package com.example.gamelogic.button;

import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.State;
import com.example.gamelogic.Image;
import com.example.gamelogic.Text;
import com.example.gamelogic.UIElement;
import com.example.gamelogic.figure.Figure;

import com.example.androidengine.TouchEvent;
import com.example.gamelogic.figure.Hexagon;
import com.example.gamelogic.figure.Square;
import com.example.gamelogic.figure.Triangle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Clase que representa un boton en la interfaz del juego
 */
public class Button extends UIElement {

    //Atributos del botón
    private float w,h;

    //Determina si tiene esquinas redondeadas
    private boolean isRound = false;
    //Radio del arco bordeado
    private float arcRadius;

    //Determina si el boton esta activado
    private boolean isEnable = true;

    //Determina si el boton esta visible
    private boolean isVisible = true;

    //Texto del boton
    Text text;
    //id del boton
    String id;
    String color; //Color por defecto
    ArrayList<Image> images = new ArrayList<Image>(); //Imagen
    ArrayList<Figure> figures = new ArrayList<Figure>(); //Figura del botón

    private JSONObject callback;

    //Funcion callback asociada al boton
    private ButtonClickListener onClickFunction;


    //Constructora que te crea un boton a partir de un Json
    public Button(JSONObject json)
    {
        super(json);
        try {
            this.w= json.getInt("w");
            this.h=json.getInt("h");
            this.isRound=json.getBoolean("isRound");
            if(isRound)
                this.arcRadius=json.getInt("ar");

            this.color=json.getString("color");


        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    //Constructora que te crea un boton a partir de un Json con mas parametros
    //Quitar el booleano luego, es solo para hacer otra constrctora
    public Button(JSONObject json, AndroidGraphics gr)
    {
        super(json);
        try {
            this.id=json.getString("id");
            this.w= json.getInt("w");
            this.h=json.getInt("h");
            this.isRound=json.getBoolean("isRound");
            if(isRound)
                this.arcRadius=json.getInt("ar");

            this.color=json.getString("color");
            this.callback=json.getJSONObject("callback");

            //Ahora cargamos los elementos que haya en el boton

            JSONObject textData = json.optJSONObject("text");
            if(textData != null)
                this.text = new Text(textData);

            JSONObject figData = json.optJSONObject("figure");
            if(figData != null){
                String form = figData.getString("form");
                switch (form){
                    case "triangle":
                        this.figures.add(new Triangle(figData));
                        break;
                    case "square":
                        this.figures.add(new Square(figData));
                        break;
                    case "hexagon":
                        this.figures.add(new Hexagon(figData));
                        break;
                }
            }

            JSONObject iconData = json.optJSONObject("icon");
            if(iconData != null){
                this.images.add(new Image(iconData,gr));
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Setters de atributos del botón
     */
    public void setText(Text text) {
        this.text = text;
    }
    //metodo que cambia el string del texto
    public void changeText(String message)
    {
        this.text.setText(message);
    }

    //setters
    public void setColor(String color){ this.color = color; }
    public void setFigures(Figure fig){ this.figures.add(fig); }
    public void setImages(Image img){this.images.add(img);}

    public void cleanImages(){this.images.clear();}
    public void setEnabled(boolean enabled){this.isEnable = enabled;}
    public void setVisible(boolean visible){this.isVisible = visible;}

    // Setter para asignar el callback
    public void setOnClickListener(ButtonClickListener listener) {this.onClickFunction = listener;}

    // Setter para asignar el callback
    public void setCallback(State state) {
        try {
            callback.getString("method");
            Method method =state.getClass().getMethod(callback.getString("method"), Button.class);
            method.invoke(state,this);
        } catch (JSONException | NoSuchMethodException | InvocationTargetException |
                 IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    //getter de los parametros de tamaño y posicion
    public float getWidth(){return this.w;}
    public float getHeight(){return this.h;}

    public Text getTextButton(){ return this.text; }

    public String getId(){return this.id;}

    public Image getImgButton(int i){ return this.images.get(i); }
    public Figure getFigButton(int i){ return this.figures.get(i); }

    public JSONObject getCallback(){return this.callback;}
    public boolean isEmptyImages(){
        return images.isEmpty();
    }
    public boolean isEmptyFigures(){
        return figures.isEmpty();
    }
    public boolean isEnable(){ return this.isEnable; }
    public  boolean isVisible(){ return this.isVisible; }
    /**
     * Comprueba si la coordenada x,y está dentro del botón
     */
    public boolean contains(float x, float y){
        return x >= this.x-this.w/2 && x <= this.x + this.w/2 &&
                y >= this.y-this.h/2 && y <= this.y + this.h/2;
    }


    /**
     * Metodo que renderiza el boton
     * @param gr
     */
    @Override
    public void Render(AndroidGraphics gr) {
        if(this.isVisible&&this.y>=-h){
            //Renderizamos el cuadrado que representa el botón
            gr.setColor(this.color);

            //Vemos si es redondeado o no
            if(isRound)
                gr.rellenarCuadradoRedondeado(this.x,this.y,this.w,this.h,this.arcRadius);
            else
                gr.rellenarCuadrado(this.x,this.y,this.w,this.h);

            //Renderizamos imagen si la tiene
            if(!this.images.isEmpty()){
                for(Image i : images){
                    if(i != null)
                        i.RenderCentrado((int)this.x,(int)this.y);
                }
            }

            //Renderizamos figuras si las tiene
            if(!this.figures.isEmpty()){
                for(Figure f : figures){
                    if(f != null)
                        f.RenderCentrado(gr, (int)this.x,(int)this.y);
                }
            }

            //Renderizamos texto centrado
            if(this.text != null){
                this.text.RenderCentrado(gr,this.x,this.y);
            }

        }

    }

    /**
     * Metodo que ejecuta el metodo callback asociado al boton si es pulsado y devuelve true
     * @param event evento de interacción con el usuario
     * @return true si se ha pulsado y false si no se ha pulsado
     */
    public boolean handleInput(TouchEvent event){
        if(event.type == TouchEvent.TouchEventType.TOUCH_DOWN){
            if(contains(event.x, event.y) && this.isEnable && onClickFunction != null){
                onClickFunction.onClick();
                return true;
            }
        }
        return false;
    }

}
