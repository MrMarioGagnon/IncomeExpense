package com.mg.incomeexpense.contributor;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
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

        View rootView = LayoutInflater.from(getContext()).inflate(R.layout.spinner_dropdown_item, parent, false);
        TextView textView = (TextView) rootView.findViewById(R.id.text_view_text);

        final Contributor contributor = this.getItem(position);
        textView.setText(contributor.getName());
        if (contributor.getId() < 0l) {
            textView.setTypeface(null, Typeface.BOLD);
        }

        return rootView;

    }

}
