package com.mg.incomeexpense.account;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.CursorAdapter;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mg.incomeexpense.R;

/**
 * Created by mario on 2016-07-19.
 */
public class AccountListAdapter extends CursorAdapter {

    public AccountListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.account_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        Account account = Account.create(cursor, context.getContentResolver());

        viewHolder.textViewName.setText(account.getName());
        viewHolder.textViewContributors.setText(account.getContributorsForDisplay());
        viewHolder.textViewPosition.setText(account.getPosition().toString());
        if (account.getIsClose()) {
            view.setBackgroundColor(Color.RED);
        } else {

            TypedValue a = new TypedValue();
            context.getTheme().resolveAttribute(android.R.attr.windowBackground, a, true);

            if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
                // windowBackground is a color
                int color = a.data;
                view.setBackgroundColor(color);
            } else {
                // TODO What to do if windowBackground is not a color, probably a drawable
                Drawable d = context.getResources().getDrawable(a.resourceId);
            }
        }
    }

    public static class ViewHolder {
        public final TextView textViewName;
        public final TextView textViewContributors;
        public final TextView textViewPosition;

        public ViewHolder(View view) {
            textViewName = (TextView) view.findViewById(R.id.text_view_account_name);
            textViewContributors = (TextView) view.findViewById(R.id.text_view_contributors);
            textViewPosition = (TextView) view.findViewById(R.id.text_view_position);
        }
    }
}
