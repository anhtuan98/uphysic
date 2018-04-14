package uPhysic.logic;


import java.util.ArrayList;

import logger.Logger;

import android.graphics.Canvas;

public abstract class Laboratory implements TouchableObject {
	
	public static final int PENDULUM = 0;
	public static final int SPRING = 1;
	public static final int COLLISION = 2;
	public static final int PROJECTILE = 3;
	
	public static final int METER = 0;
	public static final int SECOND = 1;
	public static final int METER_PER_SECOND = 2;
	public static final int JUN = 3;
	public static final int KILOGRAM = 4;
	public static final int NEWTON_PER_METER = 5;
	public static final int RADIAN = 6;
	public static final int METER_PER_SECOND_SECOND = 7;
	public static final int RADIAN_PER_SECOND = 8;
	public static final int TYPE_COLLSION = 9;
	public static final int DEGREE = 10;
	public static final int METER_PER_SECOND_POSITIVE = 11;
	
	public float timePerFrame = 0.02f;
	
	public boolean onPrepare = true;
	public static final String[] INPUT_MSG_ERROR = new String[]{
		"Unit m must in range (0 -> 1000)",
		"Unit s Must be a positive number",
		"Unit m/s must be smaller than 1000",
		".",
		"Unit kg must in range (0 -> 1000)",
		"Unit N/m must in range (0 -> 1000)",
		"Unit radian must in range (-2PI -> 2PI)",
		".",
		".",
		"Type of collision must be 0(elastic) or 1(soft)",
		"Angle must in range -90 -> 90",
		"Initial velocity must in range (0 -> 1000)"
	};
	
	/**
	 *  0 = m (1m = 100f)
	 *  1 = s (1s = 50f)
	 *  2 = m/s (1m/s = 100f)
	 *  3 = J
	 *  4 = kg
	 *  5 = N / m
	 *  6 = radian
	 */
	
	public String name;
	public int type;
	public float[] startParams;
	public float[] calculateParams;
	public String[] startParamStrings;
	public String[] calculateParamStrings;
	public int[] startParamsUnits;
	public int[] calculateParamsUnits;
	protected Vector[] vectors;
	public String[] vectorNames;
	public int[] vectorMask;
	protected MultipleGraph[] multipleGraphs;
	protected Graph[] graphs;
	public String[] graphNames;
	public int currentGraph = 0;
	public boolean graphMode = false;
	public int editMode = 0;
	public float pi = (float) Math.PI;
	public Logger logger = new Logger();
	
	public abstract void draw(Canvas c);
	protected abstract void drawCalculateParams(Canvas c);
	public abstract void calculate();
	public abstract void setInvisibleParams(int[] index);
	public abstract void clearCurrentGraph();
	public abstract void clearAllGraph();
	public abstract void showVector(ArrayList<Integer> id);
	public abstract void resetGraphic();
	
}
