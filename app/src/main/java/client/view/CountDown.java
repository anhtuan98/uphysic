package client.view;


import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;
import android.widget.Toast;

public class CountDown {
	private CountDownTimer mCountDown;
	private Context mContext;
	private TextView mTextview;
	public CountDown(long timer,TextView textview,Context context){
		mContext = context;
		mTextview = textview;
		mCountDown = new CountDownTimer(timer,1000) {
			
			@Override
			public void onTick(long millisUntilFinished) {
				  int seconds = (int) (millisUntilFinished / 1000);
                  int minutes = seconds / 60;
                  seconds = seconds % 60;
                  mTextview.setText("" + minutes + ":" + String.format("%02d", seconds));
				
			}
			
			@Override
			public void onFinish() {
				Toast.makeText(mContext, "Time Over", Toast.LENGTH_LONG).show();
				//ma.showDialog(0);
			}
		};
	}
	public void start(){
		mCountDown.start();
	}
	
	public void stop(){
		mCountDown.cancel();
	}


		
	
}

