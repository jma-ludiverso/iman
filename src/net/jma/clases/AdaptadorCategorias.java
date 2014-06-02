package net.jma.clases;

import net.jma.eventos.Evento_Acciones;
import net.jma.eventos.Evento_Datos;
import net.jma.eventos.Eventos;
import net.jma.iman.R;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class AdaptadorCategorias extends CursorAdapter {

	Context context;
	private final LayoutInflater mInflater;
	
	int idLista;
	en_VistaLista tipoVista;

	public void setDatosLista(int idLista, en_VistaLista pTipoVista) {
		this.idLista = idLista;
		this.tipoVista = pTipoVista;
	}

	public AdaptadorCategorias(Context context, Cursor c) {
		super(context, c);
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}

	public AdaptadorCategorias(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		if(this.idLista==-1){
			this.Enlace_Categorias(arg0, arg1, arg2);
		}else{
			this.Enlace_CategoriasLista(arg0, arg1, arg2);
		}
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View vista;
		if(this.idLista==-1){
			vista = mInflater.inflate(R.layout.nuevalistacategorias, parent, false);
		}else{
			vista = mInflater.inflate(R.layout.listitemcategoria, parent, false);
		}
		return vista;
	}
	
	private void Enlace_CategoriasLista(View arg0, Context arg1, Cursor arg2){
		ImageView imgIcono = (ImageView)arg0.findViewById(R.id.imgItemCategoriaNombre);
		TextView txtCategoria = (TextView)arg0.findViewById(R.id.lblItemCategoriaNombre);
		ImageButton imgbtnAddProducto = (ImageButton)arg0.findViewById(R.id.imgAddProducto);
		
		byte[] bb = arg2.getBlob(arg2.getColumnIndex(ConstantesBD.TB_CATEGORIAS_IconoCategoria));
		Bitmap bitmap = BitmapFactory.decodeByteArray(bb, 0, bb.length);
		imgIcono.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 36, 36, false));
		txtCategoria.setText(arg2.getString(arg2.getColumnIndex(ConstantesBD.TB_CATEGORIAS_Categoria)));
		
		int idCategoria = arg2.getInt(arg2.getColumnIndex("_id"));
		
		Evento_Datos evLista = new Evento_Datos();
		evLista.setInstancia((Activity)arg1);
		evLista.setTipoEvento(Evento_Acciones.CARGA_PRODUCTOS_CATEGORIA);
		int[] arrInfo = new int[3];
		arrInfo[0] = idLista;
		arrInfo[1] = idCategoria;
		if(tipoVista==en_VistaLista.VISTA_LISTA){
			arrInfo[2] = 1;
		}else{
			arrInfo[2] = 2;
		}
		evLista.setInfoEvento(arrInfo);
		arg0.setTag(evLista);
		arg0.setOnClickListener(new Eventos());
		arg0.setOnLongClickListener(new Eventos());

		Evento_Datos evAddProducto = new Evento_Datos();
		evAddProducto.setInstancia((Activity)arg1);
		evAddProducto.setTipoEvento(Evento_Acciones.ADD_PRODUCTO_CATEGORIA);
		int[] intInfoEvento = new int[2];
		intInfoEvento[0] = idLista;
		intInfoEvento[1] = idCategoria;
		evAddProducto.setInfoEvento(intInfoEvento);
		imgbtnAddProducto.setTag(evAddProducto);
		imgbtnAddProducto.setOnClickListener(new Eventos());
		
	}
	
	private void Enlace_Categorias(View arg0, Context arg1, Cursor arg2){
		TextView lblCategoria = (TextView)arg0.findViewById(R.id.lblNuevaListaItemCategoriaNombre);
		lblCategoria.setText(arg2.getString(arg2.getColumnIndex(ConstantesBD.TB_CATEGORIAS_Categoria)));
		lblCategoria.setTag(arg2.getInt(0));
	}

}
