package net.jma.iman;

import java.util.Hashtable;

import net.jma.eventos.Evento_Acciones;
import net.jma.eventos.Evento_Datos;
import net.jma.eventos.Eventos;
import android.app.Activity;
import android.content.Context;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;

public class controlUnidades extends LinearLayout {

	Boolean bVistaUnidades;
	
	RelativeLayout rlUnidades;
	RelativeLayout rlKilos;
	EditText txtUnidades;
	EditText txtControlEvento = new EditText(getContext());
	Button btnUp;
	Button btnDown;
	Spinner drpKilos;
	
	public controlUnidades(Context context) {
		super(context);
		this.InicializaControl();
	}

	public controlUnidades(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.InicializaControl();
	}
	
	private void InicializaControl(){
		txtControlEvento.setInputType(InputType.TYPE_NULL);
	    LayoutInflater li = (LayoutInflater)getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    li.inflate(R.layout.control_unidades, this, true);
	}
	
	private void InicializaControles(){
		if(bVistaUnidades){
			rlUnidades = (RelativeLayout)findViewById(R.id.rlUnidades);
			rlUnidades.setVisibility(RelativeLayout.VISIBLE);
			txtUnidades = (EditText)findViewById(R.id.txtUnidades);
			txtUnidades.setInputType(InputType.TYPE_NULL);
			Button btnUp = (Button)findViewById(R.id.btnUp);
			Button btnDown = (Button)findViewById(R.id.btnDown);
			
			Evento_Datos evUp = new Evento_Datos();
			evUp.setInstancia((Activity) getContext());
			evUp.setTipoEvento(Evento_Acciones.SUBE_UNIDADES);
			Hashtable<String, EditText> infoEvento = new Hashtable<String, EditText>();
			infoEvento.put("unidades",txtUnidades);
			evUp.setInfoEvento(infoEvento);				
			btnUp.setTag(evUp);
			btnUp.setOnClickListener(new Eventos());
			
			Evento_Datos evDown = new Evento_Datos();
			evDown.setInstancia((Activity) getContext());
			evDown.setTipoEvento(Evento_Acciones.BAJA_UNIDADES);
			Hashtable<String, EditText> infoEventoDown = new Hashtable<String, EditText>();
			infoEventoDown.put("unidades",txtUnidades);
			evDown.setInfoEvento(infoEventoDown);							
			btnDown.setTag(evDown);
			btnDown.setOnClickListener(new Eventos());
						
		}else{
			rlKilos = (RelativeLayout)findViewById(R.id.rlKilos);
			rlKilos.setVisibility(RelativeLayout.VISIBLE);
			drpKilos = (Spinner)findViewById(R.id.drpKilos);
			String[] opcKilos = new String[]{"250 gr","500 gr","750 gr","1 kg","1,5 kg","2 kg", "> 2 kg"};			 
			ArrayAdapter<String> adKilos = new ArrayAdapter<String>(this.getContext(), android.R.layout.simple_spinner_item, opcKilos);
			adKilos.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
			drpKilos.setAdapter(adKilos);
			
			Evento_Datos ev = new Evento_Datos();
			ev.setInstancia((Activity) getContext());
			ev.setTipoEvento(Evento_Acciones.SELECCION_KILOS);
			Hashtable<String, EditText> infoEvento = new Hashtable<String, EditText>();
			txtControlEvento.setText("CARGANDO");
			infoEvento.put("omitir",txtControlEvento);
			ev.setInfoEvento(infoEvento);			
			drpKilos.setTag(ev);
			drpKilos.setOnItemSelectedListener(new Eventos());
			
			drpKilos.setOnTouchListener(new OnTouchListener(){
		        public boolean onTouch(View v, MotionEvent event) {
		            txtControlEvento.setText("CARGADO");		        	
		            return false;
		        }
		    });
			
		}
	}	
	
	public void ConfiguraVista(Boolean bUnidades){
		bVistaUnidades = bUnidades;		
		InicializaControles();
	}
	
	public String getUnidades(){
		if(bVistaUnidades){
			return txtUnidades.getText().toString();
		}else{
			return drpKilos.getSelectedItem().toString();
		}
	}
	
	public void setUnidades(float cantidad){
		if(bVistaUnidades){
			String strCantidad = String.valueOf(cantidad);
			strCantidad = strCantidad.replace(".0", "");						
			txtUnidades.setText(strCantidad);				
		}else{
			@SuppressWarnings("unchecked")
			ArrayAdapter<String> myAdap = (ArrayAdapter<String>)drpKilos.getAdapter(); 
			String strCantidad = String.valueOf(cantidad);
			strCantidad = strCantidad.replace("0.", "");			
			strCantidad = strCantidad.replace(".", ",");
			if(cantidad<1){
				if(strCantidad.length()==1){
					strCantidad = strCantidad + "0";
				}
				strCantidad = strCantidad + "0 gr";				
			}else{
				if(cantidad>2){
					strCantidad = "> 2";
				}else{
					if(strCantidad.contains(",0")){
						strCantidad = strCantidad.replace(",0", "");
					}
				}
				strCantidad = strCantidad + " kg";
			}
			int posicion = myAdap.getPosition(strCantidad);
			drpKilos.setSelection(posicion);
		}
		
	}

}
