package com.mg.incomeexpense.contributor;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.AppCompatActivityBase;
import com.mg.incomeexpense.core.ItemRepositorySynchronizerMessageBuilder;
import com.mg.incomeexpense.core.ItemStateChangeEvent;
import com.mg.incomeexpense.data.IncomeExpenseContract;
import com.mg.incomeexpense.data.IncomeExpenseRequestWrapper;

import java.util.Objects;

/**
 * Created by mario on 2016-07-19.
 */
public class ContributorEditorActivity extends AppCompatActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.contributor_editor_activity);

        if (null == savedInstanceState) {

            Bundle bundle = getIntent().getExtras();
            Objects.requireNonNull(bundle, "A bundle with and Contributor item is mandatory");

            Contributor contributor = (Contributor) bundle.getSerializable("item");
            Objects.requireNonNull(contributor, "A contributor object is mandatory");

            ActionBar actionBar = getSupportActionBar();

            if (actionBar != null) {
                actionBar.setTitle(getString(contributor.isNew() ? R.string.title_contributor_editor_add : R.string.title_contributor_editor_update));
            }

            bundle.putSerializable("names", IncomeExpenseRequestWrapper.getAvailableContributorName(getContentResolver(), contributor));

            ContributorEditorFragment fragment = new ContributorEditorFragment();
            fragment.addListener(this);
            fragment.setArguments(bundle);
            fragment.setRetainInstance(true);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.contributor_editor_container, fragment)
                    .commit();
        }

    }

    @Override
    public void onItemStateChange(@NonNull ItemStateChangeEvent event) {

        Objects.requireNonNull(event, "Parameter event of type ItemStateChangeEvent is mandatory");

        if (event.isCancelled()) {
            setResult(RESULT_CANCELED);
        } else {

            ContributorRepositorySynchronizer synchronizer = new ContributorRepositorySynchronizer(getContentResolver(),
                    IncomeExpenseContract.ContributorEntry.CONTENT_URI, ItemRepositorySynchronizerMessageBuilder.build(this, ContributorRepositorySynchronizer.class.getSimpleName()));

            synchronizer.Save(event.getItem());
            setResult(RESULT_OK);
        }

        finish();
    }

}
