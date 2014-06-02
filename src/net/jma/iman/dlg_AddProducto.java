package net.jma.iman;

import net.jma.clases.AccionesListas;
import net.jma.clases.ConstantesBD;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class dlg_AddProducto extends dlg_Base {
	
	int idLista;
	int idCategoria;

	public dlg_AddProducto(Context context, int idLista, int idCategoria) {
		super(context);
		setContentView(R.layout.dlg_addproducto);
		
		this.idLista = idLista;
		this.idCategoria = idCategoria;
		
		Button btnIncluir = (Button)findViewById(R.id.btndlgAddProducto);		
		btnIncluir.setOnClickListener(new Button.OnClickListener() {				
			public void onClick(View v) {
				bPulsadoIncluir = true;
				cancel();					
			}
		});			

		//Cargar el nombre de la categoría afectada
		TextView txtCategoria = (TextView)findViewById(R.id.lblAddProductoCategoria2);
		AccionesListas accListas = new AccionesListas((Activity)context);
		Cursor cCategoria = accListas.DameCategorias(idCategoria);
		cCategoria.moveToFirst();
		txtCategoria.setText(cCategoria.getString(cCategoria.getColumnIndex(ConstantesBD.TB_CATEGORIAS_Categoria)));
		cCategoria.close();
		
	}

	public int idCategoria(){
		return this.idCategoria;
	}
	
	public int idLista(){
		return this.idLista;
	}
}
