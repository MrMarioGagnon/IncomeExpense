package com.mg.incomeexpense.contributor;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mg.incomeexpense.paymentmethod.PaymentMethod;

import java.util.List;

/**
 * Created by mario on 2016-08-14.
 */
public class ContributorSpinnerAdapter extends ArrayAdapter<Contributor> {

    private Context mContext;
    private List<Contributor> mContributors;

    public ContributorSpinnerAdapter(Context context, int resource, List<Contributor> objects) {
        super(context, resource, objects);

        mContext = context;
        mContributors = objects;
    }

    public int getCount(){
        return mContributors.size();
    }

    public Contributor getItem(int position){
        return mContributors.get(position);
    }

    public long getItemId(int position){
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        TextView label = new TextView(mContext);
        label.setTextColor(Color.BLACK);
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        label.setText(mContributors.get(position).getName());

        // And finally return your dynamic (or custom) view for each spinner item
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(mContext);
        label.setTextColor(Color.BLACK);
        label.setText(mContributors.get(position).getName());

        return label;
    }

}
