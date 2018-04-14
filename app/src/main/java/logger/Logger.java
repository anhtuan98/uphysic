package logger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import client.view.ClientActivity;

public class Logger {
	private File logPath;
	private Calendar c;

	public Logger(){
		try{
			logPath = new File(ClientActivity.ROOT_PATH, "log.txt");
			if(!logPath.exists())
				logPath.createNewFile();
			c = Calendar.getInstance();
		} catch(IOException ignored){
			
		}
	}
	
	public void logEvent(String user, String event, String object, String content){
		try {
			BufferedReader br = new BufferedReader(new FileReader(logPath));
			StringBuilder data = new StringBuilder();
			String line = "";
			while((line = br.readLine()) != null){
				data.append(line);
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(logPath));
			String time = c.get(Calendar.DAY_OF_MONTH) + "/" + c.get(Calendar.MONTH) + "/" + c.get(Calendar.YEAR) + "/" + c.get(Calendar.HOUR_OF_DAY) + ":" + c.get(Calendar.MINUTE) + ":" + c.get(Calendar.SECOND);
			String log = data + time + "-" + user + "-" + event + "-" + object + "-" + content + "@@";
			bw.write(log);
			bw.flush();
			bw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
