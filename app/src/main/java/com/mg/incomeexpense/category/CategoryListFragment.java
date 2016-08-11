package com.mg.incomeexpense.category;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.mg.incomeexpense.R;

/**
 * Created by mario on 2016-08-07.
 */
public class CategoryListFragment extends Fragment {
    private View rootView;
    private ExpandableListView lv;
    private ExpandableListAdapter mAdapter;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.category_list_fragment, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        lv = (ExpandableListView) view.findViewById(R.id.expListView);
        mAdapter = new CategoryListAdapter(getActivity());
        lv.setAdapter(mAdapter);
        lv.setGroupIndicator(null);

    }

    public ExpandableListAdapter getAdapter(){
        return mAdapter;
    }

}