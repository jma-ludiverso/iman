package net.jma.ws;

import java.io.IOException;

import net.jma.clases.AccesoBD;
import net.jma.clases.ConstantesBD;
import net.jma.clases.Seguridad;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.Marshal;
import org.ksoap2.serialization.MarshalFloat;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.kxml2.kdom.Element;
import org.kxml2.kdom.Node;
import org.xmlpull.v1.XmlPullParserException;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class WS_Iman {

	SoapObject request;
	String url;
	String user;
	String pass;
	
	public WS_Iman(Activity pAct,String metodo) throws Exception{
		try{
			request = new SoapObject(Constantes_WS.WS_NAMESPACE, metodo);
	
			int version;
			PackageManager manager = pAct.getPackageManager();
			try{
				PackageInfo info = manager.getPackageInfo(pAct.getPackageName(), 0);
				version = info.versionCode;					
			}catch (Exception ex){
				version = 1;
			}	
			
			AccesoBD accBD = new AccesoBD(pAct,ConstantesBD.BD_NOMBRE,null,version);
			SQLiteDatabase db = accBD.getReadableDatabase();    
			String[] columnas = new String[] {"rowid AS _id", ConstantesBD.TB_REGISTRO_GCM_ImanServer, ConstantesBD.TB_REGISTRO_GCM_ImanUser, ConstantesBD.TB_REGISTRO_GCM_ImanPass};
	    	Cursor datos = db.query(ConstantesBD.BD_TABLA_REGISTRO_GCM, columnas, null, null, null, null, null);
	    	if(datos.moveToFirst()){
	    		url = datos.getString(datos.getColumnIndex(ConstantesBD.TB_REGISTRO_GCM_ImanServer));
	    		user = datos.getString(datos.getColumnIndex(ConstantesBD.TB_REGISTRO_GCM_ImanUser));
	    		pass = datos.getString(datos.getColumnIndex(ConstantesBD.TB_REGISTRO_GCM_ImanPass));
	    	}
	    	datos.close();
	    	db.close();
		}catch (Exception ex){
			throw ex;
		}
	}
	
	public void CompartirLista(reqIman_ComparteLista infoCompartir) throws Exception{
		try{
			PropertyInfo pi = new PropertyInfo();
			pi.setName("new_Request");
			pi.setType(reqIman_ComparteLista.class);

			SoapObject new_Request = new SoapObject(Constantes_WS.WS_NAMESPACE, "new_Request");
			reqIman_Usuario infoUsuario = (reqIman_Usuario)infoCompartir.getProperty(0);

			SoapSerializationEnvelope envelope = this.SobreSoap(pi);
			String parteFirma = infoCompartir.getProperty(2).toString();
			String emailFirma = infoUsuario.getProperty(2).toString();
			emailFirma = emailFirma.substring(0, emailFirma.length() / 2);
			parteFirma += emailFirma;
			envelope.headerOut = this.encabezadoSoap(parteFirma); 			
			
			infoUsuario.setProperty(0, Seguridad.Encriptar(infoUsuario.getProperty(0).toString()));
			infoUsuario.setProperty(1, Seguridad.Encriptar(infoUsuario.getProperty(1).toString()));
			infoUsuario.setProperty(2, Seguridad.Encriptar(infoUsuario.getProperty(2).toString()));
			
			envelope.addMapping(Constantes_WS.WS_NAMESPACE, "reqIman_Usuario", reqIman_Usuario.class);
			envelope.addMapping(Constantes_WS.WS_NAMESPACE, "reqIman_ComparteLista", reqIman_ComparteLista.class);
			
			PropertyInfo piPropietario = new PropertyInfo();
			piPropietario.setName("Propietario");
			piPropietario.setType(reqIman_Usuario.class);
			piPropietario.setValue(infoUsuario);			
			new_Request.addProperty(piPropietario);
			PropertyInfo piDestinatarios = new PropertyInfo();
			piDestinatarios.setName("Destinatarios");
			piDestinatarios.setType(arrayCadenas.class);
			piDestinatarios.setValue(infoCompartir.getProperty(1));
			new_Request.addProperty(piDestinatarios);			
			new_Request.addProperty("IdLista", infoCompartir.getProperty(2));
			new_Request.addProperty("Lista", Seguridad.Encriptar(infoCompartir.getProperty(3).toString()));
			request.addSoapObject(new_Request);	
			
			envelope.addMapping(Constantes_WS.WS_NAMESPACE, "Destinatarios", arrayCadenas.class);
			
			SoapPrimitive respuesta = this.Llamada(envelope);
			Boolean bCompartido = Boolean.parseBoolean(respuesta.toString());			
			if(!bCompartido){
				throw new Exception();
			}
			
		}catch (Exception ex){
			throw ex;
		}
	}
	
	private Element[] encabezadoSoap(String firma){
		Element[] ret = new Element[1];
		try {
			ret[0] = new Element().createElement(Constantes_WS.WS_NAMESPACE, Constantes_WS.WS_SoapHeader);
			Element user = new Element().createElement(Constantes_WS.WS_NAMESPACE, "Username");
			String palabra = Seguridad.damePalabra(this.user, this.pass);
			String contenidoFirma = this.pass + firma + palabra;
			user.addChild(Node.TEXT, Seguridad.Hash(contenidoFirma));
			Element pass = new Element().createElement(Constantes_WS.WS_NAMESPACE, "Password");
			pass.addChild(Node.TEXT, "");
			ret[0].addChild(Node.ELEMENT, user);
			ret[0].addChild(Node.ELEMENT, pass);	
			
			SoapObject valSoap = new SoapObject(Constantes_WS.WS_NAMESPACE, Constantes_WS.WS_SoapHeader);
			valSoap.addProperty("Username", Seguridad.Hash(contenidoFirma));
			valSoap.addProperty("Password", "");
			request.addSoapObject(valSoap);					
		} catch (Exception e) {
			
		}
		return ret;
	}
	
	public respIman_ListadoUsuarios ListadoUsuarios(reqIman_ListadoUsuarios buscados){
		respIman_ListadoUsuarios ret = null;
		try{
			PropertyInfo pi = new PropertyInfo();
			pi.setName("new_Request");
			pi.setType(reqIman_ListadoUsuarios.class);
			
			SoapObject new_Request = new SoapObject(Constantes_WS.WS_NAMESPACE, "new_Request");
			new_Request.addProperty("Solicitante", buscados.getProperty(0));
			new_Request.addProperty("Buscados", buscados.getProperty(1));
			request.addSoapObject(new_Request);			
			
			SoapSerializationEnvelope envelope = this.SobreSoap(pi);
			reqIman_Usuario datosUsuario = (reqIman_Usuario) buscados.getProperty(0);
			arrayCadenas arrBuscados = (arrayCadenas) buscados.getProperty(1);
			String emailFirma = datosUsuario.getProperty(2).toString();
			emailFirma = emailFirma.substring(0, emailFirma.length() / 2);
			emailFirma += emailFirma + arrBuscados.getPropertyCount();
			envelope.headerOut = this.encabezadoSoap(emailFirma); 			
			SoapPrimitive respuesta = this.Llamada(envelope);
			//ret = respuesta;			
			
		}catch(Exception ex){
			ret = null;
		}				
		return ret;
	}
	
	private SoapPrimitive Llamada(SoapSerializationEnvelope envelope) throws Exception{
		SoapPrimitive respuesta = null;
		HttpTransportSE t = new HttpTransportSE(this.url); 
		//t.debug = true;
		//t.requestDump;
		//t.responseDump;
		try {
			t.call(Constantes_WS.WS_NAMESPACE + "/" + request.getName(), envelope);
			respuesta = (SoapPrimitive)envelope.getResponse();	
		} catch (IOException e) {
			throw e;
		} catch (XmlPullParserException e) {
			throw e;
		} catch (Exception e){
			throw e;
		}		
		return respuesta;
	}
	
	public Boolean RegistroUsuario(reqIman_Usuario datosUsuario){
		Boolean bRegistrado = false;
		try{
			PropertyInfo pi = new PropertyInfo();
			pi.setName("new_Request");
			pi.setType(reqIman_Usuario.class);
			
			SoapObject new_Request = new SoapObject(Constantes_WS.WS_NAMESPACE, "new_Request");
			new_Request.addProperty("RegID", Seguridad.Encriptar(datosUsuario.getProperty(0).toString()));
			new_Request.addProperty("NumTelefono", Seguridad.Encriptar(datosUsuario.getProperty(1).toString()));
			new_Request.addProperty("Email", Seguridad.Encriptar(datosUsuario.getProperty(2).toString()));
			request.addSoapObject(new_Request);			
			
			SoapSerializationEnvelope envelope = this.SobreSoap(pi);
			String parteFirma = datosUsuario.getProperty(0).toString();
			parteFirma = parteFirma.substring(parteFirma.length() / 2);
			String emailFirma = datosUsuario.getProperty(2).toString();
			emailFirma = emailFirma.substring(0, emailFirma.length() / 2);
			parteFirma += emailFirma;
			envelope.headerOut = this.encabezadoSoap(parteFirma); 			
			SoapPrimitive respuesta = this.Llamada(envelope);
			bRegistrado = Boolean.parseBoolean(respuesta.toString());
		}catch (Exception ex){
			bRegistrado = false;
		}
		return bRegistrado;
	}
	
	private SoapSerializationEnvelope SobreSoap(PropertyInfo pi){
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11); 
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request); 
		envelope.addMapping(Constantes_WS.WS_NAMESPACE, pi.getType().getClass().getSimpleName(), pi.getType().getClass());
		Marshal floatMarshal = new MarshalFloat();
		floatMarshal.register(envelope);
		return envelope;
	}
	
}
