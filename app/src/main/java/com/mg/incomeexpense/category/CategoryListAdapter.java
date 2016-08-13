package com.mg.incomeexpense.category;

import android.app.Activity;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.data.IncomeExpenseContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mario on 2016-08-05.
 */
public class CategoryListAdapter extends BaseExpandableListAdapter {

    private final LayoutInflater inf;
    private Category[] mCategories = null;

    public CategoryListAdapter(Activity activity){
        inf = LayoutInflater.from(activity);

        refresh(activity);
    }

    public void refresh(Activity activity){
        Cursor cursor = activity.getContentResolver().query(IncomeExpenseContract.CategoryEntry.CONTENT_URI, null, null, null, null);

        List<Category> categories = new ArrayList<>();
        for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
            categories.add( Category.create( cursor ));
        }
        mCategories = new Category[categories.size()];
        categories.toArray( mCategories );

    }

    public Category[] getCategories(){
        return mCategories;
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
        ViewHolder holder;

        if (convertView == null) {
            convertView = inf.inflate(R.layout.category_list_header_fragment, parent, false);

            holder = new ViewHolder();
            holder.text = (TextView) convertView.findViewById(R.id.lblListHeader);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText(getGroup(groupPosition).toString());

        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if(convertView == null){
            convertView = inf.inflate(R.layout.category_list_detail_fragment, parent, false);
            holder = new ViewHolder();

            holder.text = (TextView) convertView.findViewById(R.id.lblListItem);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        holder.text.setText( getChild(groupPosition, childPosition).toString()  );

        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

//    public TextView getGenericView(Context context) {
//        // Layout parameters for the ExpandableListView
//        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
//                ViewGroup.LayoutParams.MATCH_PARENT, 64);
//
//        TextView textView = new TextView(context);
//        textView.setLayoutParams(lp);
//        // Center the text vertically
//        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
//        // Set the text starting position
//        textView.setPadding(60, 0, 0, 0);
//        return textView;
//    }

    private class ViewHolder {
        TextView text;
    }
}
