package uPhysic.logic;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;

import icetea.physiclab.R;

public class Vector {

	private float direction;
	private float value;
	private float xend;
	private float yend;
	public static final float pi = (float) Math.PI;
	public int color = -16776961;
	public String label = "v";
	public boolean edit = true;
	public boolean editMode = false;
	private float[] arrow;
	private int rate = 1;
	private Context context;
	
	public Vector(Context context, float direction, float value){
		setDirection(direction);
		arrow = new float[8];
		this.value = Math.abs(value);
		this.context = context;
	}
	
	public Vector(Context context){
		arrow = new float[8];
		this.context = context;
	}
	
	public void setRate(int r){
		rate = r;
	}
	
	public static float getDirection(float xstart, float ystart, float xend, float yend){
		float a = xend - xstart;
		float b = yend - ystart;
		float c = (float) Math.sqrt(a * a + b * b);
		float alpha = (float) Math.abs(Math.asin(b / c)) % (2 * pi);
		if(alpha > pi / 2)
			alpha = pi - alpha;
		if(a >= 0 && b >= 0)
			return alpha;
		else if(a >= 0 && b < 0)
			return 2 * pi - alpha;
		else if(a <= 0 && b >= 0)
			return pi - alpha;
		else 
			return pi + alpha;
	}
	
	public Vector perpendicularProjection(float dir){
		Vector v = new Vector(context);
		float delta = Math.abs(dir - direction);
		if(delta > pi / 2)
			v.setDirection(dir + pi);
		else
			v.setDirection(dir);
		v.value = (float) Math.abs(value * Math.cos(delta));
		return v;
	}
	
	public Vector inverse(){
		return new Vector(context, direction + pi, value);
	}
	
	public void setDirection(float dir){
		direction = dir % (pi * 2);
	}
	
	public float getDirection(){
		return direction;
	}
	
	public static Vector totalVector(Context context, Vector v1, Vector v2){
		
		Vector v3 = new Vector(context);
		float alpha1 = v1.getDirection();
		float alpha2 = v2.getDirection();
		float a = v1.value;
		float b = v2.value;
		float deltaA = alpha1 - alpha2;
		float alpha = Math.abs(deltaA - pi);
		float c = (float) Math.sqrt((a * a) + (b * b) - 2 * a * b * Math.cos(alpha));
		v3.value = c;
		float deltaB = (float) Math.acos(((a * a) + (c * c) - (b * b)) / (2 * a * c));
		float beta;
		if(deltaB > pi)
			deltaB = 2 * pi - deltaB;
		if(Math.abs(deltaA) < pi){
			if(deltaA > 0)
				beta = alpha1 - deltaB;
			else
				beta = alpha1 + deltaB;
		}
		else {
			if(deltaA > 0)
				beta = 2 * pi - (alpha1 - deltaB);
			else
				beta = alpha1 - deltaB;
		}
		v3.setDirection(beta);
		return v3;
	}
	
	public void draw(Canvas c, float x, float y){
		xend = (float) (x + value * rate * Math.cos(direction) / 10);
		yend = (float) (y + value * rate * Math.sin(direction) / 10);
		Paint p = new Paint();
		float dir1 = direction + 0.5f + pi;
		float dir2 = direction - 0.5f + pi;
		arrow[0] = (float) (xend + 10 * Math.cos(dir1));
		arrow[1] = (float) (yend + 10 * Math.sin(dir1));
		arrow[6] = (float) (xend + 10 * Math.cos(dir2));
		arrow[7] = (float) (yend + 10 * Math.sin(dir2));
		
		arrow[2] = xend;
		arrow[3] = yend;
		arrow[4] = xend;
		arrow[5] = yend;

		p.setColor(color);
		p.setStrokeWidth(context.getResources().getDimensionPixelSize(R.dimen.graph_width));
		p.setTextSize(context.getResources().getDimensionPixelSize(R.dimen.txt_normal));
		c.drawLine(x, y, xend, yend, p);
		c.drawLines(arrow, p);
		c.drawText(label, xend + 5, yend + 5, p);
	}

	public static float getValue(float xstart, float ystart, float xend, float yend){
		return (float) Math.sqrt((xstart - xend) * (xstart - xend) + (ystart - yend) * (ystart - yend));
	}
	
	public float getValue(){
		return value;
	}
	public void setValue(float v){
		value = Math.abs(v);
	}
}                                                                                  
