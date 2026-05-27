package com.example.gamelogic.button;

import com.example.androidengine.AndroidGraphics;
import com.example.gamelogic.Image;
import com.example.gamelogic.Text;
import com.example.gamelogic.figure.Figure;

import com.example.androidengine.TouchEvent;
import com.example.gamelogic.figure.Hexagon;
import com.example.gamelogic.figure.Square;
import com.example.gamelogic.figure.Triangle;

import org.json.JSONObject;
import org.json.JSONException;

/**
 * Clase que representa un boton en la interfaz del juego
 */
public class Button {

    //Atributos del botón
    private float x;
    private float y;
    private float w;
    private float h;

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
    Image imagen; //Imagen
    Figure figura; //Figura del botón

    //Funcion callback asociada al boton
    private ButtonClickListener onClickFunction;

    /**
     * Constructora del botón que inicializa su posición, dimensiones, y si es redondeado o no
     */
    public Button(float x, float y, float w, float h, boolean isRound, float ar){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        this.isRound = isRound;
        this.arcRadius = ar;
    }
    public Button(float x, float y, float w, float h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }
    //Constructora que te crea un boton a partir de un Json
    public Button(JSONObject json)
    {
        try {
            this.x = json.getInt("x");
            this.y= json.getInt("y");
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
        try {
            this.id=json.getString("id");
            this.x = json.getInt("x");
            this.y= json.getInt("y");
            this.w= json.getInt("w");
            this.h=json.getInt("h");
            this.isRound=json.getBoolean("isRound");
            if(isRound)
                this.arcRadius=json.getInt("ar");

            this.color=json.getString("color");

            //Ahora cargamos los elementos que haya en el boton

            JSONObject textData = json.optJSONObject("text");
            if(textData != null)
                this.text = new Text(textData);

            JSONObject figData = json.optJSONObject("figure");
            if(figData != null){
                String form = figData.getString("form");
                switch (form){
                    case "triangle":
                        this.figura = new Triangle(figData);
                        break;
                    case "square":
                        this.figura = new Square(figData);
                        break;
                    case "hexagon":
                        this.figura = new Hexagon(figData);
                        break;
                }
            }

            JSONObject iconData = json.optJSONObject("icon");
            if(iconData != null){
                this.imagen = new Image(iconData,gr);
            }

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
    /**
     * Setters de atributos del botón
     */
    //setter de los parametros de posicion texto color imagen y figura
    public void setX(float x){this.x=x;}
    public void setY(float y){this.y=y;}
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
    public void setFigura(Figure fig){ this.figura = fig; }
    public void setImagen(Image img){this.imagen = img;}
    public void setEnabled(boolean enabled){this.isEnable = enabled;}
    public void setVisible(boolean visible){this.isVisible = visible;}

    // Setter para asignar el callback
    public void setOnClickListener(ButtonClickListener listener) {this.onClickFunction = listener;}

    //getter de los parametros de tamaño y posicion
    public float getWidth(){return this.w;}
    public float getHeight(){return this.h;}
    public float getX(){return this.x;}
    public float getY(){return this.y;}

    public Text getTextButton(){ return this.text; }

    public String getId(){return this.id;}

    public Image getImgButton(){ return this.imagen; }
    public Figure getFigButton(){ return this.figura; }
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
            if(this.imagen != null){
                this.imagen.RenderCentrado((int)this.x,(int)this.y);
            }

            //Renderizamos figura centrada
            if(this.figura != null){
                this.figura.RenderCentrado(gr,this.x,this.y);
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
