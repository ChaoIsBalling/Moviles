package com.example.engine;

/**
 * Interfaz que define las operaciones específicas de la plataforma móvil.
 * El motor del juego (Engine) utiliza esta interfaz para interactuar con
 * componentes nativos de Android sin acoplarse directamente a esa plataforma.
 *
 * Implementada normalmente por MainActivity en el módulo App.
 */
public interface Mobile {
    /**
     * Crea una notifiación
     */
    void makeNotification();

    /**
     * Muestra un anuncio recompensado.
     *
     * @param listener Callback que será notificado cuando el usuario
     *                 complete la acción y gane la recompensa o cierre el anuncio.
     */
    //public void showRewardedAd(RewardListener listener);

    /**
     * Programa una notificación utilizando WorkManager.
     * Permite ejecutar tareas incluso si el dispositivo móvil se cierra
     */
    void scheduleNotificationWithWorkManager();
}
