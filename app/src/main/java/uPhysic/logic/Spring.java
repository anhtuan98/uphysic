package uPhysic.logic;

import icetea.physiclab.R;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

public class Spring extends Laboratory {
	private Ball ball;
	private Paint lineStyle;
	private float[] spring = new float[40];
	
	private float maxA;
	private float xRoot;
	private float yRoot;
	private float blance;
	
	private float omega;
	private float direction;
	private float springLenght;
	private float tablex;
	private float tabley;
	private Paint textPaint;
	private Paint pointPaint;
	private Resources r;
	
	private float newx;
	private float oldx;
	private float newv;
	private float g;
	private float a;
	
	public Spring(float[] params, Context context){
		r = context.getResources();
		name = "Spring";
		
		graphNames = new String[]{r.getString(R.string.x_amplitude), r.getString(R.string.x_velocity), r.getString(R.string.x_accelerator), r.getString(R.string.clear_graph), r.getString(R.string.hide_graph)};
		graphs = new Graph[3];
		for(int i = 0; i < graphs.length; i++)
			graphs[i] = new Graph(r, 50, 600, r.getString(R.string.time), graphNames[i], 750, 550);
		
		vectors = new Vector[1];
		float dir = Vector.getDirection(params[6] + params[3], params[5], params[4], params[5]);
		vectors[0] = new Vector(context, dir, 0);
		vectors[0].label = "vx";
		vectorNames = new String[]{r.getString(R.string.x_velocity)};
		vectorMask = new int[0];
		
		startParams = params;
		startParamStrings = new String[]{r.getString(R.string.stiffness), r.getString(R.string.mass), r.getString(R.string.resistance), r.getString(R.string.amplitude), r.getString(R.string.x_root), r.getString(R.string.y_root), r.getString(R.string.x_blance)};
		startParamsUnits = new int[]{5, 4, -1, 0, 0, 0, 0};
		
		calculateParams = new float[3];
		calculateParamStrings = new String[]{r.getString(R.string.omega), r.getString(R.string.total_time), r.getString(R.string.period)};
		calculateParamsUnits = new int[]{8, 1, 1};
		
		ball = new Ball(startParams[6] + params[3], startParams[5], 10, Ball.BLACK, context);
		calculateParams[0] = omega = (float) Math.sqrt(params[0] / params[1]);
		calculateParams[1] = 0;
		calculateParams[2] = 2 * pi / calculateParams[0];
		
		
		maxA = params[3];
		xRoot = params[4];
		yRoot = params[5];
		blance = params[6];
		
		lineStyle = new Paint();
		tablex = 20;
		tabley = 20;
		
		textPaint = new Paint();
		textPaint.setColor(Color.RED);
		textPaint.setTextSize(r.getDimensionPixelSize(R.dimen.txt_normal));
		pointPaint = new Paint();
		pointPaint.setColor(Color.YELLOW);
		
		createSpring();
	}
	
	
	public void calculate(){
		
		if(editMode == 0){
			calculateParams[1] = calculateParams[1] + timePerFrame;
		}

		oldx = ball.xCoordinate;
		a = (float) (maxA * Math.cos(omega * calculateParams[1]));
		newx = blance + a;
		newv = (newx - oldx) / timePerFrame;
		g = startParams[0] * (newx - blance) / startParams[1];
		ball.setxCoordinate(newx);
		ball.setyCoordinate(yRoot);
		ball.setxVelocity(newv);
		calculateSpring();
		if(editMode == 0){
			graphs[0].calculate(calculateParams[1], a / 100);
			graphs[1].calculate(calculateParams[1], newv / 100);
			graphs[2].calculate(calculateParams[1], g / 100);
			vectors[0].setDirection(Vector.getDirection(oldx, yRoot, newx, yRoot)); 
			vectors[0].setValue(newv);
		}
		
	}
	
	//tinh toan lo xo
	public void createSpring(){
		direction = Vector.getDirection(xRoot, yRoot, ball.getxCoordinate(), yRoot);
		springLenght = (float) (Vector.getValue(xRoot, yRoot, ball.getxCoordinate(), yRoot) * Math.cos(direction));
		float s = (4f / 35f) * springLenght;
		spring[0] = xRoot;
		spring[1] = yRoot;
		spring[2] = xRoot + 0.1f * springLenght;
		spring[3] = yRoot;
		spring[4] = spring[2];
		spring[5] = yRoot;
		spring[6] = spring[4] + (2f / 35f) * springLenght;
		spring[7] = yRoot + 20;
		spring[34] = xRoot + 0.9f * springLenght;
		spring[35] = yRoot;
		spring[36] = spring[34];
		spring[37] = yRoot;
		spring[38] = ball.getxCoordinate();
		spring[39] = yRoot;
		float h = 1;	
		for(int i = 8; i <= 30; i = i + 4){
			h = h * -1;
			spring[i] = spring[i - 2];
			spring[i + 1] = spring[i - 1];
			spring[i + 2] = spring[i] + s;
			spring[i + 3] = yRoot + 20 * h;
		}
		spring[32] = spring[30];
		spring[33] = spring[31];
	}
	
