package net.jma.ws;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class reqIman_ListadoUsuarios implements KvmSerializable {
	
    reqIman_Usuario v_Solicitante;
    arrayCadenas v_Buscados;


	public Object getProperty(int arg0) {
		switch(arg0){
			case 0:
				return v_Solicitante;
			case 1:
				return v_Buscados;	
			default:
				return null;
		}
	}

	public int getPropertyCount() {
		return 2;
	}

	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		switch(arg0){
			case 0:
				arg2.type = reqIman_Usuario.class;
				arg2.name = "Solicitante";
				break;
			case 1:
				arg2.type = arrayCadenas.class;
				arg2.name = "Buscados";
				break;
		}
	}

	public void setProperty(int arg0, Object arg1) {
		switch(arg0){
			case 0:
				this.v_Solicitante = (reqIman_Usuario)arg1;
				break;
			case 1:
				this.v_Buscados = (arrayCadenas)arg1;
				break;
		}
	}

}
