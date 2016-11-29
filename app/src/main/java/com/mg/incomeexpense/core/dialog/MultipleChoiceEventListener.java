package com.mg.incomeexpense.core.dialog;


import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.widget.ListView;

public class MultipleChoiceEventListener implements DialogInterface.OnClickListener {

	private MultipleChoiceEventHandler mHandler;

	public MultipleChoiceEventListener(MultipleChoiceEventHandler handler) {
		this.mHandler = handler;
	}

	@Override
	public void onClick(DialogInterface dialog, int which) {

		dialog.dismiss();
		
		AlertDialog ad = (AlertDialog) dialog;
		
		ListView lv = ad.getListView();
		
		boolean[] itemChecked = new boolean[lv.getCount()];
		for(int i = 0; i < lv.getCount(); i++){
			itemChecked[i] = lv.isItemChecked(i);
		}
		
		mHandler.execute(itemChecked);
	}

}
