package uPhysic.logic;

import icetea.physiclab.R;

import java.util.ArrayList;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;

public class Collision extends Laboratory implements TouchableObject {
	private boolean check = false;
	private boolean b = false;
	private Ball ball1,ball2;
	private int type = 0;
	private float distance = 0;
	private float time_collision = 0;
	private float position_collision = 0;
	private float E1,E2;
	private MultipleGraph mg;
	private Vector vector[];
	private Paint textPaint;
	private float tablex;
	private float tabley;
	private float x01 = 100f;
	private float y0 = 500f;
	private float x02 = 300f;
	private Context context;
	

	public Collision(float param[],Context context){
		Resources r = context.getResources();
		this.context = context;
		name = "Collision";
		startParams = param;
		startParamStrings = new String[] {r.getString(R.string.mass_ball_1),r.getString(R.string.v1_before_collsion),r.getString(R.string.mass_ball_2),r.getString(R.string.v2_before_collsion),r.getString(R.string.type_collision)};
		startParamsUnits = new int[] {4,2,4,2,9};
		mg = new MultipleGraph(50, 400, "Thời gian (s)","Động Năng (J)", 700, 350, 1 / 50f, 1 , 2,10000,new int[]{Color.RED,Color.DKGRAY});
		


		ball1 = new Ball(x01, y0, 30, Ball.BLACK, context);
		ball2 = new Ball(x02, y0, 30, Ball.BLACK, context);//500f
		ball1.setMass(startParams[0]);
		ball1.setxVelocity(startParams[1]);
		ball2.setMass(startParams[2]);
		ball2.setxVelocity(startParams[3]);		
		
		calculateParams = new float[3];
		calculateParamStrings = new String[] {r.getString(R.string.v1_after_collsion),r.getString(R.string.v2_after_collsion),r.getString(R.string.total_time)};
		calculateParams[2] = 0;
		calculateParamsUnits = new int[]{2,2,1}; 
		if(startParams[4] == 0){
			calculateParams[0] = (( ball1.xVelocity * (ball1.getMass() - ball2.getMass())) + (2 * ball2.getMass() * ball2.xVelocity)) / ( ball1.getMass() + ball2.getMass() );
			calculateParams[1] = (( ball2.xVelocity * (ball2.getMass() - ball1.getMass())) + (2 * ball1.getMass() * ball1.xVelocity)) / ( ball1.getMass() + ball2.getMass() );
		}
		else{
			calculateParams[0] = calculateParams[1] = (ball1.getMass() * ball1.xVelocity + ball2.getMass() * ball2.xVelocity) /( ball1.getMass() + ball2.getMass() );
		}
		

		
		
		
		calculateCollision();
		


		vectors = new Vector[1];
		vectorMask = new int[0];
		vectorNames = new String[]{"Vận tốc"};
		vectors[0] = new Vector(context, getDirection(ball1), Math.abs(ball1.xVelocity));
		vectors[0].label = "v1";
		
		vector = new Vector[1];
		vector[0] = new Vector(context, getDirection(ball2), Math.abs(ball2.xVelocity));
		vector[0].label = "v2";
		
		
		textPaint = new Paint();
		textPaint.setTextSize(r.getDimensionPixelSize(R.dimen.txt_normal));
		textPaint.setColor(Color.RED);
		
		tablex = 300;
		tabley = 200;
	}	
	
	public void calculateCollision(){
		distance = ball2.xCoordinate - ball1.xCoordinate - ( ball1.getRadius() + ball2.getRadius() );
		if(ball1.xVelocity * ball2.xVelocity < 0){
			if(ball1.xVelocity > 0 && distance >= 0){
				time_collision = distance / (Math.abs(ball1.xVelocity)+ Math.abs(ball2.xVelocity));
				position_collision = ball1.xCoordinate + ball1.xVelocity * time_collision + ball1.getRadius();
				type = 1;
			}
			else{
				time_collision = -10;
			}
		}
		else{
			if(ball1.xVelocity > 0){
				if(Math.abs(ball1.xVelocity) <= Math.abs(ball2.xVelocity)){
					time_collision = -10;
				}
				else{
					time_collision = distance / (Math.abs(ball1.xVelocity)- Math.abs(ball2.xVelocity));
					position_collision = ball1.xCoordinate + ball1.xVelocity * time_collision + ball1.getRadius();
					type = 2;
				}
			}
			else{
				if(Math.abs(ball2.xVelocity) <= Math.abs(ball1.xVelocity)){
					time_collision = -10;
				}
				else{
					time_collision = distance / (Math.abs(ball2.xVelocity)- Math.abs(ball1.xVelocity));
					position_collision = ball1.xCoordinate + ball1.xVelocity * time_collision + ball1.getRadius();
					type = 3;
				}
			}
		}
	}
	
