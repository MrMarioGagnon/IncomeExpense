package com.mg.incomeexpense.category;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ExpandableListAdapter;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.ItemSelectedEvent;
import com.mg.incomeexpense.core.ItemSelectedListener;
import com.mg.incomeexpense.core.dialog.DialogUtils;
import com.mg.incomeexpense.core.dialog.SingleChoiceEventHandler;

/**
 * Created by mario on 2016-07-19.
 */
public class CategoryListActivity extends AppCompatActivity implements ItemSelectedListener {

    private static final String LOG_TAG = CategoryListActivity.class.getSimpleName();
    private static final int EDITOR_ACTIVITY_ADD = 1;
    private static final int EDITOR_ACTIVITY_UPDATE = 2;
    private com.mg.floatingactionbutton.FloatingActionsMenu mFabMenu;
    private final OnClickListener mFabAddOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {

            mFabMenu.collapseImmediately();
            Category category = Category.createNew();

            showCategoryEditor(category);
        }
    };
    private ExpandableListAdapter mAdapter;
    private SingleChoiceEventHandler mCategoryClicked = new SingleChoiceEventHandler() {

        @Override
        public void execute(int idSelected) {

            Category[] availableCategory = ((CategoryListAdapter) mAdapter).getCategories();

            Category selectedCategory = availableCategory[idSelected];

            showCategoryEditor(selectedCategory);

        }

    };
    private final OnClickListener mFabEditOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            mFabMenu.collapseImmediately();
            showAvailableCategory();
        }
    };

    private void showCategoryEditor(Category category) {

        Intent i = new Intent(this, CategoryEditorActivity.class);

        Bundle bundle = new Bundle();
        bundle.putSerializable("item", category);
        i.putExtras(bundle);

        startActivityForResult(i, category.isNew() ? EDITOR_ACTIVITY_ADD : EDITOR_ACTIVITY_UPDATE);
    }

    private void showAvailableCategory() {

        String[] availableCategories = new String[mAdapter.getGroupCount()];
        for (int i = 0; i < availableCategories.length; i++) {
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


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Boolean hideHomeButton = bundle.getBoolean("hideHomeButton");
            if (hideHomeButton != null && hideHomeButton) {
                if (getSupportActionBar() != null) {
                    getSupportActionBar().setDisplayHomeAsUpEnabled(false);
                }
            }
        }

        com.mg.floatingactionbutton.FloatingActionButton fab = (com.mg.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabAdd);
        if (fab != null) {
            fab.setOnClickListener(mFabAddOnClickListener);
        }

        fab = (com.mg.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabEdit);
        if (fab != null) {
            fab.setOnClickListener(mFabEditOnClickListener);
        }

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.category_list_fragment_container);

        ((CategoryListFragment) fragment).addListener(this);

        mAdapter = ((CategoryListFragment) fragment).getAdapter();

        mFabMenu = (com.mg.floatingactionbutton.FloatingActionsMenu) findViewById(R.id.floating_action_menu_action);
    }

    @Override
    public void onItemSelected(ItemSelectedEvent event) {

        Intent intent = new Intent();
        intent.putExtra("item", event.getItem());
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        ((CategoryListAdapter) mAdapter).refresh(this);
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
