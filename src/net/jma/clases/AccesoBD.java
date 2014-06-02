package net.jma.clases;

import net.jma.iman.R;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class AccesoBD extends SQLiteOpenHelper {	

	Context v_Context;
	
	public AccesoBD(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		v_Context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase arg0) {
		String sql = "CREATE TABLE USUARIOS (Telefono TEXT, Email TEXT, Pass TEXT, Activo INTEGER, Nombre TEXT, Logado INTEGER)";
		arg0.execSQL(sql);
		sql = "CREATE TABLE LISTAS (idLista INTEGER, NombreLista TEXT, Descripcion TEXT, IconoLista BLOB)";
		arg0.execSQL(sql);
		sql = "CREATE UNIQUE INDEX index_idLista ON LISTAS (idLista ASC)";
		arg0.execSQL(sql);
		sql = "CREATE TABLE CATEGORIAS (idCategoria INTEGER PRIMARY KEY  NOT NULL  UNIQUE , Categoria TEXT NOT NULL , IconoCategoria BLOB)";
		arg0.execSQL(sql);
		sql = "CREATE TABLE LISTAS_CATEGORIAS (idLista INTEGER NOT NULL , idCategoria INTEGER NOT NULL , PRIMARY KEY (idLista, idCategoria))";
		arg0.execSQL(sql);
		sql = "CREATE TABLE LISTAS_PRODUCTOS (idLista INTEGER NOT NULL , idCategoria INTEGER NOT NULL , idProducto INTEGER NOT NULL , Producto TEXT NOT NULL , Cantidad INTEGER, Actualizado DATETIME, TipoCantidad INTEGER, seleccionadoLista BOOL, seleccionadoTienda BOOL, PRIMARY KEY (idLista, idCategoria, idProducto))";
		arg0.execSQL(sql);
		sql = "CREATE TABLE REGISTRO_GCM (ServidorGCM VARCHAR NOT NULL , API_Key VARCHAR NOT NULL , Reg_Id VARCHAR, Tlf_Email VARCHAR, Tlf_Numero VARCHAR,ImanServer VARCHAR NOT NULL, ImanUser VARCHAR, ImanPass VARCHAR, SenderId VARCHAR, RegistradoServidor BOOL)";
		arg0.execSQL(sql);
		sql = "CREATE TABLE CONTACTOS_IMAN (idContacto VARCHAR PRIMARY KEY  NOT NULL , nombreContacto VARCHAR, telfContacto VARCHAR, emailContacto VARCHAR, photo BLOB)";
		arg0.execSQL(sql);
		//TODO: faltarían los inserts por defecto para los valores pre-configurados
		sql = (String) v_Context.getResources().getText(R.string.GCM_INSERT_V0);
		arg0.execSQL(sql);		
		
		sql = (String) v_Context.getResources().getText(R.string.CATEGORIAS_INSERT_V0_1);
		arg0.execSQL(sql);
		sql = (String) v_Context.getResources().getText(R.string.CATEGORIAS_INSERT_V0_2);
		arg0.execSQL(sql);
		sql = (String) v_Context.getResources().getText(R.string.CATEGORIAS_INSERT_V0_3);
		arg0.execSQL(sql);
		sql = (String) v_Context.getResources().getText(R.string.CATEGORIAS_INSERT_V0_4);
		arg0.execSQL(sql);
		sql = (String) v_Context.getResources().getText(R.string.CATEGORIAS_INSERT_V0_5);
		arg0.execSQL(sql);
		sql = (String) v_Context.getResources().getText(R.string.CATEGORIAS_INSERT_V0_6);
		arg0.execSQL(sql);
		sql = (String) v_Context.getResources().getText(R.string.CATEGORIAS_INSERT_V0_7);
		arg0.execSQL(sql);
		sql = (String) v_Context.getResources().getText(R.string.CATEGORIAS_INSERT_V0_8);
		arg0.execSQL(sql);
		sql = (String) v_Context.getResources().getText(R.string.CATEGORIAS_INSERT_V0_9);
		arg0.execSQL(sql);
		sql = (String) v_Context.getResources().getText(R.string.CATEGORIAS_INSERT_V0_10);
		arg0.execSQL(sql);
		sql = (String) v_Context.getResources().getText(R.string.CATEGORIAS_INSERT_V0_11);
		arg0.execSQL(sql);
		sql = (String) v_Context.getResources().getText(R.string.CATEGORIAS_INSERT_V0_12);
		arg0.execSQL(sql);
		sql = (String) v_Context.getResources().getText(R.string.CATEGORIAS_INSERT_V0_13);
		arg0.execSQL(sql);
		sql = (String) v_Context.getResources().getText(R.string.CATEGORIAS_INSERT_V0_14);
		arg0.execSQL(sql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//		String sql;
//		if(oldVersion<2){
//			sql = "CREATE TABLE LISTAS (idLista INTEGER, NombreLista TEXT, Descripcion TEXT, IconoLista BLOB)";
//			db.execSQL(sql);			
//		}
	}
	
	public Boolean existeEnBD(SQLiteDatabase db, String tabla, String campo, String whereCondicion, String[] whereValores){
		Boolean ret = false;
		String[] columna = new String[]{campo};
		Cursor cID=null;
		if (whereCondicion==""){
			cID = db.query(tabla, columna, null, null, null, null, null);
		}else{
			cID = db.query(tabla, columna, whereCondicion, whereValores, null, null, null);			
		}
		if(cID.moveToFirst()){		
			ret = true;
		}			
		cID.close();
		return ret;
	}
	
	public Integer obtenMaxId(SQLiteDatabase db, String tabla, String campo, String whereCondicion, String[] whereValores){
		Integer ret=0;
		String[] columna = new String[]{"MAX(" + campo + ") AS ID"};
		Cursor cID=null;
		if (whereCondicion==""){
			cID = db.query(tabla, columna, null, null, null, null, null);
		}else{
			cID = db.query(tabla, columna, whereCondicion, whereValores, null, null, null);			
		}
		if(cID.moveToFirst()){			
			if(cID.isNull(0)){
				ret = 1;							
			}else{
				ret = cID.getInt(0) + 1;							
			}
		}
		cID.close();
		return ret;
	}

}
