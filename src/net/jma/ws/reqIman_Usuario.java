package net.jma.ws;

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

public class reqIman_Usuario implements KvmSerializable {

    String v_RegID;
    String v_NumTelefono;
    String v_Email;
	
	public Object getProperty(int arg0) {
		switch(arg0){
			case 0:
				return v_RegID;
			case 1:
				return v_NumTelefono;	
			case 2:
				return v_Email;					
		}
		return null;
	}

	public int getPropertyCount() {
		return 3;
	}

	public void getPropertyInfo(int arg0, Hashtable arg1, PropertyInfo arg2) {
		switch(arg0){
			case 0:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "RegID";
				break;
			case 1:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "NumTelefono";
				break;
			case 2:
				arg2.type = PropertyInfo.STRING_CLASS;
				arg2.name = "Email";
				break;				
		}
	}

	public void setProperty(int arg0, Object arg1) {
		switch(arg0){
			case 0:
				this.v_RegID = arg1.toString();
				break;
			case 1:
				this.v_NumTelefono = arg1.toString();
				break;
			case 2:
				this.v_Email = arg1.toString();
				break;
		}
	}

}
