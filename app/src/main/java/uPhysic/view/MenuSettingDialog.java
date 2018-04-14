package uPhysic.view;

import uPhysic.logic.Config;
import icetea.physiclab.R;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.SeekBar;
import android.content.DialogInterface.OnClickListener;

public class MenuSettingDialog extends android.support.v4.app.DialogFragment {
	
	public EditText ip1;
	public EditText ip2;
	public EditText ip3;
	public EditText ip4;
	public EditText portInput;
	private LayoutInflater inflater;
	public Config config;
	public SeekBar speedBar;
	
	public Dialog onCreateDialog(Bundle saveInstanceState){
		 AlertDialog.Builder builder = new AlertDialog.Builder(getActivity()); 
		 config = new Config();
		 config.readConfig();
		 String[] ip = config.ip.split("\\.");
		 inflater = getActivity().getLayoutInflater();
		 View view = inflater.inflate(R.layout.menu_setting, null);
		 builder.setView(view);
		 builder.setTitle(getResources().getString(R.string.menu_settings));
		 ip1 = (EditText) view.findViewById(R.id.ip1);
		 ip2 = (EditText) view.findViewById(R.id.ip2);
		 ip3 = (EditText) view.findViewById(R.id.ip3);
		 ip4 = (EditText) view.findViewById(R.id.ip4);
		 portInput = (EditText) view.findViewById(R.id.port);
		 
		 speedBar = (SeekBar) view.findViewById(R.id.seekBar1);
		 speedBar.setProgress(config.speed);
		 speedBar.setMax(2);
		 
		 ip1.setText(ip[0]);
		 ip2.setText(ip[1]);
		 ip3.setText(ip[2]);
		 ip4.setText(ip[3]);
		 portInput.setText("" + config.port);
		 builder.setNegativeButton(getResources().getString(R.string.btn_cancel), new OnClickListener() {	
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				
			}
		 });
		 builder.setPositiveButton(getResources().getString(R.string.btn_ok), ((CustomActivity)getActivity()));
		 return builder.create();
	}
}
