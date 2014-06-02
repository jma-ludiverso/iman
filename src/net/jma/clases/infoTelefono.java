package net.jma.clases;

import java.io.InputStream;

import net.jma.iman.R;
import net.jma.ws.reqIman_Usuario;
import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class infoTelefono {
		
	Activity v_Activity;	
	int version;

	public infoTelefono(Activity pActivity) {
		super();
		this.v_Activity = pActivity;
		PackageManager manager = this.v_Activity.getPackageManager();
		try{
			PackageInfo info = manager.getPackageInfo(this.v_Activity.getPackageName(), 0);
			version = info.versionCode;					
		}catch (Exception ex){
			version = 1;
		}			
	}
	
	public String NumeroTelefono(){
		String ret="";
		TelephonyManager v_mTelephonyManager = (TelephonyManager) v_Activity.getSystemService(Context.TELEPHONY_SERVICE);
		ret = v_mTelephonyManager.getLine1Number();		
		return ret;
	}
	
	public InfoContacto[] dameContactos(){
		InfoContacto[] ret = null;
		try{
        	ContentResolver cr = this.v_Activity.getContentResolver();
        	Cursor cur = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
        	if (cur.getCount()>0){
        		int idElemento = 0;
        		ret = new InfoContacto[cur.getCount()];
        		while (cur.moveToNext()){
        			//recuperar los datos principales del contacto
        			String id = cur.getString(cur.getColumnIndex(ContactsContract.Contacts._ID));
        			String nombre = cur.getString(cur.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
        			String telefono = "";
        			if (Integer.parseInt(cur.getString(cur.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)))>0){
        				Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, 
        			 		    null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?", 
        			 		    new String[]{id}, null);
			 	        while (pCur.moveToNext()) {
			 	        	telefono = telefono + pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DATA)) + ";";
			 	        } 
			 	        pCur.close();
			 	        if(telefono.endsWith(";")){
			 	        	telefono = telefono.substring(0,telefono.length()-1);
			 	        }
        			}
        			String email = "";
        			Cursor emailCur = cr.query( ContactsContract.CommonDataKinds.Email.CONTENT_URI, 
        					null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?", 
        					new String[]{id}, null); 
    				while (emailCur.moveToNext()) { 
    				    email = emailCur.getString(emailCur.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
    				    break;
    			 	} 
    			 	emailCur.close();
    			 	//recuperar la foto del contacto
    			    Uri contactUri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(id));    			    
    			    InputStream isPhoto = ContactsContract.Contacts.openContactPhotoInputStream(cr, contactUri);
    			 	//recopilar la información del contacto a devolver
    			 	InfoContacto contacto = new InfoContacto();
    			 	contacto.setIdContacto(id);
    			 	contacto.setNombreContacto(nombre);
    			 	contacto.setTelfContacto(telefono);
    			 	contacto.setEmailContacto(email);
    			 	if(isPhoto!=null){
    			 		contacto.setPhoto(BitmapFactory.decodeStream(isPhoto));
    			 	}else{
    			 		contacto.setPhoto(BitmapFactory.decodeResource(v_Activity.getResources(), R.drawable.icon));    			 		
    			 	}
    			 	//---
    			 	ret[idElemento] = contacto;
    			 	idElemento +=1;
        		}
        	}     		
        	cur.close();
		}catch (Exception ex){
			Toast.makeText(v_Activity, v_Activity.getResources().getText(net.jma.iman.R.string.msgErrorDameContactos),Toast.LENGTH_SHORT).show();						
		}
		return ret;
	}

	public InfoContacto[] dameContactosIMAN(){
		InfoContacto[] ret = null;
		try{
			AccesoBD accBD = new AccesoBD(this.v_Activity,ConstantesBD.BD_NOMBRE,null,version);
			SQLiteDatabase db = accBD.getReadableDatabase();
			String[] arrCols = new String[]{"rowid AS _id", ConstantesBD.TB_CONTACTOS_IMAN_idContacto, ConstantesBD.TB_CONTACTOS_IMAN_nombreContacto, ConstantesBD.TB_CONTACTOS_IMAN_telfContacto, ConstantesBD.TB_CONTACTOS_IMAN_emailContacto, ConstantesBD.TB_CONTACTOS_IMAN_photo};
			Cursor cContactos = db.query(ConstantesBD.BD_TABLA_CONTACTOS_IMAN, arrCols, null, null, null, null, null);
			if(cContactos.moveToFirst()){
        		int idElemento = 0;
        		ret = new InfoContacto[cContactos.getCount()];
				do{
    			 	InfoContacto contacto = new InfoContacto();
    			 	contacto.setIdContacto(cContactos.getString(cContactos.getColumnIndex(ConstantesBD.TB_CONTACTOS_IMAN_idContacto)));
    			 	contacto.setNombreContacto(cContactos.getString(cContactos.getColumnIndex(ConstantesBD.TB_CONTACTOS_IMAN_nombreContacto)));
    			 	contacto.setTelfContacto(cContactos.getString(cContactos.getColumnIndex(ConstantesBD.TB_CONTACTOS_IMAN_telfContacto)));
    			 	contacto.setEmailContacto(cContactos.getString(cContactos.getColumnIndex(ConstantesBD.TB_CONTACTOS_IMAN_emailContacto)));
    			 	byte[] photo = cContactos.getBlob(cContactos.getColumnIndex(ConstantesBD.TB_CONTACTOS_IMAN_photo));
    			 	if(photo!=null){
    			 		contacto.setPhoto(BitmapFactory.decodeByteArray(photo, 0, photo.length));
    			 	}else{
    			 		contacto.setPhoto(BitmapFactory.decodeResource(v_Activity.getResources(), R.drawable.icon));    			 		
    			 	}
    			 	ret[idElemento] = contacto;
    			 	idElemento +=1;										
				}while(cContactos.moveToNext());				
			}			
			cContactos.close();
		}catch (Exception ex){
			Toast.makeText(v_Activity, v_Activity.getResources().getText(net.jma.iman.R.string.msgErrorDameContactos),Toast.LENGTH_SHORT).show();						
		}
		return ret;
	}
	
	public reqIman_Usuario dameInfoUsuario() throws Exception{
		reqIman_Usuario ret = new reqIman_Usuario();
		try{
			AccesoBD accBD = new AccesoBD(this.v_Activity,ConstantesBD.BD_NOMBRE,null,version);
			SQLiteDatabase db = accBD.getReadableDatabase();
			String[] arrCols = new String[]{"rowid AS _id", ConstantesBD.TB_REGISTRO_GCM_Reg_Id, ConstantesBD.TB_REGISTRO_GCM_Tlf_Numero, ConstantesBD.TB_REGISTRO_GCM_Tlf_Email};
			Cursor cUser = db.query(ConstantesBD.BD_TABLA_REGISTRO_GCM, arrCols, null, null, null, null, null);
			if(cUser.moveToFirst()){
				ret.setProperty(0, cUser.getString(cUser.getColumnIndex(ConstantesBD.TB_REGISTRO_GCM_Reg_Id)));
				ret.setProperty(1, cUser.getString(cUser.getColumnIndex(ConstantesBD.TB_REGISTRO_GCM_Tlf_Numero)));
				ret.setProperty(2, cUser.getString(cUser.getColumnIndex(ConstantesBD.TB_REGISTRO_GCM_Tlf_Email)));				
			}		
			cUser.close();
			db.close();
		}catch (Exception ex){
			throw ex;
		}
		return ret;
	}
	
	public String emailCuentaPrincipal(){
		String ret="";
		try{
			Account[] cuentas = AccountManager.get(v_Activity).getAccounts();
			for (Account cuenta : cuentas){
				ret = cuenta.name;
				if(ret!=""){
					break;
				}
			}        	
		}catch (Exception ex){  	
		}		
		return ret;
	}
	
}
