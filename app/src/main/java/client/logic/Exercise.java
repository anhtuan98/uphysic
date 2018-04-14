/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.logic;

/**
 *
 * @author Ieatea
 */
public class Exercise extends Object{
    private String title;
    private int time;
    private Question[] questions;
    private int type;
    public static final int TYPE_1 = 0;
    public static final int TYPE_2 = 1;
    public static String[] EXERCISE_PATH = new String[]{"loai1","loai2"};
    
    public Exercise(String t, int type, int time, Question[] q){
        title = t;
        this.type = type;
        this.time = time;
        questions = q;
    }
    
    public String getTitle(){
        return title;
    }
    public int getTime(){
        return time;
    }
    public Question[] getQuestions(){
        return questions;
    }

    @Override
    public String toString() {
        return title;
    }
    
    public String getStringType(){
        return Exercise.EXERCISE_PATH[type];
    }
}
