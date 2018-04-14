/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.logic;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Ieatea
 */
public class Receiver extends Thread {
    
    private Message msg;
    private BufferedReader reader;
    private Client client;
    
    public Receiver(Client client){
    	this.client = client;
        try {
            reader = new BufferedReader(new InputStreamReader(client.s.getInputStream()));
        } catch (IOException ex) {
            Logger.getLogger(Receiver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
            
    @Override
    public void run(){
        while(true){
            try {
                String m = reader.readLine();
                msg = Message.stringToMessage(m);
                if(msg != null)
                	client.messHandler.MessageHandle(msg);                
            } catch (IOException ex) {
            	client.status = 0;
                break;
            }
        }
    }
}
