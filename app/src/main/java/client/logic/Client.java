/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.logic;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeoutException;

import android.content.Context;
import android.os.Handler;
import android.widget.Toast;

/**
 *
 * @author 
 */

public class Client extends Thread{    
	
	public Sender sender;
	public Receiver receiver;
	public MessageHandler messHandler;
	public Socket s;
	private String addr;
	private int port;
	public Context context;
	public int status = 0;
	public boolean login = false;
	public boolean searching;

	
    public Client(String address, int port, Context context) throws UnknownHostException, IOException{
    	addr = address;
    	this.port = port;
    	this.context = context;
    	
    }

    public void run(){
        try {
        		searching = true;
	        	s = new Socket();
	        	s.connect(new InetSocketAddress(addr, port), 10000);
		        receiver = new Receiver(this);
		        receiver.start();
		        sender = new Sender(s, context);
		        sender.send(new Message(Message.REQUEST_CONNECT, null));   
		        searching = false;
        } catch (UnknownHostException ex) {     	
        	status = 4;	
        } catch (IllegalArgumentException ex){
        	status = 2;
        } catch (IOException e) {    	
			status = 3;
        }
    }
    
    public void setMessHandler(MessageHandler mess){
    	this.messHandler = mess;
    }
    
	public void disconnect() throws IOException{
		
			if(s != null){
				if(sender != null){
					Message mDisconnect = new Message(Message.DISCONNECT, null);
					sender.send(mDisconnect);
				}
				s.close();
			}
	
	}
}
