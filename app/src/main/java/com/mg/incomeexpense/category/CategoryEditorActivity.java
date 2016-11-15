package com.mg.incomeexpense.category;

import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.AppCompatActivityBase;
import com.mg.incomeexpense.core.ItemRepositorySynchronizerMessageBuilder;
import com.mg.incomeexpense.core.ItemStateChangeEvent;
import com.mg.incomeexpense.core.dialog.DialogUtils;
import com.mg.incomeexpense.data.IncomeExpenseContract;
import com.mg.incomeexpense.data.IncomeExpenseRequestWrapper;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by mario on 2016-07-19.
 */
public class CategoryEditorActivity extends AppCompatActivityBase {

    private static final String LOG_TAG = CategoryEditorActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.category_editor_activity);

        if (null == savedInstanceState) {

            Bundle bundle = getIntent().getExtras();
            Objects.requireNonNull(bundle, "A bundle with and Category item is mandatory");

            Category category = (Category) bundle.getSerializable("item");
            Objects.requireNonNull(category, "An category object is mandatory");

            bundle.putSerializable("names", IncomeExpenseRequestWrapper.getAvailableCategoryName(getContentResolver(), category));

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
    public void onItemStateChange(@NonNull ItemStateChangeEvent event) {

        Objects.requireNonNull(event, "Parameter event of type ItemStateChangeEvent is mandatory");

        if (event.isCancelled() || !(event.getItem() instanceof Category)) {
            setResult(RESULT_CANCELED);
            finish();
        } else {

            CategoryRepositorySynchronizer synchronizer = new CategoryRepositorySynchronizer(getContentResolver(),
                    IncomeExpenseContract.CategoryEntry.CONTENT_URI, ItemRepositorySynchronizerMessageBuilder.build(this, CategoryRepositorySynchronizer.class.getSimpleName()));

            Category category = (Category) event.getItem();
            try {
                synchronizer.Save(category);
                setResult(RESULT_OK);
                finish();
            } catch (SQLiteConstraintException e) {
                String message = getString(R.string.error_foreign_key_constraint, getString(R.string.account), category.getName());
                Log.i(LOG_TAG, message);
                DialogUtils.messageBox(this, message, getString(R.string.dialog_title_error_deleting_item, getString(R.string.account))).show();

                category.setDead(false);
            }

        }

    }

}
