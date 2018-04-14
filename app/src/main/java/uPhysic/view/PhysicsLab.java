package uPhysic.view;

import logger.Logger;
import uPhysic.drawer.Environment;
import uPhysic.logic.Laboratory;
import icetea.physiclab.R;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.InputType;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import android.support.v4.app.DialogFragment;;

public class PhysicsLab extends CustomActivity implements OnClickListener {

	private Environment environment;
	private Laboratory laboratory;
	private ToggleButton btnControl;
	private Button btnReset;
	private Button btnGraph;
	private Button btnVector;
	private Button btnLab;
	
	private ChoiceDialog d1;
	private ChoiceDialog d2;
	private float variable[];
	private int lab;
	private TableLayout tableProperties;
	private Intent reciver;
	private Logger logger;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		logger = new Logger();
		setContentView(R.layout.activity_physics_lab);
		reciver = getIntent();
		lab = reciver.getIntExtra("lab", 1);
		
		btnControl = (ToggleButton) findViewById(R.id.btn_control);
		btnReset = (Button) findViewById(R.id.btn_reset);
		btnGraph = (Button) findViewById(R.id.btn_graph);
		btnVector = (Button) findViewById(R.id.btn_show_vector);
		btnLab = (Button) findViewById(R.id.btn_select_lab);
		
		btnControl.setOnClickListener(this);
		btnReset.setOnClickListener(this);
		btnGraph.setOnClickListener(this);
		btnVector.setOnClickListener(this);
		btnLab.setOnClickListener(this);
		
		tableProperties = (TableLayout) findViewById(R.id.table_properties);
		environment = (Environment) findViewById(R.id.environment);
		environment.createLab(lab);
		laboratory = environment.getLab();
		laboratory.timePerFrame = (config.speed + 1) * 0.01f;
		createTableProperties(tableProperties);
		setTitle();
	}
	
	public void createTableProperties(TableLayout tl){
		variable = laboratory.startParams;
		int[] units = laboratory.startParamsUnits;
		String[] names = laboratory.startParamStrings;
		
		int l = laboratory.startParamStrings.length;
		for(int i = 0; i < l; i++){
				TableRow tr = new TableRow(this);
				LayoutParams p = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
				tr.setLayoutParams(p);
				//tr.setBackgroundDrawable(getResources().getDrawable(R.drawable.table_cell));
				TextView tv = new TextView(this);
				EditText et = new EditText(this);
				et.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL|InputType.TYPE_NUMBER_FLAG_SIGNED);
				tv.setText(names[i]);
				tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.table_cell));
				et.setText("" + transformVariable(0, units[i], variable[i]));
				et.setBackgroundDrawable(getResources().getDrawable(R.drawable.table_cell));
				tr.addView(tv);
				tr.addView(et);
				tl.addView(tr);
			
		}
	}
	
	public float transformVariable(int type, int unit, float var){
		if(unit == Laboratory.METER || unit == Laboratory.METER_PER_SECOND|| unit == Laboratory.METER_PER_SECOND_SECOND || unit == Laboratory.METER_PER_SECOND_POSITIVE)
			if(type == 0)
				return var / 100;
			else
				return var * 100;
		return var;
	}
	
	public void onSaveInstanceState(Bundle savedInstanceState){
		//savedInstanceState.putFloatArray(key, value)
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_control:
			if(btnControl.isChecked()){
				environment.stopLab();
				logger.logEvent("*", "click", "button start", "*");
			}
			else{
				environment.startLab();
				logger.logEvent("*", "click", "button stop", "*");
			}
			break;
		case R.id.btn_graph:
			d1 = ChoiceDialog.newInstance(laboratory.graphNames, 1, null);
			d1.show(getSupportFragmentManager(), "test");
			logger.logEvent("*", "click", "button graph", "*");
			break;
		case R.id.btn_show_vector:
			d2 = ChoiceDialog.newInstance(laboratory.vectorNames, 2, laboratory.vectorMask);
			d2.show(getSupportFragmentManager(), "test");
			logger.logEvent("*", "click", "button show vector", "*");
			break;
		case R.id.btn_reset:
			environment.stopLab();
			logger.logEvent("*", "click", "button reset", "*");
			float[] var = getVariable(tableProperties);
			if(var != null){
				variable = var;
				
			}
			environment.setLab(variable, lab);
			if(!btnControl.isChecked()){
				btnControl.setChecked(true);
			}
			environment.postInvalidate();
			laboratory = environment.getLab();
			laboratory.timePerFrame = (config.speed + 1) * 0.01f;
			break;
		case R.id.btn_select_lab:
			environment.stopLab();
			logger.logEvent("*", "click", "button select lab", "*");
			finish();
			break;
		default:
			break;
		}
	}
	
	public Environment getEnvironment(){
		return environment;
	}
	
	public int gateKeeper(int unit, float value){
		switch (unit) {
		case Laboratory.METER:
			if(value < 0 || value > 1000)
				return 0;
			break;
		case Laboratory.SECOND:
			if(value < 0)
				return 0;
			break;
		case Laboratory.METER_PER_SECOND:
			if(value > 1000)
				return 0;
			break;
		case Laboratory.JUN:
			
			break;
		case Laboratory.KILOGRAM:
			if(value < 0 || value > 1000)
				return 0;
			break;
		case Laboratory.NEWTON_PER_METER:
			if(value < 0 || value > 1000)
				return 0;
			break;
		case Laboratory.RADIAN:
			if(value < - 2 * Math.PI || value > 2 * Math.PI)
				return 0;
			break;
		case Laboratory.METER_PER_SECOND_SECOND:
			
			break;
		case Laboratory.RADIAN_PER_SECOND:
			
			break;
		case Laboratory.TYPE_COLLSION:
			if(value != 0 && value != 1)
				return 0;
			break;
		case Laboratory.DEGREE:
			if(value < -90 || value > 90)
				return 0;
			break;
		case Laboratory.METER_PER_SECOND_POSITIVE:
			if(value < 0 || value > 1000)
				return 0;
		default:
			break;
		}
		return -1;
	}
	
	public void showInputError(int unit){
		Toast.makeText(this, Laboratory.INPUT_MSG_ERROR[unit], Toast.LENGTH_LONG).show();
	}
	
	public float[] getVariable(TableLayout tl){
		try{
		float[] variable = new float[laboratory.startParamStrings.length];
		for (int i = 0;i < laboratory.startParamStrings.length ; i++){
			TableRow tr = (TableRow) tl.getChildAt(i);
			EditText et = (EditText) tr.getChildAt(1);
			float value = Float.valueOf(et.getText().toString());
			int unit = laboratory.startParamsUnits[i];
			if(gateKeeper(unit, value) == 0){
				showInputError(unit);
				return null;
			}
			value = transformVariable(1, unit, value);
			variable[i] = value;
		}
		return variable;
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public void resetTable(){
		tableProperties.removeAllViews();
		createTableProperties(tableProperties);
	}
	
	private void setTitle(){
		switch(lab){
		case 0:
			this.setTitle(getString(R.string.pendulum));
			break;
		case 1:
			this.setTitle(getString(R.string.spring));
			break;
		case 2:
			this.setTitle(getString(R.string.collision));
			break;
		case 3:
			this.setTitle(getString(R.string.projectile));
			break;
		default:
			this.setTitle(getString(R.string.pendulum));
			break;
		}
	}
	
	public void onClick(DialogInterface dialog, int which) {
		super.onClick(dialog, which);
		laboratory.timePerFrame = (config.speed + 1) * 0.01f;
	} 

}
