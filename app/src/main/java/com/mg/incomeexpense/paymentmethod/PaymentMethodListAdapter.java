package com.mg.incomeexpense.paymentmethod;

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
import com.mg.incomeexpense.account.Account;

import java.text.DecimalFormat;

/**
 * Created by mario on 2016-07-19.
 */
public class PaymentMethodListAdapter extends CursorAdapter {

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
        viewHolder.textViewExchangeRate.setText(paymentMethod.getExchangeRateAsString());
        if(paymentMethod.getIsClose()){
            view.setBackgroundColor(Color.RED);
        }else{
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
