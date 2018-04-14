package client.logic;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketException;

import android.content.Context;
import android.widget.Toast;

public class Sender {
	private BufferedWriter buffWriter;
	private Context context;
	public Sender(Socket s, Context context) throws IOException{
		buffWriter = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
		this.context = context;
	}
	public void send(Message mess) throws IOException {
		try{
			buffWriter.write(mess.messageToString()+ "\n");
			buffWriter.flush();
		} catch(SocketException se){
			Toast.makeText(context, "Mất kết nối tới máy chủ", Toast.LENGTH_SHORT).show();
		}
	}

}
