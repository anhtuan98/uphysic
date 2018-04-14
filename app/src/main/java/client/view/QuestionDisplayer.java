package client.view;

import logger.Logger;
import client.logic.Question;
import icetea.physiclab.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

@SuppressLint({ "ViewConstructor", "NewApi" })
public class QuestionDisplayer extends LinearLayout implements OnClickListener{

	private Question question;
	private TextView numberQuestion;
	private TextView question_title; 
	private RadioGroup radio_group;
	private RadioButton[] radio_button;
	private Logger logger;
	
	public QuestionDisplayer(Context context,Question q) {
		super(context);		
		logger = new Logger();
		question = q;
		numberQuestion = new TextView(context);
		question_title = new TextView(context);
		radio_group = new RadioGroup(context);
		radio_button = new RadioButton[q.getTotalOption()];
		for(int i =0 ; i < q.getTotalOption() ; i++){
			radio_button[i] = new RadioButton(context);
			radio_button[i].setId(i);
		}
		
		LayoutParams wrap = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		
		LinearLayout line_1 = new LinearLayout(context);
		LayoutParams loline_1 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		loline_1.setMargins(15, 15, 15, 15);
		line_1.setLayoutParams(loline_1);	
		line_1.setPadding(5, 5, 5, 5);
		line_1.setOrientation(HORIZONTAL);
		LayoutParams loNumberQuestion = new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
		LayoutParams loQuestion_title = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		numberQuestion.setLayoutParams(loNumberQuestion);
		int number = question.getNumberQuestion() + 1;
		numberQuestion.setText("Câu " + number + ": ");
		numberQuestion.setTextSize(20);
		//
		numberQuestion.setTextColor(Color.WHITE);
		question_title.setLayoutParams(loQuestion_title);
		question_title.setText(question.getQuestion());
		question_title.setTextSize(20);
		//
		question_title.setTextColor(Color.WHITE);
		line_1.addView(numberQuestion);
		line_1.addView(question_title);
		line_1.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.border));
		//line_1.setBackgroundColor(Color.parseColor("#9C9C9C"));
		
		
		LinearLayout line_2 = new LinearLayout(context);
		LayoutParams loline_2 = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		loline_2.setMargins(15, 15, 15, 15);
		line_1.setPadding(5, 5, 5, 5);
		line_2.setLayoutParams(loline_2);
		LayoutParams loRadioButton = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		String options[] = question.getOptions();
		for(int i = 0; i < q.getTotalOption() ; i++){
			if(question.getAnswer() == i ){
				radio_button[i].setChecked(true);
			}
			radio_button[i].setOnClickListener(this);
			radio_button[i].setText(options[i]);
			radio_button[i].setTextSize(18);			
			radio_group.addView(radio_button[i], loRadioButton);
		}
				
		line_2.addView(radio_group, loRadioButton);
		
		this.setOrientation(VERTICAL);
		this.addView(line_1);
		this.addView(line_2);
		this.setLayoutParams(wrap);	
		
	}
	@Override
	public void onClick(View v) {
		int answer = v.getId();
		question.setAnswer(answer);
		int q = question.getNumberQuestion() + 1;
		logger.logEvent("*", "choice answer option", "radio button", "question:" + q + "-answer:" + answer);
	}
	
	

}
