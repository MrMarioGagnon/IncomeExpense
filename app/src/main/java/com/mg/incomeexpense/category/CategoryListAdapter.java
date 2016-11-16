package com.mg.incomeexpense.category;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mg.incomeexpense.R;

/**
 * Created by mario on 2016-07-19.
 */
public class CategoryListAdapter extends CursorAdapter {

    public CategoryListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.category_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        Category category = Category.create(cursor);

        viewHolder.textViewName.setText(category.getName());
        viewHolder.textViewType.setText(category.getType().toString());
    }

    public static class ViewHolder {
        public final TextView textViewName;
        public final TextView textViewType;

        public ViewHolder(View view) {
            textViewName = (TextView) view.findViewById(R.id.text_view_category_name);
            textViewType = (TextView) view.findViewById(R.id.text_view_category_extension_type);
        }
    }
}
