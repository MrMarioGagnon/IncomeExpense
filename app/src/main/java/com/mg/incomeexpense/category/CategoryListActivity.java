package com.mg.incomeexpense.category;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListAdapter;
import android.widget.Toast;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.account.AccountEditorActivity;
import com.mg.incomeexpense.core.ItemSelectedEvent;
import com.mg.incomeexpense.core.ItemSelectedHandler;
import com.mg.incomeexpense.core.ItemSelectedListener;
import com.mg.incomeexpense.core.dialog.DialogUtils;
import com.mg.incomeexpense.core.dialog.SingleChoiceEventHandler;
import com.mg.incomeexpense.data.IncomeExpenseContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mario on 2016-07-19.
 */
public class CategoryListActivity extends AppCompatActivity implements ItemSelectedListener {

    private static final String LOG_TAG = CategoryListActivity.class.getSimpleName();
    private static final int EDITOR_ACTIVITY_ADD = 1;
    private static final int EDITOR_ACTIVITY_UPDATE = 2;
    private ExpandableListAdapter mAdapter;

    private final OnClickListener mFabAddOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            Category category = Category.createNew();

            showCategoryEditor(category);
        }
    };

    private final OnClickListener mFabEditOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            showAvailableCategory();
        }
    };

    private SingleChoiceEventHandler mCategoryClicked = new SingleChoiceEventHandler() {

        @Override
        public void execute(int idSelected) {

            Category[] availableCategory = ((CategoryListAdapter) mAdapter).getCategories();

            Category selectedCategory = availableCategory[idSelected];

            showCategoryEditor(selectedCategory);

        }

    };

    private void showCategoryEditor(Category category) {

        Intent i = new Intent(this, CategoryEditorActivity.class);

            Bundle bundle = new Bundle();
            bundle.putSerializable("item", category);
            i.putExtras(bundle);

        startActivityForResult(i, category.isNew() ?  EDITOR_ACTIVITY_ADD : EDITOR_ACTIVITY_UPDATE);
    }

    private void showAvailableCategory() {

        String[] availableCategories = new String[mAdapter.getGroupCount()];
        for(int i = 0; i < availableCategories.length; i++){
            availableCategories[i] = (String) mAdapter.getGroup(i);
        }

        AlertDialog ad = DialogUtils.availableCategoryDialog(this,
                availableCategories, mCategoryClicked);

        ad.setOwnerActivity(this);
        ad.show();

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_list_activity);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAdd);
        if (fab != null) {
            fab.setOnClickListener(mFabAddOnClickListener);
        }

        fab = (FloatingActionButton) findViewById(R.id.fabEdit);
        if (fab != null) {
            fab.setOnClickListener(mFabEditOnClickListener);
        }

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.category_list_fragment_container);

//        ItemSelectedHandler f = (ItemSelectedHandler) fragment;
//        if(null != f){
//            f.addListener(this);
//        }

        mAdapter = ((CategoryListFragment)fragment).getAdapter();

    }

    @Override
    public void onItemSelected(ItemSelectedEvent event) {

//        Intent intent = new Intent(this, AccountEditorActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putSerializable("item", event.getItem());
//        intent.putExtras(bundle);
//
//        startActivityForResult(intent,EDITOR_ACTIVITY_UPDATE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

//        Bundle extras = null;
//        if(null != data)
//            extras = data.getExtras();
//
//        switch (requestCode){
//            case EDITOR_ACTIVITY_UPDATE:
//                if(resultCode == RESULT_OK){
//                    if(null != data){
//                        Account item =(Account) data.getSerializableExtra("item");
//                        Log.i(LOG_TAG, item.toString());
//                    }
//                }
//                break;
//            default:
//                break;
//        }
    }
}
