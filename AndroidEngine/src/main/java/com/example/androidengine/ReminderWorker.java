package com.example.androidengine;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

public class ReminderWorker extends Worker {
    public static final String CHANNEL_ID = "id_worker";
    public static final String CHANNEL_NAME = "channel_worker";
    public static final String CHANNEL_DESCRIPTION = "Canal_de_notificaciones_worker";

    Context context; //Referencia al contexto
    public ReminderWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
        this.context = context;
    }

    @RequiresPermission(Manifest.permission.POST_NOTIFICATIONS)
    @NonNull
    @Override
    public Result doWork() {

        // Obtener datos enviados desde AndroidEngine
        Data data = getInputData();
        String title =  data.getString("title"); //Titulo
        String text = data.getString("text"); //Texto
        int icon = data.getInt("icon",android.R.drawable.ic_dialog_info); //icono por defecto

        //creamos canal de notificaciones solo para el worker
        createNotificationChannel();

        //Creamos la notificacion con los parametros que le pasamos del Data
        NotificationCompat.Builder builder=
                new NotificationCompat.Builder(getApplicationContext(),CHANNEL_ID)
                        .setSmallIcon(icon) //icono
                        .setContentTitle(title)
                        .setContentText(text)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText(text))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT) //prioridad
                        .setAutoCancel(true); //Se desecha la notifiacion cuando el usuario la toca


        // Mostramos la notifiación
        NotificationManagerCompat manager = NotificationManagerCompat.from(this.context);

        //Comprobamos que la app tenga permisos de postear una notificación
        if (ActivityCompat.checkSelfPermission(this.context,
                android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            //si no los tiene, los solicitamos
            ActivityCompat.requestPermissions((Activity) this.context,new String[]{Manifest.permission.POST_NOTIFICATIONS},101);
        }

        // notificationId is a unique int for each notification that you must define.
        int NOTIFICATION_ID = (int) System.currentTimeMillis();  // ID único
        manager.notify(NOTIFICATION_ID, builder.build()); //Invocamos la notificación

        return Result.success();
    }

    /**
     * Metodo para crear un canal por el que enviar notificaciones desde el worker
     */
    private void createNotificationChannel(){
        // Verifica si es necesario crear un canal de notificaciones (a partir de Android 8.0)
        if (Build.VERSION. SDK_INT >= Build.VERSION_CODES. O) {
            int importance = NotificationManager. IMPORTANCE_DEFAULT; //Importancia
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID , CHANNEL_NAME, importance); //Creación del canal
            channel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this.context);
            notificationManager.createNotificationChannel(channel) ; //Crea el canal en el sistema
        }
    }
}
