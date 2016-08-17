package com.mg.incomeexpense;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TabHost;

import com.mg.incomeexpense.account.AccountListActivity;
import com.mg.incomeexpense.category.CategoryListActivity;
import com.mg.incomeexpense.contributor.ContributorListActivity;
import com.mg.incomeexpense.paymentmethod.PaymentMethodListActivity;
import com.mg.incomeexpense.transaction.Transaction;
import com.mg.incomeexpense.transaction.TransactionEditorActivity;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final int EDITOR_ACTIVITY = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        final PagerAdapter adapter = new PagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddTransaction);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShowTransactionEditor();

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                       .setAction("Action", null).show();
            }
        });
    }

    private void ShowTransactionEditor(){

        Transaction transaction = Transaction.createNew();

        Intent intent = new Intent(this, TransactionEditorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", transaction);
        intent.putExtras(bundle);

        startActivityForResult(intent,EDITOR_ACTIVITY);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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
            case R.id.action_settings:
                intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_payment_methods:
                intent = new Intent(this, PaymentMethodListActivity.class);
                startActivity(intent);
                return true;
            case R.id.action_category:
                intent = new Intent(this, CategoryListActivity.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }


    }
}
