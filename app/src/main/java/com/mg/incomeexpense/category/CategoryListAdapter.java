package com.mg.incomeexpense.category;

import android.content.Context;
import android.database.Cursor;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.mg.incomeexpense.data.IncomeExpenseContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mario on 2016-08-05.
 */
public class CategoryListAdapter extends BaseExpandableListAdapter {

    private Category[] mCategories = null;

    public CategoryListAdapter(Context context){
        refresh(context);
    }

    private void refresh(Context context) {
        Cursor cursor = context.getContentResolver().query(IncomeExpenseContract.CategoryEntry.CONTENT_URI, null, null, null, null);

        List<Category> categories = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            categories.add( Category.create( cursor ));
        }
        mCategories = categories.toArray( new Category[categories.size()] );
    }

    @Override
    public int getGroupCount() {
        return mCategories.length;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mCategories[groupPosition].getSubCategories().length;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mCategories[groupPosition].getName();
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mCategories[groupPosition].getSubCategory(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        TextView textView = getGenericView(parent.getContext());
        textView.setText(getGroup(groupPosition).toString());
        return textView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        TextView textView = getGenericView(parent.getContext());
        textView.setText(getChild(groupPosition, childPosition).toString());
        return textView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public TextView getGenericView(Context context) {
        // Layout parameters for the ExpandableListView
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 64);

        TextView textView = new TextView(context);
        textView.setLayoutParams(lp);
        // Center the text vertically
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        // Set the text starting position
        textView.setPadding(60, 0, 0, 0);
        return textView;
    }
}
