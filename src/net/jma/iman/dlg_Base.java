package net.jma.iman;

import net.jma.eventos.Evento_Datos;
import android.app.Dialog;
import android.content.Context;

public class dlg_Base extends Dialog {

	Evento_Datos evDialogo;
	boolean bPulsadoIncluir;

	public dlg_Base(Context context) {
		super(context);		
	}

	public dlg_Base(Context context, int theme) {
		super(context, theme);
	}

	public dlg_Base(Context context, boolean cancelable,
			OnCancelListener cancelListener) {
		super(context, cancelable, cancelListener);
	}
	
	public boolean pulsadoBoton(){
		return this.bPulsadoIncluir;
	}
	
	public Evento_Datos getTag(){
		return evDialogo;
	}
	
	public void setTag(Evento_Datos datos){
		this.evDialogo = datos;
	}
}
