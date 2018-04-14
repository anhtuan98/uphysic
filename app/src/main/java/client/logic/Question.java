/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package client.logic;

/**
 *
 * @author Ieatea
 */
public class Question {
    
    public static int SINGLE_CHOICE = 0;
    public static int MULTIPLE_CHOICE = 1; 
    
    public static final String[] QUESTION_TYPE = new String[]{"Chọn một đáp án", "Chọn nhiều đáp án"};
    
    private int numberQuestion;
    private int type;
    private String question;
    private String[] options;
    private int[] result;
    private int answer = -1;
    
    public Question(int t, String q, String[] o, int[] r, int n){
        type = t;
        question = q;
        options = o;
        result = r;
        numberQuestion = n;
    }
    
    public int getType(){
        return type;
    }
    
    public String getQuestion(){
        return question;
    }
    
    public String[] getOptions(){
        return options;
    }
    
    public int[] getResult(){
        return result;
    }
    public int getNumberQuestion(){
        return numberQuestion;
    }
    
    public void setAnswer(int n){
    	this.answer = n;
    }
    
    public int getAnswer(){
    	return this.answer;
    }
    
    public int getTotalOption(){
    	return options.length;
    }
}
