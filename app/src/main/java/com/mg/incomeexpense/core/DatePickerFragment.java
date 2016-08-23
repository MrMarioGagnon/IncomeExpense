package com.mg.incomeexpense.core;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by mario on 2016-08-22.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener  {

    private DatePickerDialog.OnDateSetListener mListener;

    public void setListener(DatePickerDialog.OnDateSetListener listener){
        mListener = listener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();

        if(bundle != null){

            mListener = (DatePickerDialog.OnDateSetListener)bundle.getSerializable("listener");

        }


    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

// Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {

        if(mListener != null){
            mListener.onDateSet(view, year, month, day);
        }

    }
}
