package com.mg.incomeexpense.paymentmethod;

import android.os.Bundle;
import android.support.v7.app.ActionBar;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.account.AccountRepositorySynchronizer;
import com.mg.incomeexpense.core.AppCompatActivityBase;
import com.mg.incomeexpense.core.ItemRepositorySynchronizerMessageBuilder;
import com.mg.incomeexpense.core.ItemStateChangeEvent;
import com.mg.incomeexpense.core.Tools;
import com.mg.incomeexpense.data.IncomeExpenseContract;
import com.mg.incomeexpense.data.IncomeExpenseRequestWrapper;

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
            if (null == bundle)
                throw new NullPointerException("A bundle with a Payment Method item is mandatory");

            PaymentMethod paymentMethod = (PaymentMethod) bundle.getSerializable("item");
            if (null == paymentMethod)
                throw new NullPointerException("A payment method object is mandatory");

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
    public void onItemStateChange(ItemStateChangeEvent event) {

        if (event.isCancelled()) {
            setResult(RESULT_CANCELED);
        } else {

            PaymentMethodRepositorySynchronizer synchronizer = new PaymentMethodRepositorySynchronizer(getContentResolver(),
                    IncomeExpenseContract.PaymentMethodEntry.CONTENT_URI, ItemRepositorySynchronizerMessageBuilder.build(this, AccountRepositorySynchronizer.class.getSimpleName()));

            synchronizer.Save(event.getItem());
            setResult(RESULT_OK);
        }

        finish();
    }

}
