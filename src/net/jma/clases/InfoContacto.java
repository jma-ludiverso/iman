package net.jma.clases;

import android.graphics.Bitmap;

public class InfoContacto {

	String idContacto;
	String nombreContacto;
	String telfContacto;
	String emailContacto;
	Bitmap photo;
	
	/**
	 * @return the idContacto
	 */
	public String getIdContacto() {
		return idContacto;
	}
	/**
	 * @return the nombreContacto
	 */
	public String getNombreContacto() {
		return nombreContacto;
	}
	/**
	 * @return the emailContacto
	 */
	public String getEmailContacto() {
		return emailContacto;
	}
	/**
	 * @param idContacto the idContacto to set
	 */
	public void setIdContacto(String idContacto) {
		this.idContacto = idContacto;
	}
	/**
	 * @param nombreContacto the nombreContacto to set
	 */
	public void setNombreContacto(String nombreContacto) {
		this.nombreContacto = nombreContacto;
	}
	/**
	 * @param emailContacto the emailContacto to set
	 */
	public void setEmailContacto(String emailContacto) {
		this.emailContacto = emailContacto;
	}
	/**
	 * @return the telfContacto
	 */
	public String getTelfContacto() {
		return telfContacto;
	}
	/**
	 * @param telfContacto the telfContacto to set
	 */
	public void setTelfContacto(String telfContacto) {
		this.telfContacto = telfContacto;
	}
	/**
	 * @return the photo
	 */
	public Bitmap getPhoto() {
		return photo;
	}
	/**
	 * @param photo the photo to set
	 */
	public void setPhoto(Bitmap photo) {
		this.photo = photo;
	}
	
	
	
}
