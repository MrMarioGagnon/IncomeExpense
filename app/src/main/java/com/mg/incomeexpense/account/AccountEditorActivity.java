package com.mg.incomeexpense.account;

import android.database.sqlite.SQLiteConstraintException;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.core.AppCompatActivityBase;
import com.mg.incomeexpense.core.ItemRepositorySynchronizerMessageBuilder;
import com.mg.incomeexpense.core.ItemStateChangeEvent;
import com.mg.incomeexpense.core.dialog.DialogUtils;
import com.mg.incomeexpense.data.IncomeExpenseContract;
import com.mg.incomeexpense.data.IncomeExpenseRequestWrapper;

/**
 * Created by mario on 2016-07-19.
 */
public class AccountEditorActivity extends AppCompatActivityBase {

    private static final String LOG_TAG = AccountEditorActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.account_editor_activity);

        if (null == savedInstanceState) {

            Bundle bundle = getIntent().getExtras();
            if (null == bundle)
                throw new NullPointerException("A bundle with and Account item is mandatory");

            Account account = (Account) bundle.getSerializable("item");
            if (null == account)
                throw new NullPointerException("An account object is mandatory");

            ActionBar actionBar = getSupportActionBar();

            if (actionBar != null) {
                actionBar.setTitle(getString(account.isNew() ? R.string.title_account_editor_add : R.string.title_account_editor_update));
            }

            bundle.putSerializable("names", IncomeExpenseRequestWrapper.getAvailableAccountName(getContentResolver(), account));
            bundle.putSerializable("contributors", IncomeExpenseRequestWrapper.getAvailableContributors(getContentResolver(), null));

            AccountEditorFragment fragment = new AccountEditorFragment();
            fragment.addListener(this);
            fragment.setArguments(bundle);
            fragment.setRetainInstance(true);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.account_editor_container, fragment)
                    .commit();
        }

    }

    @Override
    public void onItemStateChange(ItemStateChangeEvent event) {

        if (event.isCancelled()) {
            setResult(RESULT_CANCELED);
            finish();
        } else {

            AccountRepositorySynchronizer synchronizer = new AccountRepositorySynchronizer(getContentResolver(),
                    IncomeExpenseContract.AccountEntry.CONTENT_URI, ItemRepositorySynchronizerMessageBuilder.build(this, AccountRepositorySynchronizer.class.getSimpleName()));

            Account account = (Account) event.getItem();
            try {
                synchronizer.Save(account);
                setResult(RESULT_OK);
                finish();
            } catch (SQLiteConstraintException e) {
                String message = getString(R.string.error_foreign_key_constraint, "Account", account.getName());
                Log.i(LOG_TAG, message);
                DialogUtils.messageBox(this, message, getString(R.string.dialog_title_error_deleting_item, "Account")).show();

                account.setDead(false);
            }


        }


    }

}
