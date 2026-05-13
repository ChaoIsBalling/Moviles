package com.example.gamelogic;

public enum Color {
    BLANCO("#FFFFFFFF"),
    NEGRO("#FF000000"),
    ROJO("#FFFF0000"),
    VERDE("#FF00FF00"),
    AZUL("#FF0000FF"),
    AMARILLO_CLARO("#FFFFFB64"),
    MARRON("#FF944D03");

    private final String hex;

    Color(String hex) {
        this.hex = hex;
    }

    public String getHex() {
        return hex;
    }
}
