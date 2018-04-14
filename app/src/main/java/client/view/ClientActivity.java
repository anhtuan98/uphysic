package client.view;

import java.io.File;
import java.io.IOException;

import logger.Logger;

import uPhysic.logic.Config;
import uPhysic.view.CustomActivity;
import client.action.EventHandler;
import client.logic.Client;
import client.logic.Exercise;
import client.logic.JsonParser;
import client.logic.MessageHandler;
import client.logic.Question;
import icetea.physiclab.R;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ClientActivity extends CustomActivity {
	
	public Client client;
	public JsonParser jsonParser;
	private LinearLayout loMiddle;
	private RelativeLayout loTop,loBottom;
	private CharSequence[] items;
	public EditText userName,passWord;
	private Button btnLogin,btnChoiceQuestion,btnNext,btnBack,btnSend, btnQuit;
	private TextView userName_top,mTimeLabel,title;
	private EventHandler click;
	private MessageHandler messHandler;
	private CountDown countDownTimer;
	private QuestionDisplayer questionDisplayer;
	private Exercise exercise;
	private Question[] question;
	private Question currentQuestion;
	private int totalNumberQuestion,currentNumberQuestion;
	private int number = -1;
	public String[] answers;
	public Config config;
	public CountDownTimer cd;
	public static String ROOT_PATH = Environment.getExternalStorageDirectory() + File.separator + "uPhysic" + File.separator;
	public Logger logger;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.form_login);
		logger = new Logger();
		userName = (EditText) findViewById(R.id.et_username_login);
		passWord = (EditText) findViewById(R.id.et_password_login);
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnNext = (Button) findViewById(R.id.btn_next);
		btnBack = (Button) findViewById(R.id.btn_back);
		btnSend = (Button) findViewById(R.id.btn_send);
		btnQuit = (Button) findViewById(R.id.btn_quit);
		btnChoiceQuestion = (Button) findViewById(R.id.btn_choice_question);
		mTimeLabel = (TextView) findViewById(R.id.tv_countDown);
		userName_top = (TextView) findViewById(R.id.tv_username_top);
		title = (TextView) findViewById(R.id.tv_title);
		
		loTop = (RelativeLayout) findViewById(R.id.row_top);
		loMiddle = (LinearLayout) findViewById(R.id.row_middle);
		loBottom = (RelativeLayout) findViewById(R.id.row_bottom);
		jsonParser = new JsonParser();
		messHandler = new MessageHandler();
		messHandler.setActivity(this);
		cd = new CountDownTimer(10000, 1000) {
		     public void onTick(long millisUntilFinished) {
		         
		     }
		     public void onFinish() {
		         client.searching = false;
		         if(client.status != 1)
		        	 Toast.makeText(getBaseContext(), "Kết nối thất bại", Toast.LENGTH_SHORT).show();
		     }
		  };
		ConnectSever();
		click = new EventHandler();
		click.setActivity(this);		
		btnLogin.setOnClickListener(click);
		btnBack.setOnClickListener(click);
		btnNext.setOnClickListener(click);
		btnSend.setOnClickListener(click);
		btnChoiceQuestion.setOnClickListener(click);
		btnQuit.setOnClickListener(click);

		
	}
	
	public boolean onOptionsItemSelected(MenuItem item){
		if(R.id.menu_refresh == item.getItemId()){
			ConnectSever();
			return true;
		}
		super.onOptionsItemSelected(item);
		return false;
	}
	
	protected void onDestroy(){
		super.onDestroy();
		if(cd != null)
			cd.cancel();
		if(client != null)
			client.interrupt();
			try {
				client.disconnect();
			} catch (IOException e) {
				Toast.makeText(getBaseContext(), "Không thể đóng kết nối", Toast.LENGTH_SHORT).show();
			}
	}
	

	
	public void ConnectSever(){
		try {
			if(cd != null)
				cd.cancel();
			if(client!=null && (client.login || client.status == 1 || client.searching)){
				if(client.status == 1){
					Toast.makeText(this, "Bạn đã kết nối", Toast.LENGTH_SHORT).show();
				}
				if(client.searching){
					Toast.makeText(this, "Đang tìm kiếm máy chủ", Toast.LENGTH_SHORT).show();
				}
				return;
			}
			if(client != null){
				client.disconnect();
			}
			config = new Config();
			config.readConfig();
			client = new Client(config.ip, config.port, this);
			client.messHandler = messHandler;
			client.start();
			cd.start();
			Toast.makeText(this, "Đang kết nối...", Toast.LENGTH_SHORT).show();

		} catch (IOException e) {
			Toast.makeText(this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
		}
	}
	
	public void setToastString(final String mess){
		this.runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				Toast.makeText(getBaseContext(), mess, Toast.LENGTH_SHORT).show();
				
			}
		});
	}
	

	public void loadQuestion(String data){
		jsonParser.loadData(data);
		exercise = jsonParser.exercise;
		question = exercise.getQuestions();
		totalNumberQuestion = question.length;
		currentNumberQuestion = 0;
		currentQuestion = question[currentNumberQuestion];
		questionDisplayer = new QuestionDisplayer(this, currentQuestion);
		this.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				loBottom.setVisibility(View.VISIBLE);
				btnNext.setEnabled(true);
				loMiddle.removeAllViews();
				loMiddle.addView(questionDisplayer);		
				btnBack.setEnabled(false);	
				btnSend.setEnabled(true);
				title.setText(exercise.getTitle());
				
				countDownTimer = new CountDown(exercise.getTime() * 60000, mTimeLabel,getBaseContext());
				mTimeLabel.setVisibility(View.VISIBLE);
				countDownTimer.start();
				getWindow().setTitle(getString(R.string.title_working));
			}
		});
		logger.logEvent(userName.getText().toString(), "start exercise", "*", exercise.getTitle());
		loadListDialog(exercise);
	}
	
	public void nextQuestion(){
		currentNumberQuestion = currentNumberQuestion + 1;
		checkButton();
		currentQuestion = question[currentNumberQuestion];
		loMiddle.removeAllViews();
		questionDisplayer = new QuestionDisplayer(this, currentQuestion);
		loMiddle.addView(questionDisplayer);
	}
	
	public void backQuestion(){
		currentNumberQuestion = currentNumberQuestion - 1;
		checkButton();
		currentQuestion = question[currentNumberQuestion];
		loMiddle.removeAllViews();
		questionDisplayer = new QuestionDisplayer(this, currentQuestion);
		loMiddle.addView(questionDisplayer);
	}
	
	public void loadRandomQuestion(int number){
		currentNumberQuestion = number;
		checkButton();
		currentQuestion = question[currentNumberQuestion];
		loMiddle.removeAllViews();
		questionDisplayer = new QuestionDisplayer(this, currentQuestion);
		loMiddle.addView(questionDisplayer);
	}
	
	public void loadAnswers(){
		this.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				btnBack.setEnabled(false);				
			}
		});
		countDownTimer.stop();
		answers = new String[totalNumberQuestion];
		for(int i =0;i< totalNumberQuestion ; i++){
			answers[i]= ""+ question[i].getAnswer();
		}
	}
	
	public void loadComment(String data){
		final CommentDisplayer commentDisplay = new CommentDisplayer(this, data);
		this.runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				btnSend.setEnabled(false);
				loTop.setVisibility(View.VISIBLE);
				loBottom.setVisibility(View.INVISIBLE);
				mTimeLabel.setVisibility(View.GONE);
				loMiddle.removeAllViews();
				loMiddle.addView(commentDisplay);
				getWindow().setTitle(getString(R.string.title_result));
			}
		});
	}
	
	public void setLoadingScreen(){
		final ScreenLoadingDisplayer screenLoading = new ScreenLoadingDisplayer(this);
		this.runOnUiThread(new Runnable() {			
			@Override
			public void run() {
				loTop.setVisibility(View.VISIBLE);
				loBottom.setVisibility(View.INVISIBLE);
				btnSend.setEnabled(false);
				mTimeLabel.setVisibility(View.GONE);
				userName_top.setText("Họ và tên: " + userName.getText().toString());
				loMiddle.removeAllViews();
				loMiddle.addView(screenLoading);
				//screenLoading.animDrawable.start();
				screenLoading.startAnimation();
				getWindow().setTitle(getString(R.string.title_waitting));
			}
		});
	}
	
	public void checkButton(){
		if(currentNumberQuestion + 1 >= totalNumberQuestion){
			btnNext.setEnabled(false);
		}
		else {
			btnNext.setEnabled(true);
		}
		if(currentNumberQuestion - 1 < 0){
			btnBack.setEnabled(false);
		}
		else{
			btnBack.setEnabled(true);
		}		
	}
	
	private void loadListDialog(Exercise e){
		int number = e.getQuestions().length;
		String[] tItems = new String[number];
		for(int i = 0 ;i < number ;i++){
			int z = i + 1;
			tItems[i] = "Câu " + z + " :";
		}
		items = tItems;
	}
	
	protected Dialog onCreateDialog(int id){
		switch(id){
		case 0:
			return new AlertDialog.Builder(this)
			.setTitle("Chọn câu cần chuyển đến ")
			.setPositiveButton("Chuyển đến", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					if(number == -1 )Toast.makeText(getBaseContext(), "Phải chọn câu hỏi cần chuyển đến trước ", Toast.LENGTH_SHORT).show();
					else{
						loadRandomQuestion(number);
					}
				}
			})
			.setNegativeButton("Trở lại", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			})
			.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					number = which;
				}
			})
			.create();
		}
		return null;
	}
}
