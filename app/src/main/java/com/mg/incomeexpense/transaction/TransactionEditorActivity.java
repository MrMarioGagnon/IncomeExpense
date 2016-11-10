package com.mg.incomeexpense.transaction;

import android.os.Bundle;
import android.support.annotation.NonNull;

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
public class TransactionEditorActivity extends AppCompatActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.transaction_editor_activity);

        if (null == savedInstanceState) {

            Bundle bundle = getIntent().getExtras();
            Objects.requireNonNull(bundle, "A bundle with and Transaction item is mandatory");

            Transaction transaction = (Transaction) bundle.getSerializable("item");
            Objects.requireNonNull(transaction, "An transaction object is mandatory");

            bundle.putSerializable("paymentMethods", IncomeExpenseRequestWrapper.getAvailablePaymentMethods(getContentResolver(), transaction.getAccount().getContributors()));

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
    public void onItemStateChange(@NonNull ItemStateChangeEvent event) {

        Objects.requireNonNull(event, "Parameter event of type ItemStateChangeEvent is mandatory");

        if (event.isCancelled() || !(event.getItem() instanceof Transaction)) {
            setResult(RESULT_CANCELED);
        } else {

            TransactionRepositorySynchronizer synchronizer = new TransactionRepositorySynchronizer(getContentResolver(),
                    IncomeExpenseContract.TransactionEntry.CONTENT_URI, ItemRepositorySynchronizerMessageBuilder.build(this, TransactionRepositorySynchronizer.class.getSimpleName()));

            synchronizer.Save((Transaction) event.getItem());
            setResult(RESULT_OK);
        }

        finish();
    }

}
