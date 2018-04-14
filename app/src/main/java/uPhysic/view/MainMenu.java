package uPhysic.view;

import logger.Logger;
import client.view.ClientActivity;
import icetea.physiclab.R;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.RelativeLayout.LayoutParams;

public class MainMenu extends CustomActivity implements OnClickListener {

	private static final float RADIUS = 200;
	private static final int NUMBER_CHILD = 3;
	
	private Animation zoomInAnimation;
	private ImageButton btnHome;
	private ImageButton[] btnChilds;
	private Animation[] translateAniamtion;
	private int positionX;
	private int positionY;
	
	private float pi = (float) Math.PI;
	private float[] toXValue;
	private float[] toYValue;
	private AnimationSet[] animationSet;
	private Animation rotateAnimation;
	
	private Intent choiceLabIntent;
	private Intent clientIntent;
	private int dpi;
	private float pxRadius;
	private Logger logger;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_menu);
		logger = new Logger();
		btnHome = (ImageButton) findViewById(R.id.btn_home);
		btnChilds = new ImageButton[NUMBER_CHILD];
		btnChilds[0] = (ImageButton) findViewById(R.id.btn_1);
		btnChilds[1] = (ImageButton) findViewById(R.id.btn_2);
		btnChilds[2] = (ImageButton) findViewById(R.id.btn_3);

		toXValue = new float[NUMBER_CHILD];
		toYValue = new float[NUMBER_CHILD];

		for(int i = 0; i < NUMBER_CHILD; i++){
			btnChilds[i].setOnClickListener(this);
		}
		btnHome.setOnClickListener(this);
		animationSet = new AnimationSet[NUMBER_CHILD];
		
		choiceLabIntent = new Intent(this, ChoiceLaboratory.class);
		clientIntent = new Intent(this, ClientActivity.class);
		DisplayMetrics metrics = getResources().getDisplayMetrics();
		dpi = metrics.densityDpi;
		pxRadius = RADIUS * (dpi / 160);
	}
	
	public void onWindowFocusChanged (boolean hasFocus){
		super.onWindowFocusChanged(hasFocus);
		positionX = btnHome.getLeft();
		positionY = btnHome.getTop();
		calculateXValue();
		calculateYValue();
		for(int i = 0; i < NUMBER_CHILD; i++){
			
			LayoutParams a = (LayoutParams) btnChilds[i].getLayoutParams();
			a.leftMargin = (int)toXValue[i];
			a.topMargin = (int)toYValue[i];
			btnChilds[i].setLayoutParams(a);
		
			//btnChilds[i].layout((int)toXValue[i], (int)toYValue[i], (int)toXValue[i] + 100, (int)toYValue[i] + 100);
		}
		zoomInAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
		translateAniamtion = new TranslateAnimation[NUMBER_CHILD];
		rotateAnimation = new RotateAnimation(0, 360, 0, 0);
		for(int i = 0; i < NUMBER_CHILD; i++){
			translateAniamtion[i] = new TranslateAnimation(positionX - toXValue[i], 0, positionY - toYValue[i], 0);
			animationSet[i] = new AnimationSet(true);
			animationSet[i].setFillAfter(true);
			animationSet[i].setFillEnabled(true);
			animationSet[i].addAnimation(zoomInAnimation);
			animationSet[i].addAnimation(translateAniamtion[i]);
			animationSet[i].addAnimation(rotateAnimation);
			animationSet[i].setDuration(1500);
		}
	}
	
	public void calculateXValue(){
		float alpha = 0;
		float deltaAlpha = 2 * pi / NUMBER_CHILD;
		for(int i = 0; i < NUMBER_CHILD; i++, alpha = alpha + deltaAlpha)
			toXValue[i] = positionX + (float) (pxRadius * Math.sin(alpha)) - 50;
	}
	
	public void calculateYValue(){
		float alpha = 0;
		float deltaAlpha = 2 * pi / NUMBER_CHILD;
		for(int i = 0; i < NUMBER_CHILD; i++, alpha = alpha + deltaAlpha)
			toYValue[i] = positionY - (float) (pxRadius * Math.cos(alpha));
	}
	
	public Dialog onCreateDialog(int id){
		switch(id){
		case 0:
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			LayoutInflater inflater = this.getLayoutInflater();
			builder.setView(inflater.inflate(R.layout.dialog_about, null))
			.setTitle("Thông tin tác giả")
			.setNegativeButton("Quay lại", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			});
			return builder.create();
		}
		return null;
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_home:
			logger.logEvent("*", "click", "button home", "*");
			btnHome.setEnabled(false);
			for(int i = 0; i < NUMBER_CHILD; i++){
				btnChilds[i].startAnimation(animationSet[i]);
				btnChilds[i].setVisibility(View.VISIBLE);
			}
			break;
		case R.id.btn_1:
			showDialog(0);
			logger.logEvent("*", "click", "button about me", "*");
			break;
		case R.id.btn_3:			
			startActivity(choiceLabIntent);
			logger.logEvent("*", "click", "button choice lab", "*");
			break;
		case R.id.btn_2:
			startActivity(clientIntent);
			logger.logEvent("*", "click", "button client", "*");
			break;
		default:
			break;
		}
	}

}
