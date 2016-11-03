package com.mg.incomeexpense.dashboard;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.transaction.TransactionAmountTotal;

import java.text.DecimalFormat;
import java.util.List;

/**
 * Created by mario on 2016-08-24.
 */
public class DashboardSectionAdapter extends ArrayAdapter<TransactionAmountTotal> {

    public DashboardSectionAdapter(Context context, List<TransactionAmountTotal> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder;
        if (convertView == null) {

            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.transaction_dashboard_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textViewName = (TextView) convertView.findViewById(R.id.text_view_name);
            viewHolder.textViewExpense = (TextView) convertView.findViewById(R.id.text_view_expense);
            viewHolder.textViewIncome = (TextView) convertView.findViewById(R.id.text_view_income);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        TransactionAmountTotal item = getItem(position);
        if (item != null) {
            DecimalFormat df = new DecimalFormat("#.00");
            viewHolder.textViewName.setText(item.contributor.getName());
            viewHolder.textViewExpense.setText(df.format(item.expense));
            viewHolder.textViewIncome.setText(df.format(item.income));

            viewHolder.textViewExpense.setVisibility(item.contributor.getId() == 0L ? View.GONE : View.VISIBLE);
            viewHolder.textViewIncome.setVisibility(item.contributor.getId() == 0L ? View.GONE : View.VISIBLE);
            viewHolder.textViewName.setTypeface(item.contributor.getId() == 0L ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);

        }

        return convertView;
    }

    private static class ViewHolder {
        private TextView textViewName;
        private TextView textViewExpense;
        private TextView textViewIncome;
    }
}
