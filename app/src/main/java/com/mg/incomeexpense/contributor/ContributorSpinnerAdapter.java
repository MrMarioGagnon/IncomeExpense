package com.mg.incomeexpense.contributor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by mario on 2016-08-14.
 */
public class ContributorSpinnerAdapter extends ArrayAdapter<Contributor> {

    private List<Contributor> mContributors;

    public ContributorSpinnerAdapter(@NonNull Context context, int resource, @NonNull List<Contributor> objects) {
        super(context, resource, objects);

        if (null == context)
            throw new NullPointerException("Parameter context of type parameter is mandatory");

        if (null == objects)
            throw new NullPointerException("Parameter objects of type List<Contributor> is mandatory");

        mContributors = objects;
    }

    public int getCount() {
        return mContributors.size();
    }

    public Contributor getItem(int position) {
        return mContributors.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        TextView label = new TextView(this.getContext());
        label.setText(mContributors.get(position).getName());
        return label;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = new TextView(this.getContext());
        label.setText(mContributors.get(position).getName());

        return label;
    }

}
