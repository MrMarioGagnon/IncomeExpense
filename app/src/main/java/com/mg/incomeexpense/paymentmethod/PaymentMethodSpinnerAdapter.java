package com.mg.incomeexpense.paymentmethod;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import java.util.Objects;

/**
 * Created by mario on 2016-08-14.
 */
public class PaymentMethodSpinnerAdapter extends ArrayAdapter<PaymentMethod> {

    private List<PaymentMethod> mPaymentMethods;

    public PaymentMethodSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<PaymentMethod> objects) {
        super(context, resource, objects);

        Objects.requireNonNull(context, "Parameter context of type parameter is mandatory");
        Objects.requireNonNull(objects, "Parameter objects of type List<PaymentMethod> is mandatory");

        mPaymentMethods = objects;
    }

    public int getCount() {
        return mPaymentMethods.size();
    }

    public PaymentMethod getItem(int position) {
        return mPaymentMethods.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(getContext());
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(mPaymentMethods.get(position).getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {



        TextView label = new TextView(getContext());
        label.setText(mPaymentMethods.get(position).getName());

        return label;
    }

}
