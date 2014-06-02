package net.jma.iman;

import java.util.concurrent.ScheduledThreadPoolExecutor;

import net.jma.clases.Registro;
import net.jma.clases.infoTelefono;
import net.jma.eventos.Evento_Acciones;
import net.jma.eventos.Evento_Datos;
import net.jma.eventos.Eventos;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class activityLogin extends Activity {
	
	ScheduledThreadPoolExecutor periodico;

	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        
        boolean bRegistro = true;
        Registro regUsuario = new Registro(this);
        //Comprobar si el usuario está registrado
        bRegistro = regUsuario.compruebaRegistro();
        if(!bRegistro){
			Evento_Datos evDatos = new Evento_Datos();
			evDatos.setInstancia(this);   
			evDatos.setDestino(activityRegistro.class);
			evDatos.setTipoEvento(Evento_Acciones.SIN_ACCION);
			Eventos ev = new Eventos();
			ev.EventoCodigo(evDatos);
        }else{
        	//comprobar si está logado
        	Boolean bCreaLogin=regUsuario.UsuarioLogado();
        	regUsuario.GCM_CompruebaRegistro();
        	if(bCreaLogin){
        		//se mostrarán los controles para que el usuario se logue
        		EditText txtUser = (EditText)findViewById(R.id.etEmail);
        		infoTelefono info = new infoTelefono(this);
        		txtUser.setText(info.emailCuentaPrincipal());
        		EditText txtPass = (EditText)findViewById(R.id.etPass);
        		Button btnLogin = (Button)findViewById(R.id.btnEntrar);
        		Evento_Datos evLogin = new Evento_Datos();
        		evLogin.setInstancia(this);
        		evLogin.setDestino(activityPrincipal.class);
        		evLogin.setTipoEvento(Evento_Acciones.LOGIN);
        		evLogin.setArrObligatorios(new EditText[]{txtUser,txtPass});
        		btnLogin.setTag(evLogin);
        		btnLogin.setOnClickListener(new Eventos());
        	}
        }
        
        

        
//        //temporizador
//        //--------------------------------
//
//        try{
//        	//runOnUiThread(new wsClient(this));
//        	periodico = new ScheduledThreadPoolExecutor(3);        	
//        	//periodico.scheduleWithFixedDelay(new wsClient(txtPass), 10, 10, TimeUnit.SECONDS);
//        	periodico.scheduleWithFixedDelay(new wsClient(this), 10, 10, TimeUnit.SECONDS);
//        	//periodico2 = Executors.newScheduledThreadPool(5);
//        	//periodico2.scheduleWithFixedDelay(new wsClient(this), 10, 10, TimeUnit.SECONDS);      
//        }catch (Exception ex){
//        	txtPass.setText(ex.getMessage());
//        }
//        
//        final Button btnHola = (Button)findViewById(R.id.button1);
//        Evento_Datos evDatos = new Evento_Datos();
//        evDatos.setInstancia(this);
//        btnHola.setTag(evDatos);
//        btnHola.setOnClickListener(new Eventos());
//        
                                
    }

//
//    public String decrypt(byte[] message) throws Exception {
//    	final MessageDigest md = MessageDigest.getInstance("md5");
//    	final byte[] digestOfPassword = md.digest("HG58YZ3CR9"
//    			.getBytes("utf-8"));
//    	//final byte[] keyBytes = Arrays.copyOf(digestOfPassword, 24);
//    	final byte[] keyBytes = new byte[24];
//    	System.arraycopy(digestOfPassword, 0, keyBytes, 0, 24);
//    	for (int j = 0, k = 16; j < 8;) {
//    		keyBytes[k++] = keyBytes[j++];
//    	}
//
//    	final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
//    	final IvParameterSpec iv = new IvParameterSpec(new byte[8]);
//    	final Cipher decipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
//    	decipher.init(Cipher.DECRYPT_MODE, key, iv);
//
//    	// final byte[] encData = new
//    	// sun.misc.BASE64Decoder().decodeBuffer(message);
//    	final byte[] plainText = decipher.doFinal(message);
//
//    	return new String(plainText, "UTF-8");
//    }
    
	/* (non-Javadoc)
	 * @see android.app.Activity#onRestart()
	 */
	@Override
	protected void onRestart() {
		super.onRestart();
		finish();
	}
    
//    @Override
//    protected void onDestroy() {
//    	// TODO Auto-generated method stub
//    	super.onDestroy();
//    	if(!periodico.isShutdown()){
//    		periodico.shutdown();
//    	}
//    }
    
}