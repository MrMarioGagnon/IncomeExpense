package com.mg.incomeexpense.paymentmethod;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBar;
import android.util.Log;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.account.AccountRepositorySynchronizer;
import com.mg.incomeexpense.core.AppCompatActivityBase;
import com.mg.incomeexpense.core.ItemRepositorySynchronizerMessageBuilder;
import com.mg.incomeexpense.core.ItemStateChangeEvent;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.core.dialog.DialogUtils;
import com.mg.incomeexpense.data.IncomeExpenseContract;
import com.mg.incomeexpense.data.IncomeExpenseRequestWrapper;

import java.util.Objects;

/**
 * Created by mario on 2016-07-19.
 */
public class PaymentMethodEditorActivity extends AppCompatActivityBase {

    private static final String LOG_TAG = PaymentMethodEditorActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.payment_method_editor_activity);

        if (null == savedInstanceState) {

            Bundle bundle = getIntent().getExtras();
            Objects.requireNonNull(bundle, "A bundle with and Account item is mandatory");

            PaymentMethod paymentMethod = (PaymentMethod) bundle.getSerializable("item");
            Objects.requireNonNull(paymentMethod, "A payment method object is mandatory");

            ActionBar actionBar = getSupportActionBar();
            if (actionBar != null) {
                actionBar.setTitle(getString(paymentMethod.isNew() ? R.string.title_payment_method_editor_add : R.string.title_payment_method_editor_update));
            }

            if (paymentMethod.isNew()) {
                paymentMethod.setCurrency(Tools.getDefaultCurrency(this));
            }

            bundle.putSerializable("names", IncomeExpenseRequestWrapper.getAvailablePaymentMethodName(getContentResolver(), paymentMethod));
            bundle.putSerializable("contributors", IncomeExpenseRequestWrapper.getAvailableContributors(getContentResolver(), null));

            PaymentMethodEditorFragment fragment = new PaymentMethodEditorFragment();
            fragment.addListener(this);
            fragment.setArguments(bundle);
            fragment.setRetainInstance(true);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.payment_method_editor_container, fragment)
                    .commit();
        }

    }

    @Override
    public void onItemStateChange(@NonNull ItemStateChangeEvent event) {

        Objects.requireNonNull(event, "Parameter event of type ItemStateChangeEvent is mandatory");

        if (event.isCancelled()) {
            setResult(RESULT_CANCELED);
            finish();
        } else {

            PaymentMethodRepositorySynchronizer synchronizer = new PaymentMethodRepositorySynchronizer(getContentResolver(),
                    IncomeExpenseContract.PaymentMethodEntry.CONTENT_URI, ItemRepositorySynchronizerMessageBuilder.build(this, AccountRepositorySynchronizer.class.getSimpleName()));

            PaymentMethod paymentMethod = (PaymentMethod) event.getItem();
            try {
                synchronizer.Save(paymentMethod);
                setResult(RESULT_OK);
                finish();
            } catch (SQLiteConstraintException e) {
                String message = getString(R.string.error_foreign_key_constraint, getString(R.string.payment_method), paymentMethod.getName());
                Log.i(LOG_TAG, message);
                DialogUtils.messageBox(this, message, getString(R.string.dialog_title_error_deleting_item, getString(R.string.payment_method))).show();

                paymentMethod.setDead(false);
            }

        }

    }

}
