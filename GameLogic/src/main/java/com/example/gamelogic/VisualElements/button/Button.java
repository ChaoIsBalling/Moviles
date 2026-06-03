package com.example.gamelogic.VisualElements.button;

import com.example.androidengine.AndroidGraphics;
import com.example.androidengine.State;
import com.example.gamelogic.VisualElements.Image;
import com.example.gamelogic.VisualElements.Text;
import com.example.gamelogic.VisualElements.VisualElement;

import com.example.androidengine.TouchEvent;
import com.example.gamelogic.VisualElements.figure.Circle;
import com.example.gamelogic.VisualElements.figure.Figure;
import com.example.gamelogic.VisualElements.figure.Hexagon;
import com.example.gamelogic.VisualElements.figure.Square;
import com.example.gamelogic.VisualElements.figure.Triangle;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Clase que representa un boton en la interfaz del juego
 */
public class Button extends VisualElement {

    //Atributos del botón
    private float w;
    //Determina si tiene esquinas redondeadas
    private boolean isRound = false;
    //Radio del arco bordeado
    private float arcRadius;

    private AndroidGraphics gr;

    //Texto del boton
    Text text;
    //id del boton
    String id;
    String color; //Color por defecto
    Image image; //Imagen
    Figure figure; //Figura del botón


    private JSONObject callback;

    //Funcion callback asociada al boton
    private ButtonClickListener onClickFunction;



    //Constructora que te crea un boton a partir de un Json con mas parametros
    //Quitar el booleano luego, es solo para hacer otra constrctora
    public Button(JSONObject json, AndroidGraphics gr)
    {
        super(json);
        try {
            this.gr=gr;
            this.id=json.getString("id");
            this.w= json.getInt("w");
            this.h=json.getInt("h");
            this.isRound=json.getBoolean("isRound");
            if(isRound)
                this.arcRadius=json.getInt("ar");

            this.color=json.getString("color");
            JSONObject callbackData=json.optJSONObject("callback");
            if(callbackData!=null)
                this.callback=json.getJSONObject("callback");
            //Ahora cargamos los elementos que haya en el boton

            JSONObject textData = json.optJSONObject("text");
            if(textData != null)
                this.text = new Text(textData,gr);

            JSONObject figData = json.optJSONObject("figure");
            if(figData != null){
               setFigure(figData);
            }

            JSONObject iconData = json.optJSONObject("icon");
            if(iconData != null){
                this.image=new Image(iconData,gr);
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

    public void setID(String newID){this.id=newID;}
    //setters
    public void setColor(String color){ this.color = color;}
    public void setAppeareance(JSONObject obj)
    {
        try {
            if(obj.getString("type").equals( "Figura"))
                setFigure(obj);
            else
                setImage(obj);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    public void setFigure(JSONObject figData){
        String form = null;
        try {
            form = figData.getString("form");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        switch (form){
            case "triangle":
                this.figure= new Triangle(figData);
                break;
            case "square":
                this.figure= new Square(figData);
                break;
            case "hexagon":
                this.figure=new Hexagon(figData);
                break;
            case "circle":
                this.figure=new Circle(figData);
                break;
        }
    }
    public void setImage(JSONObject obj){
        this.image=new Image(obj,this.gr);
    }

    public void cleanImages(){this.image=null;}

    // Setter para asignar el callback
    public void setOnClickListener(ButtonClickListener listener) {this.onClickFunction = listener;}

    // Setter para asignar el callback
    public void setCallback(State state) {
        if(this.callback!=null) {
            try {
                callback.getString("method");
                Method method = state.getClass().getMethod(callback.getString("method"), Button.class);
                method.invoke(state, this);
            } catch (JSONException | NoSuchMethodException | InvocationTargetException |
                     IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //getter de los parametros de tamaño y posicion
    public float getWidth(){return this.w;}

    public Text getTextButton(){ return this.text; }

    public String getId(){return this.id;}

    public Image getImgButton(){ return this.image; }
    public Figure getFigButton(){ return this.figure; }

    public JSONObject getCallback(){return this.callback;}


    /**
     * Comprueba si la coordenada x,y está dentro del botón
     */
    public boolean contains(float x, float y){
        return x >= this.x-this.w/2 && x <= this.x + this.w/2 &&
                y >= this.y-this.h/2 && y <= this.y + this.h/2;
    }
    public boolean isImageNull(){
        return image==null;
    }

    public boolean isFigureNull(){
        return figure==null;
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
            if(this.image !=null){
                image.RenderCentrado((int)this.x,(int)this.y);
            }
            //Renderizamos figuras si las tiene
            if(this.figure !=null){
                figure.RenderCentrado(gr, (int)this.x,(int)this.y);
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
