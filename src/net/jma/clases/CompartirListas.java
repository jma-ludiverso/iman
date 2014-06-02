package net.jma.clases;

import java.util.Calendar;

import net.jma.iman.R;
import net.jma.ws.Constantes_WS;
import net.jma.ws.WS_Iman;
import net.jma.ws.arrayCadenas;
import net.jma.ws.reqIman_ComparteLista;
import net.jma.ws.reqIman_Usuario;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Toast;

public class CompartirListas {

	Activity v_Activity;
	int version;

	public CompartirListas(Activity pActivity) {
		super();
		this.v_Activity = pActivity;
		PackageManager manager = pActivity.getPackageManager();
		try{
			PackageInfo info = manager.getPackageInfo(pActivity.getPackageName(), 0);
			version = info.versionCode;					
		}catch (Exception ex){
			version = 1;
		}
	}
	
	public void CompartirLista_AddContactos(int idLista){
		try{
			ListView lvContactos = (ListView)v_Activity.findViewById(R.id.lvCompartirContactos);	
			arrayCadenas arrContactos = new arrayCadenas();
			for(int intI=0; intI<=lvContactos.getChildCount()-1;intI++){
				View itemContacto = lvContactos.getChildAt(intI);
				CheckBox chkCategoria = (CheckBox)itemContacto.findViewById(R.id.chkCompartirListaContacto);
				if(chkCategoria.isChecked()){
					String tagContacto = itemContacto.getTag().toString();
					String[] infoContacto = tagContacto.split("\\|");
					String tlf = infoContacto[1].replaceAll("-", "").trim();
					//tlf = tlf.replaceAll(";", "");
					if(!tlf.equals("")){
						arrContactos.add(tlf);
					}
				}									
			}     				
			if(!arrContactos.isEmpty()){
				reqIman_ComparteLista infoCompartir = new reqIman_ComparteLista();
				infoTelefono info = new infoTelefono(this.v_Activity);
				reqIman_Usuario infoUsuario = info.dameInfoUsuario();
				infoCompartir.setProperty(0, infoUsuario);
				infoCompartir.setProperty(1, arrContactos);
				infoCompartir.setProperty(2, idLista);
				Calendar c = Calendar.getInstance();
				infoCompartir.setProperty(3, "Ha compartido la lista contigo (" + c.getTime().toLocaleString() + ")");
				WS_Iman ws = new WS_Iman(v_Activity, Constantes_WS.WS_Funcion_Mensaje);
				ws.CompartirLista(infoCompartir);				
			}						
		}catch (Exception ex){
			Toast.makeText(v_Activity, v_Activity.getResources().getText(net.jma.iman.R.string.msgErrorCompartirLista),Toast.LENGTH_SHORT).show();						
		}
		
	}

}
