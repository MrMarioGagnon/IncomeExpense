package com.mg.incomeexpense.transaction;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.account.Account;

/**
 * Created by mario on 2016-07-19.
 */
public class TransactionListAdapter extends CursorAdapter {

    private static final String LOG_TAG = TransactionListAdapter.class.getSimpleName();

    public TransactionListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.transaction_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        Transaction transaction = Transaction.create(cursor, context.getContentResolver());

        viewHolder.textViewDate.setText(transaction.getDate());
        viewHolder.textViewAccount.setText(transaction.getAccount().getName());
        viewHolder.textViewType.setText(transaction.getType().toString() );
    }

    public static class ViewHolder {
        public final TextView textViewDate;
        public final TextView textViewAccount;
        public final TextView textViewType;

        public ViewHolder(View view) {
            textViewDate = (TextView) view.findViewById(R.id.text_view_date);
            textViewAccount = (TextView) view.findViewById(R.id.text_view_account);
            textViewType = (TextView) view.findViewById(R.id.text_view_type);
        }
    }
}
