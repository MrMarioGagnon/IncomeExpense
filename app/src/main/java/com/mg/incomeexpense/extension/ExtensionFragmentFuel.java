package com.mg.incomeexpense.extension;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.ApplicationConstant;

import java.util.Objects;

/**
 * Created by mario on 2016-11-12.
 */

public class ExtensionFragmentFuel extends Fragment {

    private String mData;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        Objects.requireNonNull(bundle, "A bundle is mandatory");

        mData = bundle.getString("data");
        if (null == mData)
            mData = "";

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        String[] dataParts = mData.split(ApplicationConstant.storageSeparator);

        LinearLayout linearLayoutMain = new LinearLayout(getActivity());
        linearLayoutMain.setOrientation(LinearLayout.VERTICAL);

        LinearLayout linearLayoutItem;
        TextView textViewQty;
        TextView textViewCost;
        EditText editTextQty;
        EditText editTextCost;
        for (int i = 0; i < 2; i++) {
            linearLayoutItem = new LinearLayout(getActivity());
            linearLayoutItem.setOrientation(LinearLayout.HORIZONTAL);

            switch (i) {
                case 0:

                    textViewQty = new TextView(getContext());
                    textViewQty.setText(getString(R.string.label_qty_liters));
                    textViewQty.setTypeface(null, Typeface.BOLD);

                    editTextQty = new EditText(getContext());
                    editTextQty.setEms(6);
                    editTextQty.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    if (dataParts.length > 0) {
                        editTextQty.setText(dataParts[0]);
                    }

                    linearLayoutItem.addView(textViewQty);
                    linearLayoutItem.addView(editTextQty);

                    break;

                case 1:

                    textViewCost = new TextView(getContext());
                    textViewCost.setText(getString(R.string.label_cost_liters));
                    textViewCost.setTypeface(null, Typeface.BOLD);

                    editTextCost = new EditText(getContext());
                    editTextCost.setEms(4);
                    editTextCost.setInputType(InputType.TYPE_CLASS_NUMBER| InputType.TYPE_NUMBER_FLAG_DECIMAL);
                    if (dataParts.length > 1) {
                        editTextCost.setText(dataParts[1]);
                    }

                    linearLayoutItem.addView(textViewCost);
                    linearLayoutItem.addView(editTextCost);
                    break;

            }
            linearLayoutMain.addView(linearLayoutItem);
        }

        return linearLayoutMain;
    }
}
