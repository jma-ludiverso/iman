package net.jma.clases;

import net.jma.iman.R;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdaptadorListas extends CursorAdapter {
	
	Context context;
	private final LayoutInflater mInflater;
	Boolean bAlternativo=false;


	public AdaptadorListas(Context context, Cursor c) {
		super(context, c);
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}

	public AdaptadorListas(Context context, Cursor c, boolean autoRequery) {
		super(context, c, autoRequery);
		this.context = context;
		mInflater = LayoutInflater.from(context);
	}

	@Override
	public void bindView(View arg0, Context arg1, Cursor arg2) {
		
		if(bAlternativo){
			arg0.setBackgroundColor(R.color.lineaLista);
			bAlternativo=false;
		}else{
			bAlternativo=true;
		}

		ImageView imgIcono = (ImageView)arg0.findViewById(R.id.imgIcono);
		TextView lblNombreLista = (TextView)arg0.findViewById(R.id.lblNombreLista);
		TextView lblDatosLista = (TextView)arg0.findViewById(R.id.lblDatosLista);

		byte[] bb = arg2.getBlob(arg2.getColumnIndex(ConstantesBD.TB_LISTAS_IconoLista));
		Bitmap bitmap = BitmapFactory.decodeByteArray(bb, 0, bb.length);
		imgIcono.setImageBitmap(bitmap);
		lblNombreLista.setText(arg2.getString(1));
		
		String descripcion = arg2.getString(2);
		if(descripcion.length()>=40){
			descripcion = descripcion.substring(0, 40) + " ...";
		}
		lblDatosLista.setText(descripcion);
		arg0.setTag(arg2.getInt(0));

	}

	@Override
	public View newView(Context arg0, Cursor arg1, ViewGroup arg2) {
		final View vista = mInflater.inflate(R.layout.lstitemlista, arg2, false);		
		return vista;
	}

}
