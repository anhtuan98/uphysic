package client.logic;


import java.io.IOException;

import android.util.Log;

import client.view.ClientActivity;



public class MessageHandler{
	private ClientActivity ma;
	private Message reply;
	private String data;
	private String question;

	public int MessageHandle(Message mess){
		int result = 0;
		switch (mess.type) {
		
		case Message.ACCEPT_LOGIN:
			ma.setLoadingScreen();
			ma.client.login = true;
			reply = new Message(Message.READY_CONNECT, null);
			try {
				ma.client.sender.send(reply);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
			
		case Message.WRONG_PASS:
			ma.setToastString("Tên đăng nhập hoặc mật khẩu không đúng!");
			break;
			
		case Message.EXERCISE:
			data = mess.getDataString();
			Log.d("LOGXXXXXX", data);
			question = data;
			reply = new Message(Message.READY_EXERCISE, null);
			try {
				ma.client.sender.send(reply);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		case Message.START_EXERCISE:
			ma.loadQuestion(question);
			break;
			
		case Message.COMMENT:
			data = mess.getDataString();
			ma.loadComment(data);
			break;
			
		case Message.TIME_OUT:
			ma.loadAnswers();
			Message reply = new Message(Message.FINISH_EXERCISE, ma.answers);
			try {
				ma.client.sender.send(reply);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ma.setLoadingScreen();
			break;
		case Message.KICK:
			ma.finish();
			break;
		case Message.DONE_CONNECT:
			ma.client.status = 1;
			ma.setToastString("Kết nối thành công!");
			break;
		default:
			break;
		}
		return result;
	}
	public void setActivity(ClientActivity activity){
		this.ma = activity;
	}
	
}
