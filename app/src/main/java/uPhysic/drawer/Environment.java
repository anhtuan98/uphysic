package uPhysic.drawer;


import icetea.physiclab.R;
import uPhysic.logic.Collision;
import uPhysic.logic.Laboratory;
import uPhysic.logic.Pendulum;
import uPhysic.logic.Projectile;
import uPhysic.logic.Spring;
import uPhysic.logic.Vector;
import uPhysic.view.PhysicsLab;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

@SuppressLint("DrawAllocation")
public class Environment extends SurfaceView implements Callback{
	// 1m = 100f
	private float[] points;
	private int pointIndex = 0;
	
	private Laboratory laboratory;
	private Vector environmentAccelerator;
	private Processer processer;
	
	private Context context;


	public Environment(Context context) {
		super(context);
		init(context);
	}
	
	public Environment(Context context, AttributeSet attrs){
		super(context, attrs);
		init(context);
	}
	
	public Environment(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}
	
	public void init(Context context){
		this.context = context;
		environmentAccelerator = new Vector(context, (float)Math.PI / 2, 1000f);
		getHolder().addCallback(this);
	}
	
	public void createLab(int lab){
		switch (lab) {
		case Laboratory.PENDULUM:
			laboratory = new Pendulum(new float[]{200f, 100f, 200f, (float) Math.PI / 3, environmentAccelerator.getValue()}, context);
			break;
		case Laboratory.SPRING:
			laboratory = new Spring(new float[]{20f, 10f, 0.1f, 100f, 100f, 200f, 300f}, context);
			break;
		case Laboratory.COLLISION:
			laboratory = new  Collision(new float[]{2f,200f,1f,-100f,0},context);
			break;
		case Laboratory.PROJECTILE:
			laboratory = new Projectile(new float[]{500f,1000f,45f,980f},context);
			break;
		default:
			laboratory = new Pendulum(new float[]{200f, 100f, 200f, (float) Math.PI / 3, environmentAccelerator.getValue()}, context);
			break;
		}
		postInvalidate();
	}
	
	
	public void addPoint(float newPoint){
		points[pointIndex] = newPoint;
		pointIndex++;
	}
	
	public void onDraw(Canvas c){
		laboratory.draw(c);
	}
	
	public float toRadian(float degree){
		return (float) ((degree * Math.PI) / 180);
	}	

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
		
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		//processer.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		stopLab();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
	    //boolean isReleased = event.getAction() == MotionEvent.ACTION_UP || event.getAction() == MotionEvent.ACTION_CANCEL;
	    boolean isPressed = event.getAction() == MotionEvent.ACTION_MOVE || event.getAction() == MotionEvent.ACTION_DOWN;
	    if(isPressed){
	    	laboratory.touchEditor(event);
	    	postInvalidate();
	    }
	    if(!isPressed){
	    	laboratory.editMode = 0;
	    	((PhysicsLab) context).resetTable();
	    }
		return true;
	}
	
	
	public void startLab(){
		stopLab();
		processer = new Processer(this);
		processer.start();
		laboratory.editMode = 0;
		laboratory.onPrepare = false;
	}
	public void stopLab(){
		if(processer != null)
			processer.killed = true;
	}
	
	public Laboratory getLab(){
		return laboratory;
	}
	
	public void setLab(float params[],int lab){
		switch(lab){
		case Laboratory.PENDULUM:
			laboratory = new Pendulum(params, context);
			break;
		case Laboratory.SPRING:
			laboratory = new Spring(params, context);
			break;
		case Laboratory.COLLISION:
			laboratory = new Collision(params, context);
			break;
		case Laboratory.PROJECTILE:
			laboratory = new Projectile(params, context);
			break;
		default:
			break;
		}
	}
}
