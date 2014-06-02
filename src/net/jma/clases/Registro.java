package net.jma.clases;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import net.jma.eventos.Evento_Acciones;
import net.jma.eventos.Evento_Datos;
import net.jma.eventos.Eventos;
import net.jma.iman.Iman;
import net.jma.iman.R;
import net.jma.iman.activityLogin;
import net.jma.iman.activityPrincipal;
import net.jma.iman.activityRegistro;
import net.jma.ws.Constantes_WS;
import net.jma.ws.WS_Iman;
import net.jma.ws.reqIman_Usuario;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;

public class Registro {
	
	Activity v_Act = null;
	Context v_Context = null;
	int version;

	public Registro(Activity pActivity) {
		super();
		this.v_Act = pActivity;

		PackageManager manager = pActivity.getPackageManager();
		try{
			PackageInfo info = manager.getPackageInfo(pActivity.getPackageName(), 0);
			version = info.versionCode;					
		}catch (Exception ex){
			version = 1;
		}				
	}
	
	public Registro(PackageManager manager, String packName, Context cnt){
		v_Context = cnt;
		try{
			PackageInfo info = manager.getPackageInfo(packName, 0);
			version = info.versionCode;
		}catch(Exception ex){
			version = 1;			
		}
	}
	
	public boolean LoginUsuario() throws Exception{
		boolean bRet=false;
		activityLogin v_Activity = (activityLogin)this.v_Act;
		try{
    		EditText txtUser = (EditText)v_Activity.findViewById(R.id.etEmail);
    		EditText txtPass = (EditText)v_Activity.findViewById(R.id.etPass);

			AccesoBD accBD = new AccesoBD(v_Activity,ConstantesBD.BD_NOMBRE,null,this.version);
			SQLiteDatabase db = accBD.getWritableDatabase();
			
			String[] columnas = new String[]{ConstantesBD.TB_USUARIOS_Logado};
			String where = ConstantesBD.TB_USUARIOS_Email + "=? and " + ConstantesBD.TB_USUARIOS_Pass + "=?";
			String[] whereArgs = new String[]{txtUser.getText().toString(),Seguridad.encrypt(txtPass.getText().toString())};
			
			Cursor cLogin = db.query(ConstantesBD.BD_TABLA_USUARIOS, columnas, where, whereArgs, null, null, null);
			if(cLogin.moveToFirst()){
				//si el login es correcto  marcar el usuario como logado
				ContentValues valLogin = new ContentValues();
				valLogin.put(ConstantesBD.TB_USUARIOS_Logado, true);
				db.update(ConstantesBD.BD_TABLA_USUARIOS, valLogin, where, whereArgs);
				bRet = true;
			}
			
			db.close();
			
		}catch (Exception ex){
			Toast.makeText(v_Activity, v_Activity.getResources().getText(net.jma.iman.R.string.msgErrorLogin),Toast.LENGTH_SHORT).show();			
			throw ex;			
		}
		return bRet;
	}
	
	public void CerrarSesion(){
		activityPrincipal v_Activity = (activityPrincipal)this.v_Act;		
		try{
			AccesoBD accBD = new AccesoBD(v_Activity,ConstantesBD.BD_NOMBRE,null,this.version);
			SQLiteDatabase db = accBD.getWritableDatabase();
			ContentValues valLogin = new ContentValues();
			valLogin.put(ConstantesBD.TB_USUARIOS_Logado, 0);
			String where = ConstantesBD.TB_USUARIOS_Activo + "=?";
			String whereArgs[] = new String[]{"1"};
			db.update(ConstantesBD.BD_TABLA_USUARIOS, valLogin, where, whereArgs);			
			db.close();
			v_Activity.finish();
		}catch (Exception ex){
			Toast.makeText(v_Activity, v_Activity.getResources().getText(net.jma.iman.R.string.msgErrorCierreSesion),Toast.LENGTH_SHORT).show();						
		}
	}
	
	public boolean compruebaRegistro(){
		boolean bRegistro = true;
		activityLogin v_Activity = (activityLogin)this.v_Act;
        try {
        	String content = "";
			FileInputStream fs = v_Activity.openFileInput("registro");
			byte[] input = new byte[fs.available()];
		    while (fs.read(input) != -1) {
			     content += new String(input);					    	
		    }
			if(!content.startsWith("Usuario registrado")){
				bRegistro = false;
			}			
			fs.close();
		} catch (FileNotFoundException e) {
			bRegistro = false;
		} catch (IOException e) {
			bRegistro = false;
		}
        return bRegistro;
	}
	
