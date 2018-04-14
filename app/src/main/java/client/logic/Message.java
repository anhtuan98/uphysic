/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.logic;


/**
 *
 * @author Ieatea
 */

public class Message {
    
    public static final int REQUEST_LOGIN = 0;
    public static final int ACCEPT_LOGIN = 1;
    public static final int WRONG_PASS = 2;
    public static final int READY_CONNECT = 3;
    public static final int EXERCISE = 4;
    public static final int READY_EXERCISE = 5;
    public static final int START_EXERCISE = 6;
    public static final int WORKING = 7;
    public static final int DONE = 8;
    public static final int CHAT_SERVER = 9;
    public static final int CHAT_CLIENT = 10;
    public static final int CREATE_USER = 11;
    public static final int FINISH_EXERCISE = 12;
    public static final int COMMENT = 13;
    public static final int TIME_OUT = 14;
    public static final int DISCONNECT = 15;
    public static final int KICK = 16;
    public static final int REQUEST_CONNECT = 17;
    public static final int DONE_CONNECT = 18;
    
    public static final String[] msgPrefix = {"request_l", "accept_l", "wrong_p", "ready_c", "excercise",
    "ready_e", "start_e", "working", "done", "chat_s", "chat_c", "create_u","finish_e","comment","time_o", "disconect", "kick",
    "request_c", "done_c"};
    public static final String separator = "@-";
    

    public String[] data;
    public int type;
   
    
    public Message(int type, String[] m){
        data = m;
        this.type = type;
    }
    
    public static Message stringToMessage(String msg){
    	if(msg == null)
    		return null;
        String[] str = msg.split(separator);
        int type = getType(str[0]);
        String[] m = new String[str.length - 1];
        for(int i = 0; i < m.length; i++) {
            m[i] = str[i + 1];
        }
        return new Message(type, m);
    }
    
    public String messageToString(){
        return msgPrefix[type] + separator + getDataString();
    }
    
    public String getDataString(){
    	if(data == null){
    		return "";
    	}
        int l = data.length;
        if(l == 0) {
            return "";
        }
        if(l == 1) {
            return data[0];
        }
        String s = data[0];
        for(int i = 1; i < data.length; i++){
            s = s + separator + data[i];
        }
        return s;
    }
    
    public static int getType(String prefix){
        for(int i = 0; i < msgPrefix.length; i++) {
            if(prefix.equals(msgPrefix[i])) {
                return i;
            }
        }
        return -1;
    }
}
