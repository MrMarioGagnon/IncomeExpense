package com.mg.incomeexpense.transaction;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.mg.incomeexpense.R;
import com.mg.incomeexpense.account.Account;
import com.mg.incomeexpense.core.ItemSelectedEvent;
import com.mg.incomeexpense.core.ItemSelectedListener;

import java.util.Objects;

/**
 * Created by mario on 2016-07-19.
 */
public class TransactionListActivity extends AppCompatActivity implements ItemSelectedListener {

    private com.mg.floatingactionbutton.FloatingActionsMenu mFabMenu;
    private Account mAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.transaction_list_activity);

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
                Transaction transaction = Transaction.createNew();
                transaction.setType(transactionType);
                transaction.setAccount(mAccount);

                Intent intent = new Intent(TransactionListActivity.this, TransactionEditorActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("item", transaction);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        };

        Bundle bundle = getIntent().getExtras();
        Objects.requireNonNull(bundle, "A bundle is mandatory");

        mAccount = (Account) bundle.getSerializable("account");
        Objects.requireNonNull(mAccount, "An Account object is mandatory");

        com.mg.floatingactionbutton.FloatingActionButton fab = (com.mg.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabAddExpense);
        fab.setOnClickListener(fabOnclickListener);

        fab = (com.mg.floatingactionbutton.FloatingActionButton) findViewById(R.id.fabAddIncome);
        fab.setOnClickListener(fabOnclickListener);

        mFabMenu = (com.mg.floatingactionbutton.FloatingActionsMenu) findViewById(R.id.floating_action_menu_Type);

        TransactionListFragment fragment = new TransactionListFragment();
        fragment.addListener(this);
        fragment.setArguments(bundle);
        fragment.setRetainInstance(true);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.transaction_list_fragment_container, fragment)
                .commit();

    }

    @Override
    public void onItemSelected(@NonNull ItemSelectedEvent event) {

        Objects.requireNonNull(event, "Parameter event of type ItemSelectedEvent is mandatory");

        Intent intent = new Intent(this, TransactionEditorActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("item", event.getItem());
        intent.putExtras(bundle);

        startActivity(intent);

    }

}
