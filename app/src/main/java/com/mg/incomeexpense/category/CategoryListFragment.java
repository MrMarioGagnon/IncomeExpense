package com.mg.incomeexpense.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.ItemSelectedEvent;
import com.mg.incomeexpense.core.ItemSelectedHandler;
import com.mg.incomeexpense.core.ItemSelectedListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mario on 2016-08-07.
 */
public class CategoryListFragment extends Fragment implements ItemSelectedHandler {
    private View rootView;
    private ExpandableListView lv;
    private ExpandableListAdapter mAdapter;
    private final List<ItemSelectedListener> mListeners = new ArrayList<>();

    private ExpandableListView.OnChildClickListener mChildCategoryClickListener = new ExpandableListView.OnChildClickListener() {

        @Override
        public boolean onChildClick(ExpandableListView parent, View view,
                                    int groupPosition, int childPosition, long rowId) {

            Category category =  ((CategoryListAdapter)parent.getExpandableListAdapter()).getCategories()[groupPosition];


            String child = (String) parent.getExpandableListAdapter().getChild(
                    groupPosition, childPosition);
            category.setSelectedSubCategory(child);

            notifyListener( new ItemSelectedEvent(category) );

/*
            Intent mIntent = new Intent();
            mIntent.putExtras(bundle);
            setResult(RESULT_OK, mIntent);
            finish();
*/
            return false;
        }

    };


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

        lv.setOnChildClickListener(mChildCategoryClickListener);

    }

    public ExpandableListAdapter getAdapter(){
        return mAdapter;
    }

    @Override
    public void addListener(ItemSelectedListener listener) {
        if (null == listener)
            return;

        if (!mListeners.contains(listener)) {
            mListeners.add(listener);
        }

    }

    @Override
    public void notifyListener(ItemSelectedEvent event) {
        if (null == event)
            return;

        for (Object item : mListeners) {
            ((ItemSelectedListener) item).onItemSelected(event);
        }

    }
}