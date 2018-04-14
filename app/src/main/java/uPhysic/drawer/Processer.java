package uPhysic.drawer;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

public class Processer extends Thread {
	private Environment environment;
	public boolean killed = false;
	
	public Processer(Environment e){
		environment = e;
	}
	
	public boolean isRunning(){
		if(!killed)
			return true;
		return false;
	}
	
	public void run(){
			Canvas c;
			SurfaceHolder holder;
			while(!killed){
				c = null;
				holder = null;
				try {
					holder = environment.getHolder();
					synchronized(holder){
						c = holder.lockCanvas();
						environment.getLab().calculate();
						environment.postInvalidate();
					}
					sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} finally {
					if (c!=null && holder!=null) holder.unlockCanvasAndPost(c);
				}
			}
		
	}
	
}