	public void GCM_CompruebaRegistro(){
		try{
			boolean bRegistrado = true;
			boolean bRegistradoServidor = true;
			AccesoBD accBD = new AccesoBD(this.v_Act,ConstantesBD.BD_NOMBRE,null,this.version);
			SQLiteDatabase db = accBD.getReadableDatabase();
			String[] arrCols = new String[]{"rowid AS _id", ConstantesBD.TB_REGISTRO_GCM_Reg_Id, ConstantesBD.TB_REGISTRO_GCM_RegistradoServidor};
			Cursor cRegistro = db.query(ConstantesBD.BD_TABLA_REGISTRO_GCM, arrCols, null, null, null, null, null);
			if(cRegistro.moveToFirst()){			
				if(cRegistro.isNull(1)){
					bRegistrado = false;
				}else{
					Iman.set_GCMSenderId(cRegistro.getString(cRegistro.getColumnIndex(ConstantesBD.TB_REGISTRO_GCM_Reg_Id)));
					bRegistradoServidor = cRegistro.getInt(cRegistro.getColumnIndex(ConstantesBD.TB_REGISTRO_GCM_RegistradoServidor)) > 0;
				}
			}else{
				bRegistrado = false;
			}
			cRegistro.close();
			if(!bRegistrado){
				this.GCM_Registrar(db);
			}else{
				if(!bRegistradoServidor){
					this.GCM_RegistroServidor();
				}
			}
			db.close();    			
		}catch (Exception ex){
			//vacio
			Toast.makeText(this.v_Act, ex.getMessage(),Toast.LENGTH_SHORT).show();			
		}
	}
	
	private void GCM_Registrar(SQLiteDatabase db){
		try{
	        GCMRegistrar.checkDevice(this.v_Act);
	        GCMRegistrar.checkManifest(this.v_Act);
			final String regId = GCMRegistrar.getRegistrationId(this.v_Act);
            if (regId.equals("")) {
                GCMRegistrar.register(this.v_Act, this.get_GCMSenderId(db)); 
            }else{
            	this.GCM_Registrar(regId);
            }
		}catch (Exception ex){
			//vacio
		}
	}	
	
	public void GCM_Registrar(String regId){
		try{
			AccesoBD accBD = null;
			if(this.v_Act!=null){
				accBD = new AccesoBD(this.v_Act,ConstantesBD.BD_NOMBRE,null,this.version);
			}else{
				accBD = new AccesoBD(this.v_Context,ConstantesBD.BD_NOMBRE,null,this.version);
			}
			SQLiteDatabase db = accBD.getWritableDatabase();

			ContentValues valGCM = new ContentValues();
			if(!regId.equals("")){
				valGCM.put(ConstantesBD.TB_REGISTRO_GCM_Reg_Id, regId);
			}else{
				valGCM.putNull(ConstantesBD.TB_REGISTRO_GCM_Reg_Id);
			}
			valGCM.put(ConstantesBD.TB_REGISTRO_GCM_RegistradoServidor, 0);
			String[] whereArgs = {"1"};				
			db.update(ConstantesBD.BD_TABLA_REGISTRO_GCM, valGCM, "rowid=?", whereArgs);

			db.close();
			
		}catch (Exception ex){
			//vacio
		}
	}
	
	public void GCM_RegistroServidor(){
		try{
			WS_Iman ws = new WS_Iman(this.v_Act, Constantes_WS.WS_Funcion_Registro);
			infoTelefono info = new infoTelefono(this.v_Act);
			reqIman_Usuario datosUsuario = info.dameInfoUsuario();
			Boolean bRegistrado = ws.RegistroUsuario(datosUsuario);
			if(bRegistrado){
				
				AccesoBD accBD = null;
				if(this.v_Act!=null){
					accBD = new AccesoBD(this.v_Act,ConstantesBD.BD_NOMBRE,null,this.version);
				}else{
					accBD = new AccesoBD(this.v_Context,ConstantesBD.BD_NOMBRE,null,this.version);
				}
				SQLiteDatabase db = accBD.getWritableDatabase();

				ContentValues valGCM = new ContentValues();
				valGCM.put(ConstantesBD.TB_REGISTRO_GCM_RegistradoServidor, 1);
				String[] whereArgs = {"1"};				
				db.update(ConstantesBD.BD_TABLA_REGISTRO_GCM, valGCM, "rowid=?", whereArgs);

				db.close();
				
			}			
		}catch (Exception ex){
			//vacio
		}
	}
	
