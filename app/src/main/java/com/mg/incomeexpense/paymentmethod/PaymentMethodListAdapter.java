package com.mg.incomeexpense.paymentmethod;

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

import java.text.DecimalFormat;

/**
 * Created by mario on 2016-07-19.
 */
public class PaymentMethodListAdapter extends CursorAdapter {
    private static final String LOG_TAG = PaymentMethodListAdapter.class.getSimpleName();

    public PaymentMethodListAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.payment_method_list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        ViewHolder viewHolder = (ViewHolder) view.getTag();

        PaymentMethod paymentMethod = PaymentMethod.create(cursor, context.getContentResolver());

        viewHolder.textViewName.setText(paymentMethod.getName());
        viewHolder.textViewCurrency.setText(paymentMethod.getCurrency());
        viewHolder.textViewOwner.setText(paymentMethod.getOwner().getName());
        DecimalFormat df = new DecimalFormat("#.00");
        viewHolder.textViewExchangeRate.setText(df.format(paymentMethod.getExchangeRate()));
        if(paymentMethod.getIsClose()){
            view.setBackgroundColor(Color.RED);
        }else{
            // TODO Manage background color
        }
    }

    public static class ViewHolder {
        public final TextView textViewName;
        public final TextView textViewCurrency;
        public final TextView textViewOwner;
        public final TextView textViewExchangeRate;

        public ViewHolder(View view) {
            textViewName = (TextView) view.findViewById(R.id.textView_payment_method_name);
            textViewCurrency = (TextView) view.findViewById(R.id.text_view_currency);
            textViewOwner = (TextView) view.findViewById(R.id.text_view_owner);
            textViewExchangeRate = (TextView)view.findViewById(R.id.text_view_exchange_rate);
        }
    }
}
