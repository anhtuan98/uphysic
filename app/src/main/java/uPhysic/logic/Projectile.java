package uPhysic.logic;

import icetea.physiclab.R;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.MotionEvent;

public class Projectile extends Laboratory {
    private final Resources r;
    private float v0;
	private float alpha;
	private float h;
	private float g;
	private ProjectitleGraph projectitleGraph;
	private float tablex = 300;
	private float tabley = 100;
	private Paint textPaint;
	private float vx0;
	private float vy0;
	private float vy;
	private float y;
	private Ball ball;
	private float xe;
	private float ye;
	private Context context;
	
	public Projectile(float params[],Context context){
	    r = context.getResources();
	    this.context = context;
		name = "Projectile";
		startParams = params;
		//graph.createBall(context);
		textPaint = new Paint();
		textPaint.setColor(Color.RED);
		textPaint.setTextSize(r.getDimensionPixelSize(R.dimen.txt_normal));
		startParamStrings = new String[] {r.getString(R.string.high_zero), r.getString(R.string.velocity_zero), r.getString(R.string.alpha_zero_2), r.getString(R.string.environment_accelerator)};
	//	startParamStrings = new String[] {"H","V0","alpha","g","s"};
		startParamsUnits = new int[]{0,11,10,7};
		calculateParams = new float[3];
		calculateParamStrings = new String[]{r.getString(R.string.total_time), r.getString(R.string.x_coordinate), r.getString(R.string.y_max)};
		calculateParamsUnits = new int[]{1,0,0,0};
		alpha = (float) (startParams[2] * pi / 180);
		if(alpha > 0)
			calculateParams[2] = ((float) (startParams[1] * Math.sin(alpha) * startParams[1] * Math.sin(alpha)) / (2 * startParams[3]) + startParams[0]) / 100;
		else
			calculateParams[2] = startParams[0] / 100;
		
		calculateParams[0] = 0;
		v0 = startParams[1] / 100;
		h = startParams[0] / 100;
		g = startParams[3] / 100;
		
		vx0 = (float) (v0 * Math.cos(alpha));
		vy0 = (float) (v0 * Math.sin(alpha));
		vy = vy0;
		if(h == 0){
			h = h + 0.01f;
		}
		calculateParams[1] = (float) ((v0 * Math.cos(alpha)) * calculateParams[0]);
		y = (float) (h + (v0 * Math.sin(alpha)) * calculateParams[0] - g * calculateParams[0] * calculateParams[0] / 2); 
		vectors = new Vector[3];
		vectorNames = new String[]{r.getString(R.string.x_velocity), r.getString(R.string.y_velocity), "Vận tốc"};
		vectors[0] = new Vector(context, 0, vx0);
		vectors[1] = new Vector(context);
		vectors[2] = new Vector(context);
		vectors[1].setRate(80);
		vectors[0].setRate(80);
		vectors[0].label = "Vx";
		vectors[1].label = "Vy";
		vectorMask = new int[0];
		projectitleGraph = new ProjectitleGraph(200, 600, r.getString(R.string.x_coordinate), r.getString(R.string.y_coordinate), 550, 550);
		ball = new Ball(0, 0, 10, Ball.BLACK, context);
		projectitleGraph.calculate(calculateParams[1], y);
		
	}

	public void calculate(){
		if(y > 0){
			calculateParams[1] = (float) ((v0 * Math.cos(alpha)) * calculateParams[0]);
			y = (float) (h + (v0 * Math.sin(alpha)) * calculateParams[0] - g * calculateParams[0] * calculateParams[0] / 2);
			float ny = y - (g * calculateParams[0] * timePerFrame * 2 + g * timePerFrame * timePerFrame) / 2;
			projectitleGraph.calculate(calculateParams[1], y);
			vy = vy0 - g * calculateParams[0];
			calculateParams[0] = calculateParams[0] + timePerFrame;
			if(ny < 0){
				y = 0;	
				calculateParams[1] = (float) ((v0 * Math.cos(alpha)) * calculateParams[0]);
				projectitleGraph.calculate(calculateParams[1], y);
			}
		}
	}
	

