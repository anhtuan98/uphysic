package uPhysic.view;

import logger.Logger;
import uPhysic.logic.Config;
import icetea.physiclab.R;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

public class CustomActivity extends FragmentActivity implements OnClickListener {
	
	private MenuSettingDialog menuSettingDialog;
	protected Config config;
	private Logger logger;
	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logger = new Logger();
		menuSettingDialog = new MenuSettingDialog();
		config = new Config();
		config.readConfig();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	protected void onDestroy() {
        super.onDestroy();
        unbindDrawables(findViewById(R.id.root_view));
        System.gc();
    }

    private void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }
    
	
	public boolean onOptionsItemSelected(MenuItem item){
		switch (item.getItemId()) {
	        case R.id.menu_settings:
	            menuSettingDialog.show(getSupportFragmentManager(), "");
	            logger.logEvent("*", "click", "button menu setting", "*");
	            return true;
	        case R.id.menu_back:
	        	finish();
	        	logger.logEvent("*", "click", "button menu back", "*");
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {
		config.ip = menuSettingDialog.ip1.getText() + "." + menuSettingDialog.ip2.getText() + "." + menuSettingDialog.ip3.getText() + "." + menuSettingDialog.ip4.getText();;
		config.port = Integer.parseInt(menuSettingDialog.portInput.getText().toString());;
		config.speed = menuSettingDialog.speedBar.getProgress();;
		boolean b = config.saveConfig();
		String msg = "Thất bại";
		if(b)
			msg = "Đã lưu thành công";
		Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
	} 
}
