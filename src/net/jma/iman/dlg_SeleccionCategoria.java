package net.jma.iman;

import net.jma.clases.AccionesListas;
import net.jma.clases.AdaptadorCategorias;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class dlg_SeleccionCategoria extends dlg_Base {

	int idListaCargada;
	
	public dlg_SeleccionCategoria(Context context, int idLista) {
		super(context);
		setContentView(R.layout.dlg_seleccioncategoria);
		
		this.idListaCargada = idLista;
		
		try{
			Button btnIncluir = (Button)findViewById(R.id.btndlgSeleccionCategoriaAceptar);		
			btnIncluir.setOnClickListener(new Button.OnClickListener() {				
				public void onClick(View v) {
					bPulsadoIncluir = true;
					cancel();					
				}
			});			
			
			AccionesListas accListas = new AccionesListas((Activity)context);
			Cursor cCategorias = accListas.DameListaNoCategorias(idLista);
			ListView lvCategorias = (ListView)findViewById(R.id.lvSeleccionCategorias);
			AdaptadorCategorias ad = new AdaptadorCategorias((Activity)context, cCategorias, false);
			ad.setDatosLista(-1, null);
			lvCategorias.setAdapter(ad);
		}catch (Exception ex){
			
		}
		
	}
	
	public int idLista(){
		return this.idListaCargada;
	}

}