	public void calculate(){
		switch (type){
		case 1:
			if(ball1.xCoordinate + ball1.getRadius() < position_collision && check == false){
				if(ball1.xCoordinate + ball1.xVelocity * timePerFrame + ball1.getRadius() > position_collision)
				{
					ball1.setxCoordinate(position_collision - ball1.xVelocity * timePerFrame - ball1.getRadius());
				}
				if(ball2.xCoordinate + ball2.xVelocity * timePerFrame  - ball2.getRadius() < position_collision){
					ball2.setxCoordinate(position_collision - ball2.xVelocity * timePerFrame + ball2.getRadius()) ;
				}
			}
			else{
				check = true;
				ball1.setxVelocity(calculateParams[0]);
				ball2.setxVelocity(calculateParams[1]);
			}
			break;
		case 2:
			if(ball1.xCoordinate + ball1.getRadius() < position_collision  && check == false){
				if(ball1.xCoordinate + ball1.xVelocity * timePerFrame + ball1.getRadius() > position_collision)
				{
					ball1.setxCoordinate(position_collision - ball1.xVelocity * timePerFrame - ball1.getRadius());
				}
				if(ball2.xCoordinate + ball2.xVelocity * timePerFrame  - ball2.getRadius() > position_collision){
					ball2.setxCoordinate(position_collision - ball2.xVelocity * timePerFrame + ball2.getRadius()) ;
				}
			}
			else{
				check = true;
				ball1.setxVelocity(calculateParams[0]);
				ball2.setxVelocity(calculateParams[1]);
			}
			break;
		case 3:
			if(ball1.xCoordinate + ball1.getRadius() > position_collision  && check == false){
				if(ball1.xCoordinate + ball1.xVelocity * timePerFrame  + ball1.getRadius() < position_collision)
					if(ball1.xCoordinate + ball1.xVelocity * timePerFrame + ball1.getRadius() < position_collision)
					{
						ball1.setxCoordinate(position_collision - ball1.xVelocity * timePerFrame - ball1.getRadius());
					}
					if(ball2.xCoordinate + ball2.xVelocity * timePerFrame  - ball2.getRadius() < position_collision){
						ball2.setxCoordinate(position_collision - ball2.xVelocity * timePerFrame + ball2.getRadius()) ;
					}
			}
			else{
				check = true;
				ball1.setxVelocity(calculateParams[0]);
				ball2.setxVelocity(calculateParams[1]);
			}
			break;
		}
		ball1.setxCoordinate(ball1.xCoordinate + ball1.xVelocity * timePerFrame);
		ball2.setxCoordinate(ball2.xCoordinate + ball2.xVelocity * timePerFrame);
		
		
		E1 = (float) (0.5 * ball1.getMass() * ball1.xVelocity * ball1.xVelocity);
		E2 = (float) (0.5 * ball2.getMass() * ball2.xVelocity * ball2.xVelocity);
		mg.calculate(calculateParams[2], new float[]{E1,E2});		
		b = true;
		
		vector[0] = new Vector(context, getDirection(ball2), Math.abs(ball2.xVelocity));
		vector[0].label = "v2";
		vectors[0] = new Vector(context, getDirection(ball1), Math.abs(ball1.xVelocity));
		vectors[0].label = "v1";
		calculateParams[2] = calculateParams[2] + timePerFrame;
	}
	
	public void draw(Canvas c){
		Paint linePaint = new Paint();
		linePaint.setStrokeWidth(context.getResources().getDimensionPixelSize(R.dimen.graph_width));
		linePaint.setColor(Color.RED);
		ball1.draw(c);
		ball2.draw(c);
		if(b){
			mg.draw(c);
		}
		
		int l = vectorMask.length;
		for (int aVectorMask : vectorMask) {
			vectors[aVectorMask].draw(c, ball1.xCoordinate, ball1.yCoordinate);
			vector[aVectorMask].draw(c, ball2.xCoordinate, ball2.yCoordinate);
		}
		drawCalculateParams(c);
	}
	

	@Override
	public void setInvisibleParams(int[] index) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void clearCurrentGraph() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void showVector(ArrayList<Integer> id) {
		int l = id.size();
		vectorMask = new int[l];
		for(int i = 0; i < l; i++)
			vectorMask[i] = id.get(i);

	}
	
	public float getDirection(Ball ball){
		if(ball.xVelocity > 0){
			return 0;
		}
		else{
			return pi;
		}
	}

	@Override
	protected void drawCalculateParams(Canvas c) {
		int length = calculateParams.length;
		for(int i = 0; i < length; i++){
			if(calculateParamsUnits[i] == 2)
				c.drawText("" + calculateParamStrings[i] + ": " + calculateParams[i] / 100f, tablex, tabley + (20 * i), textPaint);

			else
				c.drawText("" + calculateParamStrings[i] + ": " + calculateParams[i], tablex, tabley + (20 * i), textPaint);
		}
	}

	@Override
	public void clearAllGraph() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void touchEditor(MotionEvent event) {
		float x = event.getX();
		float y = event.getY();	
		if(editMode == 0){
			if(x > tablex - 20 && x < tablex + 20 && y > tabley - 20 && y < tabley + 20)
				editMode = 1;
			if(x > ball1.xCoordinate - 20 && x < ball1.yCoordinate + 20 && y > ball1.yCoordinate - 20 && y < ball1.yCoordinate + 20)
				editMode = 2;
			if(x> ball2.xCoordinate - 20 && x < ball2.yCoordinate + 20 && y > ball2.yCoordinate - 20 && y < ball2.yCoordinate + 20)
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

				break;
			default:
				break;
			}

		}
	}

	@Override
	public void resetGraphic() {
		// TODO Auto-generated method stub
		
	}
}
