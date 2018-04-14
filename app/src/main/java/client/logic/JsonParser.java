package client.logic;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {
	
	public Exercise exercise;
	
	 public void loadData(String data ){
	                try {
	                	 JSONObject jsonObject;
	                     try{
	                         jsonObject = new JSONObject(data.trim().substring(1));
	                     } catch(JSONException ex){
	                         jsonObject = new JSONObject(data);
	                     }
	                    JSONObject detn = jsonObject.getJSONObject("detn");
	                    String title = detn.getString("title");
	                    int time = detn.getInt("time");
	                    int t = detn.getInt("type");
	                    JSONArray jQuestions = detn.getJSONArray("questions");
	                    Question[] q = new Question[jQuestions.length()];
	                    for(int h = 0; h < jQuestions.length(); h++){
	                        int type = jQuestions.getJSONObject(h).getInt("type");
	                        String question = jQuestions.getJSONObject(h).getString("question");
	                        JSONArray jOptions = jQuestions.getJSONObject(h).getJSONArray("options");
	                        String[] options = new String[jOptions.length()];
	                        for(int k = 0; k < options.length; k++){
	                            options[k] = jOptions.getString(k);
	                        }
	                        JSONArray jResult = jQuestions.getJSONObject(h).getJSONArray("result");
	                        int[] result = new int[jResult.length()];
	                        for(int k = 0; k < result.length; k++){
	                            result[k] = jResult.getInt(k);
	                        }
	                        q[h] = new Question(type, question, options, result, h);
	                    }
	                    exercise = new Exercise(title, t, time, q);
	                } catch (JSONException ex) {
	                    ex.printStackTrace();
	                }
	        
	    }
}
