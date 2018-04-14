package uPhysic.logic;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class MultipleGraph {
	//1s = 50f
	//1m = 50f
	private float xCoordinate;
	private float yCoordinate;
	private float xRoot;
	private float yRoot;
	private float maxWidth;
	private float maxHeight;
	private float xState = 10;// -1,0,+1
	private float yState = 10;// -3,0,+3
	private float state;
	private int position;
	private float mask1[] = new float[]{4, -2, 2, -4, 3, -3, 1, -1, 0};
	private float mask2[] = new float[]{0, 0, 0, -1, 1, 0, 1, -1, 0.5f, 0, 0.5f, -1, 0, -0.5f, 1, -0.5f, 0.5f, -0.5f};
	private float xRate;
	private float yRate;
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
	private ArrayList<ArrayList<Float>> points;
	private float[] units;
	private boolean b = false;
	private int numberGraph;
	private int color[];
	private int rate;
	
	
	public MultipleGraph(float x, float y, String lx, String ly, float mw, float mh, float xr, float yr, int n,int rate,int color[]) {
		xCoordinate = x;
		yCoordinate = y;
		numberGraph = n;
		labelx = lx;
		labely = ly;
		maxHeight = mh;
		maxWidth = mw;
		xRate = xr;
		yRate = yr;
		xZoomLevel = 1;
		yZoomLevel = 1;
		unitx = mw / 8;
		unity = mh / 8;
		xl = new float[8];
		yl = new float[8];
		pGrap.setColor(Color.RED);
		pGrap.setStrokeWidth(2);
		pC.setColor(Color.BLUE);
		pC.setStyle(Style.STROKE);
		pC.setStrokeWidth(2);
		pLabel.setColor(Color.RED);
		pLabel.setTextSize(12);
		pBorder.setColor(Color.BLUE);
		pBorder.setStyle(Style.STROKE);
		points = new ArrayList<ArrayList<Float>>();
		for(int i = 0; i < n; i++){
			ArrayList<Float> p = new ArrayList<Float>();
			p.add(0f);
			p.add(0f);
			points.add(p);
		}
		state = 0;
		units = new float[64];
		calculateRoot();
		this.color = color;
		this.rate = rate;
	}
	
	private float[] convertArray(ArrayList<Float> point){
		int size = point.size();
		float[] p = new float[size - 6];
		for(int i = 4; i < size - 2 ; i++){
			p[i - 4] = point.get(i);
		}
		return p;
	}
		
	public void calculate(float x, float y[]){
		float yc[] = new float[y.length];
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
		for(int i = 0; i < y.length; i++){
			if(yState != 0)
					if(y[i] < 0){
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
		}
		if(state != xState + yState){
			state = xState + yState;
			position = 0;
			while(state != mask1[position])
				position++;
			calculateRoot();
		}
		for(int i = 0; i < numberGraph; i++){
			yc[i] = toyCoordinate(y[i]);
		}
		float xc = toxCoordinate(x);
		for(int i = 0; i < numberGraph; i++){
			points.get(i).add(xc);
			points.get(i).add(yc[i]);
			points.get(i).add(xc);
			points.get(i).add(yc[i]);
		}
		for(int i = 0; i < numberGraph; i++){
			ArrayList<Float> p = points.get(i);
			int j = p.size() - 1;
			if(p.get(j) < yCoordinate - maxHeight || p.get(j) > yCoordinate)
				zoomOutY();
		}
		if(xc > xCoordinate + maxWidth || xc < xCoordinate)
			zoomOutX();
		b = true;
	}
	
	public void draw(Canvas c){
		
		c.drawLine(xRoot, yRoot, xRoot, yCoordinate - maxHeight, pC);
		c.drawLine(xRoot, yRoot, xRoot, yCoordinate, pC);
		c.drawLine(xRoot, yRoot, xCoordinate + maxWidth, yRoot, pC);
		c.drawLine(xRoot, yRoot, xCoordinate, yRoot, pC);
		
		c.drawText(labelx, xCoordinate + maxWidth, yRoot - 10, pLabel);
		c.drawText(labely, xRoot, yCoordinate - maxHeight - 10, pLabel);
		c.drawText("" + 0, xRoot + 3, yRoot - 3, pLabel);
		for(int i = 0; i < yl.length; i++){
			c.drawText(yl[i] + "", xRoot - 30, units[i * 4 + 1], pLabel);
		}
		for(int i = 0; i < xl.length; i++){
			c.drawText(xl[i] + "", units[i * 4 + 32] - 10, yRoot + 13, pLabel);
		}
		c.drawLines(units, pC);
		c.drawCircle(xRoot, yRoot, 5, pGrap);
		c.drawRect(xCoordinate, yCoordinate - maxHeight, xCoordinate + maxWidth, yCoordinate, pBorder);
		if(b){
			for(int i = 0; i < numberGraph; i++){
				pGrap.setColor(color[i]);
				c.drawLines(convertArray(points.get(i)), pGrap);
			}
		}
	}
	
	
	private float toxCoordinate(float x){
		return x / (xRate * xZoomLevel) + xRoot;
	}
	private float toyCoordinate(float y){
		return  yRoot - y / (yRate * yZoomLevel);
	}
	
	
	private void calculateRoot(){
		float x = xRoot;
		float y = yRoot;
		xRoot = xCoordinate + maxWidth * mask2[position * 2];
		yRoot = yCoordinate + maxHeight *mask2[position * 2 + 1];
		x = xRoot - x;
		y = yRoot - y;
		
		for(int j = 0; j < numberGraph; j++){
			ArrayList<Float> p = points.get(j);
			int size = p.size();
			for(int i = 4; i < size; i = i + 2){
				p.set(i, p.get(i) + x);
				p.set(i + 1, p.get(i + 1) + y);
			}
		}
		float s = yRoot - yCoordinate + maxHeight;
		int i = 0;
		int j = 0;
		for(float l = unity; l <= s; l = l + unity, i = i + 4){
			units[i] = xRoot + 3;
			units[i + 1] = yRoot - l;
			units[i + 2] = xRoot - 3;
			units[i + 3] = yRoot - l;
			yl[j] = yZoomLevel * l / rate;
			j++;
		}
		s = yCoordinate - yRoot;
		for(float l = unity; l <= s; l = l + unity, i = i + 4){
			units[i] = xRoot + 3;
			units[i + 1] = yRoot + l;
			units[i + 2] = xRoot - 3;
			units[i + 3] = yRoot + l;
			yl[j] = -yZoomLevel * l / rate;
			j++;
		}
		j = 0;
		s = xRoot - xCoordinate;
		for(float l = unitx; l <= s; l = l + unitx, i = i + 4){
			units[i] = xRoot - l;
			units[i + 1] = yRoot + 3;
			units[i + 2] = xRoot - l;
			units[i + 3] = yRoot - 3;
			xl[j] = - xZoomLevel * l / unitx;
			j ++;
		}
		s = xCoordinate + maxWidth - xRoot;
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
		xZoomLevel = xZoomLevel * 2;
		for(int j = 0; j < numberGraph; j++){
			ArrayList<Float> p = points.get(j);
			int size = p.size();
			for(int i = 0; i < size; i = i + 2)
				p.set(i, (p.get(i) - xRoot) / 2 + xRoot);
		}
		for(int i = 0; i < xl.length; i++)
			xl[i] = xl[i] * 2;
		
	}
	
	private void zoomOutY(){
		yZoomLevel = yZoomLevel * 2;
		for(int j = 0; j < numberGraph; j++){
			ArrayList<Float> p = points.get(j);
			int size = p.size();
			for(int i = 1; i < size; i = i + 2)
				p.set(i, (p.get(i) - yRoot) / 2 + yRoot);
		}
		for(int i = 0; i < yl.length; i++)
			yl[i] = yl[i] * 2;
	}
	
	
	public void drawLineReference(Canvas c, float x, float y, int color){
		Paint p = new Paint();
		p.setColor(color);
		c.drawLine(x, y, x, yCoordinate, p);
		c.drawLine(x, y, xCoordinate, y, p);
	}
	
	public void clearGraph(){
		points = new ArrayList<ArrayList<Float>>();
		for(int i = 0; i < numberGraph; i++){
			ArrayList<Float> p = new ArrayList<Float>();
			p.add(0f);
			p.add(0f);
			points.add(p);
		}
	}
}
