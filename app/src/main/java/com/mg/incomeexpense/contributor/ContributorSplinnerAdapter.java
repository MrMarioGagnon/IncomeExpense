package com.mg.incomeexpense.contributor;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mg.incomeexpense.R;

import java.util.List;

/**
 * Created by mario on 2017-01-17.
 */

public class ContributorSplinnerAdapter extends ArrayAdapter<Contributor> {

    public ContributorSplinnerAdapter(Context context, int resource, List<Contributor> objects) {
        super(context, resource, objects);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        final View rootView = super.getDropDownView(position, convertView, parent);

        final Contributor contributor = this.getItem(position);
        if (contributor.getId() < 0) {
            TextView textView = (TextView) rootView.findViewById(R.id.text_view_text);
            textView.setTextColor(getContext().getColor(R.color.colorAccent));
        }

        return rootView;

    }
}
