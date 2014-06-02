package net.jma.ws;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class reqIman_ComparteLista implements KvmSerializable {

    reqIman_Usuario v_Propietario;
    arrayCadenas v_Destinatarios;
    int v_IdLista;
    String v_Lista;
	
	public Object getProperty(int arg0) {
		switch(arg0){
			case 0:
				return v_Propietario;
			case 1:
				return v_Destinatarios;	
			case 2:
				return v_IdLista;	
			case 3:
				return v_Lista;				
			default:
				return null;
		}
	}

	public int getPropertyCount() {
		return 4;
	}

	public void getPropertyInfo(int arg0, @SuppressWarnings("rawtypes") Hashtable arg1, PropertyInfo arg2) {
		switch(arg0){
			case 0:
				arg2.type = reqIman_Usuario.class;
				arg2.name = "Propietario";
				break;
			case 1:
				arg2.type = arrayCadenas.class;
				arg2.name = "Destinatarios";
				break;
			case 2:
				arg2.type = PropertyInfo.INTEGER_CLASS;
				arg2.name = "IdLista";
				break;			
			case 3:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "Lista";				
				break;
		}
	}

	public void setProperty(int arg0, Object arg1) {
		switch(arg0){
			case 0:
				this.v_Propietario = (reqIman_Usuario)arg1;
				break;
			case 1:
				this.v_Destinatarios = (arrayCadenas)arg1;
				break;
			case 2:
				this.v_IdLista = Integer.parseInt(arg1.toString());
				break;
			case 3:
				this.v_Lista = arg1.toString();
				break;
		}
	}

}
