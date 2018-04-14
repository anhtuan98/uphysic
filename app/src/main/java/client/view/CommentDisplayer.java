package client.view;

import icetea.physiclab.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.widget.LinearLayout;
import android.widget.TextView;

@SuppressLint("NewApi")
public class CommentDisplayer extends LinearLayout{

	private String[] string;
	private TextView mark,comment,labelMark,labelComment;
	
	public CommentDisplayer(Context context,String data) {
		super(context);
		
		string = data.split("@-");
		
		mark = new TextView(context);
		comment = new TextView(context);
		labelMark = new TextView(context);
		labelComment = new TextView(context);
		
		mark.setText(""+string[0]);
		mark.setTextSize(20);
		mark.setBackgroundDrawable((context.getResources().getDrawable(R.drawable.border)));
		comment.setText(" "+string[1]);
		comment.setTextSize(20);
		comment.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.border));

		labelMark.setText("Số điểm đạt được: ");
		labelMark.setTextSize(20);
		labelMark.setShadowLayer(0.6f, 1, 1, Color.BLACK);
		labelComment.setText("Nhận xét của giáo viên: ");
		labelComment.setTextSize(20);
		labelComment.setShadowLayer(0.6f, 1, 1, Color.BLACK);




		LayoutParams wrap = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
	
		LinearLayout rowTop = new LinearLayout(context);
		LayoutParams lorowTop = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);

		
		LayoutParams lomark = new LayoutParams(0,120);
		lomark.weight = 6;
		mark.setLayoutParams(lomark);
		mark.setPadding(12, 0, 0, 0);
		LayoutParams lolabelMark = new LayoutParams(0,LayoutParams.MATCH_PARENT);
		lolabelMark.weight = 4;
		labelMark.setLayoutParams(lolabelMark);
		
		
		rowTop.addView(labelMark);
		rowTop.addView(mark);
		lorowTop.setMargins(20, 20, 20, 0);
		rowTop.setLayoutParams(lorowTop);
		rowTop.setOrientation(HORIZONTAL);
		
		
		LinearLayout rowBottom = new LinearLayout(context);
		LayoutParams lorowBottom = new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		
		LayoutParams locomment = new LayoutParams(0,120);
		locomment.weight = 6;
		comment.setLayoutParams(locomment);		
		comment.setPadding(12, 0, 0, 0);
		LayoutParams lolabelComment = new LayoutParams(0,LayoutParams.MATCH_PARENT);
		lolabelComment.weight = 4;
		labelComment.setLayoutParams(lolabelComment);
		
		rowBottom.addView(labelComment);
		rowBottom.addView(comment);
		lorowBottom.setMargins(20, 20, 20, 20);
		rowBottom.setLayoutParams(lorowBottom);
		//rowBottom.setBackground(context.getResources().getDrawable(R.drawable.border));
		rowBottom.setOrientation(HORIZONTAL);
		
		this.setLayoutParams(wrap);
		this.addView(rowTop);
		this.addView(rowBottom);
		this.setOrientation(VERTICAL);
	
	}
}
