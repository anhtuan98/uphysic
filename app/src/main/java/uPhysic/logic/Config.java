package uPhysic.logic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import client.view.ClientActivity;

import android.os.Environment;

public class Config {
	public String ip;
	public int port;
	private File file;
	private File rootFile;
	private BufferedReader br;
	private BufferedWriter wr;
	private String rootPath;
	public int speed;
	
	public Config(){
		try {
			rootPath = ClientActivity.ROOT_PATH;
			rootFile = new File(rootPath);
			file = new File(rootFile, "config.txt");
			if(!rootFile.exists()){
				rootFile.mkdir();
				if(!file.exists()){
					file.createNewFile();
				}
			}
			br = new BufferedReader(new FileReader(file));

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static boolean deleteDirectory(File path) {
	    if( path.exists() ) {
	      File[] files = path.listFiles();
	      if (files == null) {
	          return true;
	      }
	      for(int i=0; i<files.length; i++) {
	         if(files[i].isDirectory()) {
	           deleteDirectory(files[i]);
	         }
	         else {
	           files[i].delete();
	         }
	      }
	    }
	    return( path.delete() );
	  }
	
	public boolean readConfig(){
		try {
			String line = "";
			if(file.length() != 0){
				while((line = br.readLine()) != null){
					String[] array = line.split(":");
					if(array[0].equals("ip"))
						ip = array[1];
					else if(array[0].equals("port"))
						port = Integer.parseInt(array[1]);
					else
						speed = Integer.parseInt(array[1]);
				}
			}
			else{
				ip = "127.0.0.1";
				port = 8888;
				speed = 1;
			}
			return true;
		} catch (IOException e) {
			return false;
		}
	}
	
	public boolean saveConfig(){		
		try {
			wr = new BufferedWriter(new FileWriter(file));
			String data = "ip:" + ip + "\n" + "port" + ":" + port + "\n" + "speed:" + speed;
			wr.write(data);
			wr.flush();
			wr.close();
			return true;
		} catch (IOException e) {
			return false;
		}
	}
}
