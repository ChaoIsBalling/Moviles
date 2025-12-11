package com.example.engine;

/**
 * Interfaz que define las operaciones específicas de la plataforma móvil.
 * El motor del juego (Engine) utiliza esta interfaz para interactuar con
 * componentes nativos de Android sin acoplarse directamente a esa plataforma.
 */
public interface Mobile {
    /**
     * Muestra un anuncio recompensado.
     */
    public void showRewardedAd();

    /**
     * Controla la visibilidad del banner
     * @param cond si es true, el anuncio se ve, de lo contrario no
     */
    public void setVisibleAdBanner(boolean cond);
}
