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

public class Pendulum extends Laboratory {	
	private Ball ball;
	private Paint arcPaint;
	private Paint pointPaint;
	private float alpha;
	private float newx;
	private float newy;
	private Paint textPaint;
	private float tablex;
	private float tabley;
	private float accelerator;
	private float velocity;
	private Resources r;
	private Context context;
	
	public Pendulum(float params[], Context context){	
		name = "Pendulum";
		
		r = context.getResources();
		this.context = context;
		
		startParams = params;
		startParamStrings = new String[]{r.getString(R.string.x_root), r.getString(R.string.y_root), r.getString(R.string.line_lenght), r.getString(R.string.alpha_zero), r.getString(R.string.environment_accelerator)};
		startParamsUnits = new int[]{0, 0, 0, 6, 7};
		
		calculateParams = new float[3];
		calculateParamStrings = new String[]{r.getString(R.string.omega), r.getString(R.string.total_time), r.getString(R.string.period), r.getString(R.string.x_velocity), r.getString(R.string.y_velocity)};
		calculateParams[0] = (float) Math.sqrt(params[4] / params[2]);
		calculateParams[1] =  0;
		calculateParams[2] = 2 * pi / calculateParams[0];
		calculateParamsUnits = new int[]{8, 1, 1};
		
		ball = new Ball((float) (startParams[0] + startParams[2] * Math.sin(startParams[3])), (float) (startParams[1] + startParams[2] * Math.cos(startParams[3])), 10, Ball.BLACK, context);
		
		graphNames = new String[]{r.getString(R.string.velocity), r.getString(R.string.accelerator), r.getString(R.string.clear_graph), r.getString(R.string.hide_graph)};
		graphs = new Graph[2];
		for(int i = 0; i < 2; i++)
			graphs[i] = new Graph(r, 50, 600, r.getString(R.string.time), graphNames[i], 750, 550);
		
		vectors = new Vector[3];
		vectors[0] = new Vector(context, (pi / 2), startParams[4]);
		vectors[1] = new Vector(context);
		vectors[0].label = "g";
		vectors[1].label = "t";
		vectors[1].setValue((float) (startParams[4] * Math.cos(startParams[3])));
		vectors[1].setDirection(Vector.getDirection(ball.xCoordinate, ball.getyCoordinate(), startParams[0], startParams[1]));
		vectors[2] = Vector.totalVector(context, vectors[0], vectors[1]);
		vectors[2].label = "t + g";
		vectorMask = new int[0];
		vectorNames = new String[]{r.getString(R.string.environment_accelerator), r.getString(R.string.line), r.getString(R.string.tangential_accelerator)};
		
		
		arcPaint = new Paint();
		arcPaint.setColor(Color.RED);
		arcPaint.setStyle(Style.STROKE);
		arcPaint.setStrokeWidth(r.getDimensionPixelSize(R.dimen.graph_width));
		textPaint = new Paint();
		textPaint.setTextSize(r.getDimensionPixelSize(R.dimen.txt_normal));
		textPaint.setColor(Color.RED);
		pointPaint = new Paint();
		pointPaint.setColor(Color.YELLOW);
		
		tablex = 20;
		tabley = 20;
		
		/*
		rectf = new RectF(startParams[0] - startParams[2], startParams[1] - startParams[2], startParams[0] + startParams[2], startParams[1] + startParams[2]);
		sweepAngle = startParams[3] * (180 / pi) * 2;
		startAngle = 90 - startParams[3] * (180 / pi);
		arcPath.addArc(rectf, startAngle, sweepAngle);
		*/
	}
	

