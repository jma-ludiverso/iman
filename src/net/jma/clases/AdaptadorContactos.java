package net.jma.clases;

import net.jma.iman.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class AdaptadorContactos extends ArrayAdapter<InfoContacto> {

	Activity v_Activity;
	InfoContacto[] datos;

	public AdaptadorContactos(Context context, int textViewResourceId,
			InfoContacto[] objects) {
		super(context, textViewResourceId, objects);
		v_Activity = (Activity)context;
		datos = objects;
	}

	/* (non-Javadoc)
	 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
	 */
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = v_Activity.getLayoutInflater();
        View item = inflater.inflate(R.layout.compartirlista_item, null);
        
        ImageView imgContacto = (ImageView)item.findViewById(R.id.imgCompartirListaContacto);
        //imgContacto.setImageBitmap(datos[position].getPhoto());
        imgContacto.setImageBitmap(Bitmap.createScaledBitmap(datos[position].getPhoto(), 72, 72, false));
 
//        TextView lblId = (TextView)item.findViewById(R.id.lblIdContacto);
//        lblId.setText(datos[position].getIdContacto());
 
        TextView lblNombre = (TextView)item.findViewById(R.id.lblNombreContacto);
        lblNombre.setText(datos[position].getNombreContacto());

//        TextView lblTelefono = (TextView)item.findViewById(R.id.lblTelefonoContacto);
//        lblTelefono.setText(datos[position].getTelfContacto() + "-" + datos[position].getEmailContacto());
        
        item.setTag(datos[position].getIdContacto() + "|" + datos[position].getTelfContacto() + "|" + datos[position].getEmailContacto());
        
        return(item);
	}

	
}
