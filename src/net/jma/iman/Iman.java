package net.jma.iman;

import net.jma.clases.AccesoBD;
import net.jma.clases.ConstantesBD;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Iman extends Application {

	private static String senderID;
	private static PackageManager manager;
	private static String nombrePaquete;
	private static Context contexto;
	
	public Iman(){
		senderID = "";
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		manager = this.getPackageManager();
		nombrePaquete = this.getPackageName();
		contexto = this.getApplicationContext();	
	}

	public static String get_GCMSenderId(){
		if(senderID.equals("")){
			int version;
			try{
				PackageInfo info = manager.getPackageInfo(nombrePaquete, 0);
				version = info.versionCode;					
			}catch (Exception ex){
				version = 1;
			}			
			try{
				AccesoBD accBD = new AccesoBD(contexto,ConstantesBD.BD_NOMBRE,null,version);
				SQLiteDatabase db = accBD.getReadableDatabase();
				String[] arrCols = new String[]{"rowid AS _id", ConstantesBD.TB_REGISTRO_GCM_Reg_Id};
				Cursor cRegistro = db.query(ConstantesBD.BD_TABLA_REGISTRO_GCM, arrCols, null, null, null, null, null);
				if(cRegistro.moveToFirst()){
					Iman.set_GCMSenderId(cRegistro.getString(cRegistro.getColumnIndex(ConstantesBD.TB_REGISTRO_GCM_Reg_Id)));					
				}
				cRegistro.close();
				db.close();
			}catch (Exception ex){
				senderID = "";
			}
		}
		return senderID;
	}
	
	public static void set_GCMSenderId(String value){
		senderID = value;
	}
	
}