	public void draw(Canvas c){
		projectitleGraph.draw(c);
		drawCalculateParams(c);
		if(editMode == 3){
			c.drawLine(xe, ye, ball.xCoordinate, ball.yCoordinate, textPaint);
		}
	}
	


	@Override
	public void setInvisibleParams(int[] index) {
		
	}

	@Override
	public void clearCurrentGraph() {

		
	}

	public void showVector(ArrayList<Integer> id) {
		int l = id.size();
		vectorMask = new int[l];
		for(int i = 0; i < l; i++)
			vectorMask[i] = id.get(i);

	}

	@Override
	protected void drawCalculateParams(Canvas c) {
		int length = calculateParams.length;
		for(int i = 0; i < length; i++){
			if(calculateParamsUnits[i] == 0)
				c.drawText("" + calculateParamStrings[i] + ": " + calculateParams[i], tablex, tabley + (20 * i), textPaint);

			else
				c.drawText("" + calculateParamStrings[i] + ": " + calculateParams[i], tablex, tabley + (20 * i), textPaint);
		}
	}

	@Override
	public void clearAllGraph() {

	}

	@Override
	public void touchEditor(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();	
		if(editMode == 0){
			if(x > tablex - 20 && x < tablex + 20 && y > tabley - 20 && y < tabley + 20)
				editMode = 1;
			if(onPrepare && x > ball.xCoordinate - 20 && x < ball.xCoordinate + 20 && y > ball.yCoordinate - 20 && y < ball.yCoordinate + 20)
				editMode = 2;
			if(onPrepare && x > ball.xCoordinate - 60 && x < ball.xCoordinate - 20 && y > ball.yCoordinate - 20 && y < ball.yCoordinate + 20)
				editMode = 3;
			logger.logEvent("*", "touch", name, "*");
		}
		else{
			switch (editMode) {
			case 1:
				tablex = x;
				tabley = y;
				break;
			case 2:
					if(y > projectitleGraph.yRoot){
						y = projectitleGraph.yRoot;
						h = 0.01f;
						startParams[0] = 0;
					}
					else{
						h = projectitleGraph.toyRealCoordinate(y);
						startParams[0] = h * 100;
					}
					ball.yCoordinate = y;
					if(alpha > 0)
						calculateParams[2] = ((float) (startParams[1] * Math.sin(alpha) * v0 * Math.sin(alpha)) / (2 * startParams[3]) + h);
					else
						calculateParams[2] = h;
				break;
			case 3:
				if(x < ball.xCoordinate){
					alpha = 2 * pi - Vector.getDirection(x, y, ball.xCoordinate, ball.yCoordinate);
					if(alpha > pi / 2)
						alpha = alpha - 2 *pi;
					startParams[1] = Vector.getValue(x, y, ball.xCoordinate, ball.yCoordinate) * 10;
					v0 = startParams[1] / 100;
					startParams[2] = alpha * 180 / pi;
					xe = x;
					ye = y;
					if(alpha > 0)
						calculateParams[2] = ((float) (startParams[1] * Math.sin(alpha) * v0 * Math.sin(alpha)) / (2 * startParams[3]) + h);
					else
						calculateParams[2] = h;
					vy = vy0 = (float) (v0 * Math.sin(alpha));
					vx0 = (float) (v0 * Math.cos(alpha));
					if(vy < 0 )
						vectors[1].setDirection(pi / 2);
					else
						vectors[1].setDirection(3 * pi / 2);
					vectors[1].setValue(vy);
					vectors[0].setValue(vx0);
					vectors[2] = Vector.totalVector(context, vectors[0], vectors[1]);
					vectors[2].setRate(80);
					vectors[2].label = "V";
				}
			default:
				break;
			}

		}
	}
	
