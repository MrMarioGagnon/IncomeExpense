package com.mg.incomeexpense.dashboard;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mg.incomeexpense.R;

import java.util.List;

/**
 * Created by mario on 2016-08-24.
 */
public class DashboardSectionAdapter extends ArrayAdapter<DashboardPeriodAmount> {

    public DashboardSectionAdapter(Context context, List<DashboardPeriodAmount> objects) {
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

        DashboardPeriodAmount item = getItem(position);
        if (item != null) {
            viewHolder.textViewName.setText(item.getContributor().getName());
            viewHolder.textViewExpense.setText(item.getExpenseAsString());
            viewHolder.textViewIncome.setText(item.getIncomeAsString());

            viewHolder.textViewExpense.setVisibility(item.getContributor().getId() == 0L ? View.GONE : View.VISIBLE);
            viewHolder.textViewIncome.setVisibility(item.getContributor().getId() == 0L ? View.GONE : View.VISIBLE);
            viewHolder.textViewName.setTypeface(item.getContributor().getId() == 0L ? Typeface.DEFAULT_BOLD : Typeface.DEFAULT);

            if(item.getContributor().getId() == 999999L){
                // Grand Total
//                if(parent!=null){
//                    ViewGroup.MarginLayoutParams mlp = (ViewGroup.MarginLayoutParams)parent.getLayoutParams();
//                    mlp.setMargins(0,0,0,100);
//
//                    parent.setLayoutParams(mlp);
//
//                }
//                LinearLayout.LayoutParams params = ((LinearLayout.LayoutParams)viewHolder.textViewName.getParent()).getLayoutP
//                params.setMargins(0,0,0,100);
//                viewHolder.textViewName.setLayoutParams(params);
            }
        }

        return convertView;
    }

    private static class ViewHolder {
        private TextView textViewName;
        private TextView textViewExpense;
        private TextView textViewIncome;
    }
}
