package com.mg.incomeexpense.category;

import android.app.AlertDialog;
import android.app.ExpandableListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.dialog.DialogUtils;

/**
 * Created by mario on 2016-08-06.
 */
public class CategoryListActivity extends ExpandableListActivity {

    private ExpandableListAdapter mAdapter;

    private final ExpandableListView.OnChildClickListener mChildCategoryClickListener;

    public CategoryListActivity(){

        mChildCategoryClickListener = new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View view,
                                        int groupPosition, int childPosition, long rowId) {
                String group = (String) parent.getExpandableListAdapter().getGroup(
                        groupPosition);
                String child = (String) parent.getExpandableListAdapter().getChild(
                        groupPosition, childPosition);

                String category = String.format("%s:%s", group, child);

                Bundle bundle = new Bundle();
                bundle.putString("category", category);

                Intent mIntent = new Intent();
                mIntent.putExtras(bundle);
                setResult(RESULT_OK, mIntent);
                finish();

                return false;
            }
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        try {
            // Set up our adapter
            mAdapter = new CategoryListAdapter(this);
            setListAdapter(mAdapter);
            registerForContextMenu(getExpandableListView());

            ExpandableListView lv = getExpandableListView();
            lv.setOnChildClickListener(mChildCategoryClickListener);

            this.setSelectedChild(2, 1, true);
  //      } catch (Exception ex) {
//            AlertDialog ad = DialogUtils.singleButtonMessageBox(this,
//                    getString(R.string.error_starting_activity),
//                    getString(R.string.activity_categories_list),
//                    mMessageBoxResponseHandler);
 //           ad.show();
        //}
    }

    @Override
    public void onGroupExpand(int groupPosition) {

        ExpandableListView elv = this.getExpandableListView();
        ExpandableListAdapter ela = this.getExpandableListAdapter();

        for (int i = 0; i < ela.getGroupCount(); i++) {
            if (i != groupPosition)
                elv.collapseGroup(i);
        }

        super.onGroupExpand(groupPosition);

    }

}