	private void calculateSpring(){
		float oldLenght = springLenght;
		direction = Vector.getDirection(xRoot, yRoot, ball.getxCoordinate(), yRoot);
		springLenght = (float) (Vector.getValue(xRoot, yRoot, ball.getxCoordinate(), yRoot) * Math.cos(direction));
		float rate = springLenght / oldLenght;
		for(int i = 2; i < 40; i = i + 2){
			spring[i] = (spring[i] - xRoot) * rate + xRoot;
		}
	}
	
	public void draw(Canvas c){
		if(!graphMode){
			drawSpring(c);
			c.drawCircle(blance, yRoot, 5, pointPaint);
			c.drawCircle(xRoot, yRoot, 5, pointPaint);
			int l = vectorMask.length;
			for (int aVectorMask : vectorMask) {
				vectors[aVectorMask].draw(c, ball.xCoordinate, ball.yCoordinate);
			}
			ball.draw(c);
			drawCalculateParams(c);
		}
		else{
			graphs[currentGraph].draw(c);
		}		
	}

	public void drawSpring(Canvas c){
		lineStyle.setColor(Color.RED);
		lineStyle.setStrokeWidth(r.getDimensionPixelSize(R.dimen.graph_width));
		c.drawLines(spring, lineStyle);
	}


	@Override
	public void setInvisibleParams(int[] index) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void clearCurrentGraph() {
		graphs[currentGraph].clearGraph();
	}


	@Override
	public void showVector(ArrayList<Integer> id) {
		int l = id.size();
		vectorMask = new int[l];
		for(int i = 0; i < l; i++)
			vectorMask[i] = id.get(i);
	}


	@Override
	public void touchEditor(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();
		if(editMode == 0){
			if(x > xRoot - 20 && x < xRoot + 20 && y > yRoot - 20 && y < yRoot + 20)
				editMode = 1;
			if(x > blance - 20 && x < blance + 20 && y > yRoot - 20 && y < yRoot + 20){
				editMode = 2;
				resetGraphic();
			}
			if(x > ball.xCoordinate - 20 && x < ball.xCoordinate + 20 && y > yRoot - 20 && y < yRoot + 20){
				editMode = 3;
				resetGraphic();
			}
			if(x > tablex - 20 && x < tablex + 20 && y > tabley - 20 && y < tabley + 20)
				editMode = 4;
			logger.logEvent("*", "touch", name, "*");
		}else{
			switch (editMode) {
			case 1:
				float deltax = x - xRoot;
				startParams[6] = blance = blance + deltax;
				ball.xCoordinate = ball.xCoordinate + deltax;
				startParams[4] = xRoot = x;
				startParams[5] = yRoot = y;
				createSpring();
				calculate();
				break;
			case 2:
				startParams[6] = blance = x;
				calculateParams[1] = 0;
				calculate();
				break;
			case 3:
				ball.xCoordinate = x;
				maxA = ball.xCoordinate - blance;
				startParams[3] = Math.abs(maxA);
				calculateParams[0] = omega = (float) Math.sqrt(startParams[0] / startParams[1]);
				calculateParams[1] = 0;
				calculateParams[2] = 2 * pi / calculateParams[0];
				calculate();
				break;
			case 4:
				tablex = x;
				tabley = y;
			default:
				break;
			}
		}
	}

	@Override
	public void resetGraphic(){
		for(int i = 0; i < graphs.length; i++)
			graphs[i] = new Graph(r, 50, 600, r.getString(R.string.time), graphNames[i], 750, 550);
	}

	@Override
	public void clearAllGraph() {
		for(int i = 0; i < graphs.length; i++){
			graphs[i].clearGraph();
		}
	}


	@Override
	protected void drawCalculateParams(Canvas c) {
		int lenght = calculateParams.length;
		for(int i = 0; i < lenght; i++){
			c.drawText("" + calculateParamStrings[i] + ": " + calculateParams[i], tablex, tabley + (20 * i), textPaint);
		}
	}
}
