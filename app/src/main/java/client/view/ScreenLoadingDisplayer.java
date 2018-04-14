package client.view;

import icetea.physiclab.R;
import icetea.physiclab.R.anim;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.view.Gravity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ScreenLoadingDisplayer extends LinearLayout {

	private ImageView animImage,textImage;
	private Animation rotate;
	private TextView tv1,tv2;
	public ScreenLoadingDisplayer(Context context) {
		super(context);	
	
		animImage = new ImageView(context);
		textImage = new ImageView(context);
		tv1 = new TextView(context);
		tv2 = new TextView(context);
		animImage.setImageResource(R.drawable.top_loadingscreen);
		textImage.setImageResource(R.drawable.bottom_loadingscreen);
		rotate = AnimationUtils.loadAnimation(context, R.anim.screen_loading);
		LayoutParams wrap = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
		

		
		LayoutParams loanimImage = new LayoutParams(LayoutParams.MATCH_PARENT,0);
		loanimImage.weight = 5;
		animImage.setLayoutParams(loanimImage);
		
		LayoutParams lolineBottom = new LayoutParams(LayoutParams.MATCH_PARENT,0);
		lolineBottom.weight = 5;		
		LinearLayout lineBottom = new LinearLayout(context);
		lineBottom.setLayoutParams(lolineBottom);
		
		LayoutParams loBottomLeft = new LayoutParams(0,LayoutParams.MATCH_PARENT);
		loBottomLeft.weight = 1.5f;
		tv1.setLayoutParams(loBottomLeft);		
		LayoutParams lotextImage = new LayoutParams(0,LayoutParams.MATCH_PARENT);
		lotextImage.weight = 7;
		textImage.setLayoutParams(lotextImage);		
		LayoutParams loBottomRight = new LayoutParams(0,LayoutParams.MATCH_PARENT);
		loBottomRight.weight = 1.5f;
		tv2.setLayoutParams(loBottomRight);

		
		lineBottom.setOrientation(HORIZONTAL);
		lineBottom.addView(tv1);
		lineBottom.addView(textImage);
		lineBottom.addView(tv2);
		
		this.setOrientation(VERTICAL);
		this.setGravity(Gravity.CENTER);
		this.addView(animImage);
		this.addView(lineBottom);
		this.setLayoutParams(wrap);
	}
	
	public void startAnimation(){
		//animImage.startAnimation(rotate);
	}

}
