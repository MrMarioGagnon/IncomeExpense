package com.mg.incomeexpense.transaction;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.ItemRepositorySynchronizerMessageBuilder;
import com.mg.incomeexpense.core.ItemStateChangeEvent;
import com.mg.incomeexpense.core.ItemStateChangeListener;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.data.IncomeExpenseContract;

/**
 * Created by mario on 2016-07-19.
 */
public class TransactionEditorActivity extends AppCompatActivity implements ItemStateChangeListener {

    private static final String LOG_TAG = TransactionEditorActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.transaction_editor_activity);

        if (null == savedInstanceState) {

            Bundle bundle = getIntent().getExtras();
            if (null == bundle)
                throw new NullPointerException("A bundle with and Transaction item is mandatory");

            Transaction transaction = (Transaction) bundle.getSerializable("item");
            if (null == transaction)
                throw new NullPointerException("An transaction object is mandatory");

            ActionBar actionBar = getSupportActionBar();

            if (actionBar != null) {
                actionBar.setTitle(getString(transaction.isNew() ? R.string.title_transaction_editor_add : R.string.title_transaction_editor_update));

                if (transaction.isNew()) {
                    transaction.setCurrency(Tools.getDefaultCurrency(this));
                }

            }

            TransactionEditorFragment fragment = new TransactionEditorFragment();
            fragment.addListener(this);
            fragment.setArguments(bundle);
            fragment.setRetainInstance(true);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.transaction_editor_container, fragment)
                    .commit();
        }

    }

    @Override
    public void onItemStateChange(ItemStateChangeEvent event) {

        if (event.isCancelled()) {
            setResult(RESULT_CANCELED);
        } else {

            TransactionRepositorySynchronizer synchronizer = new TransactionRepositorySynchronizer(getContentResolver(),
                    IncomeExpenseContract.TransactionEntry.CONTENT_URI, ItemRepositorySynchronizerMessageBuilder.build(this, TransactionRepositorySynchronizer.class.getSimpleName()));

            synchronizer.Save(event.getItem());
            setResult(RESULT_OK);
        }

        finish();
    }

}
