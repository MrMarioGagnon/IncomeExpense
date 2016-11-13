package com.mg.incomeexpense.extension;

import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.mg.incomeexpense.core.ApplicationConstant;

/**
 * Created by mario on 2016-11-13.
 */

public class ExtensionDataExtractor {

    public static String extract(View view) {

        if (!(view instanceof ViewGroup) || null == view)
            return "";

        ViewGroup viewGroup = (ViewGroup) view;

        StringBuilder buffer = new StringBuilder();

        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            if (child instanceof ViewGroup) {
                String data = extract((ViewGroup) child);
                buffer.append(String.format("%1$s%2$s", buffer.length() != 0 ? ApplicationConstant.storageSeparator : "", data));
            } else if (null != child) {
                if (child instanceof EditText) {
                    String data = ((EditText) child).getText().toString();
                    buffer.append(String.format("%1$s%2$s", buffer.length() != 0 ? ApplicationConstant.storageSeparator : "", data));
                }
            }

        }

        return buffer.toString();
    }
}