	public class ProjectitleGraph {
		//1s = unit
		//1m = unit
		private float xRoot;
		private float yRoot;
		private float maxWidth;
		private float maxHeight;
		private float xState = 10;// -1,0,+1
		private float yState = 10;// -3,0,+3
		private float xZoomLevel;
		private float yZoomLevel;
		private float[] xl;
		private float[] yl;
		private float unitx;
		private float unity;
		private String labelx;
		private String labely;
		Paint pBorder = new Paint();
		Paint pGrap = new Paint();
		Paint pC = new Paint();
		Paint pLabel = new Paint();
		private ArrayList<Float> points;
		private float[] units;
		private float[] p;
		
		
		ProjectitleGraph(float x, float y, String lx, String ly, float mw, float mh) {
			xRoot = x;
			yRoot = y;
			labelx = lx;
			labely = ly;
			maxHeight = mh;
			maxWidth = mw;
			xZoomLevel = 1;
			yZoomLevel = 1;
			unitx = mw / 8;
			unity = mh / 8;
			xl = new float[8];
			yl = new float[8];
			pGrap.setColor(Color.RED);
			pGrap.setStrokeWidth(r.getDimensionPixelSize(R.dimen.graph_width));
			pC.setColor(Color.BLUE);
			pC.setStyle(Style.STROKE);
			pC.setStrokeWidth(r.getDimensionPixelSize(R.dimen.graph_width));
			pLabel.setColor(Color.RED);
			pLabel.setTextSize(r.getDimensionPixelSize(R.dimen.txt_normal));
			pBorder.setColor(Color.BLUE);
			pBorder.setStyle(Style.STROKE);
			points = new ArrayList<Float>();
			points.add(0f);
			points.add(0f);
			units = new float[64];
			
			calculateRoot();
		
		}
		
		private float[] convertArray(){
			int size = points.size();
			float[] p = new float[size - 6];
			for(int i = 4; i < size - 2 ; i++){
				p[i - 4] = points.get(i);
			}
			return p;
		}
		

		void calculate(float x, float y){


			if(xState != 0)
				if(x < 0){
					if(xState == 1)
						xState = 0;
					else
						xState = -1;
				}
				else{
					if(xState == -1)
						xState = 0;
					else
						xState = 1;
				}

			
			if(yState != 0)
				if(y < 0){
					if(yState == 3)
						yState = 0;
					else
						yState = -3;
				}
				else{
					if(yState == -3)
						yState = 0;
					else
						yState = 3;
				}
			
			float xc = toxCoordinate(x);
			float yc = toyCooridnate(y);
			if(!onPrepare){
				points.add(xc);
				points.add(yc);
				points.add(xc);
				points.add(yc);
				p = convertArray();
			}
			if(xc > xRoot + maxWidth || xc < xRoot){
				zoomOutX();
				zoomOutY();
			}
			while(yc < yRoot - maxHeight){
				zoomOutY();
				zoomOutX();
				yc = (yc - yRoot) / 2 + yRoot;
			}	
			ball.xCoordinate = xc;
			ball.yCoordinate = yc;
				if(vy < 0 )
					vectors[1].setDirection(pi / 2);
				else
					vectors[1].setDirection(- pi / 2);
				vectors[1].setValue(vy);
				vectors[2] = Vector.totalVector(context, vectors[0], vectors[1]);
				vectors[2].setRate(80);
				vectors[2].label = "V";
		}
		
