package com.mg.incomeexpense.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.ItemStateChangeEvent;
import com.mg.incomeexpense.core.ItemStateChangeListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by mario on 2016-07-19.
 */
public class CategoryEditorActivity extends AppCompatActivity implements ItemStateChangeListener {

    private static final String LOG_TAG = CategoryEditorActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.category_editor_activity);

        if (null == savedInstanceState) {

            Bundle bundle = getIntent().getExtras();
            if (null == bundle)
                throw new NullPointerException("A bundle with and Category item is mandatory");

            List<String> categories = (List<String>) bundle.getSerializable("item");
            if (null == categories)
                throw new NullPointerException("An list of category is mandatory");

            ActionBar actionBar = getSupportActionBar();

            if (actionBar != null) {
                actionBar.setTitle(R.string.title_category_editor_update);
            }

            CategoryEditorFragment fragment = new CategoryEditorFragment();
            fragment.addListener(this);
            fragment.setArguments(bundle);
            fragment.setRetainInstance(true);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.category_editor_container, fragment)
                    .commit();
        }

    }

    @Override
    public void onItemStateChange(ItemStateChangeEvent event) {

        if (event.isCancelled()) {
            setResult(RESULT_CANCELED);
        } else {
            Category category = (Category) event.getItem();
            if (null != category) {
                ArrayList<String> categories = new ArrayList<>(category.getCategories());
                Intent intent = new Intent();
                intent.putExtra("item", categories);
                setResult(RESULT_OK, intent);
            }

        }

        finish();
    }

}