	public void calculate(){
		
		if(editMode == 0){
			calculateParams[1] = calculateParams[1] + timePerFrame;
		}
		
		alpha = (float) (startParams[3] * Math.cos(calculateParams[0] * calculateParams[1]));
		newx = (float) (startParams[0] + startParams[2] * Math.sin(alpha));
		newy = (float) (startParams[1] + startParams[2] * Math.cos(alpha));

		ball.setxCoordinate(newx);
		ball.setyCoordinate(newy);
		vectors[1] = vectors[0].perpendicularProjection(Vector.getDirection(startParams[0], startParams[1], newx, newy)).inverse();
		vectors[1].label = "t";
		vectors[2] = Vector.totalVector(context, vectors[0], vectors[1]);
		vectors[2].label = "t + g";
		
		if(editMode == 0){
			if(alpha > 0)
				accelerator = vectors[2].getValue();
			else
				accelerator = -vectors[2].getValue();
			velocity = velocity + accelerator * timePerFrame;
			graphs[0].calculate(calculateParams[1], velocity / 100);
			graphs[1].calculate(calculateParams[1], accelerator / 100);
		}
	}

	public void draw(Canvas c){
		if(!graphMode){
			//c.drawPath(arcPath, arcPaint);
			float x = ball.xCoordinate;
			float y = ball.getyCoordinate();
			c.drawLine(startParams[0], startParams[1], x, y, arcPaint);
			ball.draw(c);
            for (int aVectorMask : vectorMask) {
                vectors[aVectorMask].draw(c, x, y);
            }
			c.drawCircle(startParams[0], startParams[1], 5, pointPaint);
			drawCalculateParams(c);
		}
		else
			graphs[currentGraph].draw(c);
	}

	public void touchEditor(MotionEvent event){
		
		float x = event.getX();
		float y = event.getY();
		if(editMode == 0){
			if(x > startParams[0] - 20 && x < startParams[0] + 20 && y > startParams[1] - 20 && y < startParams[1] + 20)
				editMode = 1;
			if(x > ball.xCoordinate - 20 && x < ball.xCoordinate + 20 && y > ball.yCoordinate - 20 && y < ball.yCoordinate + 20){
				editMode = 2;
				resetGraphic();
			}
			if(x > tablex - 20 && x < tablex + 20 && y > tabley - 20 && y < tabley + 20)
				editMode = 3;
			logger.logEvent("*", "touch", name, "*");
		}
		else {
			switch (editMode) {
			case 1:
				startParams[0] = x;
				startParams[1] = y;
				calculate();
				break;
			case 2:
				startParams[2] = Vector.getValue(startParams[0], startParams[1], x, y);
				float a = Vector.getDirection(startParams[0], startParams[1], x, y);
				if(a >= 0 && a <= pi * 3 / 2)
					startParams[3] = pi / 2 - a;
				else
					startParams[3] = 2 * pi - (a - pi / 2);
				calculateParams[0] = (float) Math.sqrt(startParams[4] / startParams[2]);
				calculateParams[1] =  0;
				calculateParams[2] = 2 * pi / calculateParams[0];
				calculate();
				break;
			case 3:
				tablex = x;
				tabley = y;
				break;
			default:
				break;
			}
		}
		
	}
	
	public void clearCurrentGraph(){
		graphs[currentGraph].clearGraph();
	}
	
	public void clearAllGraph(){
		for(int i = 0; i < graphs.length; i++){
			graphs[i].clearGraph();
		}
	}

	@Override
	public void setInvisibleParams(int[] index) {
		
	}


	@Override
	public void showVector(ArrayList<Integer> id) {
		int l = id.size();
		vectorMask = new int[l];
		for(int i = 0; i < l; i++)
			vectorMask[i] = id.get(i);

	}


	@Override
	protected void drawCalculateParams(Canvas c) {
		int lenght = calculateParams.length;
		for(int i = 0; i < lenght; i++){
			c.drawText("" + calculateParamStrings[i] + ": " + calculateParams[i], tablex, tabley + (20 * i), textPaint);
		}
	}


	@Override
	public void resetGraphic() {
		for(int i = 0; i < graphs.length; i++)
			graphs[i] = new Graph(r, 50, 600, r.getString(R.string.time), graphNames[i], 750, 550);
	}
}