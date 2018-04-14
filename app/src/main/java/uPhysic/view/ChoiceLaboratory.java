package uPhysic.view;



import uPhysic.logic.Laboratory;
import icetea.physiclab.R;
import android.annotation.SuppressLint;
import android.app.ActionBar.LayoutParams;
import android.content.Intent;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.os.Bundle;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import android.widget.ViewSwitcher.ViewFactory;

public class ChoiceLaboratory extends CustomActivity implements ViewFactory{
	
	private int imagesList[];
	private int idLab;
	private Intent intent;
	private LinearLayout myGallery;
	private ImageSwitcher imageSwitcher;

	
	protected void onCreate(Bundle s){
		super.onCreate(s);
		setContentView(R.layout.choice_laboratory);		
		intent = new Intent(this, PhysicsLab.class);
		
		imagesList = new int[]{R.drawable.lab_0,R.drawable.lab_1,R.drawable.lab_2,R.drawable.lab_3,R.drawable.lab_4,R.drawable.lab_5};
		
		myGallery = (LinearLayout) findViewById(R.id.mygallery);
		for(int i =0; i<imagesList.length; i++){
			myGallery.addView(insertView(imagesList[i]));
		}
		imageSwitcher = (ImageSwitcher) findViewById(R.id.switcher);
		imageSwitcher.setFactory(new ViewFactory() {
			
			@Override
				public View makeView() {
					ImageView imageView = new ImageView(getApplicationContext());
					imageView.setBackgroundColor(Color.BLACK);
					imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
					imageView.setLayoutParams(new ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
					return imageView;
				}
			
		});
		
		imageSwitcher.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				switch(idLab){
				case 0:
					intent.putExtra("lab", Laboratory.PENDULUM);
					startActivity(intent);	
					break;
				case 1:
					intent.putExtra("lab", Laboratory.SPRING);
					startActivity(intent);	
					break;
				case 2:
					intent.putExtra("lab", Laboratory.COLLISION);
					startActivity(intent);	
					break;
				case 3:
					intent.putExtra("lab", Laboratory.PROJECTILE);
					startActivity(intent);
					break;
				default:
					Toast.makeText(getBaseContext(), "Bạn phải mua thí nghiệm này để sử dụng",Toast.LENGTH_LONG).show();
					break;
				}
			}
		});
				
	}
	
	@SuppressLint("NewApi")
	private View insertView(final int i){
		LinearLayout layout = new LinearLayout(getApplicationContext());
		layout.setLayoutParams(new LayoutParams(250, 250));
		layout.setGravity(Gravity.CENTER);
		ImageView imageView = new ImageView(getApplicationContext());
		imageView.setScaleType(ImageView.ScaleType.FIT_XY);
		imageView.setLayoutParams(new LayoutParams(240, 200));
		imageView.setImageDrawable(getResources().getDrawable(i));
		imageView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				imageSwitcher.setImageDrawable(getResources().getDrawable(i));
				if(i == imagesList[0])	idLab = 0;
				else{
					if(i == imagesList[1]) idLab =1;
					else{
						if(i == imagesList[2]) idLab =2;
						else{
							if(i == imagesList[3]) idLab =3;
							else idLab = -1;
						}
					}
				}
			}
		});	

		layout.addView(imageView);
		return layout;

	}
	@Override
	public View makeView() {
		// TODO Auto-generated method stub
		return null;
	}

	
}
