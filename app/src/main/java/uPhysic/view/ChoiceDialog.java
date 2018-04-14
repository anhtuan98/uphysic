package uPhysic.view;

import icetea.physiclab.R;

import java.util.ArrayList;

import uPhysic.logic.Laboratory;

import android.app.AlertDialog;
import android.app.Dialog;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.DialogInterface.OnMultiChoiceClickListener;

public class ChoiceDialog extends DialogFragment implements OnClickListener, OnMultiChoiceClickListener{
	
	private String[] items;
	private Laboratory lab;
	private int choiceType;
	private ArrayList<Integer> checkedItems;
	private int[] currentChecked;
	
	static ChoiceDialog newInstance(String[] i, int choiceType, int[] currentChecked){
		ChoiceDialog lg = new ChoiceDialog();
		
		Bundle args = new Bundle();
		args.putCharSequenceArray("items", i);
		args.putInt("choiceType", choiceType);
		args.putIntArray("currentChecked", currentChecked);
		lg.setArguments(args);
		return lg;
	}

	public Dialog onCreateDialog(Bundle saveInstanceState){
		lab = ((PhysicsLab)getActivity()).getEnvironment().getLab();
		items = getArguments().getStringArray("items");  
		choiceType = getArguments().getInt("choiceType");
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		if(choiceType == 1){
			builder.setTitle(R.string.choice_graph_title);
			builder.setItems(items, this);
		}
		else{
			builder.setTitle(R.string.choice_vector_title);
			checkedItems = new ArrayList<Integer>();
			currentChecked = getArguments().getIntArray("currentChecked");
			boolean[] maskChecked = new boolean[items.length];
			for(int i = 0; i < items.length; i++){
				maskChecked[i] = false;
			}
			if(currentChecked != null){
				for(int i = 0; i < currentChecked.length; i++){
					checkedItems.add(currentChecked[i]);
					maskChecked[currentChecked[i]] = true;
				}
			}
			builder.setMultiChoiceItems(items, maskChecked, this);
			builder.setPositiveButton(getResources().getString(R.string.btn_ok), new OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					lab.showVector(checkedItems);
					((PhysicsLab)getActivity()).getEnvironment().postInvalidate();
				}
			});
			builder.setNegativeButton(getResources().getString(R.string.btn_cancel), new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// TODO Auto-generated method stub
					
				}
			} );
		}
		return builder.create();
	}

	@Override
	public void onClick(DialogInterface arg0, int arg1) {
		if(choiceType == 1){
			if(arg1 == items.length - 1)
				lab.graphMode = false;
			else if(arg1 == items.length - 2){
				lab.clearCurrentGraph();
			}
			else{	
				lab.currentGraph = arg1;
				lab.graphMode = true;
			}
			((PhysicsLab)getActivity()).getEnvironment().postInvalidate();
		}
	}

	@Override
	public void onClick(DialogInterface dialog, int which, boolean isChecked) {
		if(isChecked)
			checkedItems.add(which);
		else if(checkedItems.contains(which))
			checkedItems.remove(Integer.valueOf(which));
		
	}

}
