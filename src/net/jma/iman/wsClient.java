package net.jma.iman;

import java.util.Calendar;

import android.widget.EditText;

public class wsClient implements Runnable {

	EditText v_cajaTexto;
	activityLogin v_Login;
	
	EditText txtPass; 
	
	public wsClient(EditText cajaTexto)
	{
		this.v_cajaTexto = cajaTexto;
	}
	
	public wsClient(activityLogin act){
		this.v_Login = act;
	}
	
	public void run() {
		//this.v_cajaTexto.setText(c.getTime().toLocaleString());		

		txtPass = (EditText)this.v_Login.findViewById(R.id.etPass);

		v_Login.runOnUiThread(new Runnable() {
		      public void run() {	        	  
		          // update your UI component here.
		    	  Calendar c = Calendar.getInstance();
		    	  txtPass.setText(c.getTime().toLocaleString());
		        }
		      });
	}
		

}