		public void draw(Canvas c){
			
			c.drawLine(xRoot, yRoot, xRoot, yRoot - maxHeight, pC);
			c.drawLine(xRoot, yRoot, xRoot, yRoot, pC);
			c.drawLine(xRoot, yRoot, xRoot + maxWidth, yRoot, pC);
			c.drawLine(xRoot, yRoot, xRoot, yRoot, pC);
			
			c.drawText(labelx, xRoot + maxWidth, yRoot - 10, pLabel);
			c.drawText(labely, xRoot, yRoot - maxHeight - 10, pLabel);
			c.drawText("" + 0, xRoot + 3, yRoot - 3, pLabel);
			for(int i = 0; i < yl.length; i++){
				c.drawText(yl[i] + "", xRoot - 20, units[i * 4 + 1], pLabel);
			}
			for(int i = 0; i < xl.length; i++){
				c.drawText(xl[i] + "", units[i * 4 + 32] - 4, yRoot + 13, pLabel);
			}
			c.drawLines(units, pC);
			c.drawCircle(xRoot, yRoot, 5, pGrap);
			c.drawRect(xRoot, yRoot - maxHeight, xRoot + maxWidth, yRoot, pBorder);
			if(!onPrepare && p != null)
				c.drawLines(p, pGrap);
			ball.draw(c);
			int l = vectorMask.length;
			for (int aVectorMask : vectorMask) {
				vectors[aVectorMask].draw(c, ball.xCoordinate, ball.yCoordinate);
			}
		}
		
		
		float toxCoordinate(float x){
			return x * (unitx / xZoomLevel) + xRoot;
		}
		float toyCooridnate(float y){
			return  yRoot - y * (unity / yZoomLevel);
		}
		float toyRealCoordinate(float yc){
			return (yRoot - yc) / (unity / yZoomLevel);
		}
		
		private void calculateRoot(){
			
			int size = points.size();
			for(int i = 4; i < size; i = i + 2){
				points.set(i, points.get(i));
				points.set(i + 1, points.get(i + 1));
			}
			float s = yRoot - yRoot + maxHeight;
			int i = 0;
			int j = 0;
			for(float l = unity; l <= s; l = l + unity, i = i + 4){
				units[i] = xRoot + 3;
				units[i + 1] = yRoot - l;
				units[i + 2] = xRoot - 3;
				units[i + 3] = yRoot - l;
				yl[j] = yZoomLevel * l / unity;
				j++;
			}
			s = yRoot - yRoot;
			for(float l = unity; l <= s; l = l + unity, i = i + 4){
				units[i] = xRoot + 3;
				units[i + 1] = yRoot + l;
				units[i + 2] = xRoot - 3;
				units[i + 3] = yRoot + l;
				yl[j] = -yZoomLevel * l / unity;
				j++;
			}
			j = 0;
			s = xRoot - xRoot;
			for(float l = unitx; l <= s; l = l + unitx, i = i + 4){
				units[i] = xRoot - l;
				units[i + 1] = yRoot + 3;
				units[i + 2] = xRoot - l;
				units[i + 3] = yRoot - 3;
				xl[j] = - xZoomLevel * l / unitx;
				j ++;
			}
			s = xRoot + maxWidth - xRoot;
			for(float l = unitx; l <= s; l = l + unitx, i = i + 4){
				units[i] = xRoot + l;
				units[i + 1] = yRoot + 3;
				units[i + 2] = xRoot + l;
				units[i + 3] = yRoot - 3;
				xl[j] = xZoomLevel * l / unitx;
				j ++;
			}
		}
		
		private void zoomOutX(){
			int size = points.size();
			xZoomLevel = xZoomLevel * 2;
			for(int i = 0; i < size; i = i + 2)
				points.set(i, (points.get(i) - xRoot) / 2 + xRoot);
			for(int i = 0; i < xl.length; i++)
				xl[i] = xl[i] * 2;
			
		}
		
		private void zoomOutY(){
			int size = points.size();
			yZoomLevel = yZoomLevel * 2;
			for(int i = 1; i < size; i = i + 2)
				points.set(i, (points.get(i) - yRoot) / 2 + yRoot);
			for(int i = 0; i < yl.length; i++)
				yl[i] = yl[i] * 2;
		}
		
		
		public void drawLineReference(Canvas c, float x, float y, int color){
			Paint p = new Paint();
			p.setColor(color);
			c.drawLine(x, y, x, yRoot, p);
			c.drawLine(x, y, xRoot, y, p);
		}
		
		public void clearGraph(){
			points = new ArrayList<Float>();
			points.add(0f);
			points.add(0f);
		}
	}

	@Override
	public void resetGraphic() {
		// TODO Auto-generated method stub
		
	}

}
