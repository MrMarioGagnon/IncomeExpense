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

public class ExtensionFragmentFood extends Fragment {

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
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayoutMain.setLayoutParams(lp);

        TextView textViewNote;
        EditText editTextNote;

        textViewNote = new TextView(getActivity());
        textViewNote.setText(getString(R.string.label_marchand));
        textViewNote.setTypeface(null, Typeface.BOLD);

        editTextNote = new EditText(getActivity());
        editTextNote.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        if (dataParts.length > 0) {
            editTextNote.setText(dataParts[0]);
        }
        linearLayoutMain.addView(textViewNote);
        linearLayoutMain.addView(editTextNote);

        return linearLayoutMain;
    }
}
