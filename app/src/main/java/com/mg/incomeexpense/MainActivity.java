package com.mg.incomeexpense;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.account.AccountListActivity;
import com.mg.incomeexpense.category.CategoryListActivity;
import com.mg.incomeexpense.contributor.ContributorListActivity;
import com.mg.incomeexpense.dashboard.DashboardFragment;
import com.mg.incomeexpense.dashboard.DashboardPagerAdapter;
import com.mg.incomeexpense.data.IncomeExpenseContract;
import com.mg.incomeexpense.data.IncomeExpenseRequestWrapper;
import com.mg.incomeexpense.paymentmethod.PaymentMethodListActivity;
import com.mg.incomeexpense.transaction.Transaction;
import com.mg.incomeexpense.transaction.TransactionEditorActivity;
import com.mg.incomeexpense.utility.UtilityActivity;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private static final int EDITOR_ACTIVITY = 1;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private com.mg.floatingactionbutton.FloatingActionsMenu mFabMenu;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        // Permet d'afficher la barre au haut de l'ecran
        setSupportActionBar(toolbar);

        List<Account> accounts = IncomeExpenseRequestWrapper.getAvailableAccounts(getContentResolver(), String.format("SUBSTR('000' || CAST(%2$S AS TEXT), -4) || LOWER(%1$s)", IncomeExpenseContract.AccountEntry.COLUMN_NAME, IncomeExpenseContract.AccountEntry.COLUMN_POSITION));
        mTabLayout = (TabLayout) findViewById(R.id.tab_layout);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        mViewPager = (ViewPager) findViewById(R.id.pager);
        final DashboardPagerAdapter adapter = new DashboardPagerAdapter
                (getSupportFragmentManager(), accounts);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);

        View.OnClickListener fabOnclickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Transaction.TransactionType transactionType;

                switch (view.getId()) {
                    case R.id.fabAddIncome:
                        transactionType = Transaction.TransactionType.Income;
                        break;
                    default:
                        transactionType = Transaction.TransactionType.Expense;
                        break;
                }

                mFabMenu.collapseImmediately();
                Account account = ((DashboardPagerAdapter) mViewPager.getAdapter()).getAccount(mTabLayout.getSelectedTabPosition());
                ShowTransactionEditor(account, transactionType);

            }
        };

        com.mg.floatingactionbutton.FloatingActionButton fab = (com.mg.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabAddExpense);
        fab.setOnClickListener(fabOnclickListener);

        fab = (com.mg.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabAddIncome);
        fab.setOnClickListener(fabOnclickListener);

        mFabMenu = (com.mg.floatingactionbutton.FloatingActionsMenu) findViewById(R.id.floating_action_menu_Type);
    }

    private void ShowTransactionEditor(@NonNull Account account, @NonNull Transaction.TransactionType type) {

        Objects.requireNonNull(account, "Parameter account of type Account is mandatory");
        Objects.requireNonNull(type, "Parameter type of type TransactionType is mandatory");

        Transaction transaction = Transaction.createNew();
        transaction.setAccount(account);
        transaction.setType(type);

        Intent intent = new Intent(this, TransactionEditorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", transaction);
        intent.putExtras(bundle);

        startActivityForResult(intent, EDITOR_ACTIVITY);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case EDITOR_ACTIVITY:
                if (resultCode == RESULT_OK) {

                    DashboardPagerAdapter dashboardPagerAdapter = (DashboardPagerAdapter) mViewPager.getAdapter();
                    DashboardFragment fragment = (DashboardFragment) dashboardPagerAdapter.getItem(mTabLayout.getSelectedTabPosition());
                    fragment.refresh();

                }
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        Intent intent = null;
        switch (id) {
            case R.id.action_contributors:
                intent = new Intent(this, ContributorListActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_accounts:
                intent = new Intent(this, AccountListActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_categories:
                intent = new Intent(this, CategoryListActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_payment_methods:
                intent = new Intent(this, PaymentMethodListActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_utility:
                intent = new Intent(this, UtilityActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
