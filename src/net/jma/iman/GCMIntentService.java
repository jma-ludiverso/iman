package net.jma.iman;

import net.jma.clases.Registro;
import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.android.gcm.GCMBaseIntentService;

public class GCMIntentService extends GCMBaseIntentService {

	public GCMIntentService() {
		super(Iman.get_GCMSenderId());
	}

	@Override
	protected void onError(Context arg0, String arg1) {
		Toast.makeText(arg0, arg1,Toast.LENGTH_SHORT).show();		
	}

	@Override
	protected void onMessage(Context arg0, Intent arg1) {
		String msg = arg1.getExtras().getString("msg");
	    //Obtenemos una referencia al servicio de notificaciones
	    String ns = Context.NOTIFICATION_SERVICE;
	    NotificationManager notManager = (NotificationManager) arg0.getSystemService(ns);
	 
	    //Configuramos la notificación
	    int icono = net.jma.iman.R.drawable.icon;
	    CharSequence textoEstado = "El imán de la nevera";
	    long hora = System.currentTimeMillis();
	 
	    Notification notif = new Notification(icono, textoEstado, hora);
	 
	    //Configuramos el Intent
	    Context contexto = arg0.getApplicationContext();
	    CharSequence titulo = "Hay algo nuevo en el imán de la nevera";
	    CharSequence descripcion = msg;
	 
	    Intent notIntent = new Intent(contexto, GCMIntentService.class);
	 
	    PendingIntent contIntent = PendingIntent.getActivity(contexto, 0, notIntent, 0);
	 
	    notif.setLatestEventInfo(contexto, titulo, descripcion, contIntent);
	 
	    //AutoCancel: cuando se pulsa la notificaión ésta desaparece
	    notif.flags |= Notification.FLAG_AUTO_CANCEL;
	 
	    //Enviar notificación
	    notManager.notify(1, notif);		
	}

	@Override
	protected void onRegistered(Context arg0, String arg1) {
		Application app = (Application)arg0;				
		Registro reg = new Registro(app.getPackageManager(), app.getPackageName(), app.getApplicationContext());
		reg.GCM_Registrar(arg1);
		reg.GCM_RegistroServidor();
	}

	@Override
	protected void onUnregistered(Context arg0, String arg1) {
		Registro reg = new Registro((Activity)arg0);
		reg.GCM_Registrar("");
	}

}
