package com.mg.incomeexpense.transaction;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mg.incomeexpense.R;

import java.text.DecimalFormat;

/**
 * Created by mario on 2016-07-19.
 */
public class TransactionListAdapter extends CursorAdapter {

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
        viewHolder.textViewCategory.setText(transaction.getCategory());
        viewHolder.textViewAmount.setText(transaction.getAmountAsString());

        int color = context.getResources().getColor(transaction.getType() == Transaction.TransactionType.Expense ? R.color.colorExpense : R.color.colorIncome, null);
        viewHolder.textViewAmount.setTextColor(color);
        viewHolder.textViewContributors.setText(transaction.getContributorsForDisplay());
    }

    public static class ViewHolder {
        public final TextView textViewDate;
        public final TextView textViewCategory;
        public final TextView textViewAmount;
        public final TextView textViewContributors;

        public ViewHolder(View view) {
            textViewDate = (TextView) view.findViewById(R.id.text_view_date);
            textViewCategory = (TextView) view.findViewById(R.id.text_view_category);
            textViewAmount = (TextView) view.findViewById(R.id.text_view_amount);
            textViewContributors = (TextView) view.findViewById(R.id.textView_contributors);
        }
    }
}
