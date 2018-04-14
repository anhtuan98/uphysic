package uPhysic.logic;

import icetea.physiclab.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Ball {
	
	public static final int BLACK = 0;
	public static final int ORANGE = 1;
	
	public float xCoordinate;// toa do x
	public float yCoordinate;// toa do y
	private float radius = 10;// ban kinh
	public float xVelocity = 0;
	public float yVelocity = 0;
	private float xAccelerator = 0;
	private float yAccelerator = 0;
	private float mass;
	private Bitmap bitmap;

	public Ball(float xc, float yc, float radius, int color, Context c){
		xCoordinate = xc;
		yCoordinate = yc;
		this.radius = radius;
		switch (color) {
		case BLACK:
			bitmap = BitmapFactory.decodeResource(c.getResources(), R.drawable.ball_black);
			break;
		case ORANGE:
			bitmap = BitmapFactory.decodeResource(c.getResources(), R.drawable.ball_orange);
			break;
		default:
			bitmap = BitmapFactory.decodeResource(c.getResources(), R.drawable.ball_black);
			break;
		}
		bitmap = Bitmap.createScaledBitmap(bitmap, (int)radius * 7 / 3 + 5, (int)radius * 7 / 3 + 5, true);
	}
	
	public void draw(Canvas c){
		c.drawBitmap(bitmap, xCoordinate - radius * 7 / 6 - 5 / 2, yCoordinate - radius * 7 / 6 - 5 / 2, null);
		//c.drawCircle(xCoordinate, yCoordinate, radius, ballStyle);
	}
	
	public void setxCoordinate(float x){
		xCoordinate = x;
	}
	public float getxCoordinate(){
		return xCoordinate;
	}
	
	public void setyCoordinate(float y){
		yCoordinate = y;
	}
	public float getyCoordinate(){
		return yCoordinate;
	}

	public float getxVelocity() {
		return xVelocity;
	}

	public void setxVelocity(float xVelocity) {
		this.xVelocity = xVelocity;
	}

	public float getyVelocity() {
		return yVelocity;
	}

	public void setyVelocity(float yVelocity) {
		this.yVelocity = yVelocity;
	}

	public float getxAccelerator() {
		return xAccelerator;
	}

	public void setxAccelerator(float xAccelerator) {
		this.xAccelerator = xAccelerator;
	}

	public float getyAccelerator() {
		return yAccelerator;
	}

	public void setyAccelerator(float yAccelerator) {
		this.yAccelerator = yAccelerator;
	}
	
	public void setMass(float m){
		this.mass = m;
	}
	
	public float getMass(){
		return mass;
	}
	
	public float getRadius(){
		return radius;
	}

}
