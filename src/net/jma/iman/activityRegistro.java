package net.jma.iman;

import net.jma.clases.infoTelefono;
import net.jma.eventos.Evento_Acciones;
import net.jma.eventos.Evento_Datos;
import net.jma.eventos.Eventos;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class activityRegistro extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registro);
		
		final EditText txtTelefono = (EditText)findViewById(R.id.txtTelefono);
		final EditText txtEmail = (EditText)findViewById(R.id.txtEmail);
		final EditText txtPass = (EditText)findViewById(R.id.txtPassword);
		
		infoTelefono info = new infoTelefono(this);
		txtTelefono.setText(info.NumeroTelefono());
		txtEmail.setText(info.emailCuentaPrincipal());
		
		final Button btnRegistro = (Button)findViewById(R.id.btnRegistro);
		Evento_Datos evDatos = new Evento_Datos();
		EditText[] arrObligatorios = {txtEmail,txtPass,txtTelefono}; 
		evDatos.setInstancia(this);
		evDatos.setDestino(activityPrincipal.class);
		evDatos.setArrObligatorios(arrObligatorios);
		evDatos.setTipoEvento(Evento_Acciones.REGISTRO_USUARIO);
		btnRegistro.setTag(evDatos);
		btnRegistro.setOnClickListener(new Eventos());  		
		
	}
	
	
}
