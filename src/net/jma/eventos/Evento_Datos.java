package net.jma.eventos;

import java.util.Hashtable;

import android.app.Activity;
import android.widget.EditText;

public class Evento_Datos {
	
	Activity instancia;
	Object destino;
	EditText[] arrObligatorios;
	int[] intInfoEvento;
	Hashtable<String, EditText> infoEvento;
	Evento_Acciones tipoEvento;
	
	/**
	 * @return the tipoEvento
	 */
	public Evento_Acciones getTipoEvento() {
		return tipoEvento;
	}
	/**
	 * @param tipoEvento the tipoEvento to set
	 */
	public void setTipoEvento(Evento_Acciones tipoEvento) {
		this.tipoEvento = tipoEvento;
	}
	/**
	 * @return the infoEvento
	 */
	public Hashtable<String, EditText> getInfoEvento() {
		return infoEvento;
	}
	public int[] getIntInfoEvento(){
		return this.intInfoEvento;
	}
	/**
	 * @param infoEvento the infoEvento to set
	 */
	public void setInfoEvento(Hashtable<String, EditText> infoEvento) {
		this.infoEvento = infoEvento;
	}
	public void setInfoEvento(int[] intInfoEvento){
		this.intInfoEvento = intInfoEvento;
	}
	/**
	 * @return the arrObligatorios
	 */
	public EditText[] getArrObligatorios() {
		return arrObligatorios;
	}
	/**
	 * @param arrObligatorios the arrObligatorios to set
	 */
	public void setArrObligatorios(EditText[] arrObligatorios) {
		this.arrObligatorios = arrObligatorios;
	}
	/**
	 * @return the destino
	 */
	public Object getDestino() {
		return destino;
	}
	/**
	 * @param destino the destino to set
	 */
	public void setDestino(Object destino) {
		this.destino = destino;
	}
	/**
	 * @return the instancia
	 */
	public Activity getInstancia() {
		return instancia;
	}
	/**
	 * @param instancia the instancia to set
	 */
	public void setInstancia(Activity instancia) {
		this.instancia = instancia;
	}
	
}