	private String get_GCMSenderId(SQLiteDatabase db){
    	String senderID = "";
		try{
			String[] arrCols = new String[]{"rowid AS _id", ConstantesBD.TB_REGISTRO_GCM_SenderId};
			Cursor cSender = db.query(ConstantesBD.BD_TABLA_REGISTRO_GCM, arrCols, null, null, null, null, null);
			if(cSender.moveToFirst()){			
				senderID = cSender.getString(1);			
				Iman.set_GCMSenderId(senderID);
			}
        	cSender.close();    						
		}catch (Exception ex){
			senderID = "";
		}
		return senderID;
	}
	
	public void RegistroUsuario() throws Exception{
		activityRegistro v_Activity = (activityRegistro)this.v_Act;
		try{			
			EditText txtTlf = (EditText)v_Activity.findViewById(net.jma.iman.R.id.txtTelefono);
			EditText txtMail = (EditText)v_Activity.findViewById(net.jma.iman.R.id.txtEmail);
			EditText txtPass = (EditText)v_Activity.findViewById(net.jma.iman.R.id.txtPassword);					
			
			AccesoBD accBD = new AccesoBD(v_Activity,ConstantesBD.BD_NOMBRE,null,this.version);
			SQLiteDatabase db = accBD.getWritableDatabase();
			
			db.beginTransaction();
			try{			
				ContentValues valRegistro = new ContentValues();
				valRegistro.put(ConstantesBD.TB_USUARIOS_Telefono, txtTlf.getText().toString());
				valRegistro.put(ConstantesBD.TB_USUARIOS_Email, txtMail.getText().toString());
				valRegistro.put(ConstantesBD.TB_USUARIOS_Pass, Seguridad.encrypt(txtPass.getText().toString()));
				valRegistro.put(ConstantesBD.TB_USUARIOS_Activo, 1);
				valRegistro.put(ConstantesBD.TB_USUARIOS_Nombre, "");
				valRegistro.put(ConstantesBD.TB_USUARIOS_Logado, 1);
				db.insert(ConstantesBD.BD_TABLA_USUARIOS, null, valRegistro);
				
				ContentValues valGCM = new ContentValues();
				valGCM.put(ConstantesBD.TB_REGISTRO_GCM_Tlf_Numero, txtTlf.getText().toString());
				valGCM.put(ConstantesBD.TB_REGISTRO_GCM_Tlf_Email, txtMail.getText().toString());				
				String[] whereArgs = {"1"};				
				db.update(ConstantesBD.BD_TABLA_REGISTRO_GCM, valGCM, "rowid=?", whereArgs);
			
				String control = "Usuario registrado " + txtMail.getText().toString(); 
				FileOutputStream fos = v_Activity.openFileOutput("registro", Context.MODE_PRIVATE);
				fos.write(control.getBytes());
				fos.close();		
				
				this.GCM_Registrar(db);
				
				db.setTransactionSuccessful();				
				
			}catch (Exception ex){
				throw ex;
			}finally{
				db.endTransaction();
				db.close();				
			}
						
		}catch (Exception ex){
			Toast.makeText(v_Activity, v_Activity.getResources().getText(net.jma.iman.R.string.msgErrorRegistro),Toast.LENGTH_SHORT).show();			
			throw ex;
		}
	}
	
	public boolean UsuarioLogado(){
		boolean bCreaLogin=true;
		try {
			AccesoBD accBD = new AccesoBD(this.v_Act,ConstantesBD.BD_NOMBRE,null,version);
			SQLiteDatabase db = accBD.getReadableDatabase();    
			String[] columnas = new String[] {ConstantesBD.TB_USUARIOS_Logado};
			String[] args = new String[] {"1"};
	    	Cursor datos = db.query(ConstantesBD.BD_TABLA_USUARIOS, columnas, "Activo=?", args, null, null, null);
	    	if(datos.moveToFirst()){
	    		int logado = datos.getInt(0);
	    		if(logado==1){        			
					Evento_Datos evDatos = new Evento_Datos();
					evDatos.setInstancia(this.v_Act);   
					evDatos.setDestino(activityPrincipal.class);
					evDatos.setTipoEvento(Evento_Acciones.SIN_ACCION);
					Eventos ev = new Eventos();
					ev.EventoCodigo(evDatos);
					bCreaLogin=false;
	    		}
	    	}
	    	datos.close();
	    	db.close();			
		} catch (Exception e) {
		}
		return bCreaLogin;
	}

}
