package com.mg.incomeexpense.account;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.widget.CursorAdapter;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.contributor.ContributorListFragment;

/**
 * Created by mario on 2016-07-19.
 */
public class AccountListAdapter extends CursorAdapter {
    private static final String LOG_TAG = AccountListAdapter.class.getSimpleName();

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
        viewHolder.textViewCurrency.setText(account.getCurrency());
        viewHolder.textViewContributors.setText(account.getContributorsForDisplay());
        if(account.getIsClose()){
            view.setBackgroundColor(Color.RED);
        }else{
//            Log.i(LOG_TAG, "INNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNNN");
//            TypedValue a = new TypedValue();
//            context.getTheme().resolveAttribute(android.R.attr.windowBackground, a, true);
//            if (a.type >= TypedValue.TYPE_FIRST_COLOR_INT && a.type <= TypedValue.TYPE_LAST_COLOR_INT) {
//                // windowBackground is a color
//                int color = a.data;
//            } else {
//                // windowBackground is not a color, probably a drawable
//                Drawable d = context.getResources().getDrawable(a.resourceId);
//            }
        }
    }

    public static class ViewHolder {
        public final TextView textViewName;
        public final TextView textViewCurrency;
        public final TextView textViewContributors;

        public ViewHolder(View view) {
            textViewName = (TextView) view.findViewById(R.id.textView_account_name);
            textViewCurrency = (TextView) view.findViewById(R.id.textView_currency);
            textViewContributors = (TextView) view.findViewById(R.id.textView_contributors);
        }
    }
}
