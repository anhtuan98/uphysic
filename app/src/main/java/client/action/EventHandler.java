package client.action;

import java.io.IOException;

import logger.Logger;

import client.logic.Message;
import client.view.ClientActivity;

import icetea.physiclab.R;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class EventHandler implements OnClickListener {
	private ClientActivity ma;
	private Message mess;
	private String string[];
	private Logger logger;
	
	public EventHandler() {
		logger = new Logger();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		
		case R.id.btn_login:
			logger.logEvent(ma.userName.getText().toString(), "click", "button login", "*");
			string = new String[]{ma.userName.getText().toString(),ma.passWord.getText().toString()};
			mess = new Message(Message.REQUEST_LOGIN, string);
			try {
				switch (ma.client.status) {
				case 0:
					Toast.makeText(ma, "Không thể kết nối tới máy chủ", Toast.LENGTH_SHORT).show();
					break;
				case 1:
					ma.client.sender.send(mess);
					break;
				case 2:
					Toast.makeText(ma, "Số PORT không hợp lệ", Toast.LENGTH_SHORT).show();
					break;
				case 3:
					Toast.makeText(ma, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
					
					break;
				case 4:
					Toast.makeText(ma, "Không xác đinh được máy chủ", Toast.LENGTH_SHORT).show();					
					break;
				default:
					break;
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			break;
		case R.id.btn_quit:
			logger.logEvent(ma.userName.getText().toString(), "click", "button quit", "*");
			ma.finish();
			break;
		case R.id.btn_next:
			logger.logEvent(ma.userName.getText().toString(), "click", "button next", "*");
			ma.nextQuestion();
			break;
		case R.id.btn_back:
			logger.logEvent(ma.userName.getText().toString(), "click", "button back", "*");
			ma.backQuestion();
			break;
			
		case R.id.btn_choice_question:
			logger.logEvent(ma.userName.getText().toString(), "click", "goto question", "*");
			ma.showDialog(0);
			break;
		case R.id.btn_send:
			logger.logEvent(ma.userName.getText().toString(), "click", "submit", "*");
			ma.loadAnswers();
			//ma.answers = new String[]{"2","3","1","2"};
			Message reply = new Message(Message.FINISH_EXERCISE, ma.answers);
			try {
				ma.client.sender.send(reply);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ma.setLoadingScreen();
			break;
			
		default:
			break;
		}
		
	}
	public void setActivity(ClientActivity activity){
		this.ma = activity;
	}
}
