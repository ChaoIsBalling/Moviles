package com.example.engine;

import java.awt.Font;

/**
 * Interfaz de gráficos de la que heredarán los gestores de gráficos tanto de Android como Desktop
 */
public interface Graphics {

    /**
     * Metodo que limpia la pantalla
     */
    public void clear();

    /**
     * Devuelve la anchura de la pantalla
     * @return anchura pantalla
     */
    public int getWidth();

    /**
     * Pinta el background del color que le pases como parametro
     * @param color color para el fondo
     */
    public void pintarFondo(int color);

    /**
     * Metodo que pinta el texto en una posición (x,y)
     * @param texto String que contiene el texto a escribir
     * @param x Posición x
     * @param y Posición y
     */
    public void pintarTexto(String texto, float x, float y);

    /**
     * Metodo que pinta una imagen en una posición (x,y)
     * @param img Interfaz para imagenes del motor
     * @param x Posición x
     * @param y Posición y
     */
    public void pintarImagen(IImage img, int x, int y);

    /**
     * Metodo que pinta el texto centrado (el calculo es diferente en cada motor) en una posición (x,y)
     * @param texto String que contiene el texto a escribir
     * @param x Posición x
     * @param y Posición y
     */
    public void pintarTextoCentrado(String texto, float x, float y);

    /**
     * Metodo que setea el color en el que va a pintar el motor
     * @param color color que queramos poner al motor
     */
    public void setColor(int color);

    /**
     * Metodos que crean un Font y devuelve su interfaz
     * @param f Nombre del font a crear
     * size Tamaño del Font
     * bold Determina si el Font va a estar en negrita
     * italic Determina si el font va a ser itálico
     */
    public IFont newFont(String f);
    public IFont newFont(String f, float size);
    public IFont newFont(String f, float size, boolean bold);
    public IFont newFont(String f, float size, boolean bold, boolean italic);

    /**
     * Metodos que crean una nueva imagen
     * path Nombre de la imagem
     * widht height anchura y altura de la imagen
     * @return Interfaz de la imagen
     */
    public IImage newImage(String path);
    public IImage newImage(String path, int width,int height);


    /**
     * Setea el font que el motor vaya a renderizar
     * @param font Interfaz de Font
     */
    public void setFont(IFont font);

    /**
     * Aplica una escala al sistema de coordenadas x,y
     * @param x Coordenada x
     * @param y Coordenada y
     */
    public void escalar(float x, float y);

    /**
     * Aplica una traslación al sistema de coordenadas x,y
     * @param x Coordenada x
     * @param y Coordenada y
     */
    public void trasladar(float x,float y);

    /**
     * Define un tamaño lógico del área del área de dibujo
     * @param w Ancho
     * @param h Alto
     */
    public void setLogicSize(float w, float h);

    /**
     * Pinta un circulo sin relleno
     * @param x Posicion x
     * @param y Posicion y
     * @param r Radio del circulo
     */
    public void pintarCirculo(float x, float y, float r);

    /**
     * Pinta un cuadrado sin relleno
     * @param x Posicion x
     * @param y Posicion y
     * @param w Ancho del cuadrado
     * @param h Alto del cuadrado
     */
    public void pintarCuadrado(float x, float y, float w, float h);

    /**
     * Pinta un poligono regular
     * @param cx Posicion x del centro
     * @param cy Posicion y del centro
     * @param r Radio del polígono
     * @param nv Número de vértices del poligono
     */
    public void pintarPoligono(float cx, float cy, float r, int nv);

    /**
     * Dibuja una línea
     * @param x1 Posicion x del inicio de linea
     * @param y1 Posicion y del inicio de linea
     * @param x2 Posicion x del final de linea
     * @param y2 Posicion y del final o de linea
     * @param width
     */
    public void pintarLinea(float x1, float y1, float x2, float y2, float width);

    /**
     * Dibuja un círculo con relleno
     * @param x Posicion x
     * @param y Posicion y
     * @param r Radio del circulo
     */
    public void rellenarCirculo(float x, float y, float r);

    /**
     * Pinta un cuadrado con relleno
     * @param x Posicion x
     * @param y Posicion y
     * @param w Ancho del cuadrado
     * @param h Alto del cuadrado
     */
    public void rellenarCuadrado(float x, float y, float w, float h);

    /**
     * Pinta un cuadrado con relleno y puntas redondeadas
     * @param x Posicion x
     * @param y Posicion y
     * @param w Ancho del cuadrado
     * @param h Alto del cuadrado
     * @param ar Radio del borde
     */
    public void rellenarCuadradoRedondeado(float x, float y, float w, float h, float ar);

    /**
     * Dibuja un poligono regular con relleno
     * @param cx Posicion x del centro
     * @param cy Posicion y del centro
     * @param r Radio del polígono
     * @param nv Número de vértices del poligono
     */
    public void rellenarPoligono(float cx, float cy, float r, int nv);

    /**
     * Dibuja un hexagono regular con relleno
     * @param cx Posicion x del centro
     * @param cy Posicion y del centro
     * @param r Radio del hexágono
     */
    public void rellenarHexagono(float cx, float cy, float r);

    /**
     * Pasa la coordenada x real a lógica
     * @param x Coordenada x
     * @return coordenada x lógica
     */
    public float real2LogicX(float x);

    /**
     * Pasa la coordenada y real a lógica
     * @param y Coordenada y
     * @return coordenada y lógica
     */
    public float real2LogicY(float y);
}
