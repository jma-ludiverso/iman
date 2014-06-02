package net.jma.ws;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class respIman_ListadoUsuarios implements KvmSerializable {

	arrayCadenas v_Encontrados;
	
	public Object getProperty(int arg0) {
		switch(arg0){
		case 0:
			return v_Encontrados;
		default:
			return null;
	}
	}

	public int getPropertyCount() {
		return 1;
	}

	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		switch(arg0){
			case 0:
				arg2.type = arrayCadenas.class;
				arg2.name = "Encontrados";
				break;
		}
	}

	public void setProperty(int arg0, Object arg1) {
		switch(arg0){
			case 0:
				this.v_Encontrados = (arrayCadenas)arg1;
				break;
		}
	}

}
